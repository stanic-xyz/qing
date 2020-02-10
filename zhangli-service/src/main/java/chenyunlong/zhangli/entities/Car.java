package chenyunlong.zhangli.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Entity
public class Car implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 车牌号
     */
    @NotBlank
    @Column(name = "carNumber")
    private String carNumber;
    /**
     * 车辆图片信息
     */
    @Column
    private String carImg;
    /**
     * 车辆状态，0，表示正常，1，表示车辆维护中，2表示其他情况
     */
    @Column
    private Integer carStatus;

    /**
     * 主键维护所有的关系信息
     */
    @ManyToOne
    private User user;
}
