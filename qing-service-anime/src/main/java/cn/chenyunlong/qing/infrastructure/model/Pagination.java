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
 *
 */

package cn.chenyunlong.qing.infrastructure.model;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.List;

/**
 * @author ryanwang
 * @date 2020-03-06
 */
@Data
public class Pagination {

    private Long totalCount;

    private Long pages;

    private Long size;

    private Long current;

    private List<RainbowPage> rainbowPages;

    private String nextPageFullPath;

    private String prevPageFullPath;

    private Boolean hasPrev;

    private Boolean hasNext;

    public Pagination(IPage animeInfoPage) {
        this.pages = animeInfoPage.getPages();
        this.totalCount = animeInfoPage.getTotal();
        this.size = animeInfoPage.getSize();
        this.hasNext = animeInfoPage.getCurrent() < animeInfoPage.getPages();
        this.hasPrev = animeInfoPage.getCurrent() > 1;
        this.current = animeInfoPage.getCurrent();
    }
}
