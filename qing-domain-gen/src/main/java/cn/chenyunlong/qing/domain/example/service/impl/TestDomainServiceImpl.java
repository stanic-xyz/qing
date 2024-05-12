package cn.chenyunlong.qing.domain.example.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.example.TestDomain;
import cn.chenyunlong.qing.domain.example.dto.creator.TestDomainCreator;
import cn.chenyunlong.qing.domain.example.dto.query.TestDomainQuery;
import cn.chenyunlong.qing.domain.example.dto.updater.TestDomainUpdater;
import cn.chenyunlong.qing.domain.example.dto.vo.TestDomainVO;
import cn.chenyunlong.qing.domain.example.mapper.TestDomainMapper;
import cn.chenyunlong.qing.domain.example.repository.TestDomainRepository;
import cn.chenyunlong.qing.domain.example.service.ITestDomainService;
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
public class TestDomainServiceImpl implements ITestDomainService {

    private final TestDomainRepository testDomainRepository;

    /**
     * createImpl
     */
    @Override
    public Long createTestDomain(TestDomainCreator creator) {
        Optional<TestDomain> testDomain = EntityOperations.doCreate(testDomainRepository)
                                              .create(() -> TestDomainMapper.INSTANCE.dtoToEntity(creator))
                                              .update(TestDomain::init)
                                              .execute();
        return testDomain.isPresent() ? testDomain.get().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateTestDomain(TestDomainUpdater updater) {
        EntityOperations.doUpdate(testDomainRepository)
            .loadById(updater.getId())
            .update(updater::updateTestDomain)
            .execute();
    }

    /**
     * valid
     */
    @Override
    public void validTestDomain(Long id) {
        EntityOperations.doUpdate(testDomainRepository)
            .loadById(id)
            .update(BaseJpaAggregate::valid)
            .execute();
    }

    /**
     * invalid
     */
    @Override
    public void invalidTestDomain(Long id) {
        EntityOperations.doUpdate(testDomainRepository)
            .loadById(id)
            .update(BaseJpaAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public TestDomainVO findById(Long id) {
        Optional<TestDomain> testDomain = testDomainRepository.findById(id);
        return new TestDomainVO(testDomain.orElseThrow(() -> new BusinessException(CodeEnum.NotFindError)));
    }

    /**
     * findByPage
     */
    @Override
    public Page<TestDomainVO> findByPage(PageRequestWrapper<TestDomainQuery> query) {
        PageRequest pageRequest = PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return testDomainRepository.findAll(pageRequest).map(TestDomainVO::new);
    }
}
