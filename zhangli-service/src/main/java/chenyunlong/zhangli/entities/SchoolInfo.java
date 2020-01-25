package chenyunlong.zhangli.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class SchoolInfo {
    @Id
    private long schoolId;
    private String schoolName;
    private String schoolCode;
}
