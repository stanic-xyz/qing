package cn.chenyunlong.qing.anime.interfaces.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * AnimeCreateRequest 单元测试
 *
 * @author chenyunlong
 * @since 1.0.0
 */
@DisplayName("动漫创建请求DTO测试")
class AnimeCreateRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("创建有效的动漫创建请求")
    void createValidAnimeCreateRequest() {
        // Given
        AnimeCreateRequest request = AnimeCreateRequest.builder()
                .name("测试动漫")
                .instruction("测试简介")
                .categoryId(1L)
                .tagIds(Arrays.asList(1L, 2L))
                .playStatus("PLAYING")
                .coverUrl("http://example.com/cover.jpg")
                .originalName("Test Anime")
                .otherName("别名1")
                .author("测试作者")
                .officialWebsite("http://example.com")
                .district("日本")
                .companyId(1L)
                .typeId(1L)
                .premiereDate("2024-01-01")
                .plotTypes(Arrays.asList("冒险", "动作"))
                .playHeat("100")
                .orderNo(1)
                .build();

        // When
        Set<ConstraintViolation<AnimeCreateRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
        assertThat(request.getName()).isEqualTo("测试动漫");
        assertThat(request.getInstruction()).isEqualTo("测试简介");
        assertThat(request.getCategoryId()).isEqualTo(1L);
        assertThat(request.getTagIds()).containsExactly(1L, 2L);
        assertThat(request.getPlayStatus()).isEqualTo("PLAYING");
        assertThat(request.getCoverUrl()).isEqualTo("http://example.com/cover.jpg");
        assertThat(request.getOriginalName()).isEqualTo("Test Anime");
        assertThat(request.getOtherName()).isEqualTo("别名1");
        assertThat(request.getAuthor()).isEqualTo("测试作者");
        assertThat(request.getOfficialWebsite()).isEqualTo("http://example.com");
        assertThat(request.getDistrict()).isEqualTo("日本");
        assertThat(request.getCompanyId()).isEqualTo(1L);
        assertThat(request.getTypeId()).isEqualTo(1L);
        assertThat(request.getPremiereDate()).isEqualTo("2024-01-01");
        assertThat(request.getPlotTypes()).containsExactly("冒险", "动作");
        assertThat(request.getPlayHeat()).isEqualTo("100");
        assertThat(request.getOrderNo()).isEqualTo(1);
    }

    @Test
    @DisplayName("动漫名称为空时验证失败")
    void validateName_WhenBlank_ShouldFail() {
        // Given
        AnimeCreateRequest request = AnimeCreateRequest.builder()
                .name("")
                .instruction("测试简介")
                .categoryId(1L)
                .build();

        // When
        Set<ConstraintViolation<AnimeCreateRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("不能为空");
    }

    @Test
    @DisplayName("动漫名称为null时验证失败")
    void validateName_WhenNull_ShouldFail() {
        // Given
        AnimeCreateRequest request = AnimeCreateRequest.builder()
                .name(null)
                .instruction("测试简介")
                .categoryId(1L)
                .build();

        // When
        Set<ConstraintViolation<AnimeCreateRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("不能为空");
    }

    @Test
    @DisplayName("动漫名称超长时验证失败")
    void validateName_WhenTooLong_ShouldFail() {
        // Given
        String longName = "a".repeat(256); // 超过255个字符
        AnimeCreateRequest request = AnimeCreateRequest.builder()
                .name(longName)
                .instruction("测试简介")
                .categoryId(1L)
                .build();

        // When
        Set<ConstraintViolation<AnimeCreateRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("动漫名称长度不能超过100个字符");
    }

    @Test
    @DisplayName("分类ID为null时验证失败")
    void validateCategoryId_WhenZero_ShouldFail() {
        // Given
        AnimeCreateRequest request = AnimeCreateRequest.builder()
                .name("测试动漫")
                .instruction("测试简介")
                .categoryId(null)
                .build();

        // When
        Set<ConstraintViolation<AnimeCreateRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("动漫分类ID不能为空");
    }

    @Test
    @DisplayName("简介超长时验证失败")
    void validateInstruction_WhenTooLong_ShouldFail() {
        // Given
        String longInstruction = "a".repeat(2001); // 超过2000个字符
        AnimeCreateRequest request = AnimeCreateRequest.builder()
                .name("测试动漫")
                .instruction(longInstruction)
                .categoryId(1L)
                .build();

        // When
        Set<ConstraintViolation<AnimeCreateRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("动漫简介长度不能超过2000个字符");
    }

    @Test
    @DisplayName("测试Builder模式")
    void testBuilderPattern() {
        // Given & When
        AnimeCreateRequest request = AnimeCreateRequest.builder()
                .name("测试动漫")
                .instruction("测试简介")
                .categoryId(1L)
                .build();

        // Then
        assertThat(request).isNotNull();
        assertThat(request.getName()).isEqualTo("测试动漫");
        assertThat(request.getInstruction()).isEqualTo("测试简介");
        assertThat(request.getCategoryId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("测试equals和hashCode")
    void testEqualsAndHashCode() {
        // Given
        AnimeCreateRequest request1 = AnimeCreateRequest.builder()
                .name("测试动漫")
                .instruction("测试简介")
                .categoryId(1L)
                .build();

        AnimeCreateRequest request2 = AnimeCreateRequest.builder()
                .name("测试动漫")
                .instruction("测试简介")
                .categoryId(1L)
                .build();

        AnimeCreateRequest request3 = AnimeCreateRequest.builder()
                .name("不同动漫")
                .instruction("测试简介")
                .categoryId(1L)
                .build();

        // Then
        assertThat(request1).isEqualTo(request2);
        assertThat(request1).isNotEqualTo(request3);
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
        assertThat(request1.hashCode()).isNotEqualTo(request3.hashCode());
    }

    @Test
    @DisplayName("测试toString")
    void testToString() {
        // Given
        AnimeCreateRequest request = AnimeCreateRequest.builder()
                .name("测试动漫")
                .instruction("测试简介")
                .categoryId(1L)
                .build();

        // When
        String toString = request.toString();

        // Then
        assertThat(toString).contains("测试动漫");
        assertThat(toString).contains("测试简介");
        assertThat(toString).contains("1");
    }
}
