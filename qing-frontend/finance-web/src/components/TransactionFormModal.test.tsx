import React from 'react';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import axios from 'axios';
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest';
import TransactionFormModal from './TransactionFormModal';
import { tagApi } from '../api/tags';
import {
  buildTransactionPayload,
  createTransactionFormValues,
  type TransactionFormCategory,
} from './transactionFormUtils';

vi.mock('axios', async () => {
  const actual = await vi.importActual<typeof import('axios')>('axios');

  return {
    ...actual,
    default: {
      ...actual.default,
      get: vi.fn(),
      post: vi.fn(),
      put: vi.fn(),
      isAxiosError: actual.default.isAxiosError,
    },
  };
});

vi.mock('../api/tags', () => ({
  tagApi: {
    list: vi.fn(),
  },
}));

const mockedAxiosGet = vi.mocked(axios.get);
const mockedTagList = vi.mocked(tagApi.list);

const categoriesFixture: TransactionFormCategory[] = [
  {
    id: 10,
    name: '餐饮',
    parentId: null,
    level: 0,
    type: 'EXPENSE',
    children: [
      {
        id: 11,
        name: '咖啡',
        parentId: 10,
        level: 1,
        type: 'EXPENSE',
        children: [],
      },
    ],
  },
  {
    id: 20,
    name: '工资',
    parentId: null,
    level: 0,
    type: 'INCOME',
    children: [
      {
        id: 21,
        name: '月薪',
        parentId: 20,
        level: 1,
        type: 'INCOME',
        children: [],
      },
    ],
  },
];

/**
 * 统一模拟表单所需的账户与分类接口返回。
 */
function mockFormOptions(categories: TransactionFormCategory[] = categoriesFixture) {
  mockedAxiosGet.mockImplementation((url) => {
    if (url === '/api/finance/accounts') {
      return Promise.resolve({
        data: {
          code: 200,
          data: [
            { id: 1, accountName: '招商银行卡', accountType: 'DEBIT' },
          ],
        },
      });
    }

    if (url === '/api/categories/tree') {
      return Promise.resolve({
        data: {
          code: 200,
          data: categories,
        },
      });
    }

    return Promise.reject(new Error(`Unexpected GET request: ${String(url)}`));
  });
}

/**
 * 渲染交易表单弹窗，便于在用例中复用。
 */
function renderModal(initialData?: Parameters<typeof TransactionFormModal>[0]['initialData']) {
  return render(
    <TransactionFormModal
      isOpen
      onClose={vi.fn()}
      onSuccess={vi.fn()}
      initialData={initialData}
    />,
  );
}

describe('TransactionFormModal', () => {
  beforeEach(() => {
    mockFormOptions();
    mockedTagList.mockResolvedValue([]);
    vi.stubGlobal('alert', vi.fn());
  });

  afterEach(() => {
    vi.unstubAllGlobals();
    vi.clearAllMocks();
  });

  it('使用 tagApi 加载标签并展示可选项', async () => {
    mockedTagList.mockResolvedValue([
      { id: 1, name: '差旅', color: '#3b82f6', type: 'USER', isDeleted: false, createdAt: '', updatedAt: '' },
    ]);

    renderModal();

    expect(await screen.findByRole('button', { name: '差旅' })).toBeInTheDocument();
    expect(screen.queryByText('暂无可用标签')).not.toBeInTheDocument();
  });

  it('方向字段会驱动金额符号，并生成与后端契约一致的提交结构', () => {
    const values = createTransactionFormValues({
      accountId: 1,
      directionType: 'EXPENSE',
      amount: 128.5,
      transactionTime: '2026-06-22T08:00:00',
    });

    const payload = buildTransactionPayload(values);

    expect(payload).toMatchObject({
      accountId: 1,
      directionType: 'EXPENSE',
      transactionType: 'EXPENSE',
      amount: -128.5,
    });
  });

  it('按关键字搜索分类时自动展开匹配分支并允许选中子分类', async () => {
    const user = userEvent.setup();

    renderModal();

    await user.type(
      screen.getByPlaceholderText('搜索分类名称，匹配分支会自动展开'),
      '咖啡',
    );

    expect(await screen.findByText('咖啡')).toBeInTheDocument();
    expect(screen.queryByText('月薪')).not.toBeInTheDocument();

    await user.click(screen.getByText('咖啡'));

    expect(await screen.findByText('已选分类: 咖啡')).toBeInTheDocument();
  });
});
