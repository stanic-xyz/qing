/*
 * Copyright (c) 2019-2023  YunLong Chen
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

/**
 * 领域基础包，提供实体操作的基础设施
 * 
 * <h2>API使用指南</h2>
 * 
 * <h3>1. 流式API（高级用法）</h3>
 * <p>
 * 适合复杂的业务场景，提供链式调用和丰富的钩子函数：
 * </p>
 * 
 * <pre>{@code
 * // 使用EntityCreator创建实体
 * Optional<User> user = new EntityCreator<>(userRepository)
 *         .create(() -> new User())
 *         .update(u -> {
 *             u.setName("张三");
 *             u.setEmail("zhangsan@example.com");
 *         })
 *         .validate(u -> {
 *             if (u.getName() == null) {
 *                 throw new IllegalArgumentException("姓名不能为空");
 *             }
 *         })
 *         .successHook(u -> log.info("用户创建成功: {}", u.getId()))
 *         .errorHook(e -> log.error("用户创建失败", e))
 *         .execute();
 * 
 * // 使用EntityUpdater更新实体
 * Optional<User> updatedUser = new EntityUpdater<>(userRepository)
 *         .loadById(userId)
 *         .update(u -> u.setEmail("newemail@example.com"))
 *         .validate(u -> validateEmail(u.getEmail()))
 *         .execute();
 * }</pre>
 * 
 * <h3>2. 简化服务API（推荐用法）</h3>
 * <p>
 * 适合大多数业务场景，提供简洁的方法调用：
 * </p>
 * 
 * <pre>{@code
 * // 创建SimpleEntityService实例
 * SimpleEntityService<User, UserId> userService = new SimpleEntityService<>(userRepository);
 * 
 * // 创建实体
 * Optional<User> user = userService.create(() -> new User(), u -> {
 *     u.setName("张三");
 *     u.setEmail("zhangsan@example.com");
 * });
 * 
 * // 更新实体
 * Optional<User> updatedUser = userService.updateById(userId, u -> {
 *     u.setEmail("newemail@example.com");
 * });
 * 
 * // 查找实体
 * Optional<User> foundUser = userService.findById(userId);
 * }</pre>
 * 
 * <h3>3. 工具类API（最简用法）</h3>
 * <p>
 * 适合简单场景和快速开发：
 * </p>
 * 
 * <pre>{@code
 * // 创建并保存实体
 * Optional<User> user = EntityUtils.createUpdateAndSave(userRepository,
 *         () -> new User(),
 *         u -> {
 *             u.setName("张三");
 *             u.setEmail("zhangsan@example.com");
 *         });
 * 
 * // 查找并更新实体
 * Optional<User> updatedUser = EntityUtils.findUpdateAndSave(userRepository, userId,
 *         u -> u.setEmail("newemail@example.com"));
 * 
 * // 检查实体是否存在
 * boolean exists = EntityUtils.exists(userRepository, userId);
 * }</pre>
 * 
 * <h3>选择建议</h3>
 * <ul>
 * <li><strong>流式API</strong>：复杂业务逻辑，需要详细的错误处理和钩子函数</li>
 * <li><strong>简化服务API</strong>：常规业务开发，平衡了功能性和易用性</li>
 * <li><strong>工具类API</strong>：简单操作，快速开发原型</li>
 * </ul>
 * 
 * @author 陈云龙
 * @since 2024-08-24
 */
package cn.chenyunlong.qing.domain.base;