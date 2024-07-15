package cn.chenyunlong.qing.infrastructure.repository;

import cn.chenyunlong.qing.domain.zan.Zan;
import cn.chenyunlong.qing.domain.zan.repository.ZanRepository;
import cn.chenyunlong.qing.infrastructure.repository.base.JpaServiceImpl;
import cn.chenyunlong.qing.infrastructure.repository.jpa.ZanJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class ZanRepositoryImpl extends JpaServiceImpl<ZanJpaRepository, Zan, Long> implements ZanRepository {

}
