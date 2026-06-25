package cn.chenyunlong.qing.service.llm.dto.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransactionWriteDtoTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("创建 DTO 只暴露约定的公开字段")
    void createDtoShouldOnlyExposeAllowedFields() {
        Set<String> fields = Arrays.stream(CreateTransactionRecordDto.class.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toSet());

        assertTrue(fields.contains("accountId"));
        assertTrue(fields.contains("transactionTime"));
        assertTrue(fields.contains("amount"));
        assertTrue(fields.contains("directionType"));
        assertTrue(fields.contains("categoryId"));
        assertTrue(fields.contains("categoryName"));
        assertTrue(fields.contains("merchant"));
        assertTrue(fields.contains("counterpartyId"));
        assertTrue(fields.contains("counterpartyStr"));

        assertFalse(fields.contains("channelId"));
        assertFalse(fields.contains("originalId"));
        assertFalse(fields.contains("sourceFile"));
        assertFalse(fields.contains("linkedId"));
        assertFalse(fields.contains("linkedGroupId"));
        assertFalse(fields.contains("originalData"));
        assertFalse(fields.contains("fundType"));
    }

    @Test
    @DisplayName("更新 DTO 能区分字段未传与显式传空")
    void updateDtoShouldTrackMissingAndExplicitNullDifferently() throws Exception {
        UpdateTransactionRecordDto missingFieldDto =
                objectMapper.readValue("{}", UpdateTransactionRecordDto.class);
        UpdateTransactionRecordDto explicitNullDto =
                objectMapper.readValue("{\"merchant\":null,\"categoryId\":null,\"categoryName\":null,\"directionType\":null}",
                        UpdateTransactionRecordDto.class);

        assertFalse(missingFieldDto.isMerchantSpecified());
        assertFalse(missingFieldDto.isCategoryIdSpecified());
        assertFalse(missingFieldDto.isCategoryNameSpecified());
        assertFalse(missingFieldDto.isDirectionTypeSpecified());

        assertTrue(explicitNullDto.isMerchantSpecified());
        assertTrue(explicitNullDto.isCategoryIdSpecified());
        assertTrue(explicitNullDto.isCategoryNameSpecified());
        assertTrue(explicitNullDto.isDirectionTypeSpecified());
    }
}
