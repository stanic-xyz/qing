package cn.chenyunlong.qing.service.llm.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "finance_parser_config")
@Data
public class ParserConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;           // 解析器名称

    @Column(nullable = false)
    private String channel;        // 适用的渠道, 比如 ALIPAY, WECHAT

    @Column(nullable = false)
    private String fileType;       // 适用文件类型 CSV, EXCEL

    private String encoding = "UTF-8"; // 编码 UTF-8, GBK

    private Integer skipRows = 0;      // 跳过行数 (数据起始行)

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private String metadataRules;  // 元数据提取规则 JSON: List<MetadataRule>

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private String fieldMappingRules; // 交易字段映射规则 JSON: List<FieldMappingRule>

    @Column(columnDefinition = "TEXT")
    private String script;         // 兜底脚本

    private Boolean isBuiltIn = false; // 是否内置预设
    
    // 操作状态：DRAFT (草稿), PUBLISHED (已发布)
    @Column(length = 20)
    private String status = "PUBLISHED"; 
}