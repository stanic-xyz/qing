package chenyunlong.zhangli.task;

import chenyunlong.zhangli.common.service.BilibiliAnimeService;
import chenyunlong.zhangli.common.utils.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 定时任务调度测试
 *
 * @author ruoyi
 */
@Component("zhangliTask")
public class LiTask {

    //数据库操作
    private final BilibiliAnimeService bilibiliAnimeService;


    public LiTask(BilibiliAnimeService bilibiliAnimeService) {
        this.bilibiliAnimeService = bilibiliAnimeService;
    }


    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i) {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void ryParams(String params) {
        System.out.println("执行有参方法：" + params);
    }

    public void ryNoParams() {
        System.out.println("执行无参方法");
    }

    public void syncAnimeList() {
        bilibiliAnimeService.updateAnimeInfo();
    }
}
