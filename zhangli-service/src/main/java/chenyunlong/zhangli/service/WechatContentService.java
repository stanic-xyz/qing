package chenyunlong.zhangli.service;

import chenyunlong.zhangli.entities.WechatContent;

import java.util.List;

public interface WechatContentService {

    List<WechatContent> getAllWechatContents();

    WechatContent addWechatContent(WechatContent wechatContent);
}
