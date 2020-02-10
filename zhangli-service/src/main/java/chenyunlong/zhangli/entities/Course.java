package chenyunlong.zhangli.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Data
@Entity
public class Course implements Serializable {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 车辆ID
     */
    @ManyToOne
    private Car car;
    /**
     * 课程名称
     */
    private String courseName;
    /**
     * 用户ID
     */
    @ManyToOne
    private User user;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:dd", timezone = " GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:dd")
    private Date createTime;
    /**
     * 上课时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:dd", timezone = " GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:dd")
    private Date courseTime;
}
