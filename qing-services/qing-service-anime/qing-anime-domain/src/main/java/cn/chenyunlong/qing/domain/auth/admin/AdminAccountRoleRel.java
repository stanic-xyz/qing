package cn.chenyunlong.qing.domain.auth.admin;

import cn.chenyunlong.qing.domain.common.AggregateId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminAccountRoleRel {

    private AggregateId adminAccountId;

    private AggregateId roleId;

}
