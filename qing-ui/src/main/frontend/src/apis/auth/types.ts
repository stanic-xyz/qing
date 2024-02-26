export type UserInfo = {
  username: string;
  nickname: string;
  token: string;
};

export type RefreshTokenResult = {
  success: boolean;
  data: {
    /** `token` */
    accessToken: string;
    /** 用于调用刷新`accessToken`的接口时所需的`token` */
    refreshToken: string;
    /** `accessToken`的过期时间（格式'xxxx/xx/xx xx:xx:xx'） */
    expires: Date;
  };
};

export type UserResult = {
  success: boolean;
  data: {
    /** 用户名 */
    username: string;
    /** 当前登陆用户的角色 */
    roles: Array<string>;
    /** `token` */
    accessToken: string;
    /** 用于调用刷新`accessToken`的接口时所需的`token` */
    refreshToken: string;
    /** `accessToken`的过期时间（格式'xxxx/xx/xx xx:xx:xx'） */
    expires: Date;
  };
};

type Directions = Extract<Direction, "desc" | "asc">;

type Direction = "desc" | "DESC" | "asc" | "ASC";

interface Sort {
  column: string;
  direction: Directions;
}

/**
 * @description 整体路由配置表（包括完整子路由）
 */
export interface PageRequest<T> {
  /**
   *查询参数
   */
  bean?: T;
  /** 当前页 */
  page: number;
  /** 分页大小 */
  pageSize?: number;

  /**
   * 排序
   */
  sorts?: Sort[];
}
