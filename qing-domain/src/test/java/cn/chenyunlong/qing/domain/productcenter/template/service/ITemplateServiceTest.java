package cn.chenyunlong.qing.domain.productcenter.template.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import cn.chenyunlong.qing.domain.AbstractDomainTests;
import cn.chenyunlong.qing.domain.productcenter.template.TemplateType;
import cn.chenyunlong.qing.domain.productcenter.template.dto.creator.TemplateCategoryCreator;
import cn.chenyunlong.qing.domain.productcenter.template.dto.creator.TemplateCreator;
import cn.hutool.core.util.IdUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ITemplateServiceTest extends AbstractDomainTests {

    @Autowired
    private ITemplateService templateService;

    @Autowired
    private ITemplateCategoryService templateCategoryService;

    @Test
    void createTemplate() {

        TemplateCategoryCreator categoryCreator = new TemplateCategoryCreator();
        categoryCreator.setName("手机");
        categoryCreator.setPid(null);
        categoryCreator.setSortNum(1);
        Long templateCategory = templateCategoryService.createTemplateCategory(categoryCreator);

        TemplateCreator creator = new TemplateCreator();
        creator.setTemplateType(TemplateType.PRODUCT);
        creator.setName("华为Mate 60 Pro 超白金");
        creator.setDescription("手机");
        creator.setCategoryId(templateCategory);
        creator.setMetaData("描述信息");
        creator.setCode(IdUtil.getSnowflakeNextIdStr());
        Long template = templateService.createTemplate(creator);
        assertNotNull(template);
    }

    @Test
    void updateTemplate() {
    }
}
