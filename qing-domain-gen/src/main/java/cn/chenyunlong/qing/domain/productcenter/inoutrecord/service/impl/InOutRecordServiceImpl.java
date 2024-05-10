package cn.chenyunlong.qing.domain.productcenter.inoutrecord.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.InOutRecord;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.creator.InOutRecordCreator;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.query.InOutRecordQuery;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.updater.InOutRecordUpdater;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.vo.InOutRecordVO;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.mapper.InOutRecordMapper;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.repository.InOutRecordRepository;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.service.IInOutRecordService;
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
public class InOutRecordServiceImpl implements IInOutRecordService {

    private final InOutRecordRepository inOutRecordRepository;

    /**
     * createImpl
     */
    @Override
    public Long createInOutRecord(InOutRecordCreator creator) {
        Optional<InOutRecord> inOutRecord = EntityOperations.doCreate(inOutRecordRepository)
            .create(() -> InOutRecordMapper.INSTANCE.dtoToEntity(creator))
            .update(InOutRecord::init)
            .execute();
        return inOutRecord.isPresent() ? inOutRecord.get().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateInOutRecord(InOutRecordUpdater updater) {
        EntityOperations.doUpdate(inOutRecordRepository)
            .loadById(updater.getId())
            .update(updater::updateInOutRecord)
            .execute();
    }

    /**
     * valid
     */
    @Override
    public void validInOutRecord(Long id) {
        EntityOperations.doUpdate(inOutRecordRepository)
            .loadById(id)
            .update(BaseJpaAggregate::valid)
            .execute();
    }

    /**
     * invalid
     */
    @Override
    public void invalidInOutRecord(Long id) {
        EntityOperations.doUpdate(inOutRecordRepository)
            .loadById(id)
            .update(BaseJpaAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public InOutRecordVO findById(Long id) {
        Optional<InOutRecord> inOutRecord = inOutRecordRepository.findById(id);
        return new InOutRecordVO(
            inOutRecord.orElseThrow(() -> new BusinessException(CodeEnum.NotFindError)));
    }

    /**
     * findByPage
     */
    @Override
    public Page<InOutRecordVO> findByPage(PageRequestWrapper<InOutRecordQuery> query) {
        PageRequest pageRequest =
            PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return inOutRecordRepository.findAll(pageRequest).map(InOutRecordVO::new);
    }
}
