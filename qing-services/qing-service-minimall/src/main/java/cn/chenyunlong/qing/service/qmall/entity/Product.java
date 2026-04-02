package cn.chenyunlong.qing.service.qmall.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "product")
@Data
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer stock;

    // 乐观锁版本号
    @Version
    private Integer version;
}
