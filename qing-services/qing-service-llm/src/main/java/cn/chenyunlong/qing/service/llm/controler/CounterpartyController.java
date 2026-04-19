package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.qing.service.llm.dto.Result;
import cn.chenyunlong.qing.service.llm.dto.counterpayty.CounterpartyUpdateDto;
import cn.chenyunlong.qing.service.llm.entity.Category;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import cn.chenyunlong.qing.service.llm.entity.CounterpartyAccount;
import cn.chenyunlong.qing.service.llm.entity.Counterparty;
import java.util.ArrayList;
import cn.chenyunlong.qing.service.llm.repository.CategoryRepository;
import cn.chenyunlong.qing.service.llm.repository.CounterpartyRepository;
import cn.chenyunlong.qing.service.llm.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/finance/counterparties")
public class CounterpartyController {

    @Autowired
    private CounterpartyRepository counterpartyRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public Result<List<Counterparty>> getAll() {
        return Result.success(counterpartyRepository.findAll());
    }

    @GetMapping("/active")
    public Result<List<Counterparty>> getActive() {
        return Result.success(counterpartyRepository.findByIsActiveTrue());
    }

    @GetMapping("/search")
    public Result<List<Counterparty>> search(@RequestParam("keyword") String keyword) {
        if (keyword == null || keyword.trim().length() < 2) {
            return Result.success(new ArrayList<>());
        }
        String likePattern = "%" + keyword.trim() + "%";
        
        Specification<Counterparty> spec = (root, query, cb) -> {
            query.distinct(true);
            Join<Counterparty, CounterpartyAccount> accountsJoin = root.join("accounts", JoinType.LEFT);
            
            Predicate nameMatch = cb.like(root.get("name"), likePattern);
            Predicate accountNoMatch = cb.like(accountsJoin.get("accountNo"), likePattern);
            Predicate bankNameMatch = cb.like(accountsJoin.get("bankName"), likePattern);
            
            return cb.and(
                cb.isTrue(root.get("isActive")),
                cb.or(nameMatch, accountNoMatch, bankNameMatch)
            );
        };

        Pageable pageable = PageRequest.of(0, 50);
        List<Counterparty> results = counterpartyRepository.findAll(spec, pageable).getContent();
        return Result.success(results);
    }

    @PostMapping
    public Result<Counterparty> create(@RequestBody Counterparty counterparty) {
        return Result.success(counterpartyRepository.save(counterparty));
    }

    @PutMapping("/{id}")
    public Result<Counterparty> update(@PathVariable Long id, @RequestBody CounterpartyUpdateDto counterparty) {
        Optional<Counterparty> counterpartyOptional = counterpartyRepository.findById(id);
        String defaultCategory = counterparty.getDefaultCategory();
        Category nameAndIsDeletedFalse;

        // 当前分类不存在或者为空的时候分类设置为空
        if (defaultCategory == null || defaultCategory.isEmpty()) {
            nameAndIsDeletedFalse = null;
        } else {
            nameAndIsDeletedFalse = categoryRepository.findByNameAndIsDeletedFalse(defaultCategory);
        }

        return counterpartyOptional.map(existing -> {
            existing.setName(counterparty.getName());
            existing.setType(counterparty.getType());
            existing.setDefaultCategory(nameAndIsDeletedFalse);
            existing.setRemark(counterparty.getRemark());
            existing.setIsActive(counterparty.getIsActive());
            return Result.success(counterpartyRepository.save(existing));
        }).orElse(Result.error(404, "交易对手不存在"));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        counterpartyRepository.deleteById(id);
        return Result.success(null);
    }
}
