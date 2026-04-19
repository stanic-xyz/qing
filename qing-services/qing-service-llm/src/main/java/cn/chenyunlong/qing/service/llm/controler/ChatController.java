package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.qing.service.llm.ai.Assistant;
import dev.langchain4j.data.message.*;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@RestController
public class ChatController {

    @RequestMapping("/chat")
    public String chat(@RequestParam("message") String message) {

        OllamaChatModel ollamaChatModel = OllamaChatModel.builder()
                .baseUrl("https://api.minimaxi.com/v1")
                .modelName("MiniMax-M2.7")
                .temperature(1.0)
                .build();

        OpenAiChatModel openAiChatModel = OpenAiChatModel.builder()
                .apiKey("")
                .baseUrl("https://api.minimaxi.com/v1")
                .modelName("MiniMax-M2.7")
                .build();

        // 2. 准备图片和文本内容
        ImageContent imageContent = ImageContent.from("https://pic.code-nav.cn/post_cover/1608440217629360130/JUn2bwobBoBREM7S.webp");
        TextContent textContent = TextContent.from(message);
        UserMessage userMessage = UserMessage.from(textContent, imageContent);

        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(100)
                .build();

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(openAiChatModel)
                .chatMemoryProvider((Object userId) -> chatMemory)
                .build();

        String answer1 = assistant.chat(1, "我叫张三，我喜欢编程。"); // 用户ID为1
        System.out.println("answer1 = " + answer1);
        String answer2 = assistant.chat(1, "你叫什么名字？"); // 模型会基于记忆回答"张三"
        System.out.println("answer2 = " + answer2);

        // 3. 调用模型并打印回复
        ChatResponse response = openAiChatModel.chat(userMessage);

        String text = response.aiMessage().text();
        System.out.println("模型回复: " + text);
        return text;
    }
}
