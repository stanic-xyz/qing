package cn.chenyunlong.qing.infrastructure.config;

import org.hibernate.dialect.MySQL5InnoDBDialect;

public class MySQLConfig extends MySQL5InnoDBDialect {
    @Override
    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8";
    }
}
