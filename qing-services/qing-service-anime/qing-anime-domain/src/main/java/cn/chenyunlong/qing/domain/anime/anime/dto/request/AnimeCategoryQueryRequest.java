package cn.chenyunlong.qing.domain.anime.anime.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class AnimeCategoryQueryRequest implements Request {

    private Long id;

}
