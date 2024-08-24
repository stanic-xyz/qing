package cn.chenyunlong.qing.application.manager.web.anime;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.anime.playlist.dto.creator.PlayListCreator;
import cn.chenyunlong.qing.domain.anime.playlist.dto.query.PlayListQuery;
import cn.chenyunlong.qing.domain.anime.playlist.dto.request.PlayListCreateRequest;
import cn.chenyunlong.qing.domain.anime.playlist.dto.request.PlayListQueryRequest;
import cn.chenyunlong.qing.domain.anime.playlist.dto.request.PlayListUpdateRequest;
import cn.chenyunlong.qing.domain.anime.playlist.dto.response.PlayListResponse;
import cn.chenyunlong.qing.domain.anime.playlist.dto.updater.PlayListUpdater;
import cn.chenyunlong.qing.domain.anime.playlist.dto.vo.PlayListVO;
import cn.chenyunlong.qing.domain.anime.playlist.mapper.PlayListMapper;
import cn.chenyunlong.qing.domain.anime.playlist.service.IPlayListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("api/v1/play-list")
@RequiredArgsConstructor
public class PlayListController {

    private final IPlayListService playListService;

    /**
     * createRequest
     */
    @PostMapping
    public JsonResult<Long> createPlayList(
        @RequestBody
        PlayListCreateRequest request) {
        PlayListCreator creator = PlayListMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(playListService.createPlayList(creator));
    }

    /**
     * update request
     */
    @PostMapping("updatePlayList")
    public JsonResult<String> updatePlayList(
        @RequestBody
        PlayListUpdateRequest request) {
        PlayListUpdater updater = PlayListMapper.INSTANCE.request2Updater(request);
        playListService.updatePlayList(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * valid
     */
    @PostMapping("valid/{id}")
    public JsonResult<String> validPlayList(
        @PathVariable("id")
        Long id) {
        playListService.validPlayList(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * invalid
     */
    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidPlayList(
        @PathVariable("id")
        Long id) {
        playListService.invalidPlayList(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * findById
     */
    @GetMapping("findById/{id}")
    public JsonResult<PlayListResponse> findById(
        @PathVariable("id")
        Long id) {
        PlayListVO vo = playListService.findById(id);
        PlayListResponse response = PlayListMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    /**
     * findByPage request
     */
    @PostMapping("page")
    public JsonResult<Page<PlayListResponse>> page(
        @RequestBody
        PageRequestWrapper<PlayListQueryRequest> request) {
        PageRequestWrapper<PlayListQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(PlayListMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<PlayListResponse> responses = playListService.findByPage(wrapper).map(PlayListMapper.INSTANCE::vo2CustomResponse);
        return JsonResult.success(responses);
    }
}
