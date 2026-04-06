package cn.chenyunlong.qing.service.llm.repository;

import cn.chenyunlong.qing.service.llm.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByIsDeletedFalse();

    List<Category> findByParentIdAndIsDeletedFalse(Long parentId);

    boolean existsByParentIdAndIsDeletedFalse(Long parentId);

    boolean existsByNameAndIsDeletedFalse(String name);

    Category findByNameAndIsDeletedFalse(String name);
}
