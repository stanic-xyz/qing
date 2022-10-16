package cn.chenyunlong.zhangli.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * User login env.
 *
 * @author Stan
 * @version 1.0
 * @date 2020/3/31 2:39 下午
 */
@Data
@ToString
@AllArgsConstructor
public class LoginPreCheckDTO {

    private Boolean needMfaCode;

}
