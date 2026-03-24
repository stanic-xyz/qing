package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component("YIPAY")
public class YipayParser extends BaseFileParser {
    @Override
    public List<TransactionRecord> parse(InputStream inputStream, String originalFilename) throws Exception {
        return List.of();
    }
    // 实现类似支付宝
}
