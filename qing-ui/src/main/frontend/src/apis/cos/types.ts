export interface TempSecret {
  tmpSecretId: string;
  tmpSecretKey: string;
  sessionToken: string;
  startTime: number;
  expiredTime: number;
}
