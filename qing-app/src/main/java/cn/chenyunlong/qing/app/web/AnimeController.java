package cn.chenyunlong.qing.app.web;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.anime.AnimeInfo;
import cn.chenyunlong.qing.domain.anime.creator.AnimeInfoCreator;
import cn.chenyunlong.qing.domain.anime.mapper.AnimeInfoMapper;
import cn.chenyunlong.qing.domain.anime.request.AnimeInfoCreateRequest;
import cn.chenyunlong.qing.domain.anime.request.AnimeInfoUpdateRequest;
import cn.chenyunlong.qing.domain.anime.service.IAnimeInfoService;
import cn.chenyunlong.qing.domain.anime.updater.AnimeInfoUpdater;
import cn.chenyunlong.qing.domain.anime.vo.AnimeInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "动漫信息管理")
@Slf4j
@RestController
@RequestMapping("api/v1/anime")
@RequiredArgsConstructor
public class AnimeController {

    // 发布信息
    private final IAnimeInfoService animeInfoService;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Operation(summary = "通过ID查询单条数据")
    @GetMapping("{id}")
    public ResponseEntity<AnimeInfoVO> queryById(@PathVariable Long id) {
        return ResponseEntity.ok(animeInfoService.findById(id));
    }

    /**
     * 分页查询
     */
    @Operation(summary = "分页查询")
    @PostMapping("page")
    public ResponseEntity<Page<AnimeInfoVO>> pageQuery(
        @RequestBody PageRequestWrapper<AnimeInfo> requestWrapper) {
        return ResponseEntity.ok(
            animeInfoService.findByPage(requestWrapper.getBean(), requestWrapper.getWrapper()));
    }

    /**
     * 新增数据
     *
     * @param createRequest 实例对象
     * @return 实例对象
     */
    @Operation(summary = "新增数据")
    @PostMapping
    public ResponseEntity<Long> add(AnimeInfoCreateRequest createRequest) {
        AnimeInfoCreator infoCreator = AnimeInfoMapper.INSTANCE.request2Dto(createRequest);
        return ResponseEntity.ok(animeInfoService.createAnimeInfo(infoCreator));
    }

    /**
     * 更新数据
     *
     * @param updateRequest 更新对象
     * @return 实例对象
     */
    @Operation(summary = "更新数据")
    @PutMapping
    public ResponseEntity<Void> edit(AnimeInfoUpdateRequest updateRequest) {
        AnimeInfoUpdater animeInfoUpdater = AnimeInfoMapper.INSTANCE.request2Updater(updateRequest);
        animeInfoService.updateAnimeInfo(animeInfoUpdater);
        return ResponseEntity.ok().build();
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Operation(summary = "通过主键删除数据")
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Long id) {
        animeInfoService.invalidAnimeInfo(id);
        return ResponseEntity.ok().build();
    }
}
