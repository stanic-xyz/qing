package cn.chenyunlong.qing.service.llm.controler;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.output.Response;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
public class ChatController {

    @RequestMapping("/chat")
    public String chat(@RequestParam("message") String message) {
        OllamaChatModel ollamaChatModel = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("deepseek-r1:1.5b")
                .temperature(0.0)
                .build();
        String generate = ollamaChatModel.chat(message);
        System.out.println(generate);
        return "Hello, " + generate;
    }
}
