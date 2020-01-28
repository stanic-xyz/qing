package chenyunlong.zhangli.dao;

import chenyunlong.zhangli.entities.SchoolInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolInfoReposutory extends JpaRepository<SchoolInfo, Long> {
}
