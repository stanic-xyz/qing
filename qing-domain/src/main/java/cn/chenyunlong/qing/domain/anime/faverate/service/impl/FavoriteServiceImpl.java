package cn.chenyunlong.qing.domain.anime.faverate.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.anime.faverate.Favorite;
import cn.chenyunlong.qing.domain.anime.faverate.dto.creator.FavoriteCreator;
import cn.chenyunlong.qing.domain.anime.faverate.dto.query.FavoriteQuery;
import cn.chenyunlong.qing.domain.anime.faverate.dto.updater.FavoriteUpdater;
import cn.chenyunlong.qing.domain.anime.faverate.dto.vo.FavoriteVO;
import cn.chenyunlong.qing.domain.anime.faverate.mapper.FavoriteMapper;
import cn.chenyunlong.qing.domain.anime.faverate.repository.FavoriteRepository;
import cn.chenyunlong.qing.domain.anime.faverate.service.IFavoriteService;
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
public class FavoriteServiceImpl implements IFavoriteService {

    private final FavoriteRepository favoriteRepository;

    /**
     * createImpl
     */
    @Override
    public Long createFavorite(FavoriteCreator creator) {
        Optional<Favorite> favorite;
        favorite = EntityOperations.doCreate(favoriteRepository)
                       .create(() -> FavoriteMapper.INSTANCE.dtoToEntity(creator))
                       .update(Favorite::init)
                       .execute();
        return favorite.isPresent() ? favorite.get().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateFavorite(FavoriteUpdater updater) {
        EntityOperations.doUpdate(favoriteRepository)
            .loadById(updater.getId())
            .update(updater::updateFavorite)
            .execute();
    }

    /**
     * valid
     */
    @Override
    public void validFavorite(Long id) {
        EntityOperations.doUpdate(favoriteRepository)
            .loadById(id)
            .update(BaseJpaAggregate::valid)
            .execute();
    }

    /**
     * invalid
     */
    @Override
    public void invalidFavorite(Long id) {
        EntityOperations.doUpdate(favoriteRepository)
            .loadById(id)
            .update(BaseJpaAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public FavoriteVO findById(Long id) {
        Optional<Favorite> favorite = favoriteRepository.findById(id);
        return new FavoriteVO(favorite.orElseThrow(() -> new BusinessException(CodeEnum.NotFoundError)));
    }

    /**
     * findByPage
     */
    @Override
    public Page<FavoriteVO> findByPage(PageRequestWrapper<FavoriteQuery> query) {
        PageRequest pageRequest = PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return favoriteRepository.findAll(pageRequest).map(FavoriteVO::new);
    }
}
