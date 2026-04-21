// 重新组织的 Import 主页面
// 简化后：组件只负责 UI，数据通过 props 传入，状态通过 Zustand 管理
import { useState, useEffect, useCallback } from 'react';
import axios from 'axios';
import { UploadCloud, FileText, Settings } from 'lucide-react';

import UploadView from './components/UploadView';
import ImportRecordList from './components/ImportRecordList';
import RulesPanel from './components/RulesPanel';
import ParserConfigDrawer from './components/ParserConfigDrawer';
import type { Account, ActiveRule } from './types';

export default function ImportPage() {
  // ===== 基础数据 =====
  const [records, setRecords] = useState<any[]>([]);
  const [accounts, setAccounts] = useState<Account[]>([]);
  const [activeRules, setActiveRules] = useState<ActiveRule[]>([]);

  // ===== 视图控制 =====
  const [isUploadView, setIsUploadView] = useState(false);
  const [showRulesPanel, setShowRulesPanel] = useState(false);
  const [showParserConfig, setShowParserConfig] = useState(false);

  // ===== 展开状态 =====
  const [expandedUploadId, setExpandedUploadId] = useState<string | null>(null);

  // ===== 选中规则 =====
  const [selectedRuleIds, setSelectedRuleIds] = useState<Set<number>>(new Set());

  // ===== 加载初始数据 =====
  const fetchAll = useCallback(async () => {
    const [uploadsRes, accountsRes, rulesRes] = await Promise.allSettled([
      axios.get('/api/finance/uploads'),
      axios.get('/api/finance/accounts'),
      axios.get('/api/finance/matchers/active'),
    ]);

    if (uploadsRes.status === 'fulfilled') {
      setRecords(uploadsRes.value.data?.data?.content || []);
    }
    if (accountsRes.status === 'fulfilled' && accountsRes.value.data?.code === 200) {
      setAccounts(accountsRes.value.data?.data || []);
    }
    if (rulesRes.status === 'fulfilled' && rulesRes.value.data?.code === 200) {
      setActiveRules(rulesRes.value.data?.data || []);
    }
  }, []);

  useEffect(() => { fetchAll(); }, [fetchAll]);

  // ===== 展开 =====
  const handleToggleExpand = useCallback((uploadId: string) => {
    setExpandedUploadId(prev => prev === uploadId ? null : uploadId);
  }, []);

  // ===== 删除 =====
  const handleDeleteUpload = useCallback(async (id: number) => {
    if (!window.confirm('确认删除该批次吗？此操作不可恢复。')) return;
    try {
      await axios.delete(`/api/finance/uploads/${id}?softDelete=false`);
      setRecords(prev => prev.filter(r => r.id !== id));
      if (expandedUploadId === String(id)) setExpandedUploadId(null);
    } catch {
      alert('删除失败');
    }
  }, [expandedUploadId]);

  // ===== 规则切换 =====
  const handleToggleRule = useCallback((id: number) => {
    setSelectedRuleIds(prev => {
      const next = new Set(prev);
      next.has(id) ? next.delete(id) : next.add(id);
      return next;
    });
  }, []);

  // ===== 导入成功回调 =====
  const handleImportSuccess = useCallback(() => {
    setExpandedUploadId(null);
    fetchAll();
  }, [fetchAll]);

  // ===== 渲染 =====
  return (
    <div className="flex flex-col h-full gap-6 pb-12 relative">

      {/* 标题栏 */}
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">账单导入</h1>
          <p className="mt-1 text-sm text-gray-500">
            多文件批量上传解析，断点续传，智能匹配与入库
          </p>
        </div>

        <div className="flex space-x-3">
          <button
            onClick={() => setShowParserConfig(true)}
            className="flex items-center px-4 py-2 bg-white text-gray-700 rounded-md border border-gray-200 hover:bg-gray-50 transition-colors shadow-sm cursor-pointer"
          >
            <Settings size={18} className="mr-2" />
            解析器配置
          </button>

          <button
            onClick={() => setShowRulesPanel(v => !v)}
            className={`flex items-center px-4 py-2 rounded-md border shadow-sm cursor-pointer transition-colors ${
              showRulesPanel
                ? 'bg-blue-50 border-blue-200 text-blue-700'
                : 'bg-white border-gray-200 text-gray-700 hover:bg-gray-50'
            }`}
          >
            <FileText size={18} className="mr-2" />
            生效规则 ({activeRules.length})
          </button>

          {!isUploadView && (
            <button
              onClick={() => setIsUploadView(true)}
              className="flex items-center px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 shadow-sm transition-colors cursor-pointer"
            >
              <UploadCloud size={18} className="mr-2" />
              上传账单
            </button>
          )}
        </div>
      </div>

      {/* 主内容 */}
      <div className="flex gap-6 items-start">
        <div className={`flex-1 transition-all ${showRulesPanel ? 'w-2/3' : 'w-full'}`}>
          {isUploadView ? (
            <UploadView
              accounts={accounts}
              onClose={() => { setIsUploadView(false); fetchAll(); }}
            />
          ) : (
            <ImportRecordList
              records={records}
              expandedUploadId={expandedUploadId}
              onToggleExpand={handleToggleExpand}
              onDeleteUpload={handleDeleteUpload}
              accounts={accounts}
              selectedRuleIds={selectedRuleIds}
              onImportSuccess={handleImportSuccess}
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

      {showParserConfig && (
        <ParserConfigDrawer
          isOpen={showParserConfig}
          onClose={() => setShowParserConfig(false)}
        />
      )}
    </div>
  );
}
