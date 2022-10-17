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
 */

package cn.chenyunlong.natcross.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import cn.chenyunlong.natcross.common.SystemFormat;
import cn.chenyunlong.natcross.model.CertModel;

import java.io.File;

/**
 * <p>
 * 文件服务
 * </p>
 *
 * @author Pluto
 * @since 2020-01-10 13:01:28
 */
@Service
public class FileServer {

    @Autowired
    private CertModel certModel;

    /**
     * 保存证书文件
     *
     * @param certFile   正式文件
     * @param listenPort 监听端口
     * @throws Exception 保存文件异常信息
     * @author Pluto
     * @since 2020-01-10 13:24:51
     */
    public String saveCertFile(MultipartFile certFile, Integer listenPort) throws Exception {
        String listenCertFilename = SystemFormat.getListenCertFilename(certFile.getOriginalFilename(), listenPort);
        String formatCertPath = certModel.formatCertPath(listenCertFilename);
        certFile.transferTo(new File(formatCertPath));
        return listenCertFilename;
    }

}
