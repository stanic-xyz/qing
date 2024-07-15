package cn.chenyunlong.qing.infrastructure.repository.auth;

import cn.chenyunlong.qing.domain.auth.department.Department;
import cn.chenyunlong.qing.domain.auth.department.repository.DepartmentRepository;
import cn.chenyunlong.qing.infrastructure.repository.base.JpaServiceImpl;
import cn.chenyunlong.qing.infrastructure.repository.jpa.DepartmentJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class DepartmentRepositoryImpl extends JpaServiceImpl<DepartmentJpaRepository, Department, Long> implements DepartmentRepository {

}
