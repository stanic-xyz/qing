package cn.chenyunlong.common.model.serializer;

import cn.chenyunlong.common.enums.CommonEnum;
import cn.chenyunlong.common.model.vo.CommonEnumVO;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public class CommonEnumJsonSerializer extends JsonSerializer {

    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws
        IOException {
        CommonEnum commonEnum = (CommonEnum) o;
        CommonEnumVO commonEnumVO = CommonEnumVO.from(commonEnum);
        jsonGenerator.writeObject(commonEnumVO);
    }
}
