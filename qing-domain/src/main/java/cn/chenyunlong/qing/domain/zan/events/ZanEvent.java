package cn.chenyunlong.qing.domain.zan.events;

import cn.chenyunlong.qing.domain.zan.Zan;
import lombok.Value;

public class ZanEvent {

    @Value
    public static class ZanCreateEvent {
        Zan student;
    }

    @Value
    public static class ZanRemoveEvent {
        Zan student;
    }

}
