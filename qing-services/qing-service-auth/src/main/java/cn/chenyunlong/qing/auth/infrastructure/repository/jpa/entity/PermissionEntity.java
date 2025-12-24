package cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.jpa.support.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 权限JPA实体
 */
@Getter
@Setter
@Entity
@Table(name = "sys_permission")
@ToString
public class PermissionEntity extends BaseJpaEntity {

    @FieldDesc(name = "权限编码")
    @Column(name = "code", unique = true, nullable = false, length = 100)
    private String code;

    @FieldDesc(name = "权限名称")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @FieldDesc(name = "权限描述")
    @Column(name = "description")
    private String description;

    @FieldDesc(name = "权限类型")
    @Column(name = "type", nullable = false, length = 20)
    private String type;

    @FieldDesc(name = "权限状态")
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @FieldDesc(name = "资源路径")
    @Column(name = "resource", nullable = false, length = 255)
    private String resource;

    @FieldDesc(name = "操作动作")
    @Column(name = "action", nullable = false, length = 50)
    private String action;

    @FieldDesc(name = "排序号")
    @Column(name = "sort_order")
    private Integer sortOrder;

    @FieldDesc(name = "父权限ID")
    @Column(name = "parent_id")
    private Long parentId;
}

