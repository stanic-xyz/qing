package cn.chenyunlong.common.enums;

import com.google.common.collect.Maps;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.stereotype.Component;

/**
 * Created by taoli on 2022/12/10.
 * gitee : <a href="https://gitee.com/litao851025/lego">...</a>
 * 编程就像玩 Lego
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class CommonEnumRegistry {
    private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
    private static final String BASE__ENUM_CLASS_NAME = CommonEnum.class.getName();

    @Getter
    private final Map<String, List<CommonEnum>> nameDict = Maps.newLinkedHashMap();

    @Getter
    private final Map<Class<?>, List<CommonEnum>> classDict = Maps.newLinkedHashMap();

    @Value("${baseEnum.basePackage:''}")
    private String basePackage;

    private final ResourceLoader resourceLoader;

    /**
     * 初始化枚举字典
     */
    @PostConstruct
    public void initDict() {
        if (StringUtils.isEmpty(basePackage)) {
            return;
        }
        ResourcePatternResolver resourcePatternResolver =
            ResourcePatternUtils.getResourcePatternResolver(this.resourceLoader);
        MetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory();
        try {
            String pkg = toPackage(this.basePackage);
            // 对 basePackage 包进行扫描
            String packageSearchPath =
                ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + pkg + DEFAULT_RESOURCE_PATTERN;
            Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    try {
                        MetadataReader metadataReader =
                            metadataReaderFactory.getMetadataReader(resource);
                        ClassMetadata classMetadata = metadataReader.getClassMetadata();

                        String[] interfaceNames = classMetadata.getInterfaceNames();
                        // 实现 BASE_ENUM_CLASS_NAME 接口
                        if (Arrays.asList(interfaceNames).contains(BASE__ENUM_CLASS_NAME)) {
                            String className = classMetadata.getClassName();

                            // 加载 cls
                            Class<?> cls = Class.forName(className);
                            if (cls.isEnum() && CommonEnum.class.isAssignableFrom(cls)) {
                                Object[] enumConstants = cls.getEnumConstants();
                                List<CommonEnum> commonEnums = Arrays.stream(enumConstants)
                                    .filter(e -> e instanceof CommonEnum)
                                    .map(e -> (CommonEnum) e)
                                    .collect(Collectors.toList());

                                String key = convertKeyFromClassName(cls.getSimpleName());

                                this.nameDict.put(key, commonEnums);
                                this.classDict.put(cls, commonEnums);
                            }
                        }
                    } catch (Throwable ex) {
                        // ignore
                    }
                }
            }
        } catch (IOException e) {
            log.error("failed to load dict by auto register", e);
        }
    }

    private String toPackage(String basePackage) {
        String result = basePackage.replace(".", "/");
        return result + "/";
    }

    private String convertKeyFromClassName(String className) {
        return Character.toLowerCase(className.charAt(0)) + className.substring(1);
    }
}
