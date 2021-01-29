package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.mapper.AnimeTypeMapper;
import chenyunlong.zhangli.mapper.DistrictMapper;
import chenyunlong.zhangli.mapper.VersionMapper;
import chenyunlong.zhangli.model.vo.AnimeOptionsModel;
import chenyunlong.zhangli.service.AnimeOptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.Cacheable;

import java.util.LinkedList;
import java.util.List;

/**
 * 选项服务
 *
 * @author Stan
 */
@Service
public class AnimeOptionsServiceImpl implements AnimeOptionsService {
    @Autowired
    private AnimeTypeMapper animeTypeMapper;
    @Autowired
    private VersionMapper versionMapper;
    @Autowired
    private DistrictMapper districtMapper;

    @Cacheable("options")
    @Override
    public AnimeOptionsModel getOptions() {
        AnimeOptionsModel optionsModel = new AnimeOptionsModel();
        optionsModel.setTypeList(animeTypeMapper.listTypes());
        optionsModel.setDistrictList(districtMapper.getDistrictInfo());
        optionsModel.setVersionList(versionMapper.getAllVersions());
        char a = 'A';
        List<String> indexList = new LinkedList<>();
        for (int i = 0; i < 26; i++) {
            indexList.add(String.valueOf(a));
            a += 1;
        }
        optionsModel.setIndexList(indexList);
        return optionsModel;
    }
}
