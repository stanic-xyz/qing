package stan.zhangli.natcross.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * 加密模型，全局使用统一的
 * </p>
 *
 * @author Pluto
 * @since 2020-01-10 09:53:51
 */
@Data
public class SecretModel {

    /**
     * base64格式的AES密钥
     */
    private String aeskey;

    /**
     * 交互签名密钥
     */
    private String tokenKey;

    /**
     * 判断是否启用加密模式
     * 
     * @author Pluto
     * @since 2020-01-10 09:57:55
     * @return
     */
    public boolean isValid() {
        return StringUtils.isNoneBlank(this.getAeskey(), this.getTokenKey());
    }

}
