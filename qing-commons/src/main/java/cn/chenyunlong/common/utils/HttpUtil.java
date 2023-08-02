/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.common.utils;


import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import java.net.URI;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author 陈云龙
 * @date 2021/01/04
 **/
@Slf4j
public class HttpUtil {

    /**
     * 使用RestTemplate作为Http请求的工具
     */
    private final static RestTemplate REST_TEMPLATE = new RestTemplate();
    private static final int READ_TIMEOUT = 10000;
    private static final int CONNECTION_TIMEOUT = 5000;

    static {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10000);
        requestFactory.setReadTimeout(10000);
        //添加编码，RestTemplate默认编码器是ISO-8859-1,如果接口后台服务对编码进行了控制，会发生中文乱码的情况
        //REST_TEMPLATE.getMessageConverters().set(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        REST_TEMPLATE.setRequestFactory(requestFactory);
    }

    /**
     * get请求
     *
     * @param url 请求地址
     */
    public static String sendGet(String url) {
        return sendGet(url, null, null);
    }

    /**
     * Get请求，Map参数
     *
     * @param url          URL地址
     * @param parameterMap 参数信息
     * @param body         请求实体（请求体）
     * @return 请求结果
     */
    public static String sendGet(String url, Map<String, String> parameterMap, Object body) {
        return sendGet(url, null, parameterMap, body);
    }

    /**
     * Get请求，Map参数
     *
     * @param url          主接口地址
     * @param parameterMap 请求路径参数
     * @param headerMap    请求头
     */
    public static String sendGet(String url, Map<String, String> headerMap,
                                 Map<String, String> parameterMap, Object body) {
        return execute(Method.GET, url, headerMap, parameterMap, body);
    }

    /**
     * Get请求，Map参数
     *
     * @param url          主接口地址
     * @param parameterMap 请求路径参数
     * @param headerMap    请求头
     */
    public static String execute(Method method, String url, Map<String, String> headerMap,
                                 Map<String, String> parameterMap, Object body) {

        HttpHeaders headers = new HttpHeaders();
        if (headerMap != null) {
            headerMap.forEach(headers::set);
        }
        String requestBody = JSONObject.toJSONString(body);
        //添加请求体，通过json方式传递值
        if (body != null) {
            requestBody = JSONObject.toJSONString(body);
        }
        headers.setContentType(MediaType.APPLICATION_JSON);

        URI uri = buildUri(url, parameterMap);
        HttpEntity<String> stringHttpEntity = new HttpEntity<>(requestBody, headers);
        String result = "";
        try {

            cn.hutool.http.HttpRequest httpRequest =
                cn.hutool.http.HttpUtil.createRequest(method, uri.toString());
            httpRequest.body(stringHttpEntity.getBody());
            httpRequest.addHeaders(headerMap);
            httpRequest.contentType(MediaType.APPLICATION_JSON_VALUE);
            httpRequest.setReadTimeout(READ_TIMEOUT);
            httpRequest.setConnectionTimeout(CONNECTION_TIMEOUT);
            HttpResponse response = httpRequest.execute();
            if (response.isOk()) {
                result = response.body();
                if (log.isDebugEnabled()) {
                    log.debug("URL   \t: {}", uri);
                    try {
                        log.debug("result\t:\n{}", JSONObject.toJSONString(JSONObject.parse(result),
                            SerializerFeature.PrettyFormat));
                    } catch (Exception exception) {
                        //不是json结构的数据直接输出
                        log.debug("result\t:\n{}", result);
                    }
                }
            } else {
                if (log.isErrorEnabled()) {
                    log.error("请求失败");
                    log.error("url \t: {}", uri);
                    log.error("code\t: {}", response.getStatus());
                    log.error("result\t:\n{}", response.body());
                }
            }
        } catch (Exception exp) {
            log.error("接口请求发生异常", exp);
        }
        return result;
    }

    /**
     * 构建请求地址
     *
     * @param url          URL
     * @param parameterMap 路径参数
     * @return uri
     */
    private static URI buildUri(String url, Map<String, String> parameterMap) {
        UriComponentsBuilder urlComponent = UriComponentsBuilder.fromHttpUrl(url);
        if (parameterMap != null && !parameterMap.isEmpty()) {
            LinkedMultiValueMap<String, String> parameterValueMap = new LinkedMultiValueMap<>();
            parameterMap.forEach(parameterValueMap::add);
            urlComponent.queryParams(parameterValueMap);
        }
        return urlComponent.build().toUri();
    }

    /**
     * Get请求，Map参数
     *
     * @param url          URL地址
     * @param parameterMap 参数信息（query参数）
     * @return 请求结果
     */
    public static String sendGet(String url, Map<String, String> parameterMap) {
        return sendGet(url, null, parameterMap, null);
    }

    /**
     * Post请求
     *
     * @param url 请求地址
     */
    public static String sendPost(String url) {
        return sendPost(url, null, null);
    }

    /**
     * Post请求，Map参数
     *
     * @param url          URL地址
     * @param parameterMap 参数信息
     * @param body         请求实体（请求体）
     * @return 请求结果
     */
    public static String sendPost(String url, Map<String, String> parameterMap, Object body) {
        return sendPost(url, null, parameterMap, body);
    }

    /**
     * Post请求，Map参数
     *
     * @param url          主接口地址
     * @param parameterMap 请求路径参数
     * @param headerMap    请求头
     */
    public static String sendPost(String url, Map<String, String> headerMap,
                                  Map<String, String> parameterMap, Object body) {
        return execute(Method.POST, url, headerMap, parameterMap, body);
    }

    /**
     * Get请求，Map参数
     *
     * @param url          URL地址
     * @param parameterMap 参数信息（query参数）
     * @return 请求结果
     */
    public static String sendPost(String url, Map<String, String> parameterMap) {
        return sendPost(url, null, parameterMap, null);
    }

    /**
     * Post请求
     *
     * @param url 请求地址
     */
    public static String sendPut(String url) {
        return sendPut(url, null, null);
    }

    /**
     * Post请求，Map参数
     *
     * @param url          URL地址
     * @param parameterMap 参数信息
     * @param body         请求实体（请求体）
     * @return 请求结果
     */
    public static String sendPut(String url, Map<String, String> parameterMap, Object body) {
        return sendPut(url, null, parameterMap, body);
    }

    /**
     * Post请求，Map参数
     *
     * @param url          主接口地址
     * @param parameterMap 请求路径参数
     * @param headerMap    请求头
     */
    public static String sendPut(String url, Map<String, String> headerMap,
                                 Map<String, String> parameterMap, Object body) {
        return execute(Method.PUT, url, headerMap, parameterMap, body);
    }

    /**
     * Get请求，Map参数
     *
     * @param url          URL地址
     * @param parameterMap 参数信息（query参数）
     * @return 请求结果
     */
    public static String sendPut(String url, Map<String, String> parameterMap) {
        return sendPut(url, null, parameterMap, null);
    }
}
