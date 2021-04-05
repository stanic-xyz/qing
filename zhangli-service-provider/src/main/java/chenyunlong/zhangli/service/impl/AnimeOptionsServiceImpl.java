package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.mapper.AnimeInfoMapper;
import chenyunlong.zhangli.mapper.AnimeTypeMapper;
import chenyunlong.zhangli.mapper.DistrictMapper;
import chenyunlong.zhangli.mapper.VersionMapper;
import chenyunlong.zhangli.model.vo.AnimeOptionsModel;
import chenyunlong.zhangli.model.vo.YearInfo;
import chenyunlong.zhangli.service.AnimeOptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.Cacheable;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

/**
 * 选项服务
 *
 * @author Stan
 */
@Service
public class AnimeOptionsServiceImpl implements AnimeOptionsService {

    private final AnimeInfoMapper animeInfoMapper;
    private final AnimeTypeMapper animeTypeMapper;
    private final VersionMapper versionMapper;
    private final DistrictMapper districtMapper;

    public AnimeOptionsServiceImpl(AnimeInfoMapper animeInfoMapper, AnimeTypeMapper animeTypeMapper, VersionMapper versionMapper, DistrictMapper districtMapper) {
        this.animeInfoMapper = animeInfoMapper;
        this.animeTypeMapper = animeTypeMapper;
        this.versionMapper = versionMapper;
        this.districtMapper = districtMapper;
    }

    @Cacheable("options")
    @Override
    public AnimeOptionsModel getOptions() {
        LocalDate earliestAnime = animeInfoMapper.selectEarliestyear();
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
        //添加查询年份信息
        List<YearInfo> years = new LinkedList<>();

        LocalDate now = LocalDate.now();
        for (int i = 0; i < 10; i++) {
            YearInfo yearInfo = new YearInfo();
            yearInfo.setQueryValue("[" + now.getYear() + "," + now.plusYears(-1).getYear() + ")");
            yearInfo.setName(String.valueOf(now.getYear()));
            now = now.plusYears(-1);
            years.add(yearInfo);
        }
        YearInfo yearInfo = new YearInfo();
        yearInfo.setQueryValue("[" + now.getYear() + "," + (earliestAnime != null ? earliestAnime.getYear() : "1980") + "]");
        yearInfo.setName(now.getYear() + "及以前");
        years.add(yearInfo);
        optionsModel.setYears(years);
        return optionsModel;
    }

    @Override
    public int getRecentPageSize() {
        return 25;
    }
}
