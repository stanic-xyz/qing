import {http} from "@/utils/service";

const service_url = "qing-service-anime";

export const login = function (loginForm: any) {
    return http.post(service_url + "/user/login", loginForm);
};

export const menu = function () {
    return http.get(service_url + "/user/menu");
};

export const permission = function () {
    let param;
    return http.get(service_url + "/user/permission", param);
};

export const userInfo = function (params: any) {
    return http.get(service_url + "user/getUserInfo");
};
