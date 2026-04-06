package cn.chenyunlong.qing.service.llm.service.parser;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MetaData {

    private String supportedFileExtension;

    private String channelCode;

    private String parserName;
}
