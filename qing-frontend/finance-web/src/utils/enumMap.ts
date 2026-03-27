export const EnumMap = {
  status: {
    SUCCESS: '成功',
    FAILED: '失败',
    PENDING: '处理中',
    DELETED: '已删除'
  },
  type: {
    INCOME: '收入',
    EXPENSE: '支出',
    OTHER: '其他'
  },
  channel: {
    ALIPAY: '支付宝',
    WECHAT: '微信',
    QIANJI: '钱迹',
    JINGDONG: '京东',
    CMB: '招商银行',
    BOC_CREDIT: '中国银行信用卡',
    CITIC_CREDIT: '中信银行信用卡',
    CCB: '建设银行',
    PINGAN: '平安银行',
    BOCOM_CREDIT: '交通银行信用卡',
    MANUAL: '手动记账'
  }
};

export const getEnumText = (category: keyof typeof EnumMap, code: string) => {
  if (!code) return '-';
  const map = EnumMap[category];
  return map[code as keyof typeof map] || code;
};
