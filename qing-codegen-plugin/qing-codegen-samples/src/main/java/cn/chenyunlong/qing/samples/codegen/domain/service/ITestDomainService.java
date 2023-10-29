package cn.chenyunlong.qing.samples.codegen.domain.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.samples.codegen.domain.dto.creator.TestDomainCreator;
import cn.chenyunlong.qing.samples.codegen.domain.dto.query.TestDomainQuery;
import cn.chenyunlong.qing.samples.codegen.domain.dto.updater.TestDomainUpdater;
import cn.chenyunlong.qing.samples.codegen.domain.dto.vo.TestDomainVO;
import java.lang.Long;
import org.springframework.data.domain.Page;

public interface ITestDomainService {
    /**
     * create
     */
    Long createTestDomain(TestDomainCreator creator);

    /**
     * update
     */
    void updateTestDomain(TestDomainUpdater updater);

    /**
     * valid
     */
    void validTestDomain(Long id);

    /**
     * invalid
     */
    void invalidTestDomain(Long id);

    /**
     * findById
     */
    TestDomainVO findById(Long id);

    /**
     * findByPage
     */
    Page<TestDomainVO> findByPage(PageRequestWrapper<TestDomainQuery> query);
}
