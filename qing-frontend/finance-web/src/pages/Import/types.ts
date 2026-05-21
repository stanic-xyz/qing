export interface Account {
    id: number;
    accountId?: string;
    accountName: string;
    accountType: string;
    bankName?: string;
    channel?: string;
    channelDto?: ChannelItem;
    icon?: string;
    remark?: string;
    cardNumber?: string;
    openingDate?: string;
    initialBalance?: number;
    currentBalance?: number;
    balanceAsOf?: string;
    status?: string;
}

export interface PreviewRecord {
    id: string;
    transactionTime: string;
    directionType: 'IN' | 'OUT';
    channel: string;
    type: string;
    account: Account;
    amount: number;
    counterparty: string;
    merchant: string;
    status: string;
    matchStatus: string;
    matchRuleName: string;
    targetAccountId?: number;
    fundType?: string;
    fundSource?: string;
    fundSourceAccountId?: number;
    extData?: Record<string, any>;
}

export interface UploadBatchPreviewResponse {
    uploadId: string;
    fileName: string;
    parsedCount: number;
    previewRecords: PreviewRecord[] | null;  // null表示返回概览而非明细
}

export interface UploadBatchOverview {
    uploadId: string;
    fileName: string;
    fileSize: number;
    totalRecords: number;
    incomeCount: number;
    expenseCount: number;
    transferCount: number;
    totalIncome: number;
    totalExpense: number;
    transactionStartTime: string;
    transactionEndTime: string;
    batchCount: number;
    accountName: string;
}

export interface UploadBatch {
    id: number;
    uploadId: string;
    batchNo: string;
    status: 'PENDING' | 'MATCHING' | 'COMPLETED';
    totalRecords: number;
    matchedRecords: number;
    unmatchedRecords: number;
    suspiciousRecords: number;
    transactionStartTime: string;
    transactionEndTime: string;
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

export interface ParserItem {
    id: number;
    name: string;
    channel?: ChannelItem;
    fileType: string;
    isBuiltIn: boolean;
}

export interface ChannelItem {
    id: number;
    name: string;
    code: string;
    icon?: string;
    type: string;
}

export interface ChannelItemModal {
    id?: number;
    name: string;
    code: string;
    icon?: string;
    type: string;
}

export interface ParserConfig extends ParserItem {
    encoding: string;
    skipRows: number;
    metadataRules: string;
    fieldMappingRules: string;
    script?: string;
    postScript?: string;
    postScriptLanguage?: string;
    postScriptEnabled?: boolean;
    status?: 'PUBLISHED' | 'DRAFT';
    sourceType?: string;
}

export type AdapterType = 'PARSER' | 'SCRIPT' | 'LLM';
export type DraftBatchStatus = 'DRAFTED' | 'MATCHING' | 'MATCHED' | 'CONFIRMING' | 'IMPORTED' | 'FAILED';

export interface DraftBatch {
    id: number;
    batchNo: string;
    adapterType: AdapterType;
    status: DraftBatchStatus;
    progress: number;
    totalRecords: number;
    errorMessage?: string;
    allowedActions: string[];
    createdAt: string;
    updatedAt: string;
}
