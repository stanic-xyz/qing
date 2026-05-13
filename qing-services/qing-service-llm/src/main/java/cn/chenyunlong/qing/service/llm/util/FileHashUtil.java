package cn.chenyunlong.qing.service.llm.util;

import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.io.InputStream;

public class FileHashUtil {
    public static String calcMD5(InputStream inputStream) throws IOException {
        return DigestUtils.md5DigestAsHex(inputStream);
    }

    public static String calculateMD5(byte[] data) {
        return DigestUtils.md5DigestAsHex(data);
    }
}
