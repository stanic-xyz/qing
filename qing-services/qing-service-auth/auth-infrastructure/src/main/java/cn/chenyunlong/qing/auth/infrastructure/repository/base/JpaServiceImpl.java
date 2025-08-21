package cn.chenyunlong.qing.infrastructure.auth.repository.base;

import cn.chenyunlong.jpa.support.BaseJpaQueryRepository;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
import cn.chenyunlong.qing.domain.common.repository.BaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Slf4j
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public abstract class JpaServiceImpl<R extends BaseJpaQueryRepository<T, ID>, T extends BaseAggregate, ID extends AggregateId>
    implements BaseRepository<T, ID>, InitializingBean {

    /**
     * **@Resource** 默认是按照名称匹配的，所以这里会如果使用@Resource的话，就会匹配到很多的实现
     */
    @Autowired
    protected R baseJpaRepository;

    @Override
    public void afterPropertiesSet() {
        log.debug("初始化Jpa仓储类型：{}", baseJpaRepository.getClass().getName());
    }

    @Override
    public T save(T entity) {
        return baseJpaRepository.save(entity);
    }

    @Override
    public Optional<T> findById(ID id) {
        return baseJpaRepository.findById(id);
    }

}
