package cn.chenyunlong.qing.service.llm.service.parser;

import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 测试基类，提供通用的测试资源查找功能。
 * 所有测试资源放在 src/test/resources/mock/ 目录下，
 * 用户匿名化账单文件后可放入对应目录运行测试。
 *
 * 目录结构:
 * mock/
 * ├── alipay/
 * │   └── alipay_test.csv
 * ├── wechat/
 * │   └── wechat_test.xlsx
 * ├── jd/
 * │   └── jd_test.csv
 * ├── qianji/
 * │   └── qianji_test.csv
 * ├── ccb/
 * │   ├── excel/ccb_test.xls
 * │   └── csv/ccb_test.csv
 * ├── pab/
 * │   └── pab_test.xlsx
 * ├── cmb/
 * │   ├── pdf/cmb_test.pdf
 * │   ├── xlsx/cmb_test.xlsx
 * │   └── txt/cmb_test.txt
 * ├── citic/
 * │   └── citic_test.xls
 * ├── boc/
 * │   └── pdf/boc_test.pdf
 * ├── bocom/
 * │   └── pdf/bocom_test.pdf
 * └── icbc/
 *     └── pdf/icbc_test.pdf
 */
public abstract class BaseParserTest {

    private static final String MOCK_BASE = "mock";

    /**
     * 从 classpath 获取测试文件输入流（推荐方式）
     * @param relativePath mock/ 目录下的相对路径，如 "alipay/alipay_test.csv"
     */
    protected InputStream getResourceAsStream(String relativePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(MOCK_BASE + "/" + relativePath);
        if (!resource.exists() || !resource.isReadable()) {
            return null;
        }
        return resource.getInputStream();
    }

    /**
     * 检查测试文件是否存在
     */
    protected boolean resourceExists(String relativePath) {
        ClassPathResource resource = new ClassPathResource(MOCK_BASE + "/" + relativePath);
        return resource.exists() && resource.isReadable();
    }

    /**
     * 获取测试文件路径（classpath 方式）
     * @param relativePath mock/ 目录下的相对路径
     * @return 找到的文件输入流，未找到返回 null
     */
    protected InputStream getFirstFileInDir(String subDir, String suffix) throws Exception {
        // 优先从 classpath 查找
        Path classpathDir = Paths.get("src/test/resources", MOCK_BASE, subDir);
        if (!Files.exists(classpathDir) || !Files.isDirectory(classpathDir)) {
            return null;
        }

        try (Stream<Path> stream = Files.walk(classpathDir)) {
            Optional<Path> firstFile = stream
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().toLowerCase().endsWith(suffix.toLowerCase()))
                    .findFirst();

            if (firstFile.isPresent()) {
                return Files.newInputStream(firstFile.get());
            }
        }
        return null;
    }
}
