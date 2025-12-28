package cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository.entiry;

import cn.chenyunlong.jpa.support.BaseJpaEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "sys_user_role")
public class UserRoleEntity extends BaseJpaEntity {

    private Long userId;
    private Long roleId;
    private Boolean revoked;

}
