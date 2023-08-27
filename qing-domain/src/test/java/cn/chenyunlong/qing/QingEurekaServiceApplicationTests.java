package cn.chenyunlong.qing;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest(classes = QingBootstrapServiceApplication.class)
public class QingEurekaServiceApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    WebTestClient client;

    @BeforeEach
    void setUp() {
        client = WebTestClient.bindToApplicationContext(applicationContext)
            .configureClient()
            .baseUrl("/graphql")
            .build();

    }

    @Test
    @DisplayName("应用启动测试")
    void contextLoads() {
        log.info("应用启动成功，测试成功");
    }

    @Test
    @DisplayName("测试GraphQL")
    void graphContextLoads() {
        log.info("应用启动成功，测试成功");
        HttpGraphQlTester tester = HttpGraphQlTester.create(client);

        String document = """
              {
              project(slug:"spring-framework") {
               releases {
                 version
               }
              }
            }
            """;
        tester.document(document)
            .execute()
            .path("project.release[*].version")
            .entityList(String.class)
            .hasSizeGreaterThan(1);
    }

}

