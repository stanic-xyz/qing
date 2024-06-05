package cn.chenyunlong.qing.application.manager.repository;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.jpa.support.BaseRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public abstract class JpaServiceImpl<R extends BaseJpaRepository<T, ID>, T, ID> implements BaseRepository<T, ID> {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
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
}
