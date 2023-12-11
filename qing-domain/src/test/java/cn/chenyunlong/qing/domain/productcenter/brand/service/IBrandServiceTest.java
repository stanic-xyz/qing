package cn.chenyunlong.qing.domain.productcenter.brand.service;

import cn.chenyunlong.qing.domain.AbstractDomainTests;
import cn.chenyunlong.qing.domain.productcenter.brand.Brand;
import cn.chenyunlong.qing.domain.productcenter.brand.dto.creator.BrandCreator;
import cn.chenyunlong.qing.domain.productcenter.brand.dto.updater.BrandUpdater;
import cn.chenyunlong.qing.domain.productcenter.brand.repository.BrandRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

class IBrandServiceTest extends AbstractDomainTests {

    @Autowired
    private IBrandService brandService;

    @Autowired
    private BrandRepository brandRepository;

    @Test
    void createBrand() {
        BrandCreator creator = new BrandCreator();
        creator.setDescription("华为");
        creator.setLogo("https://localhost/huawei.png");
        creator.setName("华为");
        creator.setProvider("华为");
        Long brand = brandService.createBrand(creator);
        Assertions.assertNotNull(brand);
    }

    @Test
    void updateBrand() {
        BrandCreator creator = new BrandCreator();
        creator.setDescription("华为");
        creator.setLogo("https://localhost/huawei.png");
        creator.setName("华为");
        creator.setProvider("华为");
        Long brandId = brandService.createBrand(creator);
        Assertions.assertNotNull(brandId);

        Optional<Brand> optionalBrand = brandRepository.findById(brandId);
        Assertions.assertTrue(optionalBrand.isPresent());

        BrandUpdater updater = new BrandUpdater();
        BeanUtils.copyProperties(creator, updater);

        updater.setId(brandId);
        updater.setDescription("修改后的描述信息");

        brandService.updateBrand(updater);

        Brand brand1 = optionalBrand.get();
        Assertions.assertEquals(brandId, brand1.getId());

        Brand brandInfo = brandRepository.findById(brandId).orElseThrow();

        Assertions.assertEquals("修改后的描述信息", brandInfo.getDescription());


    }
}