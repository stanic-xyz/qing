package cn.chenyunlong.qing.application.manager.jimmer.controller;

import cn.chenyunlong.qing.application.manager.jimmer.entity.Book;
import cn.chenyunlong.qing.application.manager.jimmer.entity.BookTable;
import cn.chenyunlong.qing.application.manager.jimmer.enumes.Gender;
import cn.chenyunlong.qing.application.manager.jimmer.repository.BookRepository;
import jakarta.annotation.Nullable;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.babyfish.jimmer.Page;
import org.babyfish.jimmer.client.meta.Api;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.ast.Predicate;
import org.babyfish.jimmer.sql.ast.query.Order;
import org.babyfish.jimmer.sql.fetcher.Fetcher;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Api
@ResponseBody
@RestController
@RequestMapping("/test/jimmer")
@RequiredArgsConstructor
public class TestJimmerController {

    private final JSqlClient sqlClient;
    private final BookRepository bookRepository;

    private static final BookTable T = BookTable.$;

    @Api
    @GetMapping("jimmer")
    public Page<Book> testJimmer() {
        return findBooks(1, 10, null, null, null, null, null, null, null, null, Gender.MALE);
    }

    public @NotNull Page<Book> findBooks(

        int pageIndex, // 从0开始
        int pageSize,

        @Nullable
        Fetcher<Book> fetcher,
        // 例如: "name asc, edtion desc"
        @Nullable
        String sortCode,

        @Nullable
        String name,
        @Nullable
        BigDecimal minPrice,
        @Nullable
        BigDecimal maxPrice,
        @Nullable
        String storeName,
        @Nullable
        String storeWebsite,
        @Nullable
        String authorName,
        @Nullable
        Gender authorGender
    ) {
        return sqlClient
                   .createQuery(T)
                   .where(T.name().ilikeIf(name))
                   .where(T.price().betweenIf(minPrice, maxPrice))
                   .where(T.store().name().ilikeIf(storeName))
                   .where(T.store().website().ilikeIf(storeWebsite))
                   .where(
                       T.authors(author ->
                                     Predicate.or(
                                         author.firstName().ilikeIf(authorName),
                                         author.lastName().ilikeIf(authorName)
                                     )
                       )
                   )
                   .where(T.authors(author -> author.gender().eqIf(authorGender)))
                   .orderBy(
                       Order.makeOrders(
                           T,
                           sortCode != null ? sortCode : "name asc, edition desc"
                       )
                   )
                   .select(
                       BookTable.$.__disableJoin("这里不可以关联").fetch(fetcher)
                   )
                   .fetchPage(pageIndex, pageSize);
    }
}
