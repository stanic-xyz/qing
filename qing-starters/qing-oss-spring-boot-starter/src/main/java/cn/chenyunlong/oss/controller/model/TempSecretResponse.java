/*
 * Copyright (c) 2023  YunLong Chen
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

package cn.chenyunlong.oss.controller.model;

import lombok.Data;

/**
 * 临时密钥。
 */
@Data
public class TempSecretResponse {

    /**
     * 临时密钥ID。
     */
    private String tmpSecretId;

    /**
     * 临时密钥。
     */
    private String tmpSecretKey;

    /**
     * 临时密钥的accessToken。
     */
    private String sessionToken;

    /**
     * 开始时间
     */
    public long startTime;

    /**
     * 过期时间
     */
    public long expiredTime;

    /**
     * 存储桶
     */
    private String bucketName;

    /**
     * 区域
     */
    private String region;

}
