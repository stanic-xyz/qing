import { useState, useEffect } from 'react';
import axios from 'axios';
import { UploadCloud, FileText } from 'lucide-react';
import UploadView from './components/UploadView';
import ImportRecordList from './components/ImportRecordList';
import RulesPanel from './components/RulesPanel';
import type { Account, ActiveRule, PreviewRecord } from './types';

export default function Import() {
  const [records, setRecords] = useState<any[]>([]);
  const [accounts, setAccounts] = useState<Account[]>([]);
  const [activeRules, setActiveRules] = useState<ActiveRule[]>([]);
  const [selectedRuleIds, setSelectedRuleIds] = useState<Set<number>>(new Set());

  // 视图控制
  const [isUploadView, setIsUploadView] = useState(false);
  const [showRulesPanel, setShowRulesPanel] = useState(false);
  const [expandedUploadId, setExpandedUploadId] = useState<string | null>(null);

  // 处理阶段共享状态
  const [processPreview, setProcessPreview] = useState<{ uploadId: string; previewRecords: PreviewRecord[] } | null>(null);
  const [isMatching, setIsMatching] = useState(false);
  const [isImporting, setIsImporting] = useState(false);
  const [modifiedRecords, setModifiedRecords] = useState<Record<string, any>>({});
  const [lockedTempIds, setLockedTempIds] = useState<Set<string>>(new Set());
  const [processStep, setProcessStep] = useState(1);

  useEffect(() => {
    fetchUploads();
    fetchAccounts();
    fetchActiveRules();
  }, []);

  const fetchUploads = async () => {
    try {
      const res = await axios.get('/api/finance/uploads');
      setRecords(res.data.data.content || []);
    } catch (e) {
      console.error(e);
    }
  };

  const fetchAccounts = async () => {
    try {
      const res = await axios.get('/api/finance/accounts');
      if (res.data.code === 200) {
        setAccounts(res.data.data || []);
      }
    } catch (error) {
      console.error('获取账户列表失败');
    }
  };

  const fetchActiveRules = async () => {
    try {
      const res = await axios.get('/api/finance/matchers/active');
      if (res.data.code === 200) {
        setActiveRules(res.data.data || []);
      }
    } catch (error) {
      console.error('获取规则失败');
    }
  };

  const handleDeleteUpload = async (id: number) => {
    if (window.confirm('确认撤销/删除该批次导入吗？该操作会将所有关联交易记录标记为删除。')) {
      try {
        await axios.delete(`/api/finance/uploads/${id}?softDelete=false`);
        fetchUploads();
        if (expandedUploadId === String(id)) {
          setExpandedUploadId(null);
        }
      } catch (e) {
        alert('删除失败');
      }
    }
  };

  const handleToggleExpand = async (uploadId: string) => {
    if (expandedUploadId === uploadId) {
      setExpandedUploadId(null);
      return;
    }
    
    setExpandedUploadId(uploadId);
    setProcessStep(1);
    setModifiedRecords({});
    setLockedTempIds(new Set());
    setProcessPreview(null);
    
    try {
      const res = await axios.get(`/api/bills/preview/${uploadId}`);
      setProcessPreview(res.data.data);
    } catch (e) {
      alert('获取预览数据失败');
    }
  };

  const handleToggleRule = (id: number) => {
    setSelectedRuleIds(prev => {
      const next = new Set(prev);
      if (next.has(id)) {
        next.delete(id);
      } else {
        next.add(id);
      }
      return next;
    });
  };

  return (
    <div className="flex flex-col h-full gap-6 pb-12 relative">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">账单导入</h1>
          <p className="mt-1 text-sm text-gray-500">支持多文件批量上传解析，并可随时断点续传执行智能匹配与入库</p>
        </div>
        <div className="flex space-x-3">
          <button
            onClick={() => setShowRulesPanel(!showRulesPanel)}
            className={`flex items-center px-4 py-2 rounded-md shadow-sm transition-colors border ${showRulesPanel ? 'bg-blue-50 border-blue-200 text-blue-700' : 'bg-white border-gray-200 text-gray-700 hover:bg-gray-50'}`}
          >
            <FileText size={18} className="mr-2" />
            生效匹配规则 ({activeRules.length})
          </button>
          {!isUploadView && (
            <button
              onClick={() => setIsUploadView(true)}
              className="flex items-center px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 shadow-sm transition-colors"
            >
              <UploadCloud size={18} className="mr-2" />
              上传账单
            </button>
          )}
        </div>
      </div>

      <div className="flex gap-6 items-start">
        <div className={`flex-1 transition-all ${showRulesPanel ? 'w-2/3' : 'w-full'}`}>
          {isUploadView ? (
            <UploadView 
              accounts={accounts} 
              onClose={() => {
                setIsUploadView(false);
                fetchUploads();
              }} 
            />
          ) : (
            <ImportRecordList 
              records={records}
              expandedUploadId={expandedUploadId}
              onToggleExpand={handleToggleExpand}
              onDeleteUpload={handleDeleteUpload}
              processPreview={processPreview}
              setProcessPreview={setProcessPreview}
              accounts={accounts}
              selectedRuleIds={selectedRuleIds}
              processStep={processStep}
              setProcessStep={setProcessStep}
              modifiedRecords={modifiedRecords}
              setModifiedRecords={setModifiedRecords}
              lockedTempIds={lockedTempIds}
              setLockedTempIds={setLockedTempIds}
              isMatching={isMatching}
              setIsMatching={setIsMatching}
              isImporting={isImporting}
              setIsImporting={setIsImporting}
              onImportSuccess={() => {
                setExpandedUploadId(null);
                fetchUploads();
              }}
            />
          )}
        </div>

        {showRulesPanel && (
          <RulesPanel 
            activeRules={activeRules} 
            selectedRuleIds={selectedRuleIds} 
            onToggleRule={handleToggleRule} 
            onClose={() => setShowRulesPanel(false)} 
          />
        )}
      </div>
    </div>
  );
}