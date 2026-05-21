import { useEffect, useState } from 'react';
import { Plus, Edit2, Trash2, Tag as TagIcon } from 'lucide-react';
import { tagApi, type Tag } from '../api/tags';
import type { CreateTagRequest, UpdateTagRequest } from '../api/tags';

const TAG_COLORS = [
  '#ef4444', '#f97316', '#f59e0b', '#84cc16', '#22c55e',
  '#10b981', '#14b8a6', '#06b6d4', '#0ea5e9', '#3b82f6',
  '#6366f1', '#8b5cf6', '#a855f7', '#d946ef', '#ec4899',
  '#f43f5e', '#78716c', '#64748b', '#475569', '#1e293b',
];

export default function Tags() {
  const [tags, setTags] = useState<Tag[]>([]);
  const [statistics, setStatistics] = useState<Record<string, number>>({});
  const [loading, setLoading] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [editingTag, setEditingTag] = useState<Tag | null>(null);
  const [formData, setFormData] = useState({ name: '', color: TAG_COLORS[0] });

  const fetchTags = async () => {
    setLoading(true);
    try {
      const res = await tagApi.list();
      if (res) {
        setTags(Array.isArray(res) ? res : []);
      }
    } catch (error) {
      console.error('获取标签列表失败', error);
    } finally {
      setLoading(false);
    }
  };

  const fetchStatistics = async () => {
    try {
      const res = await tagApi.statistics();
      if (res) {
        setStatistics((res as any) || {});
      }
    } catch (error) {
      console.error('获取标签统计失败', error);
    }
  };

  useEffect(() => {
    fetchTags();
    fetchStatistics();
  }, []);

  const handleOpenModal = (tag?: Tag) => {
    if (tag) {
      setEditingTag(tag);
      setFormData({ name: tag.name, color: tag.color || TAG_COLORS[0] });
    } else {
      setEditingTag(null);
      setFormData({ name: '', color: TAG_COLORS[Math.floor(Math.random() * TAG_COLORS.length)] });
    }
    setShowModal(true);
  };

  const handleSave = async () => {
    if (!formData.name.trim()) {
      alert('请输入标签名称');
      return;
    }

    try {
      if (editingTag) {
        const data: UpdateTagRequest = { name: formData.name, color: formData.color };
        await tagApi.update(editingTag.id, data);
        setShowModal(false);
        fetchTags();
      } else {
        const data: CreateTagRequest = { name: formData.name, color: formData.color };
        await tagApi.create(data);
        setShowModal(false);
        fetchTags();
        fetchStatistics();
      }
    } catch (error: any) {
      alert(error.message || '操作失败');
    }
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm('确定要删除这个标签吗？')) return;
    try {
      await tagApi.delete(id);
      fetchTags();
      fetchStatistics();
    } catch (error: any) {
      alert(error.message || '删除失败');
    }
  };

  return (
    <div className="p-6">
      <div className="flex justify-between items-center mb-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">标签管理</h1>
          <p className="text-sm text-gray-500 mt-1">管理交易标签，便于分类统计和筛选</p>
        </div>
        <button
          onClick={() => handleOpenModal()}
          className="flex items-center gap-2 px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
        >
          <Plus size={18} />
          新增标签
        </button>
      </div>

      {loading ? (
        <div className="text-center py-12 text-gray-500">加载中...</div>
      ) : tags.length === 0 ? (
        <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-12 text-center">
          <TagIcon className="w-12 h-12 text-gray-300 mx-auto mb-4" />
          <p className="text-gray-500">暂无标签数据</p>
          <p className="text-sm text-gray-400 mt-1">点击上方按钮创建第一个标签</p>
        </div>
      ) : (
        <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4">
          {tags.map((tag) => (
            <div
              key={tag.id}
              className="bg-white rounded-lg shadow-sm border border-gray-200 p-4 hover:shadow-md transition-shadow group"
            >
              <div className="flex items-start justify-between">
                <div className="flex items-center gap-2">
                  <div
                    className="w-4 h-4 rounded-full flex-shrink-0"
                    style={{ backgroundColor: tag.color || '#64748b' }}
                  />
                  <span className="font-medium text-gray-800 truncate">{tag.name}</span>
                </div>
                <div className="flex gap-1 opacity-0 group-hover:opacity-100 transition-opacity">
                  <button
                    onClick={() => handleOpenModal(tag)}
                    className="p-1 text-gray-400 hover:text-blue-600 rounded"
                    title="编辑"
                  >
                    <Edit2 size={14} />
                  </button>
                  <button
                    onClick={() => handleDelete(tag.id)}
                    className="p-1 text-gray-400 hover:text-red-600 rounded"
                    title="删除"
                  >
                    <Trash2 size={14} />
                  </button>
                </div>
              </div>
              <div className="mt-3 flex items-center justify-between text-sm">
                <span className="text-gray-400">
                  {tag.type === 'SYSTEM' ? '系统标签' : '自定义标签'}
                </span>
                <span className="text-blue-600 font-medium">
                  {statistics[tag.name] || 0} 次使用
                </span>
              </div>
            </div>
          ))}
        </div>
      )}

      {showModal && (
        <div className="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-6 w-full max-w-md">
            <h3 className="text-lg font-medium mb-4">
              {editingTag ? '编辑标签' : '新增标签'}
            </h3>
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  标签名称 <span className="text-red-500">*</span>
                </label>
                <input
                  type="text"
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  placeholder="请输入标签名称"
                  className="w-full border-gray-300 rounded-md shadow-sm p-2 border focus:ring-blue-500 focus:border-blue-500"
                  autoFocus
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">标签颜色</label>
                <div className="flex flex-wrap gap-2">
                  {TAG_COLORS.map((color) => (
                    <button
                      key={color}
                      type="button"
                      onClick={() => setFormData({ ...formData, color })}
                      className={`w-8 h-8 rounded-full border-2 transition-all ${
                        formData.color === color
                          ? 'border-gray-800 scale-110'
                          : 'border-transparent hover:scale-105'
                      }`}
                      style={{ backgroundColor: color }}
                    />
                  ))}
                </div>
              </div>
              <div className="flex items-center gap-2 p-3 bg-gray-50 rounded-md">
                <div
                  className="w-6 h-6 rounded-full flex-shrink-0"
                  style={{ backgroundColor: formData.color }}
                />
                <span className="text-sm text-gray-600">预览: {formData.name || '标签名称'}</span>
              </div>
            </div>
            <div className="mt-6 flex justify-end gap-3">
              <button
                onClick={() => setShowModal(false)}
                className="px-4 py-2 border border-gray-300 rounded-md text-sm text-gray-700 hover:bg-gray-50"
              >
                取消
              </button>
              <button
                onClick={handleSave}
                className="px-4 py-2 bg-blue-600 text-white rounded-md text-sm hover:bg-blue-700"
              >
                保存
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
