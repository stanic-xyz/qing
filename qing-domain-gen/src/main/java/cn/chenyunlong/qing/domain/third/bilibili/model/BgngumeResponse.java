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

package cn.chenyunlong.qing.domain.third.bilibili.model;


import lombok.Getter;

@Getter
public class BgngumeResponse {


    private Long code;

    private AnimeData data;

    private String message;

    public static class Builder {

        private Long code;
        private AnimeData data;
        private String message;

        public Builder withCode(Long code) {
            this.code = code;
            return this;
        }

        public Builder withData(AnimeData data) {
            this.data = data;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public BgngumeResponse build() {
            BgngumeResponse bgngumeResponse = new BgngumeResponse();
            bgngumeResponse.code = code;
            bgngumeResponse.data = data;
            bgngumeResponse.message = message;
            return bgngumeResponse;
        }

    }

}
