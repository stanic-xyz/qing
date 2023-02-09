package cn.chenyunlong.qing.domain.anime.anime;

import cn.chenyunlong.common.constants.BaseEnum;

import java.util.Optional;

public enum AnimeInfoErrorCode implements BaseEnum {

    ASSET_HAS_IN(10010026, "动漫已经添加"),
    ASSET_HAS_OUT(10010027, "资产已经删除"),
    ASSET_CODE_NOT_EXIST(10010028, "动漫编码不存在");

    private final Integer code;
    private final String name;
    AnimeInfoErrorCode(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<AnimeInfoErrorCode> of(Integer code) {
        return Optional.ofNullable(BaseEnum.parseByCode(AnimeInfoErrorCode.class, code));
    }

    @Override
    public Integer getValue() {
        return this.code;
    }

    @Override
    public String getName() {
        return this.name;
    }

}
