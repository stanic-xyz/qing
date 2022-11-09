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

package cn.chenyunlong.codegen.test.domain.student;

import cn.chenyunlong.codegen.processor.api.*;
import cn.chenyunlong.codegen.processor.controller.GenController;
import cn.chenyunlong.codegen.processor.creator.GenCreator;
import cn.chenyunlong.codegen.processor.creator.IgnoreCreator;
import cn.chenyunlong.codegen.processor.mapper.GenMapper;
import cn.chenyunlong.codegen.processor.query.GenQuery;
import cn.chenyunlong.codegen.processor.repository.GenRepository;
import cn.chenyunlong.codegen.processor.service.GenService;
import cn.chenyunlong.codegen.processor.service.GenServiceImpl;
import cn.chenyunlong.codegen.processor.updater.GenUpdater;
import cn.chenyunlong.codegen.processor.updater.IgnoreUpdater;
import cn.chenyunlong.codegen.processor.vo.GenVo;
import cn.chenyunlong.common.constants.ValidStatus;
import com.only4play.jpa.converter.ValidStatusConverter;
import com.only4play.jpa.support.BaseJpaAggregate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@GenVo(pkgName = "cn.chenyunlong.codegen.test.domain.student.vo")
@GenCreator(pkgName = "cn.chenyunlong.codegen.test.domain.student.creator")
@GenUpdater(pkgName = "cn.chenyunlong.codegen.test.domain.student.updater")
@GenRepository(pkgName = "cn.chenyunlong.codegen.test.domain.student.repository")
@GenService(pkgName = "cn.chenyunlong.codegen.test.domain.student.service")
@GenServiceImpl(pkgName = "cn.chenyunlong.codegen.test.domain.student.service")
@GenQuery(pkgName = "cn.chenyunlong.codegen.test.domain.student.query")
@GenMapper(pkgName = "cn.chenyunlong.codegen.test.domain.student.mapper")
@GenController(pkgName = "cn.chenyunlong.codegen.test.domain.student.controller")
@GenCreateRequest(pkgName = "cn.chenyunlong.codegen.test.domain.student.api.request")
@GenUpdateRequest(pkgName = "cn.chenyunlong.codegen.test.domain.student.api.request")
@GenQueryRequest(pkgName = "cn.chenyunlong.codegen.test.domain.student.api.request")
@GenResponse(pkgName = "cn.chenyunlong.codegen.test.domain.student.api.response")
@GenFeign(pkgName = "cn.chenyunlong.codegen.test.domain.student.api.service", serverName = "service")
@Entity
@Table(name = "t_student", indexes = {
        @Index(name = "idx_student_user_id_unq", columnList = "user_id", unique = true)
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Student extends BaseJpaAggregate {

    @Convert(converter = ValidStatusConverter.class)
    @IgnoreUpdater
    @IgnoreCreator
    private ValidStatus validStatus;

    @Column(name = "user_id", unique = true)
    private Integer userId;

    public void init() {
        setValidStatus(ValidStatus.VALID);
    }

    public void valid() {
        setValidStatus(ValidStatus.VALID);
    }

    public void invalid() {
        setValidStatus(ValidStatus.INVALID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Student student = (Student) o;
        return getId() != null && Objects.equals(getId(), student.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
