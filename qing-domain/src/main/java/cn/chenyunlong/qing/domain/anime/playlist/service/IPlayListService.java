package cn.chenyunlong.qing.domain.anime.playlist.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.anime.playlist.dto.creator.PlayListCreator;
import cn.chenyunlong.qing.domain.anime.playlist.dto.query.PlayListQuery;
import cn.chenyunlong.qing.domain.anime.playlist.dto.updater.PlayListUpdater;
import cn.chenyunlong.qing.domain.anime.playlist.dto.vo.PlayListVO;
import org.springframework.data.domain.Page;

public interface IPlayListService {

    /**
     * create
     */
    Long createPlayList(PlayListCreator creator);

    /**
     * update
     */
    void updatePlayList(PlayListUpdater updater);

    void validPlayList(Long id);

    void invalidPlayList(Long id);

    /**
     * findById
     */
    PlayListVO findById(Long id);

    /**
     * findByPage
     */
    Page<PlayListVO> findByPage(PageRequestWrapper<PlayListQuery> query);
}
