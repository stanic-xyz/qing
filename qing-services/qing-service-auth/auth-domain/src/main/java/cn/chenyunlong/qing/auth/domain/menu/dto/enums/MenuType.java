package cn.chenyunlong.qing.auth.domain.menu.dto.enums;

import lombok.Getter;

/**
 * 菜单类型：类型（C目录 M菜单 B按钮）
 */
public enum MenuType {
    C("目录"),
    M("菜单"),
    B("按钮");

    @Getter
    private final String desc;

    MenuType(String desc) {
        this.desc = desc;
    }
}
