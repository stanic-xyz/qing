package cn.chenyunlong.natcross.common;

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
     * @param originName 证书原始名称
     * @param listenPort 监听端口
     * @return 获取正式文件保存的文件名
     * @author Pluto
     * @since 2020-01-10 13:21:16
     */
    public static String getListenCertFilename(String originName, Integer listenPort) {
        return String.format("P%05d.%s.%s",
                listenPort,
                DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()),
                originName);
    }

}
