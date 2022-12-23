// ---Auto Generated by Project Qing ---
package cn.chenyunlong.qing.domain.file.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.file.creator.UploadFileCreator;
import cn.chenyunlong.qing.domain.file.query.UploadFileQuery;
import cn.chenyunlong.qing.domain.file.updater.UploadFileUpdater;
import cn.chenyunlong.qing.domain.file.vo.UploadFileVO;
import org.springframework.data.domain.Page;

public interface IUploadFileService {
  /**
   * create
   */
  Long createUploadFile(UploadFileCreator creator);

  /**
   * update
   */
  void updateUploadFile(UploadFileUpdater updater);

  /**
   * valid
   */
  void validUploadFile(Long id);

  /**
   * invalid
   */
  void invalidUploadFile(Long id);

  /**
   * findById
   */
  UploadFileVO findById(Long id);

  /**
   * findByPage
   */
  Page<UploadFileVO> findByPage(PageRequestWrapper<UploadFileQuery> query);
}