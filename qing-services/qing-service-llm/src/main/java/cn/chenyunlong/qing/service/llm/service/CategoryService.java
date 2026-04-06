package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.qing.service.llm.dto.CategoryTreeDTO;
import cn.chenyunlong.qing.service.llm.dto.counterpayty.CounterpartyUpdateDto;
import cn.chenyunlong.qing.service.llm.entity.Category;
import cn.chenyunlong.qing.service.llm.repository.CategoryRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TransactionRecordRepository transactionRecordRepository;

    @PostConstruct
    public void initDefaultCategories() {
        if (categoryRepository.count() == 0) {
            String[] defaultExpenses = {"餐饮", "交通", "购物", "娱乐", "居家", "人情"};
            for (String name : defaultExpenses) {
                Category c = new Category();
                c.setName(name);
                c.setLevel(0);
                c.setType("EXPENSE");
                categoryRepository.save(c);
            }

            String[] defaultIncomes = {"工资", "理财", "红包", "其他收入"};
            for (String name : defaultIncomes) {
                Category c = new Category();
                c.setName(name);
                c.setLevel(0);
                c.setType("INCOME");
                categoryRepository.save(c);
            }
        }
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findByIsDeletedFalse();
    }

    public List<CategoryTreeDTO> getCategoryTree() {
        List<Category> all = categoryRepository.findByIsDeletedFalse();
        List<CategoryTreeDTO> dtos = all.stream().map(this::convertToDTO).collect(Collectors.toList());

        Map<Long, List<CategoryTreeDTO>> childrenMap = dtos.stream()
                .filter(d -> d.getParentId() != null)
                .collect(Collectors.groupingBy(CategoryTreeDTO::getParentId));

        List<CategoryTreeDTO> tree = new ArrayList<>();
        for (CategoryTreeDTO dto : dtos) {
            dto.setChildren(childrenMap.getOrDefault(dto.getId(), new ArrayList<>()));
            if (dto.getParentId() == null) {
                tree.add(dto);
            }
        }
        return tree;
    }

    public Category createCategory(Category category) {
        if (category.getParentId() != null) {
            Category parent = categoryRepository.findById(category.getParentId())
                    .orElseThrow(() -> new RuntimeException("父分类不存在"));
            category.setLevel(parent.getLevel() + 1);
            category.setType(parent.getType());
        } else {
            category.setLevel(0);
        }
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category updateInfo) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在"));
        category.setName(updateInfo.getName());
        category.setIcon(updateInfo.getIcon());

        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在"));

        // 1. 检查是否有子分类
        if (categoryRepository.existsByParentIdAndIsDeletedFalse(id)) {
            throw new RuntimeException("无法删除：该分类下存在子分类，请先删除子分类");
        }

        // 2. 检查是否有流水关联 (简单通过名称匹配)
        if (transactionRecordRepository.existsByCategory(category)) {
            throw new RuntimeException("无法删除：已有流水记录使用了该分类，请先解绑");
        }

        category.setIsDeleted(true);
        categoryRepository.save(category);
    }

    private CategoryTreeDTO convertToDTO(Category category) {
        CategoryTreeDTO dto = new CategoryTreeDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setParentId(category.getParentId());
        dto.setLevel(category.getLevel());
        dto.setIcon(category.getIcon());
        dto.setType(category.getType());
        return dto;
    }
}
