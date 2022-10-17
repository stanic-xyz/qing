/*
 * Copyright (c) 2019-2022 YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.chenyunlong.zhangli.service.impl;

import cn.chenyunlong.zhangli.core.exception.ServiceException;
import cn.chenyunlong.zhangli.mapper.AnimeInfoMapper;
import cn.chenyunlong.zhangli.mapper.AnimeRecommendMapper;
import cn.chenyunlong.zhangli.model.entities.AnimeRecommendEntity;
import cn.chenyunlong.zhangli.model.entities.anime.AnimeInfo;
import cn.chenyunlong.zhangli.model.params.AnimeInfoQuery;
import cn.chenyunlong.zhangli.model.params.AnimeRecommendParam;
import cn.chenyunlong.zhangli.service.AnimeRecommendService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Stan
 */
@Service
public class AnimeRecommendServiceImpl extends ServiceImpl<AnimeRecommendMapper, AnimeRecommendEntity> implements AnimeRecommendService {

    private final AnimeRecommendMapper animeRecommendMapper;

    private final AnimeInfoMapper animeInfoMapper;

    public AnimeRecommendServiceImpl(AnimeRecommendMapper animeRecommendMapper, AnimeInfoMapper animeInfoMapper) {
        this.animeRecommendMapper = animeRecommendMapper;
        this.animeInfoMapper = animeInfoMapper;
    }

    @Override
    public List<AnimeRecommendEntity> getRecommendAnimeInfoList(Pageable pageable, AnimeInfoQuery animeInfoQuery) {
//        return animeInfoMapper.listAnime(pageable, animeInfoQuery);
        return null;
    }

    @Override
    public AnimeRecommendEntity getById(Long recommendId) {
        return null;
    }

    @Override
    public AnimeRecommendEntity addRecommend(AnimeRecommendEntity recommendEntity) {

        AnimeInfo animeInfo = animeInfoMapper.selectById(recommendEntity.getAid());
        if (animeInfo == null) {
            throw new ServiceException("动漫信息不存在");
        }
        //已经添加到推荐列表了
        Long count = lambdaQuery().eq(AnimeRecommendEntity::getAid, recommendEntity.getAid()).count();
        if (count != 0) {
            throw new ServiceException("已经添加到推荐列表");
        }
        recommendEntity.preCheck();
        boolean save = save(recommendEntity);
        if (save) {
            return recommendEntity;
        } else {
            throw new ServiceException("保存失败");
        }
    }

    @Override
    public IPage<AnimeRecommendEntity> pageBy(AnimeRecommendParam typeParam, Pageable pageable) {
        return null;
    }

    @Override
    public AnimeRecommendEntity update(AnimeRecommendEntity animeRecommendEntity) {
        return null;
    }
}
