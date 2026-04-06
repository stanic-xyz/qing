package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;

import java.io.InputStream;

public interface FileParser {


    default MetaData getMetaData() {
        return MetaData.builder()
                .channelCode(this.getClass().getName())
                .parserName(this.getClass().getSimpleName())
                .supportedFileExtension("txt")
                .build();
    }


    ParseResult parse(InputStream inputStream, String originalFilename) throws Exception;
}
