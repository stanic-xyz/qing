package chenyunlong.zhangli.utils;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.junit.Assert;
import org.junit.Test;

@Slf4j
public class HutoolsTest {
    @Test
    public void testHutools() {

        int[] rainbow = PageUtil.rainbow(200, 30, 5);

        for (var index : rainbow) {
            log.info(String.valueOf(index));
        }
    }

    @Test
    public void testBase64() {
        String a = "伦家是一个非常长的字符串";
        String encode = Base64.encode(a);
        Assert.assertEquals("5Lym5a625piv5LiA5Liq6Z2e5bi46ZW/55qE5a2X56ym5Liy", encode);

        String decodeStr = Base64.decodeStr(encode);
        Assert.assertEquals(a, decodeStr);
    }

    /**
     * 测试线程工具
     */
    @Test
    public void testThreat() {
        Thread thread = new Thread(() -> {
            log.info("程序结束了");
        });
        Runtime.getRuntime().addShutdownHook(thread);
        ThreadUtil.excAsync(() ->
        {
            log.info("线程开始执行");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("线程执行完毕！");
        }, false);
    }

    @Test
    public void testCaptchaUtil() {
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(300, 60, 6, 9);
        captcha.write("d:/test.png");
    }
}
