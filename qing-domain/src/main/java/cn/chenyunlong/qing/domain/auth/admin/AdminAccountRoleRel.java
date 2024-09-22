package cn.chenyunlong.qing.domain.auth.admin;

import cn.chenyunlong.codegen.annotation.GenRepository;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@GenRepository
@Entity
@Table(name = "sys_admin_account_role_rel")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminAccountRoleRel extends BaseJpaAggregate {

    private Long adminAccountId;

    private Long roleId;

}
