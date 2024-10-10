package cn.chenyunlong.qing.domain.auth.department.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.auth.department.dto.creator.DepartmentCreator;
import cn.chenyunlong.qing.domain.auth.department.dto.query.DepartmentQuery;
import cn.chenyunlong.qing.domain.auth.department.dto.updater.DepartmentUpdater;
import cn.chenyunlong.qing.domain.auth.department.dto.vo.DepartmentVO;
import org.springframework.data.domain.Page;

public interface IDepartmentService {

    /**
     * create
     */
    Long createDepartment(DepartmentCreator creator);

    /**
     * update
     */
    void updateDepartment(DepartmentUpdater updater);

    void validDepartment(Long id);

    void invalidDepartment(Long id);

    /**
     * findById
     */
    DepartmentVO findById(Long id);

    /**
     * findByPage
     */
    Page<DepartmentVO> findByPage(PageRequestWrapper<DepartmentQuery> query);
}
