package cn.chenyunlong.qing.infrastructure.repository;

import cn.chenyunlong.qing.domain.anime.type.Type;
import cn.chenyunlong.qing.domain.anime.type.repository.TypeRepository;
import cn.chenyunlong.qing.infrastructure.repository.base.JpaServiceImpl;
import cn.chenyunlong.qing.infrastructure.repository.jpa.TypeJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class TypeRepositoryImpl extends JpaServiceImpl<TypeJpaRepository, Type, Long> implements TypeRepository {

}
