package cn.chenyunlong.qing.service.llm.service.llm;

import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.entity.Category;
import cn.chenyunlong.qing.service.llm.entity.Counterparty;
import cn.chenyunlong.qing.service.llm.entity.TransactionMatcher;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.CategoryRepository;
import cn.chenyunlong.qing.service.llm.repository.CounterpartyRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionMatcherRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * LLM 解析上下文加载器 - 从数据库加载系统上下文并缓存
 *
 * <p>支持仅加载核心数据（分类/账户/对手方），不需要所有 Repository。
 * 在测试或不需要全部数据时可以只传部分依赖。</p>
 */
@Service
@Slf4j
public class LlmParseContextLoader {

    private final CategoryRepository categoryRepository;
    private final AccountRepository accountRepository;
    private final CounterpartyRepository counterpartyRepository;
    private final TransactionMatcherRepository matcherRepository;

    /**
     * 完整构造函数（生产使用）
     */
    public LlmParseContextLoader(CategoryRepository categoryRepository,
                                  AccountRepository accountRepository,
                                  CounterpartyRepository counterpartyRepository,
                                  @Nullable TransactionMatcherRepository matcherRepository) {
        this.categoryRepository = categoryRepository;
        this.accountRepository = accountRepository;
        this.counterpartyRepository = counterpartyRepository;
        this.matcherRepository = matcherRepository;
    }

    /**
     * 最小构造函数（仅核心数据，用于测试）
     */
    public LlmParseContextLoader(CategoryRepository categoryRepository,
                                  AccountRepository accountRepository,
                                  CounterpartyRepository counterpartyRepository) {
        this(categoryRepository, accountRepository, counterpartyRepository, null);
    }

    /**
     * 加载完整系统上下文（带缓存，30分钟）
     */
    @Cacheable(cacheNames = "llm-parse-context", key = "'system-context'")
    public SystemContext load() {
        log.info("Loading LLM parse system context from database");
        List<Category> categories = categoryRepository.findByIsDeletedFalse();
        List<Account> accounts = accountRepository.findAll();
        List<Counterparty> counterparties = counterpartyRepository.findAll();

        List<TransactionMatcher> matchers = Collections.emptyList();
        if (matcherRepository != null) {
            try {
                matchers = matcherRepository.findByIsActiveTrueOrderByPriorityDesc();
            } catch (Exception e) {
                log.warn("Failed to load matchers, using empty list: {}", e.getMessage());
            }
        }

        return new SystemContext(categories, accounts, counterparties, matchers);
    }

    /**
     * 加载核心上下文（不含匹配器，用于快速测试）
     */
    public SystemContext loadCore() {
        List<Category> categories = categoryRepository.findByIsDeletedFalse();
        List<Account> accounts = accountRepository.findAll();
        List<Counterparty> counterparties = counterpartyRepository.findAll();

        return new SystemContext(categories, accounts, counterparties, Collections.emptyList());
    }

    public static String buildCategoryContext(List<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            return "无分类信息";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("【系统分类列表】\n");
        List<Category> topLevel = categories.stream()
                .filter(c -> c.getParentId() == null)
                .toList();
        for (Category top : topLevel) {
            sb.append(String.format("- %s (ID:%d)\n", top.getName(), top.getId()));
        }
        return sb.toString();
    }

    public static String buildAccountContext(List<Account> accounts) {
        if (accounts == null || accounts.isEmpty()) {
            return "无账户信息";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("【系统账户列表】\n");
        for (Account account : accounts) {
            sb.append(String.format("- %s (ID:%d, 类型:%s)\n",
                    account.getAccountName(), account.getId(), account.getAccountType()));
        }
        return sb.toString();
    }

    public static String buildCounterpartyContext(List<Counterparty> counterparties) {
        if (counterparties == null || counterparties.isEmpty()) {
            return "无对手方信息";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("【系统对手方列表】\n");
        for (Counterparty cp : counterparties) {
            sb.append(String.format("- %s (ID:%d)\n", cp.getName(), cp.getId()));
        }
        return sb.toString();
    }

    public static String buildMatcherContext(List<TransactionMatcher> matchers) {
        if (matchers == null || matchers.isEmpty()) {
            return "无匹配器信息";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("【系统匹配器列表】\n");
        for (TransactionMatcher matcher : matchers) {
            sb.append(String.format("- %s (ID:%d, 优先级:%d)\n",
                    matcher.getName(), matcher.getId(), matcher.getPriority()));
        }
        return sb.toString();
    }

    /**
     * 系统上下文数据类
     */
    @lombok.Data
    public static class SystemContext {
        private List<Category> categories;
        private List<Account> accounts;
        private List<Counterparty> counterparties;
        private List<TransactionMatcher> existingMatchers;

        public SystemContext(List<Category> categories, List<Account> accounts,
                            List<Counterparty> counterparties, List<TransactionMatcher> existingMatchers) {
            this.categories = categories;
            this.accounts = accounts;
            this.counterparties = counterparties;
            this.existingMatchers = existingMatchers;
        }

        public String toCategoryContext() {
            return buildCategoryContext(categories);
        }

        public String toAccountContext() {
            return buildAccountContext(accounts);
        }

        public String toCounterpartyContext() {
            return buildCounterpartyContext(counterparties);
        }

        public String toMatcherContext() {
            return buildMatcherContext(existingMatchers);
        }
    }
}
