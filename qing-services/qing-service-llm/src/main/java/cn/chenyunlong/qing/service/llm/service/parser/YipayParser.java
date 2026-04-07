package cn.chenyunlong.qing.service.llm.service.parser;

import org.springframework.stereotype.Component;

import java.io.InputStream;

import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;

@Component("YIPAY")
public class YipayParser extends BaseFileParser {

    public static final String CHANNEL_CODE = "YIPAY";

    @Override
    public String channelCode() {
        return CHANNEL_CODE;
    }


    @Override
    public ParseResult parse(InputStream inputStream, String originalFilename) throws Exception {
        return wrapResult(java.util.Collections.emptyList());
    }
    // 实现类似支付宝
}
