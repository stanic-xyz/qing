package chenyunlong.zhangli.natcross.server;

import chenyunlong.zhangli.natcross.common.SystemFormat;
import chenyunlong.zhangli.natcross.entity.ListenPort;
import chenyunlong.zhangli.natcross.model.CertModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
     * @param certFile
     * @param listenPort
     * @throws Exception
     * @author Pluto
     * @since 2020-01-10 13:24:51
     */
    public String saveCertFile(MultipartFile certFile, ListenPort listenPort) throws Exception {
        String listenCertFilename = SystemFormat.getListenCertFilename(certFile.getOriginalFilename(), listenPort);
        String formatCertPath = certModel.formatCertPath(listenCertFilename);
        certFile.transferTo(new File(formatCertPath));
        return listenCertFilename;
    }

}
