package cn.chenyunlong.qing.service.llm.repository;

import cn.chenyunlong.qing.service.llm.entity.ParserConfig;
import cn.chenyunlong.qing.service.llm.enums.ConfigStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParserConfigRepository extends JpaRepository<ParserConfig, Long> {
    List<ParserConfig> findAllByStatusEquals(ConfigStatusEnum status);
}
