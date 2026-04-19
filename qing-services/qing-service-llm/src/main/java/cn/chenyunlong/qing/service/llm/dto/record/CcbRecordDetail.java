package cn.chenyunlong.qing.service.llm.dto.record;

import java.io.Serializable;

public class CcbRecordDetail implements RecordBaseDetail {
    private String id;
    private String name;
    private String description;

    @Override
    public String getType() {
        return "";
    }
}
