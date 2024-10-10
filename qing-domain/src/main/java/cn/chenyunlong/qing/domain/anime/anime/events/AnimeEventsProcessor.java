package cn.chenyunlong.qing.domain.anime.anime.events;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class AnimeEventsProcessor {


    @TransactionalEventListener(value = AnimeEvents.AnimeCreated.class, phase = TransactionPhase.BEFORE_COMMIT)
    public void processAnimeEvent(AnimeEvents.AnimeCreated animeCreated) {
        String tags = animeCreated.getAnime().getTags();

        // 实现事件处理逻辑
        System.out.println("处理AnimeCreated事件：" + animeCreated.getAnime().getName());
    }

}
