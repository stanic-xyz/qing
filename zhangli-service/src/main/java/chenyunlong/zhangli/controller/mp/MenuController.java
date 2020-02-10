package chenyunlong.zhangli.controller.mp;

import chenyunlong.zhangli.model.ResultUtil;
import chenyunlong.zhangli.utils.BaseResponse;
import com.riversoft.weixin.common.menu.Menu;
import com.riversoft.weixin.common.menu.MenuItem;
import com.riversoft.weixin.common.menu.MenuType;
import com.riversoft.weixin.mp.base.AppSetting;
import com.riversoft.weixin.mp.menu.Menus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("wxmp/menu")
public class MenuController {

    @GetMapping("create")
    public BaseResponse createMenu(@RequestParam String menuInfo) {

        AppSetting appSetting = AppSetting.defaultSettings();
        Menu menu = new Menu();
        menu.add(new MenuItem().key("key").name("关键字").type(MenuType.view).url("http://www.stanic.xyz"));
        Menus.with(appSetting).create(menu);
        return ResultUtil.success("创建成功");
    }
}
