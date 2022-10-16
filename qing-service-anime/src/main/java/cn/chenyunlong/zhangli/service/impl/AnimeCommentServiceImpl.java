package cn.chenyunlong.zhangli.service.impl;

import cn.chenyunlong.zhangli.mapper.AnimeCommentMapper;
import cn.chenyunlong.zhangli.model.dto.AnimeCommentDTO;
import cn.chenyunlong.zhangli.model.entities.AnimeComment;
import cn.chenyunlong.zhangli.service.AnimeCommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnimeCommentServiceImpl implements AnimeCommentService {

    private final AnimeCommentMapper commentMapper;

    public AnimeCommentServiceImpl(AnimeCommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    @Override
    public IPage<AnimeCommentDTO> getCommentsByAnimeId(Long animeId, int page, int pageSize) {
        QueryWrapper<AnimeComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cid", animeId);
        return convertToDTO(commentMapper.selectPage(new Page<>(page, pageSize), queryWrapper));
    }

    private IPage<AnimeCommentDTO> convertToDTO(IPage<AnimeComment> commentPage) {
        Assert.notNull(commentPage, "comments page must not be null");

        // Convert
        List<AnimeCommentDTO> commentDTOPage = commentPage.getRecords().stream().map(comment
                -> (AnimeCommentDTO) new AnimeCommentDTO().convertFrom(comment)).collect(Collectors.toList());

        // Build and return
        Page<AnimeCommentDTO> page = new Page<>(commentPage.getCurrent(), commentPage.getSize(), commentPage.getTotal());
        page.setRecords(commentDTOPage);
        return page;
    }
}
