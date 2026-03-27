import { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

interface Account {
  id: number;
  accountName: string;
  accountType: string;
  channel?: string;
}

export default function Import() {
  const [file, setFile] = useState<File | null>(null);
  const [channel, setChannel] = useState('ALIPAY');
  const [accountId, setAccountId] = useState<number | undefined>(undefined);
  const [accounts, setAccounts] = useState<Account[]>([]);
  const [filteredAccounts, setFilteredAccounts] = useState<Account[]>([]);
  const [preview, setPreview] = useState<any>(null);
  const [isImporting, setIsImporting] = useState(false);
  const navigate = useNavigate();

  // 获取账户列表
  const fetchAccounts = async () => {
    try {
      const res = await axios.get('/api/finance/accounts');
      if (res.data.code === 200) {
        setAccounts(res.data.data || []);
      }
    } catch (error) {
      alert('获取账户列表失败');
    }
  };

  useEffect(() => {
    fetchAccounts();
  }, []);

  useEffect(() => {
    const filtered = accounts.filter(acc => acc.channel === channel);
    setFilteredAccounts(filtered);
    
    // 如果该渠道只有一个账户，自动选中
    if (filtered.length === 1) {
      setAccountId(filtered[0].id);
    } else if (filtered.length > 0 && !filtered.find(a => a.id === accountId)) {
      // 如果当前选中的账户不在过滤列表中，清空选择
      setAccountId(undefined);
    } else if (filtered.length === 0) {
      setAccountId(undefined);
    }
  }, [channel, accounts]);

  const handleUpload = async () => {
    if (!file) {
      alert('请先选择文件');
      return;
    }
    const formData = new FormData();
    formData.append('file', file);
    formData.append('channel', channel);

    try {
      const res = await axios.post('/api/bills/upload', formData);
      setPreview(res.data.data);
    } catch (e: any) {
      console.error(e);
      alert('上传解析失败: ' + (e.response?.data?.message || e.message));
    }
  };

  const handleConfirmImport = async () => {
    if (!preview || !preview.uploadId || !preview.previewRecords) return;

    setIsImporting(true);
    try {
      const tempIds = preview.previewRecords.map((r: any) => r.tempId);
      const res = await axios.post('/api/bills/import', {
        uploadId: preview.uploadId,
        confirmedTempIds: tempIds,
        accountId: accountId
      });
      alert(`成功导入 ${res.data.data} 条记录`);
      setPreview(null);
      setFile(null);
      // 可选：跳转到流水列表页
      navigate('/transactions');
    } catch (e: any) {
      console.error(e);
      alert('导入失败: ' + (e.response?.data?.message || e.message));
    } finally {
      setIsImporting(false);
    }
  };

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">账单导入</h1>
      <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-100 max-w-xl">
        <div className="mb-4">
          <label className="block text-sm font-medium text-gray-700 mb-1">选择渠道</label>
          <select
            value={channel}
            onChange={e => setChannel(e.target.value)}
            className="w-full border-gray-300 rounded-md shadow-sm p-2 border"
          >
            <option value="ALIPAY">支付宝</option>
            <option value="WECHAT">微信</option>
            <option value="QIANJI">钱迹</option>
            <option value="JINGDONG">京东</option>
            <option value="CMB">招商银行</option>
            <option value="BOC_CREDIT">中国银行信用卡</option>
            <option value="CITIC_CREDIT">中信银行信用卡</option>
            <option value="CCB">建设银行</option>
            <option value="PINGAN">平安银行</option>
            <option value="BOCOM_CREDIT">交通银行信用卡</option>
          </select>
        </div>
        <div className="mb-4">
          <label className="block text-sm font-medium text-gray-700 mb-1">关联账户</label>
          <div className="flex items-center space-x-2">
            <select
              value={accountId || ''}
              onChange={(e) => setAccountId(Number(e.target.value))}
              className="w-full border-gray-300 rounded-md shadow-sm p-2 border"
            >
              <option value="">请选择关联账户</option>
              {filteredAccounts.map(acc => (
                <option key={acc.id} value={acc.id}>{acc.accountName}</option>
              ))}
            </select>
            <button
              onClick={() => navigate('/accounts')}
              className="px-3 py-2 bg-gray-100 text-gray-700 rounded-md text-sm whitespace-nowrap hover:bg-gray-200 border"
            >
              去创建
            </button>
          </div>
          {!accountId && filteredAccounts.length > 0 && <p className="mt-1 text-sm text-red-500">必须选择一个关联账户才能导入</p>}
          {filteredAccounts.length === 0 && <p className="mt-1 text-sm text-amber-600">当前渠道无关联账户，请先去创建</p>}
        </div>

        <div className="mb-6">
          <label className="block text-sm font-medium text-gray-700 mb-1">账单文件</label>
          <input
            type="file"
            onChange={e => setFile(e.target.files?.[0] || null)}
            className="w-full border-gray-300 rounded-md shadow-sm p-2 border"
          />
        </div>
        <button
          onClick={handleUpload}
          className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700"
        >
          上传并解析
        </button>
      </div>

      {preview && (
        <div className="mt-8 bg-white p-6 rounded-lg shadow-sm border border-gray-100">
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-xl font-bold">预览数据 (共 {preview.previewRecords?.length} 条记录)</h2>
            <button
              onClick={handleConfirmImport}
              disabled={isImporting || !accountId}
              className="bg-green-600 text-white px-4 py-2 rounded-md hover:bg-green-700 disabled:opacity-50"
            >
              {isImporting ? '导入中...' : '确认导入'}
            </button>
          </div>

          <div className="overflow-x-auto max-h-96">
            <table className="min-w-full divide-y divide-gray-200 border">
              <thead className="bg-gray-50 sticky top-0">
                <tr>
                  <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">时间</th>
                  <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">渠道</th>
                  <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">对方</th>
                  <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">说明</th>
                  <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">收支</th>
                  <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">金额</th>
                  <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">状态</th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {preview.previewRecords?.map((r: any, idx: number) => (
                  <tr key={r.tempId || idx}>
                    <td className="px-4 py-2 whitespace-nowrap text-sm text-gray-500">{r.transactionTime}</td>
                    <td className="px-4 py-2 whitespace-nowrap text-sm text-gray-900">{r.channel}</td>
                    <td className="px-4 py-2 whitespace-nowrap text-sm text-gray-900 max-w-[150px] truncate" title={r.counterparty}>{r.counterparty}</td>
                    <td className="px-4 py-2 whitespace-nowrap text-sm text-gray-500 max-w-[200px] truncate" title={r.merchant}>{r.merchant}</td>
                    <td className="px-4 py-2 whitespace-nowrap text-sm text-gray-900">
                      <span className={r.type === 'INCOME' ? 'text-green-600' : r.type === 'EXPENSE' ? 'text-red-600' : 'text-gray-600'}>
                        {r.type}
                      </span>
                    </td>
                    <td className="px-4 py-2 whitespace-nowrap text-sm font-medium text-gray-900">{r.amount}</td>
                    <td className="px-4 py-2 whitespace-nowrap text-sm text-gray-500">{r.status}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
}
