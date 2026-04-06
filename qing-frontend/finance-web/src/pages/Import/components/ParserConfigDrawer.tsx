import { useState, useEffect } from 'react';
import axios from 'axios';
import { X, Plus, Trash2, Save, Trash } from 'lucide-react';
import type { ParserConfig } from '../types';

interface ParserConfigDrawerProps {
  isOpen: boolean;
  onClose: () => void;
}

interface MetadataRule {
  targetField: string;
  rowIndex: number;
  colIndex: number;
  regex: string;
}

interface FieldMappingRule {
  targetField: string;
  sourceIndex: number;
  dateFormat: string;
  cleanRules: string[];
  defaultValue: string;
  scriptEnabled?: boolean;
  scriptLanguage?: string;
  scriptRule: string;
}

const CHANNEL_OPTIONS = [
  'ALIPAY',
  'WECHAT',
  'CMB',
  'CCB',
  'BOC_CREDIT',
  'CITIC_CREDIT',
  'BOCOM_CREDIT',
  'PINGAN',
  'QIANJI',
  'JINGDONG',
  'BANK',
  'YIPAY',
  'TIKTOK',
];

export default function ParserConfigDrawer({ isOpen, onClose }: ParserConfigDrawerProps) {
  const [configs, setConfigs] = useState<ParserConfig[]>([]);
  const [editingId, setEditingId] = useState<string | null>(null);
  const [formData, setFormData] = useState<Partial<ParserConfig>>({});

  const [metadataRules, setMetadataRules] = useState<MetadataRule[]>([]);
  const [fieldRules, setFieldRules] = useState<FieldMappingRule[]>([]);

  const [testFile, setTestFile] = useState<File | null>(null);
  const [isTesting, setIsTesting] = useState(false);
  const [testResult, setTestResult] = useState<any>(null);

  const [ruleTestValue, setRuleTestValue] = useState<Record<number, string>>({});
  const [ruleTestStatus, setRuleTestStatus] = useState<Record<number, { loading: boolean; result?: any; error?: string }>>({});

  const getScriptSamples = (targetField?: string) => {
    if (targetField === 'amount') {
      return [
        {
          name: '金额解析',
          code:
            "def sign = (value?.toString()?.startsWith('-')) ? -1 : 1\n" +
            "def clean = value.toString().replace(',', '').replace('¥','').replace('￥','').replace('-','').replace('+','')\n" +
            "return new BigDecimal(clean) * sign\n",
        },
        {
          name: '金额+类型',
          code:
            "def sign = (value?.toString()?.startsWith('-')) ? -1 : 1\n" +
            "def clean = value.toString().replace(',', '').replace('¥','').replace('￥','').replace('-','').replace('+','')\n" +
            "def amt = new BigDecimal(clean) * sign\n" +
            "return amt\n",
        },
      ];
    }
    if (targetField === 'transactionTime') {
      return [
        {
          name: '解析时间',
          code:
            "def fmt = java.time.format.DateTimeFormatter.ofPattern('yyyy-MM-dd HH:mm:ss')\n" +
            "return java.time.LocalDateTime.parse(value.toString(), fmt)\n",
        },
      ];
    }
    return [
      {
        name: 'Trim',
        code: "return value?.toString()?.trim()\n",
      },
    ];
  };

  const isRuleScriptEnabled = (rule: FieldMappingRule) =>
    rule.scriptEnabled ?? (!!rule.scriptRule && rule.scriptRule.trim().length > 0);

  const handleTestFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      setTestFile(e.target.files[0]);
    }
  };

  const handleTest = async () => {
    if (!testFile) return;
    setIsTesting(true);
    setTestResult(null);
    try {
      const payload = {
        ...formData,
        metadataRules: JSON.stringify(metadataRules),
        fieldMappingRules: JSON.stringify(fieldRules)
      };

      const form = new FormData();
      form.append('file', testFile);
      form.append('config', JSON.stringify(payload));

      const res = await axios.post('/api/finance/parsers/test', form);
      setTestResult(res.data.data);
    } catch (e: any) {
      alert('测试失败: ' + (e.response?.data?.message || e.message));
    } finally {
      setIsTesting(false);
    }
  };

  const handleRuleScriptTest = async (idx: number) => {
    const rule = fieldRules[idx];
    if (!rule || !rule.scriptRule || rule.scriptRule.trim().length === 0) return;

    const value = ruleTestValue[idx] ?? '';
    setRuleTestStatus(prev => ({ ...prev, [idx]: { loading: true } }));

    try {
      const res = await axios.post('/api/finance/parsers/script/test', {
        language: rule.scriptLanguage || 'groovy',
        script: rule.scriptRule,
        context: { value, targetField: rule.targetField },
        allowMap: false,
        expectNumber: rule.targetField === 'amount',
      });
      setRuleTestStatus(prev => ({ ...prev, [idx]: { loading: false, result: res.data.data } }));
    } catch (e: any) {
      setRuleTestStatus(prev => ({
        ...prev,
        [idx]: { loading: false, error: e.response?.data?.message || e.message }
      }));
    }
  };

  useEffect(() => {
    if (isOpen) {
      fetchConfigs();
    }
  }, [isOpen]);

  const fetchConfigs = async () => {
    try {
      const res = await axios.get('/api/finance/parsers/configs');
      setConfigs(res.data.data);
    } catch (e) {
      console.error('获取配置失败', e);
    }
  };

  const handleEdit = (config: ParserConfig) => {
    setEditingId(config.id);
    setFormData(config);
    try {
      setMetadataRules(JSON.parse(config.metadataRules || '[]'));
      setFieldRules(JSON.parse(config.fieldMappingRules || '[]'));
    } catch (e) {
      setMetadataRules([]);
      setFieldRules([]);
    }
  };

  const handleAdd = () => {
    setEditingId('new');
    setFormData({
      name: '',
      channel: { id: 1, name: '支付宝', code: 'ALIPAY' } as any,
      fileType: 'CSV',
      encoding: 'UTF-8',
      skipRows: 0,
      postScriptEnabled: false,
      postScriptLanguage: 'groovy',
      postScript: '',
    });
    setMetadataRules([]);
    setFieldRules([]);
  };

  const handleSave = async (status: string) => {
    try {
      const payload = {
        ...formData,
        metadataRules: JSON.stringify(metadataRules),
        fieldMappingRules: JSON.stringify(fieldRules),
        status
      };

      if (editingId === 'new') {
        await axios.post('/api/finance/parsers/configs', payload);
      } else {
        await axios.put(`/api/finance/parsers/configs/${editingId}`, payload);
      }
      if (status === 'PUBLISHED') {
          setEditingId(null);
          fetchConfigs();
      }
    } catch (e) {
      alert('保存失败');
    }
  };

  const handleDelete = async (id: string) => {
    if (confirm('确认删除？')) {
      try {
        await axios.delete(`/api/finance/parsers/configs/${id}`);
        fetchConfigs();
      } catch (e) {
        alert('删除失败');
      }
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 overflow-hidden">
      <div className="absolute inset-0 bg-gray-500 bg-opacity-75 transition-opacity" onClick={onClose} />
      <div className="fixed inset-y-0 right-0 w-[95vw] max-w-[1600px] flex">
        <div className="w-full h-full bg-white shadow-xl flex flex-col">
          <div className="px-6 py-4 border-b flex justify-between items-center bg-gray-50">
            <h2 className="text-lg font-medium text-gray-900">解析器配置管理</h2>
            <button onClick={onClose} className="text-gray-400 hover:text-gray-500">
              <X size={24} />
            </button>
          </div>

          <div className="flex flex-1 overflow-hidden">
            {/* 测试区域 */}
            {editingId && (
              <div className="w-1/2 border-r bg-gray-50 flex flex-col overflow-hidden">
                <div className="p-4 border-b bg-white">
                  <h3 className="font-medium text-gray-800 mb-2">解析测试</h3>
                  <input type="file" accept={formData.fileType === 'CSV' ? '.csv' : '.xlsx,.xls'} onChange={handleTestFileChange} className="block w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded-md file:border-0 file:text-sm file:font-semibold file:bg-blue-50 file:text-blue-700 hover:file:bg-blue-100" />
                  <button onClick={handleTest} disabled={!testFile || isTesting} className="mt-2 px-4 py-2 bg-green-600 text-white rounded shadow-sm text-sm disabled:opacity-50">
                    {isTesting ? '测试中...' : '运行测试'}
                  </button>
                </div>
                <div className="flex-1 p-4 overflow-y-auto">
                  {testResult && (
                    <div className="space-y-4">
                      <div className="bg-white p-3 rounded border shadow-sm">
                        <h4 className="font-medium text-gray-700 border-b pb-2 mb-2">解析元数据</h4>
                        <pre className="text-xs text-gray-600 overflow-x-auto">
                          {JSON.stringify(testResult.metadata, null, 2)}
                        </pre>
                      </div>
                      <div className="bg-white p-3 rounded border shadow-sm">
                        <h4 className="font-medium text-gray-700 border-b pb-2 mb-2">解析记录 ({testResult.records?.length || 0}条)</h4>
                        <div className="overflow-x-auto">
                          <table className="min-w-full divide-y divide-gray-200 text-xs">
                            <thead className="bg-gray-50">
                              <tr>
                                <th className="px-2 py-1 text-left">时间</th>
                                <th className="px-2 py-1 text-left">金额</th>
                                <th className="px-2 py-1 text-left">收支</th>
                                <th className="px-2 py-1 text-left">对方</th>
                                <th className="px-2 py-1 text-left">说明</th>
                                <th className="px-2 py-1 text-left">资金源</th>
                                <th className="px-2 py-1 text-left">扩展数据</th>
                              </tr>
                            </thead>
                            <tbody className="divide-y divide-gray-200">
                              {testResult.records?.slice(0, 20).map((r: any, i: number) => (
                                <tr key={i}>
                                  <td className="px-2 py-1">{r.transactionTime}</td>
                                  <td className="px-2 py-1">{r.amount}</td>
                                  <td className="px-2 py-1">{r.type}</td>
                                  <td className="px-2 py-1">{r.counterparty}</td>
                                  <td className="px-2 py-1">{r.merchant}</td>
                                  <td className="px-2 py-1">{r.fundSource}</td>
                                  <td className="px-2 py-1 truncate max-w-[150px]" title={r.originalData}>{r.originalData}</td>
                                </tr>
                              ))}
                            </tbody>
                          </table>
                          {testResult.records?.length > 20 && <div className="text-center text-gray-500 py-2">只显示前20条</div>}
                        </div>
                      </div>
                    </div>
                  )}
                </div>
              </div>
            )}
            <div className={`overflow-y-auto p-6 ${editingId ? 'w-1/2' : 'w-full'}`}>
            {editingId ? (
              <div className="space-y-6">
                {/* 基础信息 */}
                <div className="bg-gray-50 p-4 rounded-lg space-y-4">
                  <h3 className="text-md font-medium text-gray-800">基础信息</h3>
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label className="block text-sm font-medium text-gray-700">解析器名称</label>
                      <input type="text" value={formData.name || ''} onChange={e => setFormData({...formData, name: e.target.value})} className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2" />
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700">渠道</label>
                      <input
                        type="text"
                        list="channel-options"
                        value={formData.channel?.code || ''}
                        onChange={e => setFormData({...formData, channel: { code: e.target.value } as any})}
                        className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2"
                        placeholder="如: ALIPAY"
                      />
                      <datalist id="channel-options">
                        {CHANNEL_OPTIONS.map(c => <option key={c} value={c} />)}
                      </datalist>
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700">文件类型</label>
                      <select value={formData.fileType || ''} onChange={e => setFormData({...formData, fileType: e.target.value})} className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2">
                        <option value="CSV">CSV</option>
                        <option value="EXCEL">EXCEL</option>
                      </select>
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700">编码</label>
                      <input type="text" value={formData.encoding || ''} onChange={e => setFormData({...formData, encoding: e.target.value})} className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2" placeholder="如: UTF-8 或 GBK" />
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700">跳过行数 (数据起始行)</label>
                      <input type="number" value={formData.skipRows || 0} onChange={e => setFormData({...formData, skipRows: parseInt(e.target.value)})} className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2" />
                    </div>
                  </div>
                </div>

                {/* 元数据规则 */}
                <div className="bg-gray-50 p-4 rounded-lg">
                  <div className="flex justify-between items-center mb-4">
                    <h3 className="text-md font-medium text-gray-800">元数据提取规则</h3>
                    <button onClick={() => setMetadataRules([...metadataRules, { targetField: '', rowIndex: 0, colIndex: 0, regex: '' }])} className="text-sm text-blue-600 hover:text-blue-700 flex items-center">
                      <Plus size={14} className="mr-1" /> 添加规则
                    </button>
                  </div>
                  {metadataRules.map((rule, idx) => (
                    <div key={idx} className="flex items-center space-x-2 mb-2">
                      <select value={rule.targetField} onChange={e => { const n = [...metadataRules]; n[idx].targetField = e.target.value; setMetadataRules(n); }} className="border rounded p-1.5 text-sm w-1/4">
                        <option value="">-- 选择目标字段 --</option>
                        <option value="recordCount">总笔数 (recordCount)</option>
                        <option value="startTime">起始时间 (startTime)</option>
                        <option value="endTime">结束时间 (endTime)</option>
                      </select>
                      <input type="number" placeholder="行号" value={rule.rowIndex} onChange={e => { const n = [...metadataRules]; n[idx].rowIndex = parseInt(e.target.value); setMetadataRules(n); }} className="border rounded p-1.5 text-sm w-1/6" title="行号(从0开始)" />
                      <input type="number" placeholder="列号" value={rule.colIndex} onChange={e => { const n = [...metadataRules]; n[idx].colIndex = parseInt(e.target.value); setMetadataRules(n); }} className="border rounded p-1.5 text-sm w-1/6" title="列号(从0开始)" />
                      <input type="text" placeholder="正则提取(选填)" value={rule.regex || ''} onChange={e => { const n = [...metadataRules]; n[idx].regex = e.target.value; setMetadataRules(n); }} className="border rounded p-1.5 text-sm flex-1" title="如: \[(\d+)\]" />
                      <button onClick={() => setMetadataRules(metadataRules.filter((_, i) => i !== idx))} className="text-red-500 hover:text-red-700"><Trash size={16} /></button>
                    </div>
                  ))}
                  {metadataRules.length === 0 && <div className="text-sm text-gray-500">暂无规则，将尝试自动推算。</div>}
                </div>

                {/* 字段映射规则 */}
                <div className="bg-gray-50 p-4 rounded-lg">
                  <div className="flex justify-between items-center mb-4">
                    <h3 className="text-md font-medium text-gray-800">交易字段映射规则</h3>
                    <button onClick={() => setFieldRules([...fieldRules, { targetField: '', sourceIndex: 0, dateFormat: '', cleanRules: [], defaultValue: '', scriptEnabled: false, scriptLanguage: 'groovy', scriptRule: '' }])} className="text-sm text-blue-600 hover:text-blue-700 flex items-center">
                      <Plus size={14} className="mr-1" /> 添加映射
                    </button>
                  </div>
                  {fieldRules.map((rule, idx) => (
                    <div key={idx} className="flex flex-wrap items-center gap-2 mb-3 p-2 border border-dashed rounded bg-white">
                      <div className="flex w-full space-x-2 items-center">
                        <select value={rule.targetField} onChange={e => { const n = [...fieldRules]; n[idx].targetField = e.target.value; setFieldRules(n); }} className="border rounded p-1.5 text-sm flex-1">
                          <option value="">-- 目标字段 --</option>
                          <option value="transactionTime">交易时间 (transactionTime)</option>
                          <option value="amount">金额 (amount)</option>
                          <option value="type">收支类型 (type)</option>
                          <option value="counterparty">交易对方 (counterparty)</option>
                          <option value="merchant">商品/商户 (merchant)</option>
                          <option value="status">状态 (status)</option>
                          <option value="category">分类 (category)</option>
                          <option value="originalId">单号/流水号 (originalId)</option>
                          <option value="remark">备注 (remark)</option>
                          <option value="fundSource">资金来源 (fundSource)</option>
                          <option value="extData.counterpartyAccount">扩展:对方账号 (extData.counterpartyAccount)</option>
                          <option value="extData.counterpartyName">扩展:对方名称 (extData.counterpartyName)</option>
                          <option value="extData.goodsDescription">扩展:商品说明 (extData.goodsDescription)</option>
                          <option value="extData.tradeOrderNo">扩展:交易订单号 (extData.tradeOrderNo)</option>
                          <option value="extData.merchantOrderNo">扩展:商家订单号 (extData.merchantOrderNo)</option>
                          <option value="extData.paymentMethod">扩展:收付款方式 (extData.paymentMethod)</option>
                          <option value="extData.cardLast4">扩展:卡号后四位 (extData.cardLast4)</option>
                          <option value="extData.billingAmount">扩展:结算金额 (extData.billingAmount)</option>
                          <option value="extData.billingCurrency">扩展:结算币种 (extData.billingCurrency)</option>
                        </select>
                        <input type="number" placeholder="列号(0开始)" value={rule.sourceIndex} onChange={e => { const n = [...fieldRules]; n[idx].sourceIndex = parseInt(e.target.value); setFieldRules(n); }} className="border rounded p-1.5 text-sm w-24" title="CSV列号(从0开始)" />
                        <input type="text" placeholder="默认值(选填)" value={rule.defaultValue || ''} onChange={e => { const n = [...fieldRules]; n[idx].defaultValue = e.target.value; setFieldRules(n); }} className="border rounded p-1.5 text-sm w-32" />
                        <button onClick={() => setFieldRules(fieldRules.filter((_, i) => i !== idx))} className="text-red-500 hover:text-red-700 ml-auto"><Trash size={16} /></button>
                      </div>

                      <div className="flex w-full space-x-2 items-center mt-1">
                        {rule.targetField === 'transactionTime' && (
                          <input type="text" placeholder="日期格式 (如: yyyy-MM-dd HH:mm:ss)" value={rule.dateFormat || ''} onChange={e => { const n = [...fieldRules]; n[idx].dateFormat = e.target.value; setFieldRules(n); }} className="border rounded p-1.5 text-sm flex-1" />
                        )}
                        <div className="flex items-center space-x-3 text-sm text-gray-600">
                          <span className="font-medium">清洗规则:</span>
                          <label className="flex items-center"><input type="checkbox" className="mr-1" checked={rule.cleanRules?.includes('TRIM')} onChange={e => { const n = [...fieldRules]; if(e.target.checked) { n[idx].cleanRules = [...(n[idx].cleanRules||[]), 'TRIM'] } else { n[idx].cleanRules = (n[idx].cleanRules||[]).filter(x => x!=='TRIM') }; setFieldRules(n); }} /> 去空白</label>
                          <label className="flex items-center"><input type="checkbox" className="mr-1" checked={rule.cleanRules?.includes('REMOVE_RMB')} onChange={e => { const n = [...fieldRules]; if(e.target.checked) { n[idx].cleanRules = [...(n[idx].cleanRules||[]), 'REMOVE_RMB'] } else { n[idx].cleanRules = (n[idx].cleanRules||[]).filter(x => x!=='REMOVE_RMB') }; setFieldRules(n); }} /> 去¥</label>
                          <label className="flex items-center"><input type="checkbox" className="mr-1" checked={rule.cleanRules?.includes('REMOVE_COMMAS')} onChange={e => { const n = [...fieldRules]; if(e.target.checked) { n[idx].cleanRules = [...(n[idx].cleanRules||[]), 'REMOVE_COMMAS'] } else { n[idx].cleanRules = (n[idx].cleanRules||[]).filter(x => x!=='REMOVE_COMMAS') }; setFieldRules(n); }} /> 去逗号</label>
                          <label className="flex items-center"><input type="checkbox" className="mr-1" checked={rule.cleanRules?.includes('REMOVE_TABS')} onChange={e => { const n = [...fieldRules]; if(e.target.checked) { n[idx].cleanRules = [...(n[idx].cleanRules||[]), 'REMOVE_TABS'] } else { n[idx].cleanRules = (n[idx].cleanRules||[]).filter(x => x!=='REMOVE_TABS') }; setFieldRules(n); }} /> 去隐藏制表符</label>
                        </div>
                      </div>
                      <div className="flex w-full mt-2 items-center gap-3">
                        <label className="flex items-center text-sm text-gray-700">
                          <input
                            type="checkbox"
                            className="mr-1"
                            checked={isRuleScriptEnabled(rule)}
                            onChange={e => {
                              const n = [...fieldRules];
                              n[idx].scriptEnabled = e.target.checked;
                              if (e.target.checked && (!n[idx].scriptLanguage || n[idx].scriptLanguage === '')) {
                                n[idx].scriptLanguage = 'groovy';
                              }
                              setFieldRules(n);
                            }}
                          />
                          使用脚本
                        </label>
                        <select
                          className="border rounded p-1.5 text-sm"
                          disabled={!isRuleScriptEnabled(rule)}
                          value={(rule.scriptLanguage || 'groovy')}
                          onChange={e => {
                            const n = [...fieldRules];
                            n[idx].scriptLanguage = e.target.value;
                            setFieldRules(n);
                          }}
                        >
                          <option value="groovy">Groovy</option>
                        </select>
                        <div className="flex-1" />
                        {isRuleScriptEnabled(rule) && (
                          <div className="flex gap-2 flex-wrap justify-end">
                            {getScriptSamples(rule.targetField).map(s => (
                              <button
                                key={s.name}
                                type="button"
                                className="px-2 py-1 text-xs border rounded bg-gray-50 hover:bg-gray-100"
                                onClick={() => {
                                  const n = [...fieldRules];
                                  n[idx].scriptRule = s.code;
                                  setFieldRules(n);
                                }}
                                title="插入示例脚本"
                              >
                                {s.name}
                              </button>
                            ))}
                          </div>
                        )}
                      </div>
                      {isRuleScriptEnabled(rule) && (
                        <div className="flex w-full mt-2">
                          <textarea
                            placeholder="脚本返回值需要符合当前字段类型。可用变量: value(清洗后字符串), targetField, 以及本行已解析出的字段。"
                            value={rule.scriptRule || ''}
                            onChange={e => {
                              const n = [...fieldRules];
                              n[idx].scriptRule = e.target.value;
                              setFieldRules(n);
                            }}
                            className="border rounded p-2 text-xs w-full font-mono bg-gray-50"
                            rows={5}
                          />
                          <div className="mt-2 flex gap-2 items-center">
                            <input
                              type="text"
                              value={ruleTestValue[idx] ?? ''}
                              onChange={e => setRuleTestValue(prev => ({ ...prev, [idx]: e.target.value }))}
                              className="flex-1 border rounded p-2 text-xs font-mono"
                              placeholder="脚本测试 value（建议填清洗后的原始值）"
                            />
                            <button
                              type="button"
                              onClick={() => handleRuleScriptTest(idx)}
                              disabled={ruleTestStatus[idx]?.loading}
                              className="px-3 py-2 text-xs border rounded bg-white hover:bg-gray-50 disabled:opacity-50"
                            >
                              {ruleTestStatus[idx]?.loading ? '测试中...' : '测试脚本'}
                            </button>
                          </div>
                          {ruleTestStatus[idx]?.error && (
                            <div className="mt-1 text-xs text-red-600">{ruleTestStatus[idx]?.error}</div>
                          )}
                          {ruleTestStatus[idx]?.result !== undefined && !ruleTestStatus[idx]?.error && (
                            <div className="mt-1 text-xs text-green-700">结果: {String(ruleTestStatus[idx]?.result)}</div>
                          )}
                        </div>
                      )}
                    </div>
                  ))}
                  {fieldRules.length === 0 && <div className="text-sm text-gray-500">暂无映射规则。</div>}
                </div>

                <div className="bg-gray-50 p-4 rounded-lg space-y-3">
                  <div className="flex items-center gap-3">
                    <label className="flex items-center gap-2 text-sm text-gray-700">
                      <input
                        type="checkbox"
                        checked={formData.postScriptEnabled ?? (!!formData.postScript && formData.postScript.trim().length > 0)}
                        onChange={e => setFormData({ ...formData, postScriptEnabled: e.target.checked })}
                      />
                      启用后置脚本（字段映射完成后执行）
                    </label>
                    <select
                      className="border rounded p-1.5 text-sm"
                      disabled={!(formData.postScriptEnabled ?? (!!formData.postScript && formData.postScript.trim().length > 0))}
                      value={(formData.postScriptLanguage || 'groovy')}
                      onChange={e => setFormData({ ...formData, postScriptLanguage: e.target.value })}
                    >
                      <option value="groovy">Groovy</option>
                    </select>
                    <div className="flex-1" />
                    <button
                      type="button"
                      className="px-2 py-1 text-xs border rounded bg-white hover:bg-gray-50"
                      onClick={() =>
                        setFormData({
                          ...formData,
                          postScriptEnabled: true,
                          postScriptLanguage: formData.postScriptLanguage || 'groovy',
                          postScript: `def amt = record?.amount
if (amt == null) return null
return [type: (amt.signum() < 0 ? 'EXPENSE' : 'INCOME')]
`
                        })
                      }
                      title="插入示例后置脚本"
                    >
                      示例脚本
                    </button>
                  </div>
                  <textarea
                    placeholder="后置脚本：可使用 record、row、extData。返回 Map（key=字段名或 extData.xxx，value=字段值）或返回 null。"
                    value={formData.postScript || ''}
                    onChange={e => setFormData({ ...formData, postScript: e.target.value })}
                    className="border rounded p-2 text-xs w-full font-mono bg-white"
                    rows={6}
                  />
                  <p className="text-xs text-gray-500">
                    用途：把“金额→收支类型”等跨字段逻辑放到这里；字段映射脚本建议仅返回标量值（如金额 BigDecimal）。
                  </p>
                </div>

                <div className="flex justify-end space-x-3 pt-4 border-t">
                  <button onClick={() => setEditingId(null)} className="px-4 py-2 border rounded-md text-gray-700 hover:bg-gray-50">取消</button>
                  <button onClick={() => handleSave('DRAFT')} className="px-4 py-2 bg-yellow-500 hover:bg-yellow-600 text-white rounded-md flex items-center shadow-sm">保存草稿</button>
                  <button onClick={() => handleSave('PUBLISHED')} className="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-md flex items-center shadow-sm"><Save size={16} className="mr-1"/> 发布</button>
                </div>
              </div>
            ) : (
              <div>
                <div className="mb-4">
                  <button onClick={handleAdd} className="flex items-center text-blue-600 hover:text-blue-700 font-medium">
                    <Plus size={16} className="mr-1" /> 新增自定义解析器
                  </button>
                </div>
                <div className="space-y-3">
                  {configs.map(config => (
                    <div key={config.id} className="border rounded-lg p-4 flex justify-between items-center hover:bg-gray-50">
                      <div>
                        <h4 className="font-medium text-gray-900">{config.name} {config.isBuiltIn ? <span className="text-xs bg-gray-200 text-gray-600 px-2 py-0.5 rounded ml-2">内置</span> : ''} {config.status === 'DRAFT' ? <span className="text-xs bg-yellow-100 text-yellow-800 px-2 py-0.5 rounded ml-2">草稿</span> : <span className="text-xs bg-green-100 text-green-800 px-2 py-0.5 rounded ml-2">已发布</span>}</h4>
                        <p className="text-sm text-gray-500 mt-1">渠道: {config.channel?.name || config.channel?.code || '未知'} | 类型: {config.fileType} | 编码: {config.encoding}</p>
                      </div>
                      <div className="flex space-x-2">
                        <button onClick={() => handleEdit(config)} className="text-blue-600 hover:text-blue-800 text-sm font-medium">编辑</button>
                        <button onClick={() => handleDelete(config.id)} className="text-red-600 hover:text-red-800"><Trash2 size={18} /></button>
                      </div>
                    </div>
                  ))}
                  {configs.length === 0 && (
                    <div className="text-center py-8 text-gray-500">暂无自定义解析器配置</div>
                  )}
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
    </div>
  );
}
