package cn.chenyunlong.qing.service.llm.service.parser;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class BaseParserTest {

    protected InputStream getFirstFileInDir(String relativeDirPath, String suffix) throws Exception {
        Path dir = Paths.get("src/test/resources/bills/理财信息", relativeDirPath);
        if (!Files.exists(dir) || !Files.isDirectory(dir)) {
            System.out.println("目录不存在，跳过测试: " + dir.toAbsolutePath());
            return null;
        }

        try (Stream<Path> stream = Files.walk(dir)) {
            Optional<Path> firstFile = stream
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().toLowerCase().endsWith(suffix.toLowerCase()))
                    .findFirst();

            if (firstFile.isPresent()) {
                System.out.println("找到测试文件: " + firstFile.get().toAbsolutePath());
                return Files.newInputStream(firstFile.get());
            } else {
                System.out.println("目录下没有找到 " + suffix + " 文件，跳过测试");
                return null;
            }
        }
    }
}
