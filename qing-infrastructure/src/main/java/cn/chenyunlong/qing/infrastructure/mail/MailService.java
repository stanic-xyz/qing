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

package cn.chenyunlong.qing.infrastructure.mail;

import java.util.Map;

/**
 * 邮件服务接口.
 *
 * @author Stan
 * @date 2020/03/22
 */
public interface MailService {

    /**
     * 发送一个简单邮件
     *
     * @param to      recipient
     * @param subject subject
     * @param content content
     */
    void sendTextMail(String to, String subject, String content);

    /**
     * Send a email with html
     *
     * @param to           recipient
     * @param subject      subject
     * @param content      content
     * @param templateName template name
     */
    void sendTemplateMail(String to, String subject, Map<String, Object> content, String templateName);

    /**
     * Send mail with attachments
     *
     * @param to             recipient
     * @param subject        subject
     * @param content        content
     * @param templateName   template name
     * @param attachFilePath attachment full path name
     */
    void sendAttachMail(String to, String subject, Map<String, Object> content, String templateName, String attachFilePath);

    /**
     * Test email server connection.
     */
    void testConnection();
}
