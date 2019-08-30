package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.dao.WechatContentRepository;
import chenyunlong.zhangli.entities.WechatContent;
import chenyunlong.zhangli.service.WechatContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WechatContentImpl implements WechatContentService {

    private final WechatContentRepository wechatContentRepository;

    @Autowired
    public WechatContentImpl(WechatContentRepository wechatContentRepository) {
        this.wechatContentRepository = wechatContentRepository;
    }

    @Override
    public List<WechatContent> getAllWechatContents() {
        return wechatContentRepository.findAll();
    }

    @Override
    public WechatContent addWechatContent(WechatContent wechatContent) {

        return wechatContentRepository.save(wechatContent);
    }
}
