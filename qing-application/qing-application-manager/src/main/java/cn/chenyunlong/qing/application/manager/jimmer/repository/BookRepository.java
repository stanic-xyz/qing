package cn.chenyunlong.qing.application.manager.jimmer.repository;

import cn.chenyunlong.qing.application.manager.jimmer.Tables;
import cn.chenyunlong.qing.application.manager.jimmer.dto.book.BookInput;
import cn.chenyunlong.qing.application.manager.jimmer.entity.Book;
import cn.chenyunlong.qing.application.manager.jimmer.entity.BookTable;
import jakarta.annotation.Nullable;
import java.util.List;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.fetcher.Fetcher;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepository {

    private final JSqlClient sqlClient;

    public BookRepository(JSqlClient sqlClient) {
        this.sqlClient = sqlClient;
    }

    @Nullable
    public Book findBookById(
        long id,
        Fetcher<Book> fetcher
    ) {
        return sqlClient.findById(
            fetcher,
            id
        );
    }

    public List<Book> findBooksByName(
        @Nullable
        String name,
        @Nullable
        Fetcher<Book> fetcher
    ) {
        BookTable table = Tables.BOOK_TABLE;
        return sqlClient
                   .createQuery(table)
                   .whereIf(
                       name != null && !name.isEmpty(),
                       table.name().ilike(name)
                   )
                   .select(
                       table.fetch(fetcher)
                   ).execute();
    }

    public void save(BookInput bookIn) {
        sqlClient.save(bookIn);
    }
}
