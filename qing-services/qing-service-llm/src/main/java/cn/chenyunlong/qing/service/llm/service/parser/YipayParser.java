package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import java.util.List;

@Component("YIPAY")
public class YipayParser extends BaseFileParser {
    @Override
    public ParseResult parse(InputStream inputStream, String originalFilename) throws Exception {
        return wrapResult(java.util.Collections.emptyList());
    }
    // 实现类似支付宝
}
