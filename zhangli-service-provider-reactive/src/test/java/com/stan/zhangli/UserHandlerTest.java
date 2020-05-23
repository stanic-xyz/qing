package com.stan.zhangli;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import javax.annotation.PostConstruct;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserHandlerTest extends TestCase {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testHandleGetUsers() {
        FluxExchangeResult<User> result = webTestClient.get().uri("/hello_world").accept(MediaType.APPLICATION_JSON)
                .exchange().returnResult(User.class);
        assert result.getStatus().value() == 200;
        List<User> users = result.getResponseBody().collectList().block();
        assert users.size() == 2;
        assert users.iterator().next().getUser().equals("User1");
    }


    @Test
    public void testHandleGetUserById() {
        System.out.println("thi is fine ");
        assert true;
    }
}