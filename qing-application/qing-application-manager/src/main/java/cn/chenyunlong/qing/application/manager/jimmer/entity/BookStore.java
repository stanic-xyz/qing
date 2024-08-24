package cn.chenyunlong.qing.application.manager.jimmer.entity;

import java.util.List;
import org.babyfish.jimmer.sql.Entity;
import org.babyfish.jimmer.sql.GeneratedValue;
import org.babyfish.jimmer.sql.GenerationType;
import org.babyfish.jimmer.sql.Id;
import org.babyfish.jimmer.sql.Key;
import org.babyfish.jimmer.sql.OneToMany;
import org.jetbrains.annotations.Nullable;

@Entity
public interface BookStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id();

    @Key
    String name();

    @Nullable
    String website();

    @OneToMany(mappedBy = "store")
    List<Book> books();
}
