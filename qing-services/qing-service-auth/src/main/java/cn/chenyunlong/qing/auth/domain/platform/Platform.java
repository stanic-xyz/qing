package cn.chenyunlong.qing.auth.domain.platform;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.qing.auth.domain.platform.dto.creator.PlatformCreator;
import cn.chenyunlong.qing.domain.common.BaseSimpleBusinessEntity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Platform extends BaseSimpleBusinessEntity<PlatformId> {

    @FieldDesc(name = "编码")
    private String code;

    @FieldDesc(name = "平台名称")
    private String name;

    public static Platform create(PlatformCreator creator) {
        Platform platform = new Platform();
        platform.setCode(creator.getCode());
        platform.setName(creator.getName());
        return platform;
    }
}
