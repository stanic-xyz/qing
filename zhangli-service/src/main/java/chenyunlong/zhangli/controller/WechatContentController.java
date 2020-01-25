package chenyunlong.zhangli.controller;

import chenyunlong.zhangli.entities.WechatContent;
import chenyunlong.zhangli.entities.WechatImages;
import chenyunlong.zhangli.enums.MessageType;
import chenyunlong.zhangli.service.WechatContentService;
import chenyunlong.zhangli.utils.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@Api(description = "微信内容控制器")
@RestController
@RequestMapping("wechatContent")
public class WechatContentController {

    private final WechatContentService wechatContentService;

    @Autowired
    public WechatContentController(WechatContentService wechatContentService) {
        this.wechatContentService = wechatContentService;
    }


    @ApiOperation(value = "获取微信列表", notes = "获取微信列表")
    @GetMapping("list")
    public List<WechatContent> list() {
        return wechatContentService.getAllWechatContents();
    }

    @ApiOperation(value = "上传威信发布内容")
    @PostMapping
    public BaseResponse uploadWechatContent(@RequestParam String content) {

        WechatContent wechatContent = new WechatContent();
        wechatContent.setContent(content);
        wechatContent.setHeadImgUrl("http://www.stanic.xyz/pic/496.jpg");
        wechatContent.setMessageType(MessageType.SIMPLE_MESSAGE);
        wechatContent.setUserId(0);

        List<WechatImages> wechatImagesList = new LinkedList<>();
        WechatImages wechatImage = new WechatImages();
        wechatImage.setImageUrl("http://www.stanic.xyz/pic/596.jpg");
        wechatImagesList.add(wechatImage);
        wechatContent.setWechatImages(wechatImagesList);

        return BaseResponse.success(wechatContentService.addWechatContent(wechatContent));
    }
}
