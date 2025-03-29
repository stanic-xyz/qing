package cn.chenyunlong.qing.application.manager.jimmer.entity;

import jakarta.annotation.Nullable;
import org.babyfish.jimmer.sql.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
public interface Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id();

    @Key
    String name();

    @Key
    int edition();

    BigDecimal price();

    @ManyToOne
    @Nullable
    BookStore store();

    @ManyToMany
    @JoinTable(
        name = "BOOK_AUTHOR_MAPPING",
        joinColumnName = "BOOK_ID",
        inverseJoinColumnName = "AUTHOR_id"
    )
    List<Author> authors();
}
