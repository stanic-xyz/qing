import React, { useState, useRef } from 'react';
import axios from 'axios';
import { UploadCloud, Download, CheckCircle, AlertTriangle, FileSpreadsheet, X } from 'lucide-react';

interface Props {
  onClose: () => void;
  onSuccess: () => void;
}

export default function AccountImportModal({ onClose, onSuccess }: Props) {
  const [step, setStep] = useState(1);
  const [file, setFile] = useState<File | null>(null);
  const [importMode, setImportMode] = useState('SKIP'); // SKIP or OVERWRITE
  const [previewData, setPreviewData] = useState<any>(null);
  const [loading, setLoading] = useState(false);
  const fileInputRef = useRef<HTMLInputElement>(null);

  const handleDownloadTemplate = async () => {
    try {
      const res = await axios.get('/api/finance/accounts/import/template', { responseType: 'blob' });
      const url = window.URL.createObjectURL(new Blob([res.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', 'account_import_template.xlsx');
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    } catch (e) {
      alert('下载模板失败');
    }
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      const selectedFile = e.target.files[0];
      if (selectedFile.size > 10 * 1024 * 1024) {
        alert('文件大小不能超过 10MB');
        return;
      }
      setFile(selectedFile);
    }
  };

  const handlePreview = async () => {
    if (!file) return;
    setLoading(true);
    const formData = new FormData();
    formData.append('file', file);
    formData.append('mode', importMode);

    try {
      const res = await axios.post('/api/finance/accounts/import/preview', formData);
      if (res.data.code === 200) {
        setPreviewData(res.data.data);
        setStep(2);
      } else {
        alert(res.data.message || '解析失败');
      }
    } catch (e: any) {
      alert(e.response?.data?.message || '解析失败');
    } finally {
      setLoading(false);
    }
  };

  const handleModeChangeInPreview = async (mode: string) => {
    setImportMode(mode);
    if (file) {
      // Re-preview with new mode
      setLoading(true);
      const formData = new FormData();
      formData.append('file', file);
      formData.append('mode', mode);
      try {
        const res = await axios.post('/api/finance/accounts/import/preview', formData);
        if (res.data.code === 200) {
          setPreviewData(res.data.data);
        }
      } catch (e) {
        console.error(e);
      } finally {
        setLoading(false);
      }
    }
  };

  const handleImport = async () => {
    if (!previewData || previewData.invalidCount > 0) {
      alert('存在校验失败的数据，请修改后重新上传');
      return;
    }
    
    setLoading(true);
    try {
      const res = await axios.post(`/api/finance/accounts/import/execute?mode=${importMode}`, previewData.items);
      if (res.data.code === 200) {
        alert(`成功导入 ${res.data.data} 条数据！`);
        onSuccess();
      } else {
        alert(res.data.message || '导入失败');
      }
    } catch (e: any) {
      alert(e.response?.data?.message || '导入失败');
    } finally {
      setLoading(false);
    }
  };

  const getStatusBadge = (status: string, msg: string) => {
    switch (status) {
      case 'VALID':
        return <span className="px-2 py-1 bg-green-100 text-green-700 text-xs rounded-full">正常新增</span>;
      case 'INVALID':
        return <span className="px-2 py-1 bg-red-100 text-red-700 text-xs rounded-full" title={msg}>校验失败: {msg}</span>;
      case 'DUPLICATE_OVERWRITE':
        return <span className="px-2 py-1 bg-yellow-100 text-yellow-700 text-xs rounded-full">将覆盖</span>;
      case 'DUPLICATE_SKIP':
        return <span className="px-2 py-1 bg-gray-100 text-gray-500 text-xs rounded-full">将跳过</span>;
      default:
        return <span>{status}</span>;
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-xl w-11/12 max-w-4xl flex flex-col max-h-[90vh]">
        <div className="p-5 border-b border-gray-200 flex justify-between items-center">
          <h2 className="text-xl font-bold text-gray-800">批量导入账户</h2>
          <button onClick={onClose} className="text-gray-400 hover:text-gray-600">
            <X className="w-6 h-6" />
          </button>
        </div>

        <div className="p-6 flex-1 overflow-y-auto">
          {/* Step 1: Upload */}
          {step === 1 && (
            <div className="space-y-6">
              <div className="flex justify-between items-center bg-blue-50 p-4 rounded-lg border border-blue-100">
                <div>
                  <h3 className="font-medium text-blue-800">第一步：下载模板</h3>
                  <p className="text-sm text-blue-600 mt-1">请下载标准模板，按要求填写账户信息后再上传。</p>
                </div>
                <button 
                  onClick={handleDownloadTemplate}
                  className="flex items-center px-4 py-2 bg-white border border-blue-300 text-blue-700 rounded-md hover:bg-blue-50 transition-colors"
                >
                  <Download className="w-4 h-4 mr-2" /> 下载模板
                </button>
              </div>

              <div className="bg-gray-50 p-4 rounded-lg border border-gray-200">
                <h3 className="font-medium text-gray-800 mb-3">第二步：选择导入策略</h3>
                <div className="flex space-x-6">
                  <label className="flex items-center cursor-pointer">
                    <input 
                      type="radio" 
                      name="importMode" 
                      value="SKIP" 
                      checked={importMode === 'SKIP'} 
                      onChange={() => setImportMode('SKIP')}
                      className="text-blue-600 focus:ring-blue-500 w-4 h-4"
                    />
                    <span className="ml-2 text-sm text-gray-700">跳过重复数据 (默认)</span>
                  </label>
                  <label className="flex items-center cursor-pointer">
                    <input 
                      type="radio" 
                      name="importMode" 
                      value="OVERWRITE" 
                      checked={importMode === 'OVERWRITE'} 
                      onChange={() => setImportMode('OVERWRITE')}
                      className="text-blue-600 focus:ring-blue-500 w-4 h-4"
                    />
                    <span className="ml-2 text-sm text-gray-700">覆盖现有数据</span>
                  </label>
                </div>
                <p className="text-xs text-gray-500 mt-2">* 重复判断规则：以上传文件中的“账户名称”在系统中是否存在为准。</p>
              </div>

              <div className="bg-gray-50 p-4 rounded-lg border border-gray-200">
                <h3 className="font-medium text-gray-800 mb-3">第三步：上传文件</h3>
                <div 
                  className="border-2 border-dashed border-gray-300 rounded-lg p-8 text-center bg-white hover:bg-gray-50 cursor-pointer transition-colors"
                  onClick={() => fileInputRef.current?.click()}
                >
                  <FileSpreadsheet className="w-12 h-12 text-gray-400 mx-auto mb-3" />
                  <p className="text-gray-600 font-medium">点击或拖拽文件到此处</p>
                  <p className="text-sm text-gray-400 mt-1">支持 .xlsx, .xls, .csv 格式，最大 10MB，单次最多 100 条</p>
                  <input 
                    type="file" 
                    ref={fileInputRef} 
                    className="hidden" 
                    accept=".xlsx, .xls, .csv" 
                    onChange={handleFileChange}
                  />
                </div>
                {file && (
                  <div className="mt-4 p-3 bg-blue-50 border border-blue-100 rounded text-sm text-blue-700 flex items-center">
                    <CheckCircle className="w-4 h-4 mr-2" />
                    已选择文件：<span className="font-semibold ml-1">{file.name}</span>
                    <span className="ml-auto text-xs text-blue-500">{(file.size / 1024).toFixed(2)} KB</span>
                  </div>
                )}
              </div>
            </div>
          )}

          {/* Step 2: Preview */}
          {step === 2 && previewData && (
            <div className="flex flex-col h-full space-y-4">
              <div className="flex justify-between items-center bg-gray-50 p-4 rounded-lg border border-gray-200">
                <div className="flex space-x-4 text-sm">
                  <div className="flex flex-col">
                    <span className="text-gray-500">总解析数</span>
                    <span className="text-xl font-bold text-gray-800">{previewData.totalCount}</span>
                  </div>
                  <div className="flex flex-col">
                    <span className="text-gray-500">正常新增</span>
                    <span className="text-xl font-bold text-green-600">{previewData.validCount}</span>
                  </div>
                  <div className="flex flex-col">
                    <span className="text-gray-500">重复覆盖</span>
                    <span className="text-xl font-bold text-yellow-600">{previewData.duplicateOverwriteCount}</span>
                  </div>
                  <div className="flex flex-col">
                    <span className="text-gray-500">重复跳过</span>
                    <span className="text-xl font-bold text-gray-500">{previewData.duplicateSkipCount}</span>
                  </div>
                  <div className="flex flex-col">
                    <span className="text-gray-500">校验失败</span>
                    <span className="text-xl font-bold text-red-600">{previewData.invalidCount}</span>
                  </div>
                </div>
                
                <div className="flex flex-col items-end space-y-1">
                  <span className="text-xs text-gray-500">当前重复策略</span>
                  <div className="flex border border-gray-300 rounded-md overflow-hidden">
                    <button 
                      onClick={() => handleModeChangeInPreview('SKIP')}
                      className={`px-3 py-1 text-xs ${importMode === 'SKIP' ? 'bg-blue-100 text-blue-700 font-medium' : 'bg-white text-gray-600 hover:bg-gray-50'}`}
                    >
                      跳过
                    </button>
                    <button 
                      onClick={() => handleModeChangeInPreview('OVERWRITE')}
                      className={`px-3 py-1 text-xs border-l border-gray-300 ${importMode === 'OVERWRITE' ? 'bg-blue-100 text-blue-700 font-medium' : 'bg-white text-gray-600 hover:bg-gray-50'}`}
                    >
                      覆盖
                    </button>
                  </div>
                </div>
              </div>

              {previewData.invalidCount > 0 && (
                <div className="bg-red-50 border border-red-200 text-red-700 p-3 rounded-md flex items-start text-sm">
                  <AlertTriangle className="w-5 h-5 mr-2 flex-shrink-0 mt-0.5" />
                  <p>检测到 <b>{previewData.invalidCount}</b> 条数据校验不通过，请检查标红的行，修改源文件后重新上传！</p>
                </div>
              )}

              <div className="flex-1 border border-gray-200 rounded-md overflow-hidden">
                <div className="overflow-x-auto overflow-y-auto max-h-[400px]">
                  <table className="min-w-full divide-y divide-gray-200 text-sm">
                    <thead className="bg-gray-50 sticky top-0 z-10">
                      <tr>
                        <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">状态</th>
                        <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">账户名称</th>
                        <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">类型</th>
                        <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">发卡机构</th>
                        <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">渠道</th>
                        <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">期初余额</th>
                      </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                      {previewData.items.map((item: any, idx: number) => (
                        <tr key={idx} className={item.processStatus === 'INVALID' ? 'bg-red-50/30' : 'hover:bg-gray-50'}>
                          <td className="px-4 py-3 whitespace-nowrap">
                            {getStatusBadge(item.processStatus, item.errorMsg)}
                          </td>
                          <td className="px-4 py-3 whitespace-nowrap">{item.accountName}</td>
                          <td className="px-4 py-3 whitespace-nowrap">{item.accountType}</td>
                          <td className="px-4 py-3 whitespace-nowrap">{item.bankName}</td>
                          <td className="px-4 py-3 whitespace-nowrap">{item.channel}</td>
                          <td className="px-4 py-3 whitespace-nowrap">{item.initialBalance}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          )}
        </div>

        <div className="p-5 border-t border-gray-200 flex justify-between bg-gray-50 rounded-b-lg">
          {step === 1 ? (
            <>
              <button onClick={onClose} className="px-4 py-2 border border-gray-300 text-gray-700 rounded-md hover:bg-white transition-colors">
                取消
              </button>
              <button 
                onClick={handlePreview} 
                disabled={!file || loading}
                className="px-6 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 disabled:bg-blue-400 disabled:cursor-not-allowed transition-colors flex items-center"
              >
                {loading ? '解析中...' : '下一步：预览数据'}
              </button>
            </>
          ) : (
            <>
              <button onClick={() => setStep(1)} className="px-4 py-2 border border-gray-300 text-gray-700 rounded-md hover:bg-white transition-colors">
                上一步：重新上传
              </button>
              <button 
                onClick={handleImport} 
                disabled={previewData?.invalidCount > 0 || loading}
                className="px-6 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 disabled:bg-blue-400 disabled:cursor-not-allowed transition-colors flex items-center"
              >
                <UploadCloud className="w-4 h-4 mr-2" />
                {loading ? '导入中...' : '确认导入'}
              </button>
            </>
          )}
        </div>
      </div>
    </div>
  );
}
