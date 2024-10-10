package cn.chenyunlong.qing.application.manager.jimmer.controller;

import cn.chenyunlong.qing.application.manager.jimmer.Fetchers;
import cn.chenyunlong.qing.application.manager.jimmer.dto.book.BookInput;
import cn.chenyunlong.qing.application.manager.jimmer.dto.book.SimpleView;
import cn.chenyunlong.qing.application.manager.jimmer.entity.Book;
import cn.chenyunlong.qing.application.manager.jimmer.repository.BookRepository;
import jakarta.annotation.Nullable;
import org.babyfish.jimmer.client.FetchBy;
import org.babyfish.jimmer.sql.fetcher.Fetcher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("permitAll()")
@RestController
public class JimmerBookController implements Fetchers {

    /**
     * Simple Book DTO which can only access `id` and `name` of `Book` itself
     */
    private static final Fetcher<Book> SIMPLE_BOOK =
        BOOK_FETCHER
            .name()
            .store(BOOK_STORE_FETCHER.name());
    /**
     * Complex Book DTO which can access not only properties of `Book` itself,
     * but also associated `BookStore` and `Author` objects with names
     */
    private static final Fetcher<Book> COMPLEX_BOOK =
        BOOK_FETCHER
            .allScalarFields()
            .store(BOOK_STORE_FETCHER.name())
            .authors(AUTHOR_FETCHER.firstName().lastName()
            );
    private final BookRepository bookRepository;

    public JimmerBookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @PreAuthorize("permitAll()")
    @Nullable
    @GetMapping("book/{id}")
    @FetchBy("COMPLEX_BOOK")
    public Book findBookById(
        @PathVariable("id")
        long id) {
        return bookRepository.findBookById(
            id,
            COMPLEX_BOOK
        );
    }

    @PostMapping("book/{id}")
    public void save(
        @RequestBody
        BookInput bookIn) {
        bookRepository.save(bookIn);
    }

    @GetMapping("books")
    public List<@FetchBy("SIMPLE_BOOK") Book> findBooksByName(
        @RequestParam(name = "name", required = false)
        String name
    ) {
        return bookRepository.findBooksByName(name, SIMPLE_BOOK);
    }

    @GetMapping("simpleBook")
    public SimpleView findBookSimple(
        @RequestParam(name = "name", required = false)
        String name
    ) {
        System.out.println("name = " + name);
        return null;
    }
}
