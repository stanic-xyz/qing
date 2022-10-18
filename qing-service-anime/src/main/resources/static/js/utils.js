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

function lazyload() {

    //
    var imgs = $('.anime_icon1_img');
    for (var i = 0; i < imgs.length; i++) {
        imgs[i].src = $('.anime_icon1:nth-child(' + (i + 1) + ') .anime_icon1_img').data('src');
    }

    var lazyloadImages = document.querySelectorAll("img.lazy");
    lazyloadImages.forEach(function (img) {
        img.classList.remove('lazy');
    });
}

document.addEventListener("DOMContentLoaded", function () {
    setTimeout(lazyload, 0.2);
});

