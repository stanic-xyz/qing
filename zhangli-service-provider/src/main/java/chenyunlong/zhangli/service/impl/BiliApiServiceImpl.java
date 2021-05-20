package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.mapper.BiliAnimeMapper;
import chenyunlong.zhangli.model.bilibili.BiliAnime;
import chenyunlong.zhangli.model.entities.bilibili.BiliAnimeInfoEntity;
import chenyunlong.zhangli.service.BilibiliAnimeService;
import chenyunlong.zhangli.utils.BeanUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Stan
 */
@Service
public class BiliApiServiceImpl implements BilibiliAnimeService {
    private final BiliAnimeMapper biliAnimeMapper;

    public BiliApiServiceImpl(BiliAnimeMapper biliAnimeMapper) {
        this.biliAnimeMapper = biliAnimeMapper;
    }

    @Override
    public void insertBatch(List<BiliAnime> animeList) {

        List<BiliAnimeInfoEntity> animeInfoEntities = animeList.stream().map(animeInfo
                -> {
            BiliAnimeInfoEntity animeInfoEntity = BeanUtils.transformFrom(animeInfo, BiliAnimeInfoEntity.class);
            assert animeInfoEntity != null;
            animeInfoEntity.setScore(StringUtils.hasText(animeInfo.getOrder()) ? Double.parseDouble(animeInfo.getOrder().replace("分", "")) : 0);
            return animeInfoEntity;
        }).collect(Collectors.toList());
        QueryWrapper<BiliAnimeInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("distinct (media_id) as media_id");
        List<BiliAnimeInfoEntity> entities = biliAnimeMapper.selectList(queryWrapper);
        animeInfoEntities.forEach(animeInfo -> {
            if (entities.stream().noneMatch(biliAnimeInfoEntity -> biliAnimeInfoEntity.getMediaId().equals(animeInfo.getMediaId()))) {
                //添加动漫信息
                biliAnimeMapper.insert(animeInfo);
            }
        });
    }
}
