package cn.chenyunlong.zhangli.controller.api.admin;


import cn.chenyunlong.zhangli.controller.base.ApiController;
import cn.chenyunlong.zhangli.core.ApiResult;
import cn.chenyunlong.zhangli.model.dto.VersionDTO;
import cn.chenyunlong.zhangli.model.params.VersionParam;
import cn.chenyunlong.zhangli.service.VersionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * (Version)表控制层
 *
 * @author makejava
 * @since 2022-05-21 23:24:25
 */
@RestController
@RequestMapping("api/versions")
public class VersionController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private VersionService versionService;

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
    public ApiResult<VersionDTO> selectOne(@PathVariable Serializable id) {
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

