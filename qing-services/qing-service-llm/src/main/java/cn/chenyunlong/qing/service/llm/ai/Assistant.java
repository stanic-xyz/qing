package cn.chenyunlong.qing.service.llm.ai;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;

// 1. 定义 AI 服务接口
public interface Assistant {
    // @MemoryId 用于标识不同用户或会话，是关键注解
    String chat(@MemoryId int userId, @UserMessage String message);
}
