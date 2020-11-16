package stan.zhangli.natcross.model;

import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * <p>
 * 加密模型，全局使用统一的
 * </p>
 *
 * @author Pluto
 * @since 2020-01-10 09:53:51
 */
@Data
@EqualsAndHashCode
public class SecretModel {

    /**
     * base64格式的AES密钥
     */
    private String aeskey;

    /**
     * 交互签名密钥
     */
    private String tokenKey;

    public SecretModel() {
    }

    /**
     * 判断是否启用加密模式
     *
     * @return 返回是否启用加密模式
     * @author Pluto
     * @since 2020-01-10 09:57:55
     */
    public boolean isValid() {
        return StringUtils.isNoneBlank(this.getAeskey(), this.getTokenKey());
    }

}
