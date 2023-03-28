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


///////////////////////////////

var __g_qlive_loadcnt = 0;

function __qlive_play_callbak(status, _in_id, _json_obj, cb_getplay_url) {
    const _purl = _json_obj['purl'];
    const _purl_mp4 = _json_obj['purl_mp4'];
    const _vurl = _json_obj['vurl'];
    const _vurl_bak = _json_obj['vurl_bak'];
    const _play_ex = _json_obj['ex'];
    const vlt_lr = __get_vlt_lr(_play_ex);

    //
    __g_qlive_loadcnt += 1;


    //for AGE --
    if (false) {
        if (_vurl.match(/^1098_/)) {
            document.getElementById(_in_id).src = (_purl + _vurl_bak);
            return;
        }
    }

    //
    if (status) {
        document.getElementById(_in_id).src = ___make_url_vlt_param((_purl_mp4 + _vurl), vlt_lr);
    } else {
        if (__g_qlive_loadcnt > 1) {
            document.getElementById(_in_id).src = ___make_url_vlt_param((_purl + _vurl_bak), vlt_lr);
        } else {
            setTimeout(function () {
                __yx_SetMainPlayIFrameSRC(_in_id, cb_getplay_url);
            }, 4000);
        }
    }
}


//////////////////////////////////


function __qlive_play(_in_playid, lhf, cb, _in_id, _json_obj, cb_getplay_url) {

    //
    if ('<play>QLIVE</play>' != _in_playid) {
        return false;
    }

    //
    var testvideo = document.createElement('video');
    testvideo.autoplay = true;
    testvideo.hidden = 'hidden';

    var testvideo_div = document.getElementById('testvideo_div');
    testvideo_div.appendChild(testvideo);

    var url = decodeURIComponent(lhf);

    testvideo.addEventListener('error', function (e) {
        testvideo.parentNode.removeChild(testvideo);
        cb(false, _in_id, _json_obj, cb_getplay_url);
    });
    testvideo.addEventListener('loadeddata', function (e) {
        testvideo.parentNode.removeChild(testvideo);
        cb(true, _in_id, _json_obj, cb_getplay_url);
    });

    //
    testvideo.setAttribute('src', url);

    //
    return true;

}

/////////////////////////////

function __ipchk_getplay(_in_data) {
    const match_resl = _in_data.match(/^ipchk:(.+)/);
    if (match_resl) {
        $('#ipchk_getplay').html(_in_data);
        $('#ipchk_getplay').removeAttr('hidden');
        return true;
    }
    //
    return false;
}

////////////////////////////

function __qpic_chkvurl_converting(_in_vurl) {
    const vurl = decodeURIComponent(_in_vurl);
    const match_resl = vurl.match(/^http.+\.f20\.mp4\?ptype=http\?w5=0&h5=0&state=1$/);
    if (match_resl) {
        setTimeout(function () {
            $('#ipchk_getplay').html('视频转码中, 请稍后再试');
            $('#ipchk_getplay').removeAttr('hidden');
        }, 5000);
        //
        return true;
    }
    //
    return false;
}


////////////////////////////

function __cb_getplay_url() {
    //
    const _url = window.location.href;
    const _rand = Math.random();
    var _getplay_url = (_url.replace(/.*\/play\/(\d{8})-(\d+?)-(\d+?)\.html.*/, '/_getplay?aid=$1&playindex=$2&epindex=$3') + '&r=' + _rand);
    //
    var re_resl = _getplay_url.match(/[&?]+epindex=(\d+)/);
    const hf_epi = ('' + FEI2(re_resl[1]));
    const t_epindex_ = 'epindex=';
    _getplay_url = _getplay_url.replace(t_epindex_ + re_resl[1], t_epindex_ + hf_epi);
    return _getplay_url;
}


/////////////////////


function __key_enc_vlt(val) {
    var n_val = Number(val);
    if (n_val > 0) {
        n_val = (n_val * 3 + 1333);
    }
    return n_val;
}

function __key_dec_vlt(val) {
    var n_val = Number(val);
    if (n_val > 0) {
        n_val = ((n_val - 1333) / 3);
    }
    return n_val;
}

function __get_vlt_lr(_in_ex) {
    const re_resl = _in_ex.match(/^#ex=.+#vlt=L(\d+)R(\d+)/);
    var vlt_lr = [0, 0];
    if (re_resl) {
        vlt_lr = [__key_enc_vlt(re_resl[1]), __key_enc_vlt(re_resl[2])];
    }
    return vlt_lr;
}


function ___make_url_vlt_param(_in_url, vlt_lr) {
    var xxx = (_in_url.indexOf('?') > 0 ? '&' : '?');
    _in_url += (xxx + 'vlt_l=' + vlt_lr[0] + '&vlt_r=' + vlt_lr[1]);
    return _in_url;
}


function __getset_play(_in_id, cb_getplay_url, cb_cnt) {
    //
    const _url = window.location.href;
    const _rand = Math.random();
    const _getplay_url = cb_getplay_url();
    if (dettchk()) {
        $.get(_getplay_url, function (_in_data, _in_status) {
            if ('err:timeout' == _in_data) {
                if (cb_cnt > 0) {
                    __getplay_pck();
                    __getplay_pck2();
                    return __getset_play(_in_id, cb_getplay_url, cb_cnt - 1);
                } else {
                    return false;
                }
            }

            //
            if (__ipchk_getplay(_in_data)) {
                return false;
            }

            //
            const _json_obj = JSON.parse(_in_data);
            const _purl = _json_obj['purl'];
            const _vurl = _json_obj['vurl'];
            const _play_ex = _json_obj['ex'];
            const vlt_lr = __get_vlt_lr(_play_ex);

            //
            if (__qpic_chkvurl_converting(_vurl)) {
                return false;
            }

            //
            const _playid = _json_obj['playid'];
            var _vurlp2_getplay_url = '';
            if (_playid.indexOf('<play>PC-') >= 0) {
                _vurlp2_getplay_url = ('&getplay_url=' + encodeURIComponent(_getplay_url));
            }

            //
            if (__qlive_play(_playid, _vurl, __qlive_play_callbak, _in_id, _json_obj, cb_getplay_url)) {
                return (true + 1);
            }

            //
            document.getElementById(_in_id).src = ___make_url_vlt_param(_purl + _vurl + _vurlp2_getplay_url, vlt_lr);
            //
            return true;
        });
    }
    //
    return false;
}

function __yx_SetMainPlayIFrameSRC(_in_id, cb_getplay_url) {
    return __getset_play(_in_id, cb_getplay_url, 3);
}


function __s_detail_hide_emptyplay() {
    var _chk_zx = undefined;
    for (var i = 0; i < 4; i++) {
        var _sel_div_tzx = $('div#tzx_0' + (i + 1));
        var _zx_text = _sel_div_tzx.text();
        _zx_text = _zx_text.replace(/[\r\n ]*/, '');
        var _sel_li_zx = $('li#zx_' + (i + 1));
        if (!_zx_text.length) {
            _sel_li_zx.attr('class', 'normalzx');
            _sel_li_zx.hide();
            _sel_div_tzx.attr('class', 'ol-content zxundis');
        } else {
            if (1 == _chk_zx) {
                $('div#tzx_0' + (_chk_zx)).attr('class', 'ol-content zxundis');
                $('li#zx_' + (_chk_zx)).attr('class', 'normalzx');
                _chk_zx = 0;
            }

            if (!_chk_zx) {
                _sel_li_zx.attr('class', 'hoverzx');
                _sel_div_tzx.attr('class', 'ol-content zxdis');
                if (_chk_zx === undefined) {
                    _sel_li_zx.css('marginLeft', '2em');
                }
                _chk_zx = (i + 1);
            }
        }
    }

    //
    return null;
}


function __detail_hide_emptyplay() {
    //
    __s_detail_hide_emptyplay();
    //
    const def_pindex = Number($('#DEF_PLAYINDEX').text());
    for (var i = 0; i < 4; i++) {
        var _sel_div_tzx = $('div#tzx_0' + (i + 1));
        var _sel_li_zx = $('li#zx_' + (i + 1));
        if (def_pindex == i) {
            _sel_li_zx.attr('class', 'hoverzx');
            _sel_div_tzx.attr('class', 'ol-content zxdis');
        } else {
            _sel_li_zx.attr('class', 'normalzx');
            _sel_div_tzx.attr('class', 'ol-content zxundis');
        }
    }
    //
    return null;
}


function __play_hide_emptyplay() {
    const _href_url = window.location.href;
    const _refresl = _href_url.match(/\/play\/(\d{8})-(\d+?)-(\d+?)\.html/);
    const _iPlay = Number(_refresl[2]) - 1;
    //
    var _chk_zx = false;
    for (var i = 0; i < 4; i++) {
        var _sel_div_tzx = $('div#tzx_0' + (i + 1));
        var _zx_text = _sel_div_tzx.text();
        _zx_text = _zx_text.replace(/[\r\n ]*/, '');
        var _sel_li_zx = $('li#zx_' + (i + 1));
        if (_iPlay != i) {
            _sel_li_zx.attr('class', 'normalxz');
            _sel_div_tzx.attr('class', 'down-content xzundis');
            if (!_zx_text.length) {
                _sel_li_zx.hide();
            }
        } else {
            _sel_li_zx.attr('class', 'hoverxz');
            _sel_div_tzx.attr('class', 'down-content xzdis');
        }
    }

    //
    return null;
}


/////////////////////////////////

function __play_ep_scroll() {
    const _href_url = window.location.href;
    const _refresl = _href_url.match(/\/play\/(\d{8})-(\d+?)-(\d+?)\.html/);
    const _iEP = Number(_refresl[3]) - 1;

    //
    const _sel_lis = $('div.xzdis ul li');
    const _ep0_pos = _sel_lis[0].offsetTop;
    const _ep_pos = _sel_lis[_iEP].offsetTop;
    $("div.xzdis ul").scrollTop(_ep_pos - _ep0_pos);

    //
    const __t_sel_ep = 'div.xzdis ul li:nth-child(' + (_iEP + 1) + ')';
    $(__t_sel_ep).css('border', '1px solid #F00');
    $(__t_sel_ep + ' a span').css('color', '#F00');
    $(__t_sel_ep + ' p').css('color', '#F00');


}


//////////////////////////////////

const __g_exXP = ['', '630', '720', '1080'];
var __g_isfullscn = false;
var __g_new_playleft_id = null;

function __playfull_set(_in_title_on, _in_exXP) {
    if (!navigator.userAgent.match(/(iPhone|iPod|Android|mobile|blackberry|webos|incognito|webmate|bada|nokia|lg|ucweb|skyfire)/i)) {
        $(".playerbox").append('<a class="fullscn' + _in_exXP + '">' + _in_title_on + '</a>');

        //
        if (!__g_isfullscn || !_in_exXP) {
            $((".fullscn" + _in_exXP)).show();
        }

        //
        $("#playleft").mouseover(function () {
            if (!__g_isfullscn || !_in_exXP) {
                $((".fullscn" + _in_exXP)).show();
            }
        }).mouseout(function () {
            $((".fullscn" + _in_exXP)).hide()
        });
        $((".fullscn" + _in_exXP)).click(function () {
            if (!__g_isfullscn) {
                $((".fullscn" + '')).html('还原窗口');
                const _new_ID = ("fullplayleft" + _in_exXP);
                $("#playleft").attr("id", _new_ID);
                __g_new_playleft_id = _new_ID;
                //
                $("#player_back").attr("id", "player_back_full");
                //
                __g_isfullscn = true;
            } else {
                $((".fullscn" + '')).html(_in_title_on);
                $(('#' + __g_new_playleft_id)).attr("id", "playleft");
                //
                $("#player_back_full").attr("id", "player_back");
                //
                __g_isfullscn = false;
            }
        });
    }
    ;
}

function __exp_playfull_set() {
    for (var i = 0; i < __g_exXP.length; i++) {
        const p1 = (__g_exXP[i] ? ('网页' + __g_exXP[i] + 'P') : '网页全屏');
        const p2 = (__g_exXP[i] ? ('-' + __g_exXP[i] + 'p') : '');
        __playfull_set(p1, p2);
    }
}


