package stan.zhangli.natcross.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import stan.zhangli.natcross.common.SystemFormat;
import stan.zhangli.natcross.entity.ListenPort;
import stan.zhangli.natcross.model.CertModel;

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
    public String saveCertFile(MultipartFile certFile, ListenPort listenPort) throws Exception {
        String listenCertFilename = SystemFormat.getListenCertFilename(certFile.getOriginalFilename(), listenPort);
        String formatCertPath = certModel.formatCertPath(listenCertFilename);
        certFile.transferTo(new File(formatCertPath));
        return listenCertFilename;
    }

}
