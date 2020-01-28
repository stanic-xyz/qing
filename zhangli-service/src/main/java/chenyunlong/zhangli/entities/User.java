package chenyunlong.zhangli.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
