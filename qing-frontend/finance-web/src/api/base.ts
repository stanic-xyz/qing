// api/base.ts
import axios from 'axios';

const client = axios.create({
  baseURL: '/api/finance',
  timeout: 30000,
});

// 响应拦截器：统一处理响应结构
client.interceptors.response.use(
  (response) => {
    // 如果是上传接口，保留完整响应
    if (response.config.url?.includes('upload') || response.config.url?.includes('batch')) {
      return response;
    }

    // 检查业务状态码
    const resData = response.data;
    if (resData && resData.code !== 200) {
      // 业务错误，抛出带有业务错误信息的错误
      const error = new Error(resData.message || '请求失败');
      (error as any).businessCode = resData.code;
      (error as any).response = resData;
      return Promise.reject(error);
    }

    // 成功时返回 data 字段
    return resData.data;
  },
  (error) => {
    console.error('API Error:', error?.response?.data || error.message);
    return Promise.reject(error);
  }
);

export default client;
