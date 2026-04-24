package cn.chenyunlong.qing.service.llm.service.llm;

import cn.chenyunlong.qing.service.llm.entity.Category;
import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.entity.Counterparty;
import cn.chenyunlong.qing.service.llm.entity.TransactionMatcher;
import cn.chenyunlong.qing.service.llm.repository.CategoryRepository;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.CounterpartyRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionMatcherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * LLM 解析上下文加载器测试
 */
@ExtendWith(MockitoExtension.class)
class LlmParseContextLoaderTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CounterpartyRepository counterpartyRepository;
    @Mock
    private TransactionMatcherRepository matcherRepository;

    private LlmParseContextLoader contextLoader;

    @BeforeEach
    void setUp() {
        contextLoader = new LlmParseContextLoader(
                categoryRepository, accountRepository,
                counterpartyRepository, matcherRepository
        );
    }

    @Test
    void testLoadReturnsContext() {
        when(categoryRepository.findByIsDeletedFalse()).thenReturn(Collections.emptyList());
        when(accountRepository.findAll()).thenReturn(Collections.emptyList());
        when(counterpartyRepository.findAll()).thenReturn(Collections.emptyList());
        when(matcherRepository.findByIsActiveTrueOrderByPriorityDesc()).thenReturn(Collections.emptyList());

        var context = contextLoader.load();

        assertNotNull(context);
        assertNotNull(context.getCategories());
        assertNotNull(context.getAccounts());
        assertNotNull(context.getCounterparties());
        assertNotNull(context.getExistingMatchers());
    }

    @Test
    void testLoadWithData() {
        Category cat1 = new Category();
        cat1.setId(1L);
        cat1.setName("餐饮美食");
        cat1.setParentId(null);

        Category cat2 = new Category();
        cat2.setId(2L);
        cat2.setName("快餐");
        cat2.setParentId(1L);

        Account account = new Account();
        account.setId(1L);
        account.setAccountName("我的银行卡");

        Counterparty counterparty = new Counterparty();
        counterparty.setId(1L);
        counterparty.setName("麦当劳");

        TransactionMatcher matcher = new TransactionMatcher();
        matcher.setId(1L);
        matcher.setName("京东匹配器");

        when(categoryRepository.findByIsDeletedFalse()).thenReturn(Arrays.asList(cat1, cat2));
        when(accountRepository.findAll()).thenReturn(Collections.singletonList(account));
        when(counterpartyRepository.findAll()).thenReturn(Collections.singletonList(counterparty));
        when(matcherRepository.findByIsActiveTrueOrderByPriorityDesc()).thenReturn(Collections.singletonList(matcher));

        var context = contextLoader.load();

        assertEquals(2, context.getCategories().size());
        assertEquals(1, context.getAccounts().size());
        assertEquals(1, context.getCounterparties().size());
        assertEquals(1, context.getExistingMatchers().size());
    }

    @Test
    void testToCategoryContext() {
        Category cat1 = new Category();
        cat1.setId(1L);
        cat1.setName("餐饮美食");
        cat1.setParentId(null);

        Category cat2 = new Category();
        cat2.setId(2L);
        cat2.setName("快餐");
        cat2.setParentId(1L);

        when(categoryRepository.findByIsDeletedFalse()).thenReturn(Arrays.asList(cat1, cat2));

        var context = contextLoader.load();
        String categoryContext = context.toCategoryContext();

        assertNotNull(categoryContext);
        assertTrue(categoryContext.contains("餐饮美食"));
        assertTrue(categoryContext.contains("快餐"));
        assertTrue(categoryContext.contains("ID:1"));
        assertTrue(categoryContext.contains("ID:2"));
    }

    @Test
    void testToCategoryContextEmpty() {
        when(categoryRepository.findByIsDeletedFalse()).thenReturn(Collections.emptyList());

        var context = contextLoader.load();
        String categoryContext = context.toCategoryContext();

        assertNotNull(categoryContext);
        assertEquals("无分类信息", categoryContext);
    }

    @Test
    void testToAccountContext() {
        Account account = new Account();
        account.setId(1L);
        account.setAccountName("我的银行卡");
        account.setAccountType(cn.chenyunlong.qing.service.llm.enums.AccountType.DEBIT);

        when(accountRepository.findAll()).thenReturn(Collections.singletonList(account));

        var context = contextLoader.load();
        String accountContext = context.toAccountContext();

        assertNotNull(accountContext);
        assertTrue(accountContext.contains("我的银行卡"));
        assertTrue(accountContext.contains("DEBIT"));
    }

    @Test
    void testToAccountContextEmpty() {
        when(accountRepository.findAll()).thenReturn(Collections.emptyList());

        var context = contextLoader.load();
        String accountContext = context.toAccountContext();

        assertNotNull(accountContext);
        assertEquals("无账户信息", accountContext);
    }

    @Test
    void testToCounterpartyContext() {
        Counterparty counterparty = new Counterparty();
        counterparty.setId(1L);
        counterparty.setName("麦当劳");

        when(counterpartyRepository.findAll()).thenReturn(Collections.singletonList(counterparty));

        var context = contextLoader.load();
        String cpContext = context.toCounterpartyContext();

        assertNotNull(cpContext);
        assertTrue(cpContext.contains("麦当劳"));
    }

    @Test
    void testToCounterpartyContextEmpty() {
        when(counterpartyRepository.findAll()).thenReturn(Collections.emptyList());

        var context = contextLoader.load();
        String cpContext = context.toCounterpartyContext();

        assertNotNull(cpContext);
        assertEquals("无对手方信息", cpContext);
    }

    @Test
    void testToMatcherContext() {
        TransactionMatcher matcher = new TransactionMatcher();
        matcher.setId(1L);
        matcher.setName("京东匹配器");
        matcher.setPriority(100);

        when(matcherRepository.findByIsActiveTrueOrderByPriorityDesc()).thenReturn(Collections.singletonList(matcher));

        var context = contextLoader.load();
        String matcherContext = context.toMatcherContext();

        assertNotNull(matcherContext);
        assertTrue(matcherContext.contains("京东匹配器"));
    }

    @Test
    void testToMatcherContextEmpty() {
        when(matcherRepository.findByIsActiveTrueOrderByPriorityDesc()).thenReturn(Collections.emptyList());

        var context = contextLoader.load();
        String matcherContext = context.toMatcherContext();

        assertNotNull(matcherContext);
        assertEquals("无匹配器信息", matcherContext);
    }
}
