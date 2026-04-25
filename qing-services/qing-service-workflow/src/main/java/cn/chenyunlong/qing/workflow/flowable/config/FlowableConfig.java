package cn.chenyunlong.qing.workflow.flowable.config;

import lombok.RequiredArgsConstructor;
import org.flowable.engine.parse.BpmnParseHandler;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class FlowableConfig implements EngineConfigurationConfigurer<SpringProcessEngineConfiguration> {

    private final GlobalTaskListenerParseHandler globalTaskListenerParseHandler;

    @Override
    public void configure(SpringProcessEngineConfiguration engineConfiguration) {
        engineConfiguration.setActivityFontName("宋体");
        engineConfiguration.setLabelFontName("宋体");
        engineConfiguration.setAnnotationFontName("宋体");


        List<BpmnParseHandler> customParseHandlers = new ArrayList<>();

        // 获取现有的解析处理器
        if (engineConfiguration.getPostBpmnParseHandlers() != null) {
            customParseHandlers.addAll(engineConfiguration.getPostBpmnParseHandlers());
        }

        // 添加自定义解析处理器
        customParseHandlers.add(globalTaskListenerParseHandler);

        // 重新设置解析处理器
        engineConfiguration.setPostBpmnParseHandlers(customParseHandlers);

    }

}
