import { useState, useEffect } from 'react';
import axios from 'axios';

interface Account {
  id: number;
  accountName: string;
  accountType: string;
}

interface Category {
  id: number;
  name: string;
  parentId: number | null;
  level: number;
  type: string;
  children?: Category[];
}

interface TransactionFormData {
  accountId: number | null;
  transactionType: string;
  amount: string;
  transactionTime: string;
  categoryId: number | null;
  categoryName: string;
  counterpartyName: string;
  merchant: string;
  remark: string;
  tags: string[];
}

interface TransactionFormModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSuccess: () => void;
  initialData?: any;
}

export default function TransactionFormModal({
  isOpen,
  onClose,
  onSuccess,
  initialData,
}: TransactionFormModalProps) {
  const [accounts, setAccounts] = useState<Account[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);
  const [tags, setTags] = useState<string[]>([]);
  const [loading, setLoading] = useState(false);

  const [formData, setFormData] = useState<TransactionFormData>({
    accountId: null,
    transactionType: 'EXPENSE',
    amount: '',
    transactionTime: new Date().toISOString().slice(0, 16),
    categoryId: null,
    categoryName: '',
    counterpartyName: '',
    merchant: '',
    remark: '',
    tags: [],
  });

  useEffect(() => {
    if (isOpen) {
      fetchAccounts();
      fetchCategories();
      fetchTags();
    }
  }, [isOpen]);

  useEffect(() => {
    if (initialData) {
      setFormData({
        accountId: initialData.accountId || null,
        transactionType: initialData.transactionType || 'EXPENSE',
        amount: initialData.amount || '',
        transactionTime: initialData.transactionTime?.slice(0, 16) || new Date().toISOString().slice(0, 16),
        categoryId: initialData.categoryId || null,
        categoryName: initialData.categoryName || '',
        counterpartyName: initialData.counterpartyName || '',
        merchant: initialData.merchant || '',
        remark: initialData.remark || '',
        tags: initialData.tags || [],
      });
    } else {
      setFormData({
        accountId: null,
        transactionType: 'EXPENSE',
        amount: '',
        transactionTime: new Date().toISOString().slice(0, 16),
        categoryId: null,
        categoryName: '',
        counterpartyName: '',
        merchant: '',
        remark: '',
        tags: [],
      });
    }
  }, [initialData, isOpen]);

  const fetchAccounts = async () => {
    try {
      const res = await axios.get('/api/finance/accounts');
      setAccounts(res.data.data || []);
    } catch (e) {
      console.error('获取账户列表失败', e);
    }
  };

  const fetchCategories = async () => {
    try {
      const res = await axios.get('/api/categories/tree');
      if (res.data.code === 200) {
        setCategories(res.data.data || []);
      }
    } catch (e) {
      console.error('获取分类列表失败', e);
    }
  };

  const fetchTags = async () => {
    try {
      const res = await axios.get('/api/tags');
      if (res.data.code === 200) {
        setTags(res.data.data?.map((t: any) => t.name) || []);
      }
    } catch (e) {
      console.error('获取标签列表失败', e);
    }
  };

  const flattenCategories = (cats: Category[]): Category[] => {
    const result: Category[] = [];
    const flatten = (items: Category[]) => {
      items.forEach((item) => {
        result.push(item);
        if (item.children) {
          flatten(item.children);
        }
      });
    };
    flatten(cats);
    return result;
  };

  const handleSubmit = async () => {
    if (!formData.accountId) {
      alert('请选择账户');
      return;
    }
    if (!formData.amount || isNaN(Number(formData.amount))) {
      alert('请输入有效金额');
      return;
    }

    setLoading(true);
    try {
      const payload = {
        accountId: formData.accountId,
        transactionType: formData.transactionType,
        amount: Number(formData.amount),
        transactionTime: formData.transactionTime,
        categoryName: formData.categoryName,
        counterpartyName: formData.counterpartyName,
        merchant: formData.merchant,
        remark: formData.remark,
        tags: formData.tags,
        status: 'SUCCESS',
        confirmed: true,
      };

      if (initialData?.id) {
        await axios.put(`/api/finance/transactions/${initialData.id}`, payload);
      } else {
        await axios.post('/api/finance/transactions', payload);
      }
      onSuccess();
      onClose();
    } catch (e: any) {
      alert(e.response?.data?.message || '保存失败');
    } finally {
      setLoading(false);
    }
  };

  const flatCategories = flattenCategories(categories);

  return (
    <div className="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg p-6 w-full max-w-lg max-h-[90vh] overflow-y-auto">
        <h3 className="text-lg font-medium mb-4">{initialData ? '编辑交易' : '新增交易'}</h3>

        <div className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              账户 <span className="text-red-500">*</span>
            </label>
            <select
              value={formData.accountId || ''}
              onChange={(e) =>
                setFormData({ ...formData, accountId: Number(e.target.value) || null })
              }
              className="w-full border-gray-300 rounded-md shadow-sm p-2 border"
            >
              <option value="">请选择账户</option>
              {accounts.map((acc) => (
                <option key={acc.id} value={acc.id}>
                  {acc.accountName}
                </option>
              ))}
            </select>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                收支类型 <span className="text-red-500">*</span>
              </label>
              <select
                value={formData.transactionType}
                onChange={(e) => setFormData({ ...formData, transactionType: e.target.value })}
                className="w-full border-gray-300 rounded-md shadow-sm p-2 border"
              >
                <option value="EXPENSE">支出</option>
                <option value="INCOME">收入</option>
                <option value="TRANSFER">转账</option>
              </select>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                金额 <span className="text-red-500">*</span>
              </label>
              <input
                type="number"
                step="0.01"
                value={formData.amount}
                onChange={(e) => setFormData({ ...formData, amount: e.target.value })}
                className="w-full border-gray-300 rounded-md shadow-sm p-2 border"
                placeholder="0.00"
              />
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">交易时间</label>
            <input
              type="datetime-local"
              value={formData.transactionTime}
              onChange={(e) => setFormData({ ...formData, transactionTime: e.target.value })}
              className="w-full border-gray-300 rounded-md shadow-sm p-2 border"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">分类</label>
            <select
              value={formData.categoryId || ''}
              onChange={(e) => {
                const catId = Number(e.target.value);
                const cat = flatCategories.find((c) => c.id === catId);
                setFormData({
                  ...formData,
                  categoryId: catId || null,
                  categoryName: cat?.name || '',
                });
              }}
              className="w-full border-gray-300 rounded-md shadow-sm p-2 border"
            >
              <option value="">请选择分类</option>
              {flatCategories.map((cat) => (
                <option key={cat.id} value={cat.id}>
                  {'　'.repeat(cat.level)}{cat.name} ({cat.type === 'EXPENSE' ? '支出' : cat.type === 'INCOME' ? '收入' : '转账'})
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">对方账户</label>
            <input
              type="text"
              value={formData.counterpartyName}
              onChange={(e) => setFormData({ ...formData, counterpartyName: e.target.value })}
              className="w-full border-gray-300 rounded-md shadow-sm p-2 border"
              placeholder="对方账户名称"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">商品/商户</label>
            <input
              type="text"
              value={formData.merchant}
              onChange={(e) => setFormData({ ...formData, merchant: e.target.value })}
              className="w-full border-gray-300 rounded-md shadow-sm p-2 border"
              placeholder="商品名称或商户"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">备注</label>
            <input
              type="text"
              value={formData.remark}
              onChange={(e) => setFormData({ ...formData, remark: e.target.value })}
              className="w-full border-gray-300 rounded-md shadow-sm p-2 border"
              placeholder="备注信息"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">标签</label>
            <div className="flex flex-wrap gap-2">
              {tags.map((tag) => (
                <button
                  key={tag}
                  type="button"
                  onClick={() => {
                    const newTags = formData.tags.includes(tag)
                      ? formData.tags.filter((t) => t !== tag)
                      : [...formData.tags, tag];
                    setFormData({ ...formData, tags: newTags });
                  }}
                  className={`px-3 py-1 rounded-full text-sm ${
                    formData.tags.includes(tag)
                      ? 'bg-blue-100 text-blue-700 border border-blue-300'
                      : 'bg-gray-100 text-gray-600 border border-gray-200'
                  }`}
                >
                  {tag}
                </button>
              ))}
              {tags.length === 0 && (
                <span className="text-sm text-gray-400">暂无可用标签</span>
              )}
            </div>
          </div>
        </div>

        <div className="mt-6 flex justify-end gap-3">
          <button
            onClick={onClose}
            className="px-4 py-2 border border-gray-300 rounded-md text-sm text-gray-700 hover:bg-gray-50"
          >
            取消
          </button>
          <button
            onClick={handleSubmit}
            disabled={loading}
            className="px-4 py-2 bg-blue-600 text-white rounded-md text-sm hover:bg-blue-700 disabled:opacity-50"
          >
            {loading ? '保存中...' : '保存'}
          </button>
        </div>
      </div>
    </div>
  );
}
