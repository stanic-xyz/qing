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

package cn.chenyunlong.qing.infrastructure.mail;

import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.lang.NonNull;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Properties;

/**
 * Java mail sender factory.
 *
 * @author johnniang
 */
public class MailSenderFactory {

    /**
     * Get mail sender.
     *
     * @param mailProperties mail properties must not be null
     * @return java mail sender
     */
    @NonNull
    public JavaMailSender getMailSender(@NonNull MailProperties mailProperties) {
        Assert.notNull(mailProperties, "Mail properties must not be null");

        // create mail sender
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        // set properties
        setProperties(mailSender, mailProperties);

        return mailSender;
    }

    private void setProperties(@NonNull JavaMailSenderImpl mailSender,
                               @NonNull MailProperties mailProperties) {
        mailSender.setHost(mailProperties.getHost());
        mailSender.setPort(mailProperties.getPort());
        mailSender.setUsername(mailProperties.getUsername());
        mailSender.setPassword(mailProperties.getPassword());
        mailSender.setProtocol(mailProperties.getProtocol());
        mailSender.setDefaultEncoding(mailProperties.getDefaultEncoding().name());

        if (!CollectionUtils.isEmpty(mailProperties.getProperties())) {
            Properties properties = new Properties();
            properties.putAll(mailProperties.getProperties());
            mailSender.setJavaMailProperties(properties);
        }
    }
}
