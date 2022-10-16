package cn.chenyunlong.zhangli.service;

import cn.chenyunlong.zhangli.model.dto.AnimeCommentDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;


public interface AnimeCommentService {
    /**
     * 获取动漫平均信息
     *
     * @param animeId  动漫ID
     * @param page     当前页
     * @param pageSize 分页大小
     * @return 动漫信息（包含分页信息）
     */
    IPage<AnimeCommentDTO> getCommentsByAnimeId(Long animeId, int page, int pageSize);
}
