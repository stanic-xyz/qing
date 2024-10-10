package cn.chenyunlong.qing.application.manager.web.graphql;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Tag(name = "书籍")
@Controller
public class BookController {

    @QueryMapping
    public Book bookById(
        @Argument
        String id) {
        return Book.getById(id);
    }

    /**
     * 查询书籍列表。
     */
    @QueryMapping
    public List<Book> books(
        @Argument
        String id) {
        return Book.books;
    }

    @SchemaMapping
    public Author author(Book book) {
        return Author.getById(book.authorId());
    }
}
