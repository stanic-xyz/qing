package cn.chenyunlong.codegen.example.domain;

import cn.chenyunlong.qing.domain.common.Identifiable;
import lombok.Data;

@Data
public class OrderId implements Identifiable<Long> {

    private Long id;

    @Override
    public Long id() {
        return id;
    }
}
