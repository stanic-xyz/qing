package stan.zhangli.natcross.common;

import stan.zhangli.natcross.entity.ListenPort;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 * 格式化类
 * </p>
 *
 * @author wangmin1994@qq.com
 * @since 2019-03-28 11:16:26
 */
public final class SystemFormat {

    /**
     * 获取某个端口此时上传的证书文件，保存的文件名
     *
     * @param originName
     * @param listenPort
     * @return
     * @author Pluto
     * @since 2020-01-10 13:21:16
     */
    public static String getListenCertFilename(String originName, ListenPort listenPort) {
        return String.format("P%05d.%s.%s", listenPort.getListenPort(),
                DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()), originName);
    }

}
