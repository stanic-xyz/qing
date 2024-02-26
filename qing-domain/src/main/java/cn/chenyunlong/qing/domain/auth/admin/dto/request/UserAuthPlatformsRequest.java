package cn.chenyunlong.qing.domain.auth.admin.dto.request;

import cn.chenyunlong.common.model.Request;
import java.util.List;
import lombok.Data;

@Data
public class UserAuthPlatformsRequest implements Request {

    private Long accountId;

    private List<Long> platformIds;

}
