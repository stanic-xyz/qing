package cn.chenyunlong.common.controller;

import cn.chenyunlong.common.enums.CommonEnum;
import cn.chenyunlong.common.enums.CommonEnumRegistry;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.vo.CommonEnumVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "通用字典接口")
@RestController
@RequestMapping("/enumDict")
@Slf4j
@RequiredArgsConstructor
public class EnumDictController {

    private final CommonEnumRegistry commonEnumRegistry;

    /**
     * 获取所有枚举
     *
     * @return 所有枚举类型
     */
    @GetMapping("all")
    public JsonResult<Map<String, List<CommonEnumVO>>> allEnums() {
        Map<String, List<CommonEnum>> dict = this.commonEnumRegistry.getNameDict();
        Map<String, List<CommonEnumVO>> dictVo = Maps.newHashMapWithExpectedSize(dict.size());
        for (Map.Entry<String, List<CommonEnum>> entry : dict.entrySet()) {
            dictVo.put(entry.getKey(), CommonEnumVO.from(entry.getValue()));
        }
        return JsonResult.success(dictVo);
    }

    @GetMapping("types")
    public JsonResult<List<String>> enumTypes() {
        Map<String, List<CommonEnum>> dict = this.commonEnumRegistry.getNameDict();
        return JsonResult.success(Lists.newArrayList(dict.keySet()));
    }

    /**
     * 根据枚举类型获取枚举
     *
     * @param type 枚举类型
     * @return 枚举类型Vo
     */
    @GetMapping("/{type}")
    public JsonResult<List<CommonEnumVO>> dictByType(@PathVariable("type") String type) {
        Map<String, List<CommonEnum>> dict = this.commonEnumRegistry.getNameDict();
        List<CommonEnum> commonEnums = dict.get(type);

        return JsonResult.success(CommonEnumVO.from(commonEnums));
    }
}
