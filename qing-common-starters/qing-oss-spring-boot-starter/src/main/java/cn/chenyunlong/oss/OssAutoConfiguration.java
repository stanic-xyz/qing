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

package cn.chenyunlong.oss;

import cn.chenyunlong.oss.client.OssClient;
import cn.chenyunlong.oss.client.S3OssClient;
import cn.chenyunlong.oss.config.OssProperties;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;
import java.util.stream.Stream;

@Configuration
@EnableConfigurationProperties(OssProperties.class)
public class OssAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(S3OssClient.class)
    public OssClient ossClient(AmazonS3 amazonS3) {
        return new S3OssClient(amazonS3);
    }


    /**
     * 参考文档
     * <a href="https://docs.aws.amazon.com/zh_cn/sdk-for-java/v1/developer-guide/credentials.html">...</a>
     * 区域选择这块
     * <a href="https://docs.aws.amazon.com/zh_cn/sdk-for-java/v1/developer-guide/java-dg-region-selection.html">...</a>
     *
     * @param ossProperties 欧松参数
     * @return S3客户端
     */
    @Bean
    @ConditionalOnMissingBean(AmazonS3.class)
    @ConditionalOnProperty(prefix = "oss", name = "enable", havingValue = "true")
    public AmazonS3 amazonS3(OssProperties ossProperties) {
        long nullSize = Stream
                .<String>builder()
                .add(ossProperties.getEndpoint())
                .add(ossProperties.getAccessSecret())
                .add(ossProperties.getAccessKey())
                .build()
                .filter(Objects::isNull)
                .count();
        if (nullSize > 0) {
            throw new RuntimeException("oss 配置错误,请检查");
        }
        AWSCredentials awsCredentials = new BasicAWSCredentials(ossProperties.getAccessKey(), ossProperties.getAccessSecret());
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
        return AmazonS3Client
                .builder()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(ossProperties.getEndpoint(), ossProperties.getRegion()))
                .withCredentials(awsCredentialsProvider)
                .disableChunkedEncoding()
                .withPathStyleAccessEnabled(ossProperties.isPathStyleAccess())
                .build();
    }
}
