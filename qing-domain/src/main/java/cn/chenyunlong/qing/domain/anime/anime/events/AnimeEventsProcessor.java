package cn.chenyunlong.qing.domain.anime.anime.events;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AnimeEventsProcessor {


    @EventListener(AnimeEvents.AnimeCreated.class)
    public void processAnimeEvent(AnimeEvents.AnimeCreated animeCreated) {

        String tags = animeCreated.getAnime().getTags();

        // 实现事件处理逻辑
        System.out.println("处理AnimeCreated事件：" + animeCreated.getAnime().getName());
    }

}
