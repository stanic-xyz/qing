package chenyunlong.zhangli.controller.api.system;

import chenyunlong.zhangli.common.annotation.Log;
import chenyunlong.zhangli.common.constant.UserConstants;
import chenyunlong.zhangli.common.core.controller.BaseController;
import chenyunlong.zhangli.common.core.page.TableDataInfo;
import chenyunlong.zhangli.common.exception.ServiceException;
import chenyunlong.zhangli.model.entities.sys.SysConfig;
import chenyunlong.zhangli.common.enums.BusinessType;
import chenyunlong.zhangli.common.service.ISysConfigService;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 参数配置 信息操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/config")
public class SysConfigController extends BaseController {
    private final String prefix = "system/config";

    private final ISysConfigService configService;

    public SysConfigController(ISysConfigService configService) {
        this.configService = configService;
    }

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
