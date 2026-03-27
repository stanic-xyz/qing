import { 
  CreditCard, 
  Landmark, 
  Wallet, 
  Smartphone, 
  Shield, 
  Activity, 
  ShoppingCart, 
  Banknote 
} from 'lucide-react';

export interface ChannelConfig {
  code: string;
  name: string;
  icon: any; // React component
  colorClass: string;
}

export const CHANNELS: Record<string, ChannelConfig> = {
  ALIPAY: {
    code: 'ALIPAY',
    name: '支付宝',
    icon: Wallet,
    colorClass: 'text-blue-500',
  },
  WECHAT: {
    code: 'WECHAT',
    name: '微信',
    icon: Smartphone,
    colorClass: 'text-green-500',
  },
  CMB: {
    code: 'CMB',
    name: '招商银行',
    icon: Landmark,
    colorClass: 'text-red-500',
  },
  CCB: {
    code: 'CCB',
    name: '建设银行',
    icon: Landmark,
    colorClass: 'text-blue-600',
  },
  BOC_CREDIT: {
    code: 'BOC_CREDIT',
    name: '中国银行信用卡',
    icon: CreditCard,
    colorClass: 'text-red-600',
  },
  CITIC_CREDIT: {
    code: 'CITIC_CREDIT',
    name: '中信银行信用卡',
    icon: CreditCard,
    colorClass: 'text-red-500',
  },
  PINGAN: {
    code: 'PINGAN',
    name: '平安银行',
    icon: Shield,
    colorClass: 'text-orange-500',
  },
  BOCOM_CREDIT: {
    code: 'BOCOM_CREDIT',
    name: '交通银行信用卡',
    icon: CreditCard,
    colorClass: 'text-blue-500',
  },
  JINGDONG: {
    code: 'JINGDONG',
    name: '京东',
    icon: ShoppingCart,
    colorClass: 'text-red-600',
  },
  QIANJI: {
    code: 'QIANJI',
    name: '钱迹',
    icon: Activity,
    colorClass: 'text-blue-400',
  },
  OTHER: {
    code: 'OTHER',
    name: '其他渠道',
    icon: Banknote,
    colorClass: 'text-gray-500',
  }
};

export const CHANNEL_LIST = Object.values(CHANNELS);
