package cn.chenyunlong.common.model.vo;

import cn.chenyunlong.common.enums.CommonEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.util.CollectionUtils;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(title = "通用枚举")
public class CommonEnumVO {
    @Schema(title = "Code")
    int code;

    @Schema(title = "Name")
    String name;

    @Schema(title = "描述")
    String desc;

    /**
     * 从枚举转换为枚举Vo
     *
     * @param commonEnum 公共枚举
     * @return 枚举Vo
     */
    public static CommonEnumVO from(CommonEnum commonEnum) {
        if (commonEnum == null) {
            return null;
        }
        return new CommonEnumVO(commonEnum.getCode(), commonEnum.getName(),
            commonEnum.getDescription());
    }

    /**
     * 从枚举List 转换为枚举Vo List
     *
     * @param commonEnums 公共枚举列表
     * @return 枚举 VoList
     */
    public static List<CommonEnumVO> from(List<CommonEnum> commonEnums) {
        if (CollectionUtils.isEmpty(commonEnums)) {
            return Collections.emptyList();
        }
        return commonEnums.stream()
            .filter(Objects::nonNull)
            .map(CommonEnumVO::from)
            .collect(Collectors.toList());
    }
}
