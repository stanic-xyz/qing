package cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository.entiry;

import cn.chenyunlong.jpa.support.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * Outbox 实体
 */
@Setter
@Getter
@Entity
@Table(name = "domain_event_outbox")
public class OutboxEventEntity extends BaseJpaEntity {
    @Column(nullable = false, length = 128)
    private String eventType;
    @Column(nullable = false, length = 128)
    private String aggregateType;
    @Column(nullable = false, length = 64)
    private String aggregateId;
    @Column(nullable = false)
    @Lob
    private String payload;
    @Column(nullable = false)
    private Instant occurredAt;
    @Column(nullable = false)
    private Boolean processed = false;
}
