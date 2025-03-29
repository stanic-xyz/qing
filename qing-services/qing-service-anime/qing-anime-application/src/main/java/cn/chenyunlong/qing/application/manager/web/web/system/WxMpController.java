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

package cn.chenyunlong.qing.application.manager.web.web.system;

import cn.chenyunlong.qing.application.manager.web.web.system.model.ConfigAccessRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 对接微信公众平台
 *
 * @author 陈云龙
 * @since 2022/11/05
 */
@Slf4j
@Tag(name = "微信公众平台对接")
@RestController
@RequestMapping("api/wx/mp")
@RequiredArgsConstructor
public class WxMpController {

    private final WxMpService wxMpService;

    /**
     * 验证消息的确来自微信服务器
     * 开发者通过检验signature对请求进行校验。若确认此次GET请求来自微信服务器，请原样返回echostr参数内容，则接入生效
     *
     * @return 验证字符串
     */
    @GetMapping("send")
    public String configAccess(ConfigAccessRequest request) {
        // 校验签名
        if (!wxMpService.checkSignature(request.getTimestamp(), request.getNonce(), request.getSignature())) {
            log.error("签名校验 ===》 非法请求");
            // 消息签名不正确，说明不是公众平台发过来的消息
            return null;
        }
        log.error("签名校验 ===》 验证成功");
        // 返回 echostr
        return request.getEchostr();
    }

    /**
     * 创建菜单
     *
     * @throws WxErrorException 卫星异常
     */
    @GetMapping("createMenu")
    public void createMenu() throws WxErrorException {
        String json = "{}";
        String s = wxMpService.getMenuService().menuCreate(json);
        System.out.println(s);
    }
}
