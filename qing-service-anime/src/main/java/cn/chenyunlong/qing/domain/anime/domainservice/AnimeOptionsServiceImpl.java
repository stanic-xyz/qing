/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.qing.domain.anime.domainservice;

import cn.chenyunlong.qing.infrastructure.model.vo.OptionsModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 选项服务
 *
 * @author Stan
 */
@Service
@RequiredArgsConstructor
public class AnimeOptionsServiceImpl implements AnimeOptionsService {
    /**
     * 获取选项信息
     *
     * @return 获取所有筛选条件
     */
    @Override
    public OptionsModel getOptions() {
        return null;
    }

    /**
     * 获取最近列表的默认分页大小
     *
     * @return 最新记录的分页大小
     */
    @Override
    public int getRecentPageSize() {
        return 0;
    }

    /**
     * 更新配置信息
     */
    @Override
    public void updateOptions() {

    }

//    private final IVersionService versionService;
//    private final QingProperties qingProperties;

//
//    @Cacheable(value = "options:option")
//    @Override
//    public OptionsModel getOptions() {
//        LocalDate earliestAnime = LocalDate.now();
//        OptionsModel optionsModel = new OptionsModel();
//        optionsModel.setTypeList(Collections.emptyList());
//        //todo 这里代码错误了
//        optionsModel.setDistrictList(new LinkedList<>());
//        VersionVO serviceById = versionService.findById(1L);
////        optionsModel.setVersionList(versionService.findByPage(new PageRequestWrapper<>()));
//        char a = 'A';
//        List<String> indexList = new LinkedList<>();
//        for (int i = 0; i < qingProperties.getIndexSize(); i++) {
//            indexList.add(String.valueOf(a));
//            a += 1;
//        }
//        optionsModel.setIndexList(indexList);
//        //添加查询年份信息
//        List<YearInfo> years = new LinkedList<>();
//
//        LocalDate now = LocalDate.now();
//        for (int i = 0; i < qingProperties.getYearCount(); i++) {
//            YearInfo yearInfo = new YearInfo();
//            yearInfo.setQueryValue("[" + now.getYear() + "," + now.plusYears(-1).getYear() + ")");
//            yearInfo.setName(String.valueOf(now.getYear()));
//            now = now.plusYears(-1);
//            years.add(yearInfo);
//        }
//
//        YearInfo yearInfo = new YearInfo();
//        yearInfo.setQueryValue("[" + now.getYear() + "," + earliestAnime.getYear() + "]");
//        yearInfo.setName(now.getYear() + "及以前");
//        years.add(yearInfo);
//        optionsModel.setYears(years);
//        return optionsModel;
//    }
//
//    @Cacheable(value = "options:pageInfo")
//    @Override
//    public int getRecentPageSize() {
//        return qingProperties.getIndexSize();
//    }
//
//
//    @CacheEvict(value = "options:pageInfo")
//    @Override
//    public void updateOptions() {
//    }

}
