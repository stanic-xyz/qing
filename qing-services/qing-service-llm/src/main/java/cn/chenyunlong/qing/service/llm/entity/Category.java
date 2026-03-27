package cn.chenyunlong.qing.service.llm.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "finance_category")
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 分类名称

    private Long parentId; // 父级分类ID，为null表示顶级分类

    private Integer level; // 层级，0为顶级

    private String icon; // 分类图标

    private String type; // INCOME/EXPENSE/TRANSFER 用于区分收支类型分类

    private Boolean isDeleted = false; // 软删除标记

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
