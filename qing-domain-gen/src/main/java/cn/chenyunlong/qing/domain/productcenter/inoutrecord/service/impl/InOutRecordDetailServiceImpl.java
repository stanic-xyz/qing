package cn.chenyunlong.qing.domain.productcenter.inoutrecord.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.InOutRecordDetail;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.creator.InOutRecordDetailCreator;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.query.InOutRecordDetailQuery;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.updater.InOutRecordDetailUpdater;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.vo.InOutRecordDetailVO;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.mapper.InOutRecordDetailMapper;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.repository.InOutRecordDetailRepository;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.service.IInOutRecordDetailService;
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
public class InOutRecordDetailServiceImpl implements IInOutRecordDetailService {

    private final InOutRecordDetailRepository inOutRecordDetailRepository;

    /**
     * createImpl
     */
    @Override
    public Long createInOutRecordDetail(InOutRecordDetailCreator creator) {
        Optional<InOutRecordDetail> inOutRecordDetail =
            EntityOperations.doCreate(inOutRecordDetailRepository)
                .create(() -> InOutRecordDetailMapper.INSTANCE.dtoToEntity(creator))
                .update(InOutRecordDetail::init)
                .execute();
        return inOutRecordDetail.isPresent() ? inOutRecordDetail.get().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateInOutRecordDetail(InOutRecordDetailUpdater updater) {
        EntityOperations.doUpdate(inOutRecordDetailRepository)
            .loadById(updater.getId())
            .update(updater::updateInOutRecordDetail)
            .execute();
    }

    @Override
    public void validInOutRecordDetail(Long id) {
        EntityOperations.doUpdate(inOutRecordDetailRepository)
                .loadById(id)
                .update(BaseJpaAggregate::valid)
                .execute();
    }

    @Override
    public void invalidInOutRecordDetail(Long id) {
        EntityOperations.doUpdate(inOutRecordDetailRepository)
            .loadById(id)
            .update(BaseJpaAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public InOutRecordDetailVO findById(Long id) {
        Optional<InOutRecordDetail> inOutRecordDetail = inOutRecordDetailRepository.findById(id);
        return new InOutRecordDetailVO(
            inOutRecordDetail.orElseThrow(() -> new BusinessException(CodeEnum.NotFoundError)));
    }

    /**
     * findByPage
     */
    @Override
    public Page<InOutRecordDetailVO> findByPage(PageRequestWrapper<InOutRecordDetailQuery> query) {
        PageRequest pageRequest =
            PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return inOutRecordDetailRepository.findAll(pageRequest).map(InOutRecordDetailVO::new);
    }
}
