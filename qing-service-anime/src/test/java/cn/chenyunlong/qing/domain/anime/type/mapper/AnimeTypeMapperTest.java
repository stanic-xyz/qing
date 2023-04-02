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

package cn.chenyunlong.qing.domain.anime.type.mapper;

import cn.chenyunlong.qing.domain.anime.type.AnimeType;
import cn.chenyunlong.qing.domain.anime.type.creator.AnimeTypeCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AnimeTypeMapperTest {

    @Test
    void dtoToEntity() {
        AnimeTypeCreator typeCreator = new AnimeTypeCreator();

        typeCreator.setDescription("test-Description");
        typeCreator.setName("name");
        typeCreator.setOrderNo(1);
        AnimeType animeType = AnimeTypeMapper.INSTANCE.dtoToEntity(typeCreator);

        Assertions.assertEquals("test-Description", animeType.getDescription());
        Assertions.assertEquals("name", animeType.getName());
        Assertions.assertEquals(1, animeType.getOrderNo());
    }
}
