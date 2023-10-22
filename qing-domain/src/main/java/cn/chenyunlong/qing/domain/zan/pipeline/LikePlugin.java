package cn.chenyunlong.qing.domain.zan.pipeline;

import org.springframework.plugin.core.Plugin;

public interface LikePlugin<T> extends Plugin<T> {

    /**
     * 处理时间。
     *
     * @param likeModel 点赞业务上下文
     */
    void handlerLikeModel(T likeModel);

}
