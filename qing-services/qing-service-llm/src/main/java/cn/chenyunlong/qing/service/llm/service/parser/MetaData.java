package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.enums.RecordRoleEnum;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MetaData {

    private List<String> supportedFileExtension;

    private String channelCode;

    private String parserName;

    private RecordRoleEnum recordRole = RecordRoleEnum.PRIMARY;
}
