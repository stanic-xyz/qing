import moment from "moment";
import type { AxiosInstance } from "axios";
import axios from "axios";
import type {
  CancelRequestSource,
  RequestInterceptors,
} from "@/utils/http/types";
import { userInfoStore } from "@/stores/session";

moment.locale("zh-cn");

// 相关配置请参考：www.axios-js.com/zh-cn/docs/#axios-http-config-1
// const defaultConfig: QingHttpRequestConfig = {
//   baseURL: "http://localhost:8080",
//   // 请求超时时间
//   timeout: 10000,
//
//   headers: null,
// };

/**
 * 封装的请求对象
 */
class QingHttp {
  /**
   * 存放取消方法的集合
   * 在创建请求后将取消请求方法 push 到该集合中
   * 封装一个方法，可以取消请求，传入 url: string|string[]
   * 在请求之前判断同一URL是否存在，如果存在就取消请求
   */
  cancelRequestSourceList?: CancelRequestSource[];
  /** 初始化配置对象 */
  // private readonly initConfig: QingHttpRequestConfig;
  // http 实例
  private instance: AxiosInstance;
  /**
   * 存放所有请求URL的集合
   * 请求之前需要将url push到该集合中
   * 请求完毕后将url从集合中删除
   * 添加在发送请求之前完成，删除在响应之后删除
   */
  private requestUrlList?: [];
  // 拦截器对象
  private interceptorsObj?: RequestInterceptors;

  constructor() {
    // this.instance = axios.create(defaultConfig);
    this.instance = axios.create({
      baseURL: "http://localhost:8080",
    });
    // this.initConfig = defaultConfig;
    this.instance.defaults.headers.common["Content-Type"] = "application/json";
    this.instance.defaults.headers.common["Accept"] =
      "application/json, text/plain, */*";
    this.httpInterceptorsRequest();
    this.httpInterceptorsResponse();
  }

  // 取消全部请求
  cancelAllRequest() {
    this.cancelRequestSourceList?.forEach((source) => {
      const key = Object.keys(source)[0];
      source[key]();
    });
  }

  /**
   * 取消请求
   * @param url 请求地址
   */
  cancelRequest(url: string | string[]) {
    if (typeof url === "string") {
      // 取消单个请求
      const sourceIndex = this.getSourceIndex(url);
      sourceIndex >= 0 && this.cancelRequestSourceList?.[sourceIndex][url]();
    } else {
      // 存在多个需要取消请求的地址
      url.forEach((u) => {
        const sourceIndex = this.getSourceIndex(u);
        sourceIndex >= 0 && this.cancelRequestSourceList?.[sourceIndex][u]();
      });
    }
  }

  /** 单独抽离的get工具函数 */
  public get<T, P>(url: string, params?: any): Promise<P> {
    return this.instance.get(url, params);
  }

  /** 单独抽离的post工具函数 */
  public post(url: string, data: any): Promise<any> {
    return this.instance.post(url, data);
  }

  private httpInterceptorsRequest(): void {
    /** 请求白名单，放置一些不需要token的接口（通过设置请求白名单，防止token过期后再请求造成的死循环问题） */
    const whiteList = ["/refreshToken", "/login"];
    this.instance.interceptors.request.use(
      (config) => {
        // 在发送请求之前做什么
        if (config.method === "post") {
          // 序列化
          // config.data = qs.stringify(config.data);
          // config.data = JSON.stringify(config.data);
          // 温馨提示,若公司的提交能直接接受json 格式,可以不用 qs 来序列化的
        } else {
          const store = userInfoStore();
          if (store && store.accessToken) {
            // 若是有做鉴权token , 就给头部带上token
            // 让每个请求携带token-- ['X-Token']为自定义key 请根据实际情况自行修改
            // 若是需要跨站点,存放到 cookie 会好一点,限制也没那么多,有些浏览环境限制了 localstorage (隐身模式)的使用
            console.log(store.accessToken);
          }
        }
        return config;
      },
      (error) => {
        // 对请求错误做些什么，自己定义
        // 使用element-ui的message进行信息提示
        alert("请求发生了错误");
        return Promise.reject(error);
      }
    );
  }

  /** 响应拦截 */
  private httpInterceptorsResponse(): void {
    // 全局响应拦截器保证最后执行
    // this.instance.interceptors.response.use(
    //   (response) => {
    //     // 如果返回的状态码为200，说明接口请求成功，可以正常拿到数据
    //     // 否则的话抛出错误
    //     if (response.status === 200) {
    //       console.log(response);
    //       return Promise.resolve(response.data);
    //     } else {
    //       return Promise.reject(response);
    //     }
    //   },
    //   (error) => {
    //     if (error.response.status) {
    //       error.toJSON();
    //       return Promise.reject(error.response);
    //     }
    //   }
    // );
  }

  /**
   * @description: 获取指定 url 在 cancelRequestSourceList 中的索引
   * @param {string} url
   * @returns {number} 索引位置
   */
  private getSourceIndex(url: string): number {
    return this.cancelRequestSourceList?.findIndex(
      (item: CancelRequestSource) => {
        return Object.keys(item)[0] === url;
      }
    ) as number;
  }

  /**
   * @description: 删除 requestUrlList 和 cancelRequestSourceList
   * @param {string} url
   * @returns {*}
   */
  private delUrl(url: string) {
    const urlIndex = this.requestUrlList?.findIndex((u) => u === url);
    const sourceIndex = this.getSourceIndex(url);
    // 删除url和cancel方法
    urlIndex !== -1 &&
      this.cancelRequestSourceList?.splice(urlIndex as number, 1);
    sourceIndex !== -1 &&
      this.cancelRequestSourceList?.splice(sourceIndex as number, 1);
  }
}

export default QingHttp;
