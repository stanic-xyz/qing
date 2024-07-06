package cn.chenyunlong.qing.infrastructure.repository.base;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.jpa.support.BaseRepository;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@Slf4j
public abstract class JpaServiceImpl<R extends BaseJpaRepository<T, ID>, T, ID> implements BaseRepository<T, ID>, InitializingBean {

    // @Resource 默认是按照名称匹配的，所以这里会如果使用@Resource的话，就会匹配到很多的实现
    @Autowired
    protected R baseJpaRepository;

    @Override
    public T save(T entity) {
        return baseJpaRepository.save(entity);
    }

    @Override
    public Optional<T> findById(ID id) {
        return baseJpaRepository.findById(id);
    }

    @Override
    public void deleteById(ID id) {
        baseJpaRepository.deleteById(id);
    }

    @Override
    public Page<T> findAll(PageRequest pageRequest) {
        return baseJpaRepository.findAll(pageRequest);
    }

    @Override
    public List<T> findByIds(List<ID> ids) {
        return baseJpaRepository.findAllById(ids);
    }

    @Override
    public void deleteAllByIds(List<ID> ids) {
        baseJpaRepository.deleteAllById(ids);
    }

    @Override
    public void saveAll(List<T> domainList) {
        baseJpaRepository.saveAll(domainList);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("初始化：{}", baseJpaRepository);
    }
}
