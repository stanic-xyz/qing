package chenyunlong.zhangli.dao;

import chenyunlong.zhangli.entities.SchoolInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface SchoolInfoReposutory extends JpaRepository<SchoolInfo, Long> {
}
