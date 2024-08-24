package cn.chenyunlong.qing.application.manager.jimmer.entity;

import cn.chenyunlong.qing.application.manager.jimmer.enumes.Gender;
import java.util.List;
import org.babyfish.jimmer.sql.Entity;
import org.babyfish.jimmer.sql.GeneratedValue;
import org.babyfish.jimmer.sql.GenerationType;
import org.babyfish.jimmer.sql.Id;
import org.babyfish.jimmer.sql.Key;
import org.babyfish.jimmer.sql.ManyToMany;

@Entity
public interface Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id();

    @Key
    String firstName();

    @Key
    String lastName();

    /*
     * 这里，Gender是一个枚举，代码稍后给出
     */
    Gender gender();

    @ManyToMany(mappedBy = "authors")
    List<Book> books();
}
