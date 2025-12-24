package cn.chenyunlong.qing.auth.domain.user.valueObject;

import cn.chenyunlong.qing.domain.common.ValueObject;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;

/**
 * 加密密码值对象
 */
public record EncryptedPassword(String value) implements ValueObject, Comparable<EncryptedPassword> {

    public EncryptedPassword {
        if (value == null || value.length() < 6) {
            throw new IllegalArgumentException("密码长度至少6位");
        }
    }

    /**
     * 使用 BCrypt 对明文密码进行加密并返回加密后的值对象
     *
     * @param plainPassword 明文密码
     * @return 加密后的密码值对象（BCrypt）
     */
    public static EncryptedPassword of(String plainPassword) {
        String encrypted = DigestUtil.bcrypt(plainPassword);
        return new EncryptedPassword(encrypted);
    }

    public static EncryptedPassword ofEncrypted(String encryptedPassword) {
        return new EncryptedPassword(encryptedPassword);
    }

    /**
     * 校验明文密码是否与当前加密密码匹配（BCrypt 校验）
     *
     * @param plainPassword 明文密码
     * @return 是否匹配
     */
    public boolean matches(String plainPassword) {
        return DigestUtil.bcryptCheck(plainPassword, value);
    }

    @Override
    public int compareTo(EncryptedPassword encryptedPassword) {
        return StrUtil.compare(value, encryptedPassword.value, true);
    }
}
