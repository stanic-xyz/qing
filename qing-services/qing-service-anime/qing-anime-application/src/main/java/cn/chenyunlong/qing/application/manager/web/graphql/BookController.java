package cn.chenyunlong.qing.application.manager.web.graphql;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.graphql.data.GraphQlRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;

import java.util.List;

@Tag(name = "书籍")
@GraphQlRepository
public class BookController {

    @QueryMapping("bookById")
    public Book bookById(
        @Argument
        String id) {
        return Book.getById(id);
    }

    /**
     * 查询书籍列表。
     */
    @QueryMapping("books")
    public List<Book> books(
        @Argument
        String id) {
        System.out.println("创建工作计划" + id);
        return Book.books;
    }

    public Author author(Book book) {
        return Author.getById(book.authorId());
    }
}
