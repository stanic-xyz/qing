package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.mapper.AnimeCommentMapper;
import chenyunlong.zhangli.mapper.AnimeInfoMapper;
import chenyunlong.zhangli.model.dto.AnimeEpisodeDTO;
import chenyunlong.zhangli.model.dto.PlayListDTO;
import chenyunlong.zhangli.model.dto.anime.AnimeInfoMinimalDTO;
import chenyunlong.zhangli.model.dto.anime.AnimeInfoRankDTO;
import chenyunlong.zhangli.model.dto.anime.AnimeInfoUpdateDTO;
import chenyunlong.zhangli.model.entities.AnimeComment;
import chenyunlong.zhangli.model.entities.anime.AnimeInfo;
import chenyunlong.zhangli.model.params.AnimeInfoQuery;
import chenyunlong.zhangli.model.vo.anime.AnimeInfoPlayVo;
import chenyunlong.zhangli.model.vo.anime.AnimeInfoVo;
import chenyunlong.zhangli.service.AnimeEpisodeService;
import chenyunlong.zhangli.service.AnimeInfoService;
import chenyunlong.zhangli.service.PlaylistService;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Stan
 */
@Slf4j
@Service
public class AnimeInfoServiceImpl extends ServiceImpl<AnimeInfoMapper, AnimeInfo> implements AnimeInfoService {
    private final AnimeInfoMapper animeInfoMapper;
    private final AnimeEpisodeService episodeService;
    private final PlaylistService playlistService;
    private final AnimeCommentMapper commentMapper;


    public AnimeInfoServiceImpl(AnimeInfoMapper animeInfoMapper, AnimeEpisodeService episodeService,
                                PlaylistService playlistService,
                                AnimeCommentMapper commentMapper) {
        this.animeInfoMapper = animeInfoMapper;
        this.episodeService = episodeService;
        this.playlistService = playlistService;
        this.commentMapper = commentMapper;
    }

    @Override
    public IPage<AnimeInfoRankDTO> getRankPage(IPage<AnimeInfo> pageInfo, AnimeInfoQuery animeInfoQuery) {
        return page(pageInfo, buildQueryWrapper(animeInfoQuery))
                .convert(animeInfo -> new AnimeInfoRankDTO().convertFrom(animeInfo));
    }

    @Override
    public AnimeInfoVo create(AnimeInfo animeInfo) {
        save(animeInfo);
        return new AnimeInfoVo().convertFrom(animeInfo);
    }

    @Override
    public IPage<AnimeInfoVo> listByPage(IPage<AnimeInfo> page, AnimeInfoQuery query) {
        return page(page, buildQueryWrapper(query)).convert(animeInfo -> new AnimeInfoVo().convertFrom(animeInfo));
    }

    /**
     * 构建查询条件
     *
     * @param animeInfo 查询条件
     * @return 查询条件
     */
    private LambdaQueryWrapper<AnimeInfo> buildQueryWrapper(AnimeInfoQuery animeInfo) {
        LambdaQueryWrapper<AnimeInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(animeInfo.getDistrict()) && !"all".equals(animeInfo.getDistrict()), AnimeInfo::getDistrictName, animeInfo.getDistrict());
        queryWrapper.eq(StringUtils.hasText(animeInfo.getName()), AnimeInfo::getName, animeInfo.getName());
        queryWrapper.eq(StringUtils.hasText(animeInfo.getVersion()) && !"all".equals(animeInfo.getVersion()), AnimeInfo::getPlotType, animeInfo.getVersion());
        queryWrapper.eq(animeInfo.getSeasonMonth() != -1, AnimeInfo::getPlotType, animeInfo.getVersion());
        queryWrapper.eq(StringUtils.hasText(animeInfo.getResourceType()) && !"all".equals(animeInfo.getResourceType()), AnimeInfo::getTypeName, animeInfo.getResourceType());
        queryWrapper.ge(StringUtils.hasText(animeInfo.getYear()) && !"all".equals(animeInfo.getYear()), AnimeInfo::getPremiereDate, animeInfo.getStartYear());
        queryWrapper.lt(StringUtils.hasText(animeInfo.getYear()) && !"all".equals(animeInfo.getYear()), AnimeInfo::getPremiereDate, animeInfo.getEndYear());
        queryWrapper.eq(animeInfo.getSeasonMonth() != -1, AnimeInfo::getPlotType, animeInfo.getVersion());
        queryWrapper.eq(animeInfo.getSeasonMonth() != -1, AnimeInfo::getPlotType, animeInfo.getVersion());
        queryWrapper.eq(animeInfo.getSeasonMonth() != -1, AnimeInfo::getPlotType, animeInfo.getVersion());
        return queryWrapper;
    }

    @Override
    public AnimeInfoVo updateBy(AnimeInfo animeInfo) {
        updateById(animeInfo);
        return new AnimeInfoVo().convertFrom(animeInfo);
    }

    /**
     * 从持久化对象转化为砖石对象
     *
     * @param animeInfo 动漫信息
     * @return 可以用于界面展示的数据
     */
    private AnimeInfoPlayVo convertToPlayInfo(AnimeInfo animeInfo) {
        AnimeInfoPlayVo animeInfoVo = new AnimeInfoPlayVo().convertFrom(animeInfo);
        animeInfoVo.setPlayList(playlistService.listPlayListBy(animeInfo.getId()));
        return animeInfoVo;
    }

    @Override
    public void deleteAnime(Long animeId) {
        removeById(animeId);
    }


    @Override
    public IPage<AnimeInfoMinimalDTO> getUpdateAnimeInfo(Integer page, Integer pageSize) {
        LambdaQueryWrapper<AnimeInfo> queryWrapper = new LambdaQueryWrapper<>();
        return page(new Page<>(page, pageSize), queryWrapper)
                .convert(animeInfo -> new AnimeInfoMinimalDTO().convertFrom(animeInfo));
    }

    /**
     * 获取推荐用户列表
     *
     * @return 推荐动漫列表
     */
    @Cacheable("index:recommend")
    @Override
    public List<AnimeInfoMinimalDTO> getRecommendAnimeInfoList() {
        return animeInfoMapper.listRecommendAnimeInfo().stream()
                .map(animeInfo ->
                        (AnimeInfoMinimalDTO) new AnimeInfoMinimalDTO().convertFrom(animeInfo))
                .collect(Collectors.toList());
    }

    /**
     * 获取动漫的播放信息
     *
     * @param animeId 动漫ID
     * @return 动漫的所有集数
     */
    public List<AnimeEpisodeDTO> getAnimeEpisodes(Long animeId) {
        return episodeService.listEpisodeByAnimeId(animeId);
    }

    public List<PlayListDTO> getAnimePlayList(Long animeId) {
        return playlistService.listPlayListBy(animeId);
    }


    public AnimeInfoPlayVo convertToPlayVo(AnimeInfo animeInfo) {
        return convertToPlayInfo(animeInfo);
    }


    @Override
    public List<AnimeInfoMinimalDTO> getRecentUpdate(int recentPageSize) {
        Page<AnimeInfo> infoPage = new Page<>(1, recentPageSize);
        LambdaQueryWrapper<AnimeInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(AnimeInfo::getPremiereDate);
        return page(infoPage, queryWrapper).
                getRecords().stream().map(animeInfo ->
                        (AnimeInfoMinimalDTO) new AnimeInfoMinimalDTO().convertFrom(animeInfo))
                .collect(Collectors.toList());
    }

    @Override
    public void downloadImages() throws IOException {
        List<AnimeInfo> animeInfos = list(new QueryWrapper<>());
        final File[] listFiles;
        File exsitsFile = ResourceUtils.getFile("E:\\GitHub\\cdn\\age");
        if (exsitsFile.isDirectory()) {
            listFiles = exsitsFile.listFiles();
        } else {
            listFiles = new File[]{};
        }

        File file = ResourceUtils.getFile("E:\\GitHub\\cdn");
        int index = 0;
        for (AnimeInfo animeInfo : animeInfos) {
            try {
                String imgUrl = animeInfo.getCoverUrl().replace("https://anime-1255705827.cos.ap-guangzhou.myqcloud.com/", "");
                if (StringUtils.hasText(imgUrl) && Arrays.stream(Objects.requireNonNull(listFiles)).noneMatch(file1 -> file1.getName().endsWith(imgUrl))) {
                    String ageImgUrl = "https://sc04.alicdn.com/kf/" + imgUrl;
                    log.info("{}->>图片不存在，正在下载-->>", ageImgUrl);
                    log.info(imgUrl);
                    HttpUtil.downloadFile(ageImgUrl, file.getAbsolutePath());
                } else {
                    log.info("{}->>图片已存在：{}", animeInfo.getName(), animeInfo.getCoverUrl());
                }
                index++;
            } catch (Exception exception) {
                log.error(animeInfo.getCoverUrl(), exception);
            }
        }
        log.info("同步了{}条数据", index);
    }

    @Override
    public IPage<AnimeComment> getComment(Long animeId, Integer pageIndex, Integer pageSize) {
        QueryWrapper<AnimeComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(AnimeComment::getCid, animeId);
        return commentMapper.selectPage(new Page<>(pageIndex, pageSize), queryWrapper);
    }

    @Override
    public List<AnimeInfoUpdateDTO> getUpdateInfo() {
        QueryWrapper<AnimeInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .select(AnimeInfo::getId, AnimeInfo::getPremiereDate, AnimeInfo::getName)
                .orderByDesc(AnimeInfo::getPremiereDate);
        queryWrapper.last("limit 100");
        return list(queryWrapper).stream().map(animeInfo ->
        {
            AnimeInfoUpdateDTO updateDTO = new AnimeInfoUpdateDTO().convertFrom(animeInfo);
            updateDTO.setIsNew(true);
            updateDTO.setNameForNew("最新一集");
            updateDTO.setPremiereDate(updateDTO.getPremiereDate() != null ? updateDTO.getPremiereDate() : LocalDateTime.now());
            return updateDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public void addComment(Long cid, String content, String user) {
        AnimeComment comment = new AnimeComment();
        comment.setContent(content);
        comment.setUsername(user);
        comment.setCid(cid);
        comment.setCreateTime(LocalDateTime.now());
        comment.setCreateBy("");
        comment.setRemark("");
        commentMapper.insert(comment);
    }
}
