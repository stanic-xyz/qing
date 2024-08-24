package cn.chenyunlong.qing.application.manager.jimmer.repository;

import cn.chenyunlong.qing.application.manager.jimmer.Fetchers;
import cn.chenyunlong.qing.application.manager.jimmer.dto.book.ComplexBookView;
import cn.chenyunlong.qing.application.manager.jimmer.entity.Author;
import cn.chenyunlong.qing.application.manager.jimmer.entity.Book;
import cn.chenyunlong.qing.application.manager.jimmer.entity.BookFetcher;
import cn.chenyunlong.qing.application.manager.jimmer.entity.BookTable;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.sql.DataSource;
import org.babyfish.jimmer.Page;
import org.babyfish.jimmer.UnloadedException;
import org.babyfish.jimmer.jackson.ImmutableModule;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.ast.Predicate;
import org.babyfish.jimmer.sql.fetcher.Fetcher;
import org.babyfish.jimmer.sql.runtime.ConnectionManager;
import org.babyfish.jimmer.sql.runtime.SqlFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

class BookRepositoryTest {

    private static final String JDBC_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=0;DB_CLOSE_ON_EXIT=FALSE";

    private static DataSource dataSource;

    @BeforeAll
    static void beforeAll() throws IOException, SQLException {
        HikariConfig configuration = new HikariConfig();
        configuration.setJdbcUrl(JDBC_URL);
        configuration.setUsername("sa");
        configuration.setPassword("");
        configuration.setDriverClassName("org.h2.Driver");
        dataSource = new HikariDataSource(configuration);

        // 加载测试数据
        ClassPathResource pathResource = new ClassPathResource("sql/jimmer-demo-data.sql");
        String sql = IoUtil.read(pathResource.getInputStream(), StandardCharsets.UTF_8);
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(sql);
            connection.commit();
        }
    }

    @Test
    void findBookById() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            // 导入测试数据
            ConnectionManager connectionManager = ConnectionManager.singleConnectionManager(connection);
            JSqlClient sqlClient = JSqlClient
                                       .newBuilder()
                                       .setConnectionManager(connectionManager)
                                       .setSqlFormatter(SqlFormatter.pretty("    ", 1, 80))
                                       .build();

            String name = "a";
            BookTable table = BookTable.$;
            BookFetcher bookFetcher = Fetchers.BOOK_FETCHER;
            Fetcher<Book> fetcher = bookFetcher.allScalarFields()
                                        .name()
                                        .edition()
                                        .authors(Fetchers.AUTHOR_FETCHER.firstName());
            Page<Book> bookPage = sqlClient
                                      .createQuery(table)
                                      .where(
                                          Predicate.or(
                                              table.name().ilikeIf(name),
                                              table.price().betweenIf(new BigDecimal(1), new BigDecimal(2)))
                                      )
                                      .select(table.fetch(fetcher))
                                      .limit(1, 2).fetchPage(1, 2, connection);
            System.out.println("bookList = " + bookPage);

            List<Author> authors = bookPage.getRows().get(0).authors();

            System.out.println("authors = " + authors);
            Assertions.assertTrue(CollUtil.isNotEmpty(authors));
            Author author = authors.get(0);

            System.out.println("author.id() = " + author.id());
            Assertions.assertEquals("Eve", author.firstName());
            Assertions.assertThrows(UnloadedException.class, author::lastName);
        }
    }

    @Test
    void fetchByDtoLanguage() throws SQLException, IOException {
        // 加载测试数据
        ClassPathResource pathResource = new ClassPathResource("sql/jimmer-demo-data.sql");

        String sql = IoUtil.read(pathResource.getInputStream(), StandardCharsets.UTF_8);

        try (Connection connection = dataSource.getConnection()) {

            ConnectionManager connectionManager = ConnectionManager.singleConnectionManager(connection);
            JSqlClient sqlClient = JSqlClient
                                       .newBuilder()
                                       .setConnectionManager(connectionManager)
                                       .setSqlFormatter(SqlFormatter.pretty("    ", 1, 80))
                                       .build();

            String name = "a";
            BookTable table = BookTable.$;
            Page<ComplexBookView> viewPage = sqlClient
                                                 .createQuery(table)
                                                 .where(table.name().ilike(name))
                                                 .select(table.fetch(ComplexBookView.class))
                                                 .limit(1, 2).fetchPage(1, 2, connection);
            System.out.println("bookList = " + viewPage);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new ImmutableModule());
            String sequenceWriter = objectMapper
                                        .writerWithDefaultPrettyPrinter()
                                        .writeValueAsString(viewPage);

            System.out.println("sequenceWriter = " + sequenceWriter);

            ComplexBookView complexBookView = viewPage.getRows().get(0);

            List<ComplexBookView.TargetOf_authors> viewAuthors = complexBookView.getAuthors();
            System.out.println("authors = " + viewAuthors);
            Assertions.assertTrue(CollUtil.isNotEmpty(viewAuthors));
            ComplexBookView.TargetOf_authors author = viewAuthors.get(0);

            System.out.println("author.id() = " + author.getAuthorId());
            Assertions.assertNotNull(author.getLastName());
        }
    }
}
