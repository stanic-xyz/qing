package cn.chenyunlong.zhangli.mapper;

import cn.chenyunlong.zhangli.model.entities.bilibili.BiliAnimeInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BiliAnimeMapper extends BaseMapper<BiliAnimeInfoEntity> {
}