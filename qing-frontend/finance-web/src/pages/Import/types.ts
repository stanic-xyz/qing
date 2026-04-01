export interface Account {
  id: number;
  accountName: string;
  accountType: string;
  channel?: string;
}

export interface PreviewRecord {
  tempId: string;
  transactionTime: string;
  channel: string;
  type: string;
  amount: number;
  counterparty: string;
  merchant: string;
  status: string;
  matchStatus: string;
  matchRuleName: string;
  targetAccountId?: number;
}

export interface UploadBatchPreviewResponse {
  uploadId: string;
  fileName: string;
  parsedCount: number;
  previewRecords: PreviewRecord[];
}

export interface ActiveRule {
  id: number;
  name: string;
  priority: number;
  sourceChannel: string;
  matchRegex: string;
  ruleType: string;
  counterpartyId?: number;
  setType?: string;
  targetMerchant?: string;
  targetAccountKeyword?: string;
}
