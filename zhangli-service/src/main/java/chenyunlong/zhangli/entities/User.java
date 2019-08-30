package chenyunlong.zhangli.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class User {

    @Id
    private long userID;
    @Column(length = 10, name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column
    private String phone;
    @Column(length = 30)
    private String openId;
}
