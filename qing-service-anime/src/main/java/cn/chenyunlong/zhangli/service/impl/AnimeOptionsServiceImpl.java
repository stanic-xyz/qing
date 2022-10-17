/*
 * Copyright (c) 2019-2022 YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.chenyunlong.zhangli.service.impl;

import cn.chenyunlong.zhangli.config.properties.ZhangliProperties;
import cn.chenyunlong.zhangli.mapper.AnimeInfoMapper;
import cn.chenyunlong.zhangli.mapper.AnimeTypeMapper;
import cn.chenyunlong.zhangli.mapper.DistrictMapper;
import cn.chenyunlong.zhangli.mapper.VersionMapper;
import cn.chenyunlong.zhangli.model.vo.OptionsModel;
import cn.chenyunlong.zhangli.model.vo.YearInfo;
import cn.chenyunlong.zhangli.service.AnimeOptionsService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

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
    private final ZhangliProperties zhangliProperties;

    public AnimeOptionsServiceImpl(AnimeInfoMapper animeInfoMapper, AnimeTypeMapper animeTypeMapper, VersionMapper versionMapper, DistrictMapper districtMapper, ZhangliProperties zhangliProperties) {
        this.animeInfoMapper = animeInfoMapper;
        this.animeTypeMapper = animeTypeMapper;
        this.versionMapper = versionMapper;
        this.districtMapper = districtMapper;
        this.zhangliProperties = zhangliProperties;
    }

    @Cacheable(value = "options:option")
    @Override
    public OptionsModel getOptions() {
        LocalDate earliestAnime = animeInfoMapper.selectEarliestyear();
        OptionsModel optionsModel = new OptionsModel();
        optionsModel.setTypeList(animeTypeMapper.listTypes());
        optionsModel.setDistrictList(districtMapper.getDistrictInfo());
        optionsModel.setVersionList(versionMapper.getAllVersions());
        char a = 'A';
        List<String> indexList = new LinkedList<>();
        for (int i = 0; i < zhangliProperties.getIndexSize(); i++) {
            indexList.add(String.valueOf(a));
            a += 1;
        }
        optionsModel.setIndexList(indexList);
        //添加查询年份信息
        List<YearInfo> years = new LinkedList<>();

        LocalDate now = LocalDate.now();
        for (int i = 0; i < zhangliProperties.getYearCount(); i++) {
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

    @Cacheable(value = "options:pageInfo")
    @Override
    public int getRecentPageSize() {
        return zhangliProperties.getIndexSize();
    }


    @CacheEvict(value = "options:pageInfo")
    @Override
    public void updateOptions() {
    }

}
