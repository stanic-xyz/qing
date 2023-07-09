/*
 * Copyright (c) 2023  YunLong Chen
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

package cn.chenyunlong.qing.domain.anime.mapper;

import cn.chenyunlong.qing.domain.anime.AnimeInfo;
import cn.chenyunlong.qing.domain.anime.creator.AnimeInfoCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AnimeInfoMapperTest {

    @Test
    void dtoToEntity() {
        AnimeInfoCreator typeCreator = new AnimeInfoCreator();

        typeCreator.setInstruction("test-Description");
        typeCreator.setName("name");
        typeCreator.setOrderNo(1);
        AnimeInfo animeInfo = AnimeInfoMapper.INSTANCE.dtoToEntity(typeCreator);

        Assertions.assertEquals("test-Description", animeInfo.getInstruction());
        Assertions.assertEquals("name", animeInfo.getName());
        Assertions.assertEquals(1, animeInfo.getOrderNo());
    }
}
