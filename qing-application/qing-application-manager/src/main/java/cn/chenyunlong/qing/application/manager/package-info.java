/**
 * 启动包。
 */

@QueryEntities(value = {BaseEntity.class, BaseJpaAggregate.class})
package cn.chenyunlong.qing.application.manager;

import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.domain.BaseEntity;
import com.querydsl.core.annotations.QueryEntities;
