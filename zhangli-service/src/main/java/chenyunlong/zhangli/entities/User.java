package chenyunlong.zhangli.entities;

import com.sun.tracing.dtrace.ArgsAttributes;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
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

    public User(String userName, String password) {
        this.username = userName;
        this.password = password;
    }
}
