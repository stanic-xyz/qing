package cn.chenyunlong.zhangli.model.agefans;

import lombok.Data;

import java.io.Serializable;

/**
 * 友链
 *
 * @author Stan
 */
@Data
public class FriendLink implements Serializable {
    private Long id;
    private String name;
    private String path;
    private String icon;
}
