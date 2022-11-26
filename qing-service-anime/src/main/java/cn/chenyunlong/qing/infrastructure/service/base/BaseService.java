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

package cn.chenyunlong.qing.infrastructure.service.base;

import cn.chenyunlong.qing.infrastructure.model.dto.base.OutputConverter;
import cn.chenyunlong.qing.infrastructure.utils.BeanUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collections;
import java.util.List;

public interface BaseService<DOMAIN> extends IService<DOMAIN> {


    /**
     * 转换对象
     *
     * @param domain         需要转化的实体类
     * @param targetDtoClass 目标实体类
     * @param <DTO>          DTO实体类的类型
     * @return 转化完成的实体类
     */
    default <DTO extends OutputConverter<DTO, DOMAIN>> DTO toDto(DOMAIN domain, Class<DTO> targetDtoClass) {
        if (domain == null) {
            return null;
        }
        //调用实体类
        return BeanUtils.transformFrom(domain, targetDtoClass);
    }

    default <DTO extends OutputConverter<DTO, DOMAIN>> List<DTO> toListDto(List<DOMAIN> domains, Class<DTO> targetDtoClass) {
        if (domains == null || domains.isEmpty()) {
            return Collections.emptyList();
        }
        return BeanUtils.transformFromInBatch(domains, targetDtoClass);
    }
}
