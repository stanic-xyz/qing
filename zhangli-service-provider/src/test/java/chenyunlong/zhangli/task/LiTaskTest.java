package chenyunlong.zhangli.task;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
class LiTaskTest {

    @Autowired
    private LiTask liTask;

    @Test
    void syncAnimeList() {
        liTask.syncAnimeList();
    }
}