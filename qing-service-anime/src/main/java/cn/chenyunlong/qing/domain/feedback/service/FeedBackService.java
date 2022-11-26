/*
 * Copyright (c) 2019-2022 YunLong Chen
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

package cn.chenyunlong.qing.domain.feedback.service;

/**
 * @author Stan
 */
public interface FeedBackService {
    /**
     * 添加反馈信息
     *
     * @param username    用户名
     * @param cid         动漫ID
     * @param linkInvalid 链接无效
     * @param someMissing 内容确实
     * @param badQuality  质量果茶
     * @param detail      其他详情
     */
    void addReport(String username, Long cid, boolean linkInvalid, boolean someMissing, boolean badQuality, String detail);
}
