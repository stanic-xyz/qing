package cn.chenyunlong.qing.domain.productcenter.inoutrecord.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.creator.InOutRecordCreator;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.query.InOutRecordQuery;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.updater.InOutRecordUpdater;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.vo.InOutRecordVO;
import org.springframework.data.domain.Page;

public interface IInOutRecordService {

    /**
     * create
     */
    Long createInOutRecord(InOutRecordCreator creator);

    /**
     * update
     */
    void updateInOutRecord(InOutRecordUpdater updater);

    /**
     * valid
     */
    void validInOutRecord(Long id);

    /**
     * invalid
     */
    void invalidInOutRecord(Long id);

    /**
     * findById
     */
    InOutRecordVO findById(Long id);

    /**
     * findByPage
     */
    Page<InOutRecordVO> findByPage(PageRequestWrapper<InOutRecordQuery> query);
}
