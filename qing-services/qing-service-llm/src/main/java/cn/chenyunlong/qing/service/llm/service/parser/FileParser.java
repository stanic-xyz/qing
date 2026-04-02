package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;

import java.io.InputStream;

public interface FileParser {
    ParseResult parse(InputStream inputStream, String originalFilename) throws Exception;
}
