package cn.chenyunlong.qing.application.manager.jimmer.entity;

import org.babyfish.jimmer.sql.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
