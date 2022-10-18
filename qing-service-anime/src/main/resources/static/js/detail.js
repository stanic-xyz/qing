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

function onbtnrecommend(argument) {
    var comments_block = document.getElementById('comments_block');
    var recommend_block = document.getElementById('recommend_block');
    var btncomment = document.getElementById('btncomment');
    var btnrecommend = document.getElementById('btnrecommend');

    comments_block.hidden = 'hidden';
    recommend_block.hidden = null;
    btncomment.classList.remove('switchbtn_current');
    btnrecommend.classList.add('switchbtn_current');
}

function onbtncomment(argument) {
    var comments_block = document.getElementById('comments_block');
    var recommend_block = document.getElementById('recommend_block');
    var btncomment = document.getElementById('btncomment');
    var btnrecommend = document.getElementById('btnrecommend');

    comments_block.hidden = null;
    recommend_block.hidden = 'hidden';
    btnrecommend.classList.remove('switchbtn_current');
    btncomment.classList.add('switchbtn_current');
}

