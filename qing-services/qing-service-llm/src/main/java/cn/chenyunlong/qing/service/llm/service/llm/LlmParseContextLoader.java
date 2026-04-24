package cn.chenyunlong.qing.service.llm.service.llm;

import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.entity.Category;
import cn.chenyunlong.qing.service.llm.entity.Counterparty;
import cn.chenyunlong.qing.service.llm.entity.TransactionMatcher;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.CategoryRepository;
import cn.chenyunlong.qing.service.llm.repository.CounterpartyRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionMatcherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * LLM 解析上下文加载器 - 从数据库加载系统上下文并缓存
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LlmParseContextLoader {

    private final CategoryRepository categoryRepository;
    private final AccountRepository accountRepository;
    private final CounterpartyRepository counterpartyRepository;
    private final TransactionMatcherRepository matcherRepository;

    /**
     * 加载完整系统上下文（带缓存，30分钟）
     */
    @Cacheable(cacheNames = "llm-parse-context", key = "'system-context'")
    public SystemContext load() {
        log.info("Loading LLM parse system context from database");
        List<Category> categories = categoryRepository.findByIsDeletedFalse();
        List<Account> accounts = accountRepository.findAll();
        List<Counterparty> counterparties = counterpartyRepository.findAll();
        List<TransactionMatcher> matchers = matcherRepository.findByIsActiveTrueOrderByPriorityDesc();

        return new SystemContext(categories, accounts, counterparties, matchers);
    }

    /**
     * 刷新上下文缓存
     */
    public void refresh() {
        log.info("Refreshing LLM parse context cache");
        // Spring Cache 会自动处理 @CacheEvict
    }

    /**
     * 生成分类上下文字符串（Prompt 友好）
     */
    public static String buildCategoryContext(List<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            return "无分类信息";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("【系统分类列表】\n");

        // 按层级分组
        List<Category> topLevel = categories.stream()
                .filter(c -> c.getParentId() == null)
                .collect(Collectors.toList());

        for (Category top : topLevel) {
            sb.append(String.format("- %s (ID:%d)\n", top.getName(), top.getId()));

            List<Category> children = categories.stream()
                    .filter(c -> top.getId().equals(c.getParentId()))
                    .collect(Collectors.toList());

            for (Category child : children) {
                sb.append(String.format("  - %s (ID:%d)\n", child.getName(), child.getId()));
            }
        }

        return sb.toString();
    }

    /**
     * 生成账户上下文字符串（Prompt 友好）
     */
    public static String buildAccountContext(List<Account> accounts) {
        if (accounts == null || accounts.isEmpty()) {
            return "无账户信息";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("【系统账户列表】\n");

        for (Account account : accounts) {
            sb.append(String.format("- %s (ID:%d, 类型:%s)\n",
                    account.getAccountName(),
                    account.getId(),
                    account.getAccountType()));
        }

        return sb.toString();
    }

    /**
     * 生成对手方上下文字符串（Prompt 友好）
     */
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

    /**
     * 生成匹配器上下文字符串（Prompt 友好）
     */
    public static String buildMatcherContext(List<TransactionMatcher> matchers) {
        if (matchers == null || matchers.isEmpty()) {
            return "无匹配器信息";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("【系统匹配器列表】\n");

        for (TransactionMatcher matcher : matchers) {
            sb.append(String.format("- %s (ID:%d, 优先级:%d)\n",
                    matcher.getName(),
                    matcher.getId(),
                    matcher.getPriority()));
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
