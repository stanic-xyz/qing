package cn.chenyunlong.qing.application.manager.web.graphql;

import cn.chenyunlong.qing.application.manager.AbstractTests;
import jakarta.annotation.Resource;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.graphql.test.tester.GraphQlTester;


@AutoConfigureGraphQlTester
class BookControllerTest extends AbstractTests {


    @Resource
    private GraphQlTester graphQlTester;

    @Test
    void findAll() {
        String query = """
            {
              books {
                id
                name
                pageCount
                authorId
              }
            }""";
        List<Book> books = graphQlTester.document(query)
                               .execute()
                               .path("data.books[*]")
                               .entityList(Book.class)
                               .get();
        Assertions.assertFalse(books.isEmpty());
        Assertions.assertNotNull(books.get(0).id());
        Assertions.assertNotNull(books.get(0).name());
    }
}
