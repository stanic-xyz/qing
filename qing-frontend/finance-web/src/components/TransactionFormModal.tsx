import { useEffect, useMemo, useState } from 'react';
import axios from 'axios';
import { ChevronDown, ChevronRight, Search, X } from 'lucide-react';
import { tagApi } from '../api/tags';
import {
  alignAmountSign,
  buildFilteredCategoryTree,
  buildTransactionPayload,
  createTransactionFormValues,
  extractTagNames,
  findCategoryById,
  matchesCategoryDirection,
  type TransactionDirectionType,
  type TransactionFormCategory,
  type TransactionFormRecord,
  type TransactionFormValues,
  validateTransactionForm,
} from './transactionFormUtils';

interface Account {
  id: number;
  accountName: string;
  accountType: string;
}

interface ApiEnvelope<T> {
  code: number;
  message: string;
  data: T;
}

interface TransactionFormModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSuccess: () => void;
  initialData?: TransactionFormRecord;
}

/**
 * 统一处理交易新增与编辑的表单弹窗。
 */
export default function TransactionFormModal({
  isOpen,
  onClose,
  onSuccess,
  initialData,
}: TransactionFormModalProps) {
  const [accounts, setAccounts] = useState<Account[]>([]);
  const [categories, setCategories] = useState<TransactionFormCategory[]>([]);
  const [availableTags, setAvailableTags] = useState<string[]>([]);
  const [expandedCategoryIds, setExpandedCategoryIds] = useState<number[]>([]);
  const [categoryKeyword, setCategoryKeyword] = useState('');
  const [loadingOptions, setLoadingOptions] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [formData, setFormData] = useState<TransactionFormValues>(() => createTransactionFormValues(initialData));

  /**
   * 在弹窗打开或切换编辑记录时重置表单状态。
   */
  useEffect(() => {
    if (!isOpen) {
      return;
    }

    setFormData(createTransactionFormValues(initialData));
    setCategoryKeyword('');
    setExpandedCategoryIds([]);
  }, [initialData, isOpen]);

  /**
   * 拉取账户、分类与标签选项，保证新增和编辑使用同一数据源。
   */
  useEffect(() => {
    if (!isOpen) {
      return;
    }

    let cancelled = false;

    const loadFormOptions = async () => {
      setLoadingOptions(true);
      try {
        const [accountsRes, categoriesRes, tagsRes] = await Promise.all([
          axios.get<ApiEnvelope<Account[]>>('/api/finance/accounts'),
          axios.get<ApiEnvelope<TransactionFormCategory[]>>('/api/categories/tree'),
          tagApi.list(),
        ]);

        if (cancelled) {
          return;
        }

        setAccounts(accountsRes.data.data ?? []);
        setCategories(categoriesRes.data.data ?? []);
        setAvailableTags(extractTagNames(tagsRes));
      } catch (error) {
        console.error('加载交易表单选项失败', error);
      } finally {
        if (!cancelled) {
          setLoadingOptions(false);
        }
      }
    };

    void loadFormOptions();

    return () => {
      cancelled = true;
    };
  }, [isOpen]);

  /**
   * 当方向切换后，如果当前分类不再兼容则自动清空。
   */
  useEffect(() => {
    const selectedCategory = findCategoryById(categories, formData.categoryId);
    if (selectedCategory && !matchesCategoryDirection(selectedCategory.type, formData.directionType)) {
      setFormData((prev) => ({
        ...prev,
        categoryId: null,
        categoryName: '',
      }));
    }
  }, [categories, formData.categoryId, formData.directionType]);

  /**
   * 基于搜索词和方向过滤分类树，并在搜索态自动展开匹配分支。
   */
  const filteredCategoryTree = useMemo(
    () => buildFilteredCategoryTree(categories, categoryKeyword, formData.directionType),
    [categories, categoryKeyword, formData.directionType],
  );

  /**
   * 合并接口标签与当前已选标签，避免编辑时丢失历史展示。
   */
  const mergedTags = useMemo(
    () => Array.from(new Set([...availableTags, ...formData.tags])),
    [availableTags, formData.tags],
  );

  /**
   * 处理方向切换，并同步修正金额符号。
   */
  const handleDirectionChange = (directionType: TransactionDirectionType) => {
    setFormData((prev) => ({
      ...prev,
      directionType,
      amount: alignAmountSign(prev.amount, directionType),
    }));
  };

  /**
   * 处理标签的选中与取消选中。
   */
  const handleToggleTag = (tagName: string) => {
    setFormData((prev) => ({
      ...prev,
      tags: prev.tags.includes(tagName)
        ? prev.tags.filter((tag) => tag !== tagName)
        : [...prev.tags, tagName],
    }));
  };

  /**
   * 选择分类后同步写入分类 ID 与名称。
   */
  const handleSelectCategory = (category: TransactionFormCategory) => {
    setFormData((prev) => ({
      ...prev,
      categoryId: category.id,
      categoryName: category.name,
    }));
  };

  /**
   * 切换分类树节点的展开状态。
   */
  const handleToggleCategory = (categoryId: number) => {
    setExpandedCategoryIds((prev) => (
      prev.includes(categoryId)
        ? prev.filter((id) => id !== categoryId)
        : [...prev, categoryId]
    ));
  };

  /**
   * 提交表单，并保持新增与编辑走同一套校验和请求体结构。
   */
  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    const validationMessage = validateTransactionForm(formData);
    if (validationMessage) {
      window.alert(validationMessage);
      return;
    }

    setSubmitting(true);
    try {
      const payload = buildTransactionPayload(formData);
      if (initialData?.id) {
        await axios.put(`/api/finance/transactions/${initialData.id}`, payload);
      } else {
        await axios.post('/api/finance/transactions', payload);
      }
      onSuccess();
      onClose();
    } catch (error) {
      const message = axios.isAxiosError(error)
        ? error.response?.data?.message ?? '保存失败'
        : '保存失败';
      window.alert(message);
    } finally {
      setSubmitting(false);
    }
  };

  /**
   * 递归渲染分类树，搜索态下会自动展开匹配分支。
   */
  const renderCategoryNodes = (nodes: TransactionFormCategory[]) => {
    const visibleExpandedIds = categoryKeyword
      ? filteredCategoryTree.expandedIds
      : expandedCategoryIds;

    return nodes.map((category) => {
      const hasChildren = (category.children?.length ?? 0) > 0;
      const isExpanded = visibleExpandedIds.includes(category.id);
      const isSelected = formData.categoryId === category.id;
      const directionLabel = category.type === 'INCOME' ? '收入' : '支出';

      return (
        <div key={category.id} className="border-b border-gray-100 last:border-0">
          <div className={`flex items-center gap-2 p-2 ${category.level > 0 ? 'pl-8 bg-gray-50/70' : ''}`}>
            {hasChildren ? (
              <button
                type="button"
                aria-label={`${isExpanded ? '收起' : '展开'}分类 ${category.name}`}
                onClick={() => handleToggleCategory(category.id)}
                className="rounded p-1 text-gray-500 hover:bg-gray-100 hover:text-gray-700"
              >
                {isExpanded ? <ChevronDown size={16} /> : <ChevronRight size={16} />}
              </button>
            ) : (
              <span className="w-6" />
            )}

            <button
              type="button"
              onClick={() => handleSelectCategory(category)}
              className={`flex min-w-0 flex-1 items-center justify-between rounded-md px-2 py-2 text-left text-sm transition-colors ${
                isSelected
                  ? 'bg-blue-50 text-blue-700 ring-1 ring-blue-200'
                  : 'hover:bg-gray-50 text-gray-700'
              }`}
            >
              <span className="truncate font-medium">{category.name}</span>
              <span className={`ml-3 shrink-0 rounded-full px-2 py-0.5 text-xs ${
                category.type === 'INCOME'
                  ? 'bg-green-100 text-green-700'
                  : 'bg-red-100 text-red-700'
              }`}>
                {directionLabel}
              </span>
            </button>
          </div>

          {hasChildren && isExpanded && (
            <div>{renderCategoryNodes(category.children ?? [])}</div>
          )}
        </div>
      );
    });
  };

  if (!isOpen) {
    return null;
  }

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-gray-500 bg-opacity-75 px-4">
      <div className="max-h-[92vh] w-full max-w-3xl overflow-y-auto rounded-lg bg-white p-6 shadow-xl">
        <div className="mb-4 flex items-start justify-between gap-4">
          <div>
            <h3 className="text-lg font-medium text-gray-900">{initialData ? '编辑交易' : '新增交易'}</h3>
            <p className="mt-1 text-sm text-gray-500">
              新增与编辑统一使用 `directionType` 契约，收入为正数，支出为负数。
            </p>
          </div>
          <button
            type="button"
            onClick={onClose}
            className="rounded p-1 text-gray-400 hover:bg-gray-100 hover:text-gray-600"
            aria-label="关闭交易表单"
          >
            <X size={18} />
          </button>
        </div>

        <form className="space-y-5" onSubmit={(event) => { void handleSubmit(event); }}>
          <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
            <div>
              <label htmlFor="transaction-account" className="mb-1 block text-sm font-medium text-gray-700">
                账户 <span className="text-red-500">*</span>
              </label>
              <select
                id="transaction-account"
                value={formData.accountId ?? ''}
                onChange={(event) => setFormData((prev) => ({
                  ...prev,
                  accountId: Number(event.target.value) || null,
                }))}
                className="w-full rounded-md border border-gray-300 p-2 shadow-sm"
              >
                <option value="">请选择账户</option>
                {accounts.map((account) => (
                  <option key={account.id} value={account.id}>
                    {account.accountName}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label htmlFor="transaction-time" className="mb-1 block text-sm font-medium text-gray-700">
                交易时间 <span className="text-red-500">*</span>
              </label>
              <input
                id="transaction-time"
                type="datetime-local"
                value={formData.transactionTime}
                onChange={(event) => setFormData((prev) => ({
                  ...prev,
                  transactionTime: event.target.value,
                }))}
                className="w-full rounded-md border border-gray-300 p-2 shadow-sm"
              />
            </div>
          </div>

          <div className="grid grid-cols-1 gap-4 md:grid-cols-[1.2fr_1fr]">
            <div>
              <span className="mb-1 block text-sm font-medium text-gray-700">
                方向 <span className="text-red-500">*</span>
              </span>
              <div className="grid grid-cols-2 gap-3">
                {(['EXPENSE', 'INCOME'] as TransactionDirectionType[]).map((direction) => {
                  const selected = formData.directionType === direction;
                  const label = direction === 'EXPENSE' ? '支出' : '收入';
                  const selectedClassName = direction === 'EXPENSE'
                    ? 'border-red-300 bg-red-50 text-red-700'
                    : 'border-green-300 bg-green-50 text-green-700';

                  return (
                    <button
                      key={direction}
                      type="button"
                      onClick={() => handleDirectionChange(direction)}
                      className={`rounded-md border px-4 py-2 text-sm font-medium transition-colors ${
                        selected
                          ? selectedClassName
                          : 'border-gray-300 text-gray-600 hover:bg-gray-50'
                      }`}
                    >
                      {label}
                    </button>
                  );
                })}
              </div>
            </div>

            <div>
              <label htmlFor="transaction-amount" className="mb-1 block text-sm font-medium text-gray-700">
                金额 <span className="text-red-500">*</span>
              </label>
              <input
                id="transaction-amount"
                type="number"
                step="0.01"
                value={formData.amount}
                onChange={(event) => setFormData((prev) => ({
                  ...prev,
                  amount: event.target.value,
                }))}
                onBlur={() => setFormData((prev) => ({
                  ...prev,
                  amount: alignAmountSign(prev.amount, prev.directionType),
                }))}
                className="w-full rounded-md border border-gray-300 p-2 shadow-sm"
                placeholder={formData.directionType === 'INCOME' ? '例如 88.00' : '例如 -88.00'}
              />
              <p className="mt-1 text-xs text-gray-500">
                {formData.directionType === 'INCOME' ? '收入金额会保持正数。' : '支出金额会保持负数。'}
              </p>
            </div>
          </div>

          <div>
            <div className="mb-1 flex items-center justify-between gap-3">
              <label htmlFor="category-search" className="block text-sm font-medium text-gray-700">
                分类
              </label>
              {formData.categoryId && (
                <button
                  type="button"
                  onClick={() => setFormData((prev) => ({
                    ...prev,
                    categoryId: null,
                    categoryName: '',
                  }))}
                  className="text-xs text-gray-500 hover:text-gray-700"
                >
                  清空已选分类
                </button>
              )}
            </div>
            <div className="rounded-lg border border-gray-200">
              <div className="border-b border-gray-200 p-3">
                <div className="relative">
                  <Search size={16} className="pointer-events-none absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
                  <input
                    id="category-search"
                    type="text"
                    value={categoryKeyword}
                    onChange={(event) => setCategoryKeyword(event.target.value)}
                    placeholder="搜索分类名称，匹配分支会自动展开"
                    className="w-full rounded-md border border-gray-300 py-2 pl-9 pr-3 text-sm shadow-sm"
                  />
                </div>
                <p className="mt-2 text-xs text-gray-500">
                  一级分类优先展示，可展开二级分类，仅展示与当前方向兼容的分类。
                </p>
                {formData.categoryName && (
                  <div className="mt-2 inline-flex rounded-full bg-blue-50 px-3 py-1 text-xs text-blue-700">
                    已选分类: {formData.categoryName}
                  </div>
                )}
              </div>

              <div className="max-h-64 overflow-y-auto">
                {filteredCategoryTree.tree.length > 0 ? (
                  renderCategoryNodes(filteredCategoryTree.tree)
                ) : (
                  <div className="p-4 text-sm text-gray-400">暂无匹配的分类</div>
                )}
              </div>
            </div>
          </div>

          <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
            <div>
              <label htmlFor="transaction-counterparty" className="mb-1 block text-sm font-medium text-gray-700">
                对方账户
              </label>
              <input
                id="transaction-counterparty"
                type="text"
                value={formData.counterpartyStr}
                onChange={(event) => setFormData((prev) => ({
                  ...prev,
                  counterpartyStr: event.target.value,
                }))}
                className="w-full rounded-md border border-gray-300 p-2 shadow-sm"
                placeholder="对方名称"
              />
            </div>

            <div>
              <label htmlFor="transaction-merchant" className="mb-1 block text-sm font-medium text-gray-700">
                商品/商户
              </label>
              <input
                id="transaction-merchant"
                type="text"
                value={formData.merchant}
                onChange={(event) => setFormData((prev) => ({
                  ...prev,
                  merchant: event.target.value,
                }))}
                className="w-full rounded-md border border-gray-300 p-2 shadow-sm"
                placeholder="商品名称或商户"
              />
            </div>
          </div>

          <div>
            <label htmlFor="transaction-detail" className="mb-1 block text-sm font-medium text-gray-700">
              备注
            </label>
            <textarea
              id="transaction-detail"
              value={formData.detail}
              onChange={(event) => setFormData((prev) => ({
                ...prev,
                detail: event.target.value,
              }))}
              className="min-h-24 w-full rounded-md border border-gray-300 p-2 shadow-sm"
              placeholder="记录补充说明"
            />
          </div>

          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">标签</label>
            <div className="rounded-lg border border-gray-200 p-3">
              <div className="flex flex-wrap gap-2">
                {mergedTags.map((tagName) => {
                  const selected = formData.tags.includes(tagName);
                  return (
                    <button
                      key={tagName}
                      type="button"
                      onClick={() => handleToggleTag(tagName)}
                      className={`rounded-full border px-3 py-1 text-sm transition-colors ${
                        selected
                          ? 'border-blue-300 bg-blue-100 text-blue-700'
                          : 'border-gray-200 bg-gray-100 text-gray-600 hover:bg-gray-200'
                      }`}
                    >
                      {tagName}
                    </button>
                  );
                })}

                {!loadingOptions && mergedTags.length === 0 && (
                  <span className="text-sm text-gray-400">暂无可用标签</span>
                )}
              </div>

              {loadingOptions && (
                <p className="mt-2 text-xs text-gray-400">正在加载标签与分类数据...</p>
              )}
            </div>
          </div>

          <div className="flex justify-end gap-3 border-t border-gray-100 pt-4">
            <button
              type="button"
              onClick={onClose}
              className="rounded-md border border-gray-300 px-4 py-2 text-sm text-gray-700 hover:bg-gray-50"
            >
              取消
            </button>
            <button
              type="submit"
              disabled={submitting}
              className="rounded-md bg-blue-600 px-4 py-2 text-sm text-white hover:bg-blue-700 disabled:opacity-50"
            >
              {submitting ? '保存中...' : '保存'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
