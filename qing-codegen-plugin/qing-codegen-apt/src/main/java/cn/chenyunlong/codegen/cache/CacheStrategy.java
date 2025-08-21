/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.codegen.cache;

/**
 * 代码生成器缓存策略枚举。
 * 定义不同类型文件的缓存行为，以优化编译性能。
 *
 * @author 陈云龙
 * @since 2024-01-20
 */
public enum CacheStrategy {
    
    /**
     * 无缓存策略 - 每次都重新生成文件
     * 适用于开发调试阶段
     */
    NONE,
    
    /**
     * 智能缓存策略 - 基于类结构哈希判断是否需要重新生成
     * 适用于只读文件（DTO、VO、Request/Response等）
     * 只有当实体类的结构发生变化时才重新生成
     */
    SMART,
    
    /**
     * 跳过策略 - 文件存在时完全跳过生成
     * 适用于可编辑文件（Repository、Service、Controller等）
     * 避免覆盖用户的手动修改
     */
    SKIP_IF_EXISTS
}