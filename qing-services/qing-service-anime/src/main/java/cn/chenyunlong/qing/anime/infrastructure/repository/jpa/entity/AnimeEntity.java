package cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.jpa.support.BaseJpaEntity;
import cn.chenyunlong.qing.anime.domain.anime.PlayStatus;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.converter.PlayStatusConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 动漫信息。
 *
 * @author 陈云龙
 */

@Getter
@Setter
@ToString
@Entity
@Table(name = "anime_info")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class AnimeEntity extends BaseJpaEntity {

    @FieldDesc(description = "名称")
    private String name;

    @FieldDesc(description = "介绍")
    private String instruction;

    @FieldDesc(description = "区域Id")
    private Long districtId;

    @FieldDesc(description = "区域名称")
    private String districtName;

    @FieldDesc(description = "封面地址")
    private String cover;

    @FieldDesc(description = "类型信息")
    private Long typeId;

    @FieldDesc(description = "类型名称")
    private String typeName;

    @FieldDesc(description = "原始名称")
    private String originalName;

    @FieldDesc(description = "其他名称")
    private String otherName;

    @FieldDesc(description = "作者")
    private String author;

    @FieldDesc(description = "公司")
    private Long companyId;

    @FieldDesc(description = "公司名称")
    private String companyName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FieldDesc(description = "发布日期")
    private LocalDate premiereDate;

    @FieldDesc(description = "播放状态")
    @Convert(converter = PlayStatusConverter.class)
    private PlayStatus playStatus;

    @FieldDesc(description = "动漫类型")
    private String plotType;

    @FieldDesc(description = "动漫标签列表")
    private String tags;

    @FieldDesc(description = "官网")
    private String officialWebsite;

    @FieldDesc(description = "播放热度")
    private String playHeat;

    @FieldDesc(description = "最后更新时间")
    private LocalDateTime lastUpdateTime;

    @FieldDesc(description = "排序号")
    private Integer orderNo;

    private Boolean isOnShelf; // 是否上架

    private Boolean isDeleted; // 是否删除
}
