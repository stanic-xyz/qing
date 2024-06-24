package cn.chenyunlong.qing.application.manager.repository;

import cn.chenyunlong.qing.application.manager.repository.jpa.TypeJpaRepository;
import cn.chenyunlong.qing.domain.anime.type.Type;
import cn.chenyunlong.qing.domain.anime.type.repository.TypeRepository;
import org.springframework.stereotype.Service;

@Service
public class TypeRepositoryImpl extends JpaServiceImpl<TypeJpaRepository, Type, Long> implements TypeRepository {

}
