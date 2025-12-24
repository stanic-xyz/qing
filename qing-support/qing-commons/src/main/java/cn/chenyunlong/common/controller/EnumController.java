package cn.chenyunlong.common.controller;

import cn.chenyunlong.common.enums.BusinessStatus;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.vo.CommonEnumVO;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("enum")
public class EnumController {
    @GetMapping("paramToEnum")
    public JsonResult<CommonEnumVO> paramToEnum(@RequestParam("businessStatus")
                                                BusinessStatus businessStatus) {
        return JsonResult.success(CommonEnumVO.from(businessStatus));
    }

    @GetMapping("pathToEnum/{newsStatus}")
    public JsonResult<CommonEnumVO> pathToEnum(
        @PathVariable("newsStatus") BusinessStatus newsStatus) {
        return JsonResult.success(CommonEnumVO.from(newsStatus));
    }

    @PostMapping("jsonToEnum")
    public JsonResult<CommonEnumVO> jsonToEnum(
        @RequestBody NewsStatusRequestBody newsStatusRequestBody) {
        return JsonResult.success(CommonEnumVO.from(newsStatusRequestBody.getNewsStatus()));
    }

    /**
     * 从JSON转化为json
     *
     * @return json
     */
    @GetMapping("bodyToJson")
    public JsonResult<NewsStatusResponseBody> bodyToJson() {
        NewsStatusResponseBody newsStatusResponseBody = new NewsStatusResponseBody();
        newsStatusResponseBody.setNewsStatus(Arrays.asList(BusinessStatus.values()));
        return JsonResult.success(newsStatusResponseBody);
    }

    @Data
    public static class NewsStatusRequestBody {
        private BusinessStatus newsStatus;
    }

    @Data
    public static class NewsStatusResponseBody {
        private List<BusinessStatus> newsStatus;
    }
}
