package chenyunlong.zhangli.task;

import io.swagger.annotations.ApiModelProperty;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
class LiTaskTest {

    @Autowired
    private LiTask liTask;

    @Test
    void syncAnimeList() {
        liTask.syncAnimeList();
    }
}