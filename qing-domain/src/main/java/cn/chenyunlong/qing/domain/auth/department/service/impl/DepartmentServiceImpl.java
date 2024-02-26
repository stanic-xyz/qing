package cn.chenyunlong.qing.domain.auth.department.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.auth.department.Department;
import cn.chenyunlong.qing.domain.auth.department.dto.creator.DepartmentCreator;
import cn.chenyunlong.qing.domain.auth.department.dto.query.DepartmentQuery;
import cn.chenyunlong.qing.domain.auth.department.dto.updater.DepartmentUpdater;
import cn.chenyunlong.qing.domain.auth.department.dto.vo.DepartmentVO;
import cn.chenyunlong.qing.domain.auth.department.mapper.DepartmentMapper;
import cn.chenyunlong.qing.domain.auth.department.repository.DepartmentRepository;
import cn.chenyunlong.qing.domain.auth.department.service.IDepartmentService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class DepartmentServiceImpl implements IDepartmentService {

    private final DepartmentRepository departmentRepository;

    /**
     * createImpl
     */
    @Override
    public Long createDepartment(DepartmentCreator creator) {
        Optional<Department> department = EntityOperations.doCreate(departmentRepository)
            .create(() -> DepartmentMapper.INSTANCE.dtoToEntity(creator))
            .update(Department::init)
            .execute();
        return department.isPresent() ? department.get().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateDepartment(DepartmentUpdater updater) {
        EntityOperations.doUpdate(departmentRepository)
            .loadById(updater.getId())
            .update(updater::updateDepartment)
            .execute();
    }

    /**
     * valid
     */
    @Override
    public void validDepartment(Long id) {
        EntityOperations.doUpdate(departmentRepository)
            .loadById(id)
            .update(BaseJpaAggregate::valid)
            .execute();
    }

    /**
     * invalid
     */
    @Override
    public void invalidDepartment(Long id) {
        EntityOperations.doUpdate(departmentRepository)
            .loadById(id)
            .update(BaseJpaAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public DepartmentVO findById(Long id) {
        Optional<Department> department = departmentRepository.findById(id);
        return new DepartmentVO(
            department.orElseThrow(() -> new BusinessException(CodeEnum.NotFindError)));
    }

    /**
     * findByPage
     */
    @Override
    public Page<DepartmentVO> findByPage(PageRequestWrapper<DepartmentQuery> query) {
        PageRequest pageRequest =
            PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return departmentRepository.findAll(pageRequest).map(DepartmentVO::new);
    }
}
