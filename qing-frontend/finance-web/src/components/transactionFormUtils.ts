export type TransactionDirectionType = 'INCOME' | 'EXPENSE';

export interface TransactionFormCategory {
  id: number;
  name: string;
  parentId: number | null;
  level: number;
  type: string;
  children?: TransactionFormCategory[];
}

export interface TransactionFormRecord {
  id?: number;
  accountId?: number | null;
  directionType?: string | null;
  transactionType?: string | null;
  amount?: number | string | null;
  transactionTime?: string | null;
  categoryId?: number | null;
  categoryName?: string | null;
  category?: {
    id?: number | null;
    name?: string | null;
    type?: string | null;
  } | null;
  counterpartyStr?: string | null;
  counterpartyName?: string | null;
  counterparty?: {
    name?: string | null;
  } | string | null;
  merchant?: string | null;
  detail?: string | null;
  remark?: string | null;
  tags?: unknown;
}

export interface TransactionFormValues {
  accountId: number | null;
  directionType: TransactionDirectionType;
  amount: string;
  transactionTime: string;
  categoryId: number | null;
  categoryName: string;
  counterpartyStr: string;
  merchant: string;
  detail: string;
  tags: string[];
}

export interface FilteredCategoryTreeResult {
  tree: TransactionFormCategory[];
  expandedIds: number[];
}

/**
 * 将可空文本归一化为去掉首尾空格后的值。
 */
export function normalizeText(value: string | null | undefined): string {
  return value?.trim() ?? '';
}

/**
 * 生成适用于 `datetime-local` 输入框的本地时间字符串。
 */
export function createDateTimeLocalValue(date = new Date()): string {
  const offset = date.getTimezoneOffset() * 60 * 1000;
  return new Date(date.getTime() - offset).toISOString().slice(0, 16);
}

/**
 * 根据已有记录推断表单应使用的方向字段。
 */
export function normalizeDirectionType(
  directionType: string | null | undefined,
  transactionType?: string | null,
  amount?: number | string | null,
): TransactionDirectionType {
  const normalizedDirection = directionType?.toUpperCase();
  if (normalizedDirection === 'INCOME' || normalizedDirection === 'EXPENSE') {
    return normalizedDirection;
  }

  const normalizedTransactionType = transactionType?.toUpperCase();
  if (normalizedTransactionType === 'INCOME' || normalizedTransactionType === 'EXPENSE') {
    return normalizedTransactionType;
  }

  const numericAmount = typeof amount === 'string' ? Number(amount) : amount;
  if (typeof numericAmount === 'number' && !Number.isNaN(numericAmount)) {
    return numericAmount < 0 ? 'EXPENSE' : 'INCOME';
  }

  return 'EXPENSE';
}

/**
 * 让金额符号始终与当前方向一致，避免编辑旧数据时出现不一致。
 */
export function alignAmountSign(
  amount: string | number | null | undefined,
  directionType: TransactionDirectionType,
): string {
  if (amount === null || amount === undefined || amount === '') {
    return '';
  }

  const numericAmount = typeof amount === 'string' ? Number(amount) : amount;
  if (Number.isNaN(numericAmount)) {
    return String(amount);
  }
  if (numericAmount === 0) {
    return '0';
  }

  const absAmount = Math.abs(numericAmount);
  const signedAmount = directionType === 'INCOME' ? absAmount : -absAmount;
  return String(signedAmount);
}

/**
 * 统一解析接口或列表记录中的标签名称集合。
 */
export function extractTagNames(source: unknown): string[] {
  if (!source) {
    return [];
  }

  if (typeof source === 'string') {
    const normalized = source.trim();
    if (!normalized) {
      return [];
    }

    if (normalized.startsWith('[')) {
      try {
        const parsed = JSON.parse(normalized) as unknown;
        return extractTagNames(parsed);
      } catch {
        return [normalized];
      }
    }

    return normalized
      .split(',')
      .map((item) => item.trim())
      .filter(Boolean);
  }

  if (!Array.isArray(source)) {
    return [];
  }

  return Array.from(new Set(
    source
      .map((item) => {
        if (typeof item === 'string') {
          return normalizeText(item);
        }
        if (item && typeof item === 'object' && 'name' in item) {
          return normalizeText(String((item as { name?: string }).name ?? ''));
        }
        return '';
      })
      .filter(Boolean),
  ));
}

/**
 * 创建新增或编辑场景共用的表单默认值。
 */
export function createTransactionFormValues(initialData?: TransactionFormRecord): TransactionFormValues {
  const directionType = normalizeDirectionType(
    initialData?.directionType,
    initialData?.transactionType,
    initialData?.amount,
  );
  const resolvedCategoryName = normalizeText(initialData?.categoryName ?? initialData?.category?.name);
  const counterpartyName = typeof initialData?.counterparty === 'string'
    ? initialData.counterparty
    : initialData?.counterparty?.name;
  const detail = normalizeText(initialData?.detail ?? initialData?.remark);
  const amount = initialData?.amount ?? '';

  return {
    accountId: initialData?.accountId ?? null,
    directionType,
    amount: amount === '' ? '' : alignAmountSign(amount, directionType),
    transactionTime: initialData?.transactionTime
      ? createDateTimeLocalValue(new Date(initialData.transactionTime))
      : createDateTimeLocalValue(),
    categoryId: initialData?.categoryId ?? initialData?.category?.id ?? null,
    categoryName: resolvedCategoryName,
    counterpartyStr: normalizeText(initialData?.counterpartyStr ?? initialData?.counterpartyName ?? counterpartyName),
    merchant: normalizeText(initialData?.merchant),
    detail,
    tags: extractTagNames(initialData?.tags),
  };
}

/**
 * 校验表单是否满足当前前端契约。
 */
export function validateTransactionForm(values: TransactionFormValues): string | null {
  if (!values.accountId) {
    return '请选择账户';
  }

  if (!normalizeText(values.transactionTime)) {
    return '请选择交易时间';
  }

  if (!normalizeText(values.amount)) {
    return '请输入金额';
  }

  const numericAmount = Number(values.amount);
  if (Number.isNaN(numericAmount)) {
    return '请输入有效金额';
  }
  if (numericAmount === 0) {
    return '金额不能为 0';
  }

  if (values.directionType === 'INCOME' && numericAmount < 0) {
    return '收入金额必须为正数';
  }
  if (values.directionType === 'EXPENSE' && numericAmount > 0) {
    return '支出金额必须为负数';
  }

  return null;
}

/**
 * 判断分类是否与当前方向兼容。
 */
export function matchesCategoryDirection(
  categoryType: string | null | undefined,
  directionType: TransactionDirectionType,
): boolean {
  const normalizedType = categoryType?.trim().toUpperCase();
  if (!normalizedType) {
    return true;
  }
  return normalizedType === directionType;
}

/**
 * 在分类树中递归查找指定分类。
 */
export function findCategoryById(
  categories: TransactionFormCategory[],
  categoryId: number | null,
): TransactionFormCategory | null {
  if (!categoryId) {
    return null;
  }

  for (const category of categories) {
    if (category.id === categoryId) {
      return category;
    }
    const matchedChild = findCategoryById(category.children ?? [], categoryId);
    if (matchedChild) {
      return matchedChild;
    }
  }

  return null;
}

/**
 * 根据方向和关键字构建可展示的分类树，并返回搜索态需要自动展开的节点。
 */
export function buildFilteredCategoryTree(
  categories: TransactionFormCategory[],
  keyword: string,
  directionType: TransactionDirectionType,
): FilteredCategoryTreeResult {
  const normalizedKeyword = normalizeText(keyword).toLowerCase();
  const expandedIds = new Set<number>();

  /**
   * 递归过滤分类节点，并保留匹配关键字的分支上下文。
   */
  const filterNode = (node: TransactionFormCategory): TransactionFormCategory | null => {
    const filteredChildren = (node.children ?? [])
      .map(filterNode)
      .filter((item): item is TransactionFormCategory => item !== null);
    const matchesDirection = matchesCategoryDirection(node.type, directionType);
    const matchesKeyword = !normalizedKeyword || node.name.toLowerCase().includes(normalizedKeyword);

    if (!matchesDirection && filteredChildren.length === 0) {
      return null;
    }
    if (normalizedKeyword && !matchesKeyword && filteredChildren.length === 0) {
      return null;
    }
    if (filteredChildren.length > 0 && normalizedKeyword) {
      expandedIds.add(node.id);
    }

    return {
      ...node,
      children: filteredChildren,
    };
  };

  return {
    tree: categories
      .map(filterNode)
      .filter((item): item is TransactionFormCategory => item !== null),
    expandedIds: Array.from(expandedIds),
  };
}

/**
 * 组装提交给后端的请求体，统一新增与编辑流程。
 */
export function buildTransactionPayload(values: TransactionFormValues) {
  return {
    accountId: values.accountId ?? undefined,
    transactionTime: values.transactionTime,
    directionType: values.directionType,
    transactionType: values.directionType,
    amount: Number(values.amount),
    categoryId: values.categoryId ?? undefined,
    categoryName: normalizeText(values.categoryName) || undefined,
    counterpartyStr: normalizeText(values.counterpartyStr) || undefined,
    merchant: normalizeText(values.merchant) || undefined,
    detail: normalizeText(values.detail) || undefined,
    tags: values.tags,
    status: 'SUCCESS',
    confirmed: true,
  };
}
