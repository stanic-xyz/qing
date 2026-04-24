package cn.chenyunlong.qing.service.llm.service.llm;

import cn.chenyunlong.qing.service.llm.entity.Category;
import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.entity.Counterparty;
import cn.chenyunlong.qing.service.llm.enums.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * LLM 上下文加载器测试
 */
@ExtendWith(MockitoExtension.class)
class LlmParseContextLoaderTest {

    @Mock
    private cn.chenyunlong.qing.service.llm.repository.CategoryRepository categoryRepository;
    @Mock
    private cn.chenyunlong.qing.service.llm.repository.AccountRepository accountRepository;
    @Mock
    private cn.chenyunlong.qing.service.llm.repository.CounterpartyRepository counterpartyRepository;

    private LlmParseContextLoader loader;

    @BeforeEach
    void setUp() {
        // 使用仅核心数据的构造函数（不含 matcherRepository）
        loader = new LlmParseContextLoader(categoryRepository, accountRepository, counterpartyRepository);
    }

    @Test
    void testLoadCoreReturnsValidContext() {
        Category cat1 = new Category();
        cat1.setId(1L);
        cat1.setName("餐饮美食");

        Category cat2 = new Category();
        cat2.setId(2L);
        cat2.setName("购物");

        Account acc1 = new Account();
        acc1.setId(1L);
        acc1.setAccountName("招商银行储蓄卡");
        acc1.setAccountType(AccountType.DEBIT);

        Counterparty cp1 = new Counterparty();
        cp1.setId(1L);
        cp1.setName("美团");

        when(categoryRepository.findByIsDeletedFalse())
                .thenReturn(Arrays.asList(cat1, cat2));
        when(accountRepository.findAll())
                .thenReturn(Collections.singletonList(acc1));
        when(counterpartyRepository.findAll())
                .thenReturn(Collections.singletonList(cp1));

        LlmParseContextLoader.SystemContext context = loader.loadCore();

        assertNotNull(context);
        assertEquals(2, context.getCategories().size());
        assertEquals(1, context.getAccounts().size());
        assertEquals(1, context.getCounterparties().size());
        assertEquals(0, context.getExistingMatchers().size());
    }

    @Test
    void testBuildCategoryContext() {
        String ctx = LlmParseContextLoader.buildCategoryContext(null);
        assertEquals("无分类信息", ctx);

        ctx = LlmParseContextLoader.buildCategoryContext(Collections.emptyList());
        assertEquals("无分类信息", ctx);
    }

    @Test
    void testBuildAccountContext() {
        String ctx = LlmParseContextLoader.buildAccountContext(null);
        assertEquals("无账户信息", ctx);

        ctx = LlmParseContextLoader.buildAccountContext(Collections.emptyList());
        assertEquals("无账户信息", ctx);
    }

    @Test
    void testBuildCounterpartyContext() {
        String ctx = LlmParseContextLoader.buildCounterpartyContext(null);
        assertEquals("无对手方信息", ctx);

        ctx = LlmParseContextLoader.buildCounterpartyContext(Collections.emptyList());
        assertEquals("无对手方信息", ctx);
    }

    @Test
    void testBuildMatcherContext() {
        String ctx = LlmParseContextLoader.buildMatcherContext(null);
        assertEquals("无匹配器信息", ctx);

        ctx = LlmParseContextLoader.buildMatcherContext(Collections.emptyList());
        assertEquals("无匹配器信息", ctx);
    }
}
