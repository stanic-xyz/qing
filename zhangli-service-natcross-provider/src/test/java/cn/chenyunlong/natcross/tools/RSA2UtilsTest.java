package cn.chenyunlong.natcross.tools;

import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @author 陈云龙
 * @date 2020/11/18
 **/
public class RSA2UtilsTest {

    @Test
    public void testCreateKeys() {
        Map<String, String> keys = RSA2Utils.createKeys(1024);
        keys.keySet().forEach(key -> System.out.println(key + "\t:" + keys.get(key)));
    }

    public void testGetPublicKey() {
    }

    public void testGetPrivateKey() {
    }

    public void testPublicEncrypt() {
    }

    public void testPrivateDecrypt() {
    }

    public void testPrivateEncrypt() {
    }

    public void testPublicDecrypt() {
    }
}