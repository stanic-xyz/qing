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

package cn.chenyunlong.qing.controller.api.system;

import cn.chenyunlong.qing.annotation.Log;
import cn.chenyunlong.qing.core.constant.UserConstants;
import cn.chenyunlong.qing.core.controller.BaseController;
import cn.chenyunlong.qing.core.enums.BusinessType;
import cn.chenyunlong.qing.core.exception.ServiceException;
import cn.chenyunlong.qing.core.page.TableDataInfo;
import cn.chenyunlong.qing.domain.system.SysConfig;
import cn.chenyunlong.qing.service.ISysConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统配置控制器
 *
 * @author Stan
 * @date 2022/11/05
 */
@Tag(name = "系统配置")
@RestController
@RequestMapping("/system/config")
@RequiredArgsConstructor
public class SysConfigController extends BaseController {
    private final String prefix = "system/config";

    private final ISysConfigService configService;

    @GetMapping()
    public String config() {
        return prefix + "/config";
    }

    /**
     * 查询参数配置列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysConfig config) {
        startPage();
        List<SysConfig> list = configService.selectConfigList(config);
        return getDataTable(list);
    }

    @Log(title = "参数管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public List<SysConfig> export(SysConfig config) {
        //      好的  ExcelUtil<SysConfig> util = new ExcelUtil<SysConfig>(SysConfig.class);
        //      好的  return util.exportExcel(list, "参数数据");
        return configService.selectConfigList(config);
    }

    /**
     * 新增参数配置
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    @Log(title = "参数管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public int addSave(@Validated SysConfig config) {
        if (UserConstants.CONFIG_KEY_NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))) {
            throw new ServiceException("新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        return configService.insertConfig(config);
    }

    /**
     * 修改参数配置
     */
    @GetMapping("/edit/{configId}")
    public String edit(@PathVariable("configId") Long configId, ModelMap maps) {
        maps.put("config", configService.selectConfigById(configId));
        return prefix + "/edit";
    }

    /**
     * 修改保存参数配置
     */
    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public int editSave(@Validated SysConfig config) {
        if (UserConstants.CONFIG_KEY_NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))) {
            throw new ServiceException("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }

        return configService.updateConfig(config);
    }

    @Log(title = "参数管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public int remove(String ids) {
        return configService.deleteConfigByIds(ids);
    }

    /**
     * 清空缓存
     */
    @Log(title = "参数管理", businessType = BusinessType.CLEAN)
    @GetMapping("/clearCache")
    @ResponseBody
    public void clearCache() {
        configService.clearCache();
    }

    /**
     * 校验参数键名
     */
    @PostMapping("/checkConfigKeyUnique")
    @ResponseBody
    public String checkConfigKeyUnique(SysConfig config) {
        return configService.checkConfigKeyUnique(config);
    }
}
