package stan.zhangli.natcross.tools;

import junit.framework.TestCase;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author 陈云龙
 * @date 2020/11/18
 **/
public class RSAEncryptTest {

    @Test
    public void testRSA() throws Exception {
        String publicPath = "C:\\Users\\Administrator\\Desktop\\垃圾箱\\key"; //公匙存放位置
        String privatePath = "C:\\Users\\Administrator\\Desktop\\垃圾箱\\key"; //私匙存放位置

        Base64 base64 = new Base64();

        System.out.println("--------------公钥加密私钥解密过程-------------------");
        String signKey = "ihep_公钥加密私钥ff粉粉粉粉粉粉粉顶顶顶顶粉粉粉粉解密";
        // 公钥加密过程
        byte[] cipherData = RSAEncrypt.encrypt(RSAEncrypt.loadPublicKeyByStr(RSAEncrypt.loadPublicKeyByFile(publicPath)),
                signKey.getBytes());
        String cipher = new String(base64.encode(cipherData));
        // 私钥解密过程
        byte[] res = RSAEncrypt.decrypt(RSAEncrypt.loadPrivateKeyByStr(RSAEncrypt.loadPrivateKeyByFile(privatePath)),
                base64.decode(cipher));
        String restr = new String(res);
        System.out.println("原文：" + signKey);
        System.out.println("加密：" + cipher);
        System.out.println("解密：" + restr);
        System.out.println();

        System.out.println("--------------私钥加密公钥解密过程-------------------");
        // 私钥加密过程
        cipherData = RSAEncrypt.encrypt(RSAEncrypt.loadPrivateKeyByStr(RSAEncrypt.loadPrivateKeyByFile(privatePath)), signKey.getBytes());
        cipher = new String(base64.encode(cipherData));
        // 公钥解密过程
        res = RSAEncrypt.decrypt(RSAEncrypt.loadPublicKeyByStr(RSAEncrypt.loadPublicKeyByFile(publicPath)), base64.decode(cipher));
        restr = new String(res);
        System.out.println("原文：" + signKey);
        System.out.println("加密：" + cipher);
        System.out.println("解密：" + restr);
        System.out.println();
        Assert.assertEquals(signKey, restr);
    }
}