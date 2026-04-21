// api/base.ts
import axios from 'axios';

const client = axios.create({
  baseURL: '/api/finance',
  timeout: 30000,
});

// 响应拦截器：统一提取 data 字段
client.interceptors.response.use(
  (response) => {
    // 如果是上传接口，保留完整响应
    if (response.config.url?.includes('upload') || response.config.url?.includes('batch')) {
      return response;
    }
    return response.data;
  },
  (error) => {
    console.error('API Error:', error?.response?.data || error.message);
    return Promise.reject(error);
  }
);

export default client;
