package cn.chenyunlong.qing.service.llm.controller;

import cn.chenyunlong.qing.service.llm.entity.Category;
import cn.chenyunlong.qing.service.llm.repository.CategoryRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TransactionRecordRepository transactionRecordRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Category parentCategory;
    private Category childCategory;

    @BeforeEach
    public void setup() {
        categoryRepository.deleteAll();

        // 插入测试数据
        Category c1 = new Category();
        c1.setName("测试主分类");
        c1.setLevel(0);
        c1.setType("EXPENSE");
        parentCategory = categoryRepository.save(c1);

        Category c2 = new Category();
        c2.setName("测试子分类");
        c2.setParentId(parentCategory.getId());
        c2.setLevel(1);
        c2.setType("EXPENSE");
        childCategory = categoryRepository.save(c2);
    }

    @Test
    public void testGetCategoryTree() throws Exception {
        mockMvc.perform(get("/api/categories/tree"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                // 验证树形结构
                .andExpect(jsonPath("$.data[0].name").value("测试主分类"))
                .andExpect(jsonPath("$.data[0].children[0].name").value("测试子分类"));
    }

    @Test
    public void testCreateCategory() throws Exception {
        Category newCat = new Category();
        newCat.setName("新分类");
        newCat.setParentId(parentCategory.getId());

        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCat)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.name").value("新分类"))
                .andExpect(jsonPath("$.data.level").value(1));
    }

    @Test
    public void testDeleteCategoryWithChildrenShouldFail() throws Exception {
        // 尝试删除父分类，应该因为存在子分类而失败
        mockMvc.perform(delete("/api/categories/" + parentCategory.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("无法删除：该分类下存在子分类，请先删除子分类"));
    }

    @Test
    public void testDeleteCategorySuccess() throws Exception {
        // 删除子分类应该成功
        mockMvc.perform(delete("/api/categories/" + childCategory.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        // 验证已软删除
        assert categoryRepository.findById(childCategory.getId()).get().getIsDeleted();
    }
}
