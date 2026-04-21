import { fetchAllEnums } from '../api/enumApi';

interface CommonEnumVO {
    code: number;
    name: string;
    desc: string;
}

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
    },
    matchStatus: {
        ORIGINAL: '原始数据',
        AUTO_MATCHED: '自动精确匹配',
        INTERNAL_TRANSFER: '内部转账',
        MANUAL_EDITED: '手动修改',
        SUSPICIOUS: '存疑'
    },
    counterParty: {
        MERCHANT: '商户',
        INDIVIDUAL: '个人',
        CORPORATE: '企业'
    },
    accountType: {
        DEBIT: '借记账户',
        CREDIT: '贷记账户',
        WALLET: '钱包'
    }
};

// Dynamic enum cache loaded from backend
// Structure: { matchStatus: { ORIGINAL: "原始数据", ... }, ... }
let dynamicEnumMap: Record<string, Record<string, string>> = {};
let enumCacheInitialized = false;

export const initializeEnumCache = async () => {
    if (enumCacheInitialized) return;
    try {
        const res = await fetchAllEnums();
        if (res.data.data) {
            // Transform { matchStatus: [{code, name, desc}, ...] }
            // to { matchStatus: { ORIGINAL: "原始数据", ... } }
            const enumData = res.data.data as Record<string, CommonEnumVO[]>;
            for (const [key, enums] of Object.entries(enumData)) {
                dynamicEnumMap[key] = {};
                for (const item of enums) {
                    dynamicEnumMap[key][item.name] = item.desc;
                }
            }
            enumCacheInitialized = true;
        }
    } catch (e) {
        console.error('Failed to load dynamic enums:', e);
    }
};

export const getEnumText = (category: string, code: string) => {
    if (!code) return '-';
    // First check dynamic enums
    if (dynamicEnumMap[category]) {
        const val = dynamicEnumMap[category][code];
        if (val) return val;
    }
    // Fallback to static EnumMap
    const map = (EnumMap as any)[category];
    if (map) {
        return map[code] || code;
    }
    return code;
};

export { dynamicEnumMap };
