package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import cn.hutool.core.collection.CollUtil;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

public interface FileParser {

    String channelCode();

    default List<String> supportedFileExtensions() {
        return CollUtil.newArrayList();
    }

    default boolean support(String fileExtension) {
        return fileExtension.equalsIgnoreCase("txt");
    }

    default MetaData getMetaData() {
        List<String> supportedFileExtension = supportedFileExtensions();
        return MetaData.builder()
                .channelCode(channelCode())
                .parserName(this.getClass().getSimpleName())
                .supportedFileExtension(CollUtil.isEmpty(supportedFileExtension) ? CollUtil.newArrayList() : supportedFileExtension.stream().map(String::toUpperCase).collect(Collectors.toList()))
                .build();
    }


    ParseResult parse(InputStream inputStream, String originalFilename) throws Exception;
}
