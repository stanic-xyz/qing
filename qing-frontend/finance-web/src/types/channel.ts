export interface Channel {
  id: number;
  code: string;
  name: string;
  icon?: string;
  status: string;
  isEnabled: boolean;
  version: number;
  isDeleted: boolean;
  createdBy?: string;
  createdAt: string;
  updatedAt: string;
}

export interface Account {
  id: number;
  accountId: string;
  accountName: string;
  accountType: string;
  bankName: string;
  channel: string;
  icon?: string;
  remark?: string;
  cardNumber: string;
  openingDate: string;
  initialBalance: number;
  balanceAsOf: string;
  status: string;
}

export interface ChannelAccountRel {
  id: number;
  channelId: number;
  accountId: number;
  status: string;
  version: number;
  isDeleted: boolean;
}
