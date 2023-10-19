package cn.chenyunlong.qing.web.graphql;

import java.util.Arrays;
import java.util.List;

public record Author(String id, String firstName, String lastName) {

    private static final List<Author> authors = Arrays.asList(
        new Author("author-1", "Joshua", "Bloch"),
        new Author("author-2", "Douglas", "Adams"),
        new Author("author-3", "Bill", "Bryson")
    );

    /**
     * 根据ID查询工作计划。
     */
    public static Author getById(String id) {
        return authors.stream()
            .filter(author -> author.id().equals(id))
            .findFirst()
            .orElse(null);
    }
}
