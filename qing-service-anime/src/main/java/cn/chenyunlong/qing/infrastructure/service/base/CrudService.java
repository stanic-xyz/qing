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

import cn.chenyunlong.qing.infrastructure.exception.NotFoundException;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;

/**
 * CrudService interface contains some common methods.
 *
 * @param <DOMAIN> domain type
 * @author johnniang
 */
public interface CrudService<DOMAIN> extends BaseService<DOMAIN> {


    /**
     * Exists by id.
     *
     * @param id id
     * @return boolean
     */
    boolean existsById(@NonNull Serializable id);

    /**
     * Must exist by id, or throw NotFoundException.
     *
     * @param id id
     * @throws NotFoundException If the specified id does not exist
     */
    DOMAIN mustExistById(@NonNull Serializable id);

    /**
     * save by domain
     *
     * @param domain domain
     * @return DOMAIN
     */
    @Transactional
    DOMAIN create(@NonNull DOMAIN domain);

    /**
     * Updates by domain
     *
     * @param domain domain
     * @return DOMAIN
     */
    @NonNull
    @Transactional
    boolean updateBy(@NonNull DOMAIN domain);


    /**
     * Updates by domains
     *
     * @param domains domains
     * @return List
     */
    @NonNull
    @Transactional
    boolean updateInBatch(@NonNull Collection<DOMAIN> domains);

    /**
     * Remove by domain
     *
     * @param domain domain
     */
    @Transactional
    void remove(@NonNull DOMAIN domain);

    /**
     * Remove by ids
     *
     * @param ids ids
     */
    @Transactional
    void removeInBatch(@NonNull Collection<Object> ids);

}
