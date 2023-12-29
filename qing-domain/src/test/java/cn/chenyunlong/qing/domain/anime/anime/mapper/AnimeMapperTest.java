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

package cn.chenyunlong.qing.domain.anime.anime.mapper;

import cn.chenyunlong.qing.domain.anime.anime.Anime;
import cn.chenyunlong.qing.domain.anime.anime.dto.creator.AnimeCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AnimeMapperTest {

    @Test
    void dtoToEntity() {
        AnimeCreator typeCreator = new AnimeCreator();

        typeCreator.setInstruction("test-Description");
        typeCreator.setName("name");
        typeCreator.setOrderNo(1);
        Anime anime = AnimeMapper.INSTANCE.dtoToEntity(typeCreator);

        Assertions.assertEquals("test-Description", anime.getInstruction());
        Assertions.assertEquals("name", anime.getName());
        Assertions.assertEquals(1, anime.getOrderNo());
    }
}
