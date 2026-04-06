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
    tempId: string;
    transactionTime: string;
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

export interface ParserItem {
    id: string;
    name: string;
    channel?: ChannelItem;
    fileType: string;
    isBuiltIn: boolean;
}

export interface ChannelItem {
    id: string;
    name: string;
    code: string;
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
    status?: string;
}
