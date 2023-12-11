package cn.chenyunlong.qing.domain.productcenter.inoutrecord.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.creator.InOutRecordDetailCreator;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.query.InOutRecordDetailQuery;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.updater.InOutRecordDetailUpdater;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.vo.InOutRecordDetailVO;
import org.springframework.data.domain.Page;

public interface IInOutRecordDetailService {
    /**
     * create
     */
    Long createInOutRecordDetail(InOutRecordDetailCreator creator);

    /**
     * update
     */
    void updateInOutRecordDetail(InOutRecordDetailUpdater updater);

    /**
     * valid
     */
    void validInOutRecordDetail(Long id);

    /**
     * invalid
     */
    void invalidInOutRecordDetail(Long id);

    /**
     * findById
     */
    InOutRecordDetailVO findById(Long id);

    /**
     * findByPage
     */
    Page<InOutRecordDetailVO> findByPage(PageRequestWrapper<InOutRecordDetailQuery> query);
}
