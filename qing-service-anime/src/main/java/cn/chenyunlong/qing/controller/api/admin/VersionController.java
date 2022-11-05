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

package cn.chenyunlong.qing.controller.api.admin;


import cn.chenyunlong.qing.controller.base.ApiController;
import cn.chenyunlong.qing.core.ApiResult;
import cn.chenyunlong.qing.model.dto.VersionDTO;
import cn.chenyunlong.qing.model.params.VersionParam;
import cn.chenyunlong.qing.service.VersionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * (Version)表控制层
 *
 * @author makejava
 * @since 2022-05-21 23:24:25
 */
@Tag(name = "版本")
@RestController
@RequestMapping("api/versions")
@RequiredArgsConstructor
public class VersionController extends ApiController {
    /**
     * 服务对象
     */
    private final VersionService versionService;

    /**
     * 分页查询所有数据
     *
     * @return 所有数据
     */
    @GetMapping
    public ApiResult<List<VersionDTO>> selectAll() {
        return success(versionService.getAllVersions());
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ApiResult<VersionDTO> selectOne(@PathVariable Long id) {
        return success(versionService.getDetailById(id));
    }

    /**
     * 新增数据
     *
     * @param param 实体对象
     * @return 新增结果
     */
    @PostMapping
    public ApiResult<VersionDTO> insert(@RequestBody @Valid VersionParam param) {
        return success(this.versionService.createBy(param.convertTo()));
    }

    /**
     * 修改数据
     *
     * @param param 实体对象
     * @return 修改结果
     */
    @PutMapping("{id}")
    public ApiResult<Boolean> update(@RequestBody @Valid VersionParam param, @PathVariable String id) {
        this.versionService.updateBy(param, id);
        return success(true);
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public ApiResult<Boolean> delete(@RequestParam("idList") List<Long> idList) {
        return success(this.versionService.removeByIds(idList));
    }
}

