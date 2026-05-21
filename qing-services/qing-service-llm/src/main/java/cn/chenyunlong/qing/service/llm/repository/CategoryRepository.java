package cn.chenyunlong.qing.service.llm.repository;

import cn.chenyunlong.qing.service.llm.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByIsDeletedFalse();

    List<Category> findByParentIdAndIsDeletedFalse(Long parentId);

    boolean existsByParentIdAndIsDeletedFalse(Long parentId);

    boolean existsByNameAndIsDeletedFalse(String name);

    Category findByNameAndIsDeletedFalse(String name);

    Optional<Category> findByTypeAndNameAndParentIdIsNullAndIsDeletedFalse(String type, String trim);

    Optional<Category> findByTypeAndNameAndParentIdAndIsDeletedFalse(String type, String categoryName, Long parentId);

    boolean existsByTypeAndNameAndParentIdIsNullAndIsDeletedFalse(String type, String name);

    boolean existsByTypeAndNameAndParentIdAndIsDeletedFalse(String type, String name, Long parentId);
}
