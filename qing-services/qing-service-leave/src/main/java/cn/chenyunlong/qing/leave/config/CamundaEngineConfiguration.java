package cn.chenyunlong.qing.leave.config;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class CamundaEngineConfiguration {

    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean
    public ProcessEngine processEngine(DataSource dataSource) {
        return ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration()
            .setDataSource(dataSource)
            .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE)
            .buildProcessEngine();
    }

    @Bean
    @ConditionalOnMissingBean
    public RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }

    @Bean
    @ConditionalOnMissingBean
    public TaskService taskService(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }

    @Bean
    @ConditionalOnMissingBean
    public RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }
}
