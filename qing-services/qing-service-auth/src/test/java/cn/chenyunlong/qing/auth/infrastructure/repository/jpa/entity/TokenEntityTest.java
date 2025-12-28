package cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity;

import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository.TokenJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
@DisplayName("Hibernate 6.6+ @GeneratedValue 行为测试")
class TokenEntityTest {

    @Autowired
    private TokenJpaRepository tokenJpaRepository;

    @Test
    @Rollback
    void testTokenEntity() {
        TokenEntity tokenEntity = new TokenEntity();

        tokenEntity.setId(1L);
        tokenEntity.setVersion(0);
        tokenEntity.setTokenValue("12345");

        TokenEntity saved = tokenJpaRepository.save(tokenEntity);
        Assertions.assertNotNull(saved);
        Assertions.assertNotNull(saved.getId());
        Assertions.assertNotNull(saved.getVersion());
        Assertions.assertNotNull(saved.getCreatedAt());
    }
}
