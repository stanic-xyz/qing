package chenyunlong.zhangli.utils;


import chenyunlong.zhangli.model.bilibili.AnimeData;
import chenyunlong.zhangli.model.bilibili.BgngumeResponse;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class AnimeInfoUtilTest {

    @Test
    public void testLocalDate() {

        long size = 20;
        long total = 0;
        long hasNext = 0;
        long num = 0;

        String bangumiInfo = HttpUtil.sendGet("https://api.bilibili.com/pgc/season/index/result?season_version=-1&area=-1&is_finish=-1&copyright=-1&season_status=-1&season_month=-1&year=-1&style_id=-1&order=4&st=1&sort=0&page=1&season_type=1&pagesize=20&type=1");

        BgngumeResponse response = new Gson().fromJson(bangumiInfo, BgngumeResponse.class);
        if (response != null && response.getCode() == 0 && response.getData() != null) {
            AnimeData responseData = response.getData();
            total = responseData.getTotal();
            hasNext = responseData.getHasNext();
            num = responseData.getNum();
        }
    }
}
