package cn.chenyunlong.qing.application.manager.jimmer.repository;

import cn.chenyunlong.qing.application.manager.jimmer.Fetchers;
import cn.chenyunlong.qing.application.manager.jimmer.dto.book.ComplexBookView;
import cn.chenyunlong.qing.application.manager.jimmer.entity.Author;
import cn.chenyunlong.qing.application.manager.jimmer.entity.Book;
import cn.chenyunlong.qing.application.manager.jimmer.entity.BookFetcher;
import cn.chenyunlong.qing.application.manager.jimmer.entity.BookTable;
import cn.chenyunlong.qing.application.manager.jimmer.filters.TenantFilter;
import cn.chenyunlong.qing.application.manager.jimmer.filters.TenantProvider;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.babyfish.jimmer.UnloadedException;
import org.babyfish.jimmer.jackson.ImmutableModule;
import org.babyfish.jimmer.spring.repository.support.SpringPageFactory;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.ast.LikeMode;
import org.babyfish.jimmer.sql.ast.Predicate;
import org.babyfish.jimmer.sql.ast.query.ConfigurableRootQuery;
import org.babyfish.jimmer.sql.dialect.H2Dialect;
import org.babyfish.jimmer.sql.fetcher.Fetcher;
import org.babyfish.jimmer.sql.runtime.ConnectionManager;
import org.babyfish.jimmer.sql.runtime.Executor;
import org.babyfish.jimmer.sql.runtime.SqlFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;

import javax.sql.DataSource;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


class BookRepositoryTest {

    private static final String JDBC_URL = "jdbc:h2:mem:test_db;DB_CLOSE_DELAY=0;DB_CLOSE_ON_EXIT=FALSE";
    private static final Logger log = LoggerFactory.getLogger(BookRepositoryTest.class);
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new ImmutableModule());

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
            // 导入测试数据
            Statement statement = connection.createStatement();
            statement.execute(sql);
            connection.commit();
        }
    }

    @Test
    @DisplayName("测试基本查询语法-查询图书信息-测试分页查询")
    void findBookById() throws SQLException, JsonProcessingException {
        try (Connection connection = dataSource.getConnection()) {
            // 导入测试数据
            ConnectionManager connectionManager = ConnectionManager.singleConnectionManager(connection);
            JSqlClient sqlClient = JSqlClient
                .newBuilder()
                .addCustomizers(builder -> {
                    log.info("打印sql的执行器 = {}", builder);
                    builder.setExecutor(Executor.log());
                })
                .setDialect(new H2Dialect())
                // 添加过滤器
                .addFilters(new TenantFilter(new TenantProvider()))
                .setInListPaddingEnabled(true)
                .setConnectionManager(connectionManager)
                .setSqlFormatter(SqlFormatter.pretty("    ", 1, 80))
                .build()
                .filters(filterConfig -> {
                    // 禁用过滤器
                    filterConfig.disableByTypes(TenantFilter.class);
                });

            String name = "a";
            BookTable table = BookTable.$;
            BookFetcher bookFetcher = Fetchers.BOOK_FETCHER;
            Fetcher<Book> fetcher = bookFetcher
                .allScalarFields()
                .edition()
                .price(false)
                .authors(Fetchers.AUTHOR_FETCHER.firstName());
            ConfigurableRootQuery<BookTable, Book> query = sqlClient
                .createQuery(table)
                // 下面这种情况，查询不出来数据
                // .where(
                //     Expression.tuple(table.name(), table.edition())
                //         .eq(new Tuple2<>("Programming TypeScript", 3))
                // )
                .where(
                    table.name().like("Programming"),
                    table.name().isNotNull(),
                    table.name().ilikeIf("programming", LikeMode.START),
                    table.name().in(CollUtil.toList("Programming TypeScript", "GraphQL in Action", "GraphQL in Action")),
                    Predicate.or(
                        table.name().ilikeIf(name),
                        table.price().betweenIf(new BigDecimal(1), null)
                    )
                )
                .select(table.fetch(fetcher));
            long unlimitedCount = query.fetchUnlimitedCount();
            log.info("unlimitedCount = {}", unlimitedCount);

            Page<Book> bookPage = query
                .limit(1, 2)
                .fetchPage(1, 2, connection, SpringPageFactory.getInstance());
            System.out.println("bookList = " + bookPage);

            System.out.println("bookList = " + objectMapper.writer().writeValueAsString(bookPage));
            Assertions.assertEquals(bookPage.getTotalElements(), unlimitedCount);


            Book book = bookPage.getContent().get(0);
            // book.price(false);
            Assertions.assertThrows(UnloadedException.class, book::price);

            List<Author> authors = book.authors();

            System.out.println("authors = " + authors);
            Assertions.assertTrue(CollUtil.isNotEmpty(authors));
            Author author = authors.get(0);

            System.out.println("author.id() = " + author.id());
            Assertions.assertEquals("Boris", author.firstName());
            Assertions.assertThrows(UnloadedException.class, author::lastName);
        }
    }

    @Test
    @DisplayName("根据DTO查询复杂视图")
    void fetchByDtoLanguageForComplexView() throws SQLException, IOException {
        try (Connection connection = dataSource.getConnection()) {

            ConnectionManager connectionManager = ConnectionManager.singleConnectionManager(connection);
            JSqlClient sqlClient = JSqlClient
                .newBuilder()
                .setConnectionManager(connectionManager)
                .setSqlFormatter(SqlFormatter.pretty("    ", 1, 80))
                .build();

            String name = "a";
            BookTable table = BookTable.$;
            org.babyfish.jimmer.Page<ComplexBookView> viewPage = sqlClient
                .createQuery(table)
                .where(table.name().ilike(name))
                .select(table.fetch(ComplexBookView.class))
                .limit(1, 2).fetchPage(1, 2, connection);
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
