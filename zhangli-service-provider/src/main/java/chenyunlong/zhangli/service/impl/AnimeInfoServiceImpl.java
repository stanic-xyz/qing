package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.common.exception.NotFoundException;
import chenyunlong.zhangli.mapper.AnimeInfoMapper;
import chenyunlong.zhangli.mapper.AnimeTypeMapper;
import chenyunlong.zhangli.model.agefans.AgePlayInfoModel;
import chenyunlong.zhangli.model.dto.EpisodeDTO;
import chenyunlong.zhangli.model.dto.PlayListDTO;
import chenyunlong.zhangli.model.dto.anime.AnimeInfoMinimalDTO;
import chenyunlong.zhangli.model.entities.AnimeType;
import chenyunlong.zhangli.model.entities.anime.AnimeInfo;
import chenyunlong.zhangli.model.params.AnimeInfoQuery;
import chenyunlong.zhangli.model.vo.anime.AnimeInfoVo;
import chenyunlong.zhangli.service.AnimeEpisodeService;
import chenyunlong.zhangli.service.AnimeInfoService;
import chenyunlong.zhangli.service.PlaylistService;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author Stan
 */
@Slf4j
@Service
public class AnimeInfoServiceImpl implements AnimeInfoService {
    private final AnimeInfoMapper animeInfoMapper;
    private final RestTemplate restTemplate;
    private final AnimeEpisodeService episodeService;
    private final AnimeTypeMapper animeTypeMapper;
    private final PlaylistService playlistService;
    private final String domainName = "anime";


    public AnimeInfoServiceImpl(AnimeInfoMapper animeInfoMapper,
                                RestTemplate restTemplate,
                                AnimeEpisodeService episodeService,
                                AnimeTypeMapper animeTypeMapper,
                                PlaylistService playlistService) {
        this.animeInfoMapper = animeInfoMapper;
        this.restTemplate = restTemplate;
        this.episodeService = episodeService;
        this.animeTypeMapper = animeTypeMapper;
        this.playlistService = playlistService;
    }

    @Override
    public IPage<AnimeInfo> getRankPage(IPage<AnimeInfo> pageInfo, AnimeInfoQuery animeInfoQuery) {
        Wrapper<AnimeInfo> queryWrapper = new QueryWrapper<>();
        return animeInfoMapper.selectPage(pageInfo, queryWrapper);
    }

    @Override
    public AnimeInfoVo create(AnimeInfo animeInfo) {
        animeInfoMapper.insert(animeInfo);
        return convertTo(animeInfo);
    }

    @Override
    public AnimeInfo getById(Long animeId) {
        AnimeInfo animeInfo = animeInfoMapper.selectAnimationDetail(animeId);
        if (animeInfo == null) {
            throw new NotFoundException(domainName + "was not found or has been deleted");
        }
        return animeInfo;
    }

    @Override
    public long getTotalCount(String query) {
        return animeInfoMapper.countByNameLike(query);
    }

    @Override
    public IPage<AnimeInfoVo> listByPage(IPage<AnimeInfo> page, AnimeInfoQuery animeInfo) {
        QueryWrapper<AnimeInfo> queryWrapper = new QueryWrapper<>();
        return convertTo(animeInfoMapper.selectPage(page, queryWrapper));
    }

    /**
     * 获取播放页数据
     *
     * @param animeId    动漫ID
     * @param sourceType 播放分类
     * @param ep         片段
     * @return 动漫播放页参数信息
     */
    @Override
    public AnimeInfoVo getPlayDetail(Long animeId, int sourceType, int ep) {
        AnimeInfo animeInfo = animeInfoMapper.selectAnimationDetail(animeId);
        return new ObjectMapper().convertValue(animeInfo, AnimeInfoVo.class);
    }

    @Override
    public AnimeInfoVo updateBy(AnimeInfo animeInfo) {
        animeInfoMapper.update(animeInfo);
        return convertTo(animeInfo);
    }

    /**
     * 从持久化对象转化为砖石对象
     *
     * @param animeInfo 动漫信息
     * @return 可以用于界面展示的数据
     */
    private AnimeInfoVo convertTo(AnimeInfo animeInfo) {
        //TODO 还有其他数据需要等到以后再进行转化了
        AnimeInfoVo animeInfoVo = new AnimeInfoVo().convertFrom(animeInfo);
        animeInfoVo.setPlayList(playlistService.listPlayListBy(animeInfo.getId()));
        return animeInfoVo;
    }

    private IPage<AnimeInfoVo> convertTo(IPage<AnimeInfo> animeInfo) {
        //TODO 还有其他数据需要等到以后再进行转化了
        Page<AnimeInfoVo> animeInfoPage = new Page<>(animeInfo.getCurrent(), animeInfo.getSize(), animeInfo.getTotal(), animeInfo.isSearchCount());
        animeInfoPage.setRecords(animeInfo.getRecords().stream().map(this::convertTo).collect(Collectors.toList()));
        return animeInfoPage;
    }

    @Override
    public void deleteAnime(Long animeId) {
        animeInfoMapper.deleteById(animeId);
    }

    @Override
    public List<AnimeType> getAllAnimeType() {
        return animeTypeMapper.listTypes();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AnimeType addAnimeType(AnimeType animeType) {
        animeTypeMapper.addAnimeType(animeType);
        return animeType;
    }

    @Override
    public IPage<AnimeInfoVo> getUpdateAnimeInfo(IPage<AnimeInfo> animeInfoPage) {
        QueryWrapper<AnimeInfo> queryWrapper = new QueryWrapper<>();
        IPage<AnimeInfo> page = animeInfoMapper.selectPage(animeInfoPage, queryWrapper);
        Page<AnimeInfoVo> animeInfoVoPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal(), page.isSearchCount());
        animeInfoVoPage.setRecords(page.getRecords().stream().map(this::convertTo).collect(Collectors.toList()));
        return animeInfoVoPage;
    }

    /**
     * 获取推荐用户列表
     *
     * @return 推荐动漫列表
     */
    @Override
    public List<AnimeInfoMinimalDTO> getRecommendAnimeInfoList() {
        List<AnimeInfo> animeInfoList = animeInfoMapper.listRecommendAnimeInfo();
        return animeInfoList.stream().map(animeInfo -> (AnimeInfoMinimalDTO) new AnimeInfoMinimalDTO().convertFrom(animeInfo)).collect(Collectors.toList());
    }

    /**
     * 获取动漫的播放信息
     *
     * @param animeId 动漫ID
     * @return 动漫的所有集数
     */
    @Override
    public List<EpisodeDTO> getAnimeEpisodes(Long animeId) {
        return episodeService.listEpisodeBy(animeId);
    }

    @Override
    public List<PlayListDTO> getAnimePlayList(Long animeId) {
        return playlistService.listPlayListBy(animeId);
    }

    @Override
    public AnimeInfoVo convertToDetailVo(AnimeInfo animeInfo) {
        return convertTo(animeInfo);
    }

    @Override
    public AnimeInfo getById(Integer animeId) {
        AnimeInfo animeInfo = animeInfoMapper.fetchById(animeId);
        if (animeInfo == null) {
            throw new NotFoundException(domainName + "was not found or has been deleted");
        }
        return animeInfo;
    }

    @Override
    public List<AnimeInfoMinimalDTO> getRecentUpdate(int recentPageSize) {
        QueryWrapper<AnimeInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("premiere_date").last("limit 0," + recentPageSize);
        List<AnimeInfo> animeInfoList = animeInfoMapper.selectList(queryWrapper);
        return animeInfoList.stream().map(animeInfo -> (AnimeInfoMinimalDTO) new AnimeInfoMinimalDTO().convertFrom(animeInfo)).collect(Collectors.toList());
    }

    @Override
    public void downloadImages() throws IOException {
        List<AnimeInfo> animeInfos = animeInfoMapper.selectList(new QueryWrapper<>());
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
                if (StringUtils.hasText(imgUrl) && Arrays.stream(listFiles).noneMatch(file1 -> file1.getName().endsWith(imgUrl))) {
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
    }

    private void getPlayDetail() {
        String url = "https://www.agefans.tv/_getplay?aid=20120053&playindex=3&epindex=2&r=";
        String referUrl = "https://www.agefans.tv/_getplay?aid=20200101&playindex=3&epindex=1&r=123123123";
        String cookie = "fikker-ebUR-LnSf=gv7ZifqFiXqnepunyf30wdJReFQl52IZ";
        String playUrl = "https://play.agefans.tv/_getplay2?kp=4zPeWaqNhi20IUkB6rXFO2pAJ1otsSb%2FIzpjyjuYSXKly9zoxWCaSFno%2FSoM8yFblxl8IVjeTJ5eIaCbwj%2BgpYDwjV02ZDL8dxaEsoxzUR9hzjeC4pd38w%3D%3D";

        try {
            String requestUrl = url + new Random().nextDouble();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Host", "play.agefans.tv");
            ResponseEntity<AgePlayInfoModel> response = restTemplate.exchange(
                    playUrl,
                    HttpMethod.GET,
                    new HttpEntity<String>(headers),
                    AgePlayInfoModel.class);
            AgePlayInfoModel responseBody = response.getBody();

            log.info("获取地址成功了！");
            log.info("获取地址成功了！:" + responseBody);
        } catch (Exception exception) {
            log.error("获取信息错误", exception);
        }
    }
}
