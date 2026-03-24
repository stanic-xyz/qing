import { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

export default function Import() {
  const [file, setFile] = useState<File | null>(null);
  const [channel, setChannel] = useState('ALIPAY');
  const [preview, setPreview] = useState<any>(null);
  const [isImporting, setIsImporting] = useState(false);
  const navigate = useNavigate();

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
        confirmedTempIds: tempIds
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
              disabled={isImporting}
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
