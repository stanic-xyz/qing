// types.ts
import type {AxiosError, AxiosRequestConfig, AxiosResponse, Method,} from "axios";

export type RequestMethods = Extract<
  Method,
  "get" | "post" | "put" | "delete" | "patch" | "option" | "head"
>;

export interface RequestInterceptors {
  // 请求拦截
  requestInterceptors?: (config: AxiosRequestConfig) => AxiosRequestConfig;
  requestInterceptorsCatch?: (err: any) => any;
  // 响应拦截
  responseInterceptors?: <T = AxiosResponse>(config: T) => T;
  responseInterceptorsCatch?: (err: any) => any;
}

export interface CancelRequestSource {
  [index: string]: () => void;
}

export interface QingHttpError extends AxiosError {
  isCancelRequest?: boolean;
}

export interface QingHttpResponse extends AxiosResponse {
  isCancelRequest?: boolean;
}

export interface QingHttpRequestConfig extends AxiosRequestConfig {
  beforeRequestCallback?: (request: QingHttpRequestConfig) => void;
  beforeResponseCallback?: (response: QingHttpResponse) => void;
  interceptors?: RequestInterceptors;
  timeout: 10000;
}

export interface QingResponse<T> {
  code: number;
  message: string;
  devMessage: string;
  success: boolean;
  result: T;
}

export interface QingPageResponse<T> {
  total: number;
  totalPages: number;
  pageSize: number;
  pageNumber: number;
  list: T[];
}
