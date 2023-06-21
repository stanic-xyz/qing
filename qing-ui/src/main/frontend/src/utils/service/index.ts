import QingHttp from "@/utils/http";

interface QingResponse<T> {
  code: number;
  message: string;
  data: T;
}

export const qingHttp = new QingHttp();
