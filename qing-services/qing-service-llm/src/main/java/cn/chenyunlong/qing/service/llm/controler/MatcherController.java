package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.qing.service.llm.dto.Result;
import cn.chenyunlong.qing.service.llm.entity.TransactionMatcher;
import cn.chenyunlong.qing.service.llm.repository.TransactionMatcherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finance/matchers")
@RequiredArgsConstructor
public class MatcherController {

    private final TransactionMatcherRepository matcherRepository;

    @GetMapping
    public Result<List<TransactionMatcher>> getAll() {
        return Result.success(matcherRepository.findAll());
    }

    @GetMapping("/active")
    public Result<List<TransactionMatcher>> getActive() {
        return Result.success(matcherRepository.findByIsActiveTrueOrderByPriorityDesc());
    }

    @PostMapping
    public Result<TransactionMatcher> save(@RequestBody TransactionMatcher matcher) {
        return Result.success(matcherRepository.save(matcher));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable("id") Long id) {
        matcherRepository.deleteById(id);
        return Result.success(null);
    }
}
