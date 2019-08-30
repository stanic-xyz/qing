package chenyunlong.zhangli.entities.common;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseEntitiy {
    private long X_CreateUserID;
    private long X_createTime;
    private long X_LastEditTime;
    private boolean X_dataFlag;
}
