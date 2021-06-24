package chenyunlong.zhangli.task;

import chenyunlong.zhangli.model.bilibili.BiliAnime;
import chenyunlong.zhangli.service.BilibiliAnimeService;
import chenyunlong.zhangli.service.external.BiliService;
import chenyunlong.zhangli.utils.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时任务调度测试
 *
 * @author ruoyi
 */
@Component("zhangliTask")
public class LiTask {

    private final BilibiliAnimeService bilibiliAnimeService;
    private final BiliService biliService;

    public LiTask(BilibiliAnimeService bilibiliAnimeService, BiliService biliService) {
        this.bilibiliAnimeService = bilibiliAnimeService;
        this.biliService = biliService;
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
        List<BiliAnime> biliAnimeList = biliService.getBiliAnimeList();
        bilibiliAnimeService.insertBatch(biliAnimeList);
    }
}
