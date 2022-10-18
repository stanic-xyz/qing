/*
 * Copyright (c) 2019-2022 YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.qing.model.dto.base;

import cn.chenyunlong.qing.utils.BeanUtils;

/**
 * @author Stan
 */
public class DTOUtil {


    /**
     * 转换对象
     *
     * @param domain         需要转化的实体类
     * @param targetDtoClass 目标实体类
     * @param <DTO>          DTO实体类的类型
     * @param <DOMAIN>       实体类的类型
     * @return 转化完成的实体类
     */
    public static <DOMAIN, DTO extends OutputConverter<DTO, DOMAIN>> DTO newDTO(DOMAIN domain, Class<DTO> targetDtoClass) {
        if (domain == null) {
            return null;
        }
        //调用实体类
        return BeanUtils.transformFrom(domain, targetDtoClass);
    }
}
