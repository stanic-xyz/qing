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

var qike123 = qike123 || {};
qike123.switchTab = function (i, o, p) {
    var n = {
        delay: 100,
        trigger: "mouseover",
        lazyload: 1
    };
    $.extend(n, p || {});
    var j = {};
    j.load0 = 1;
    var k;
    if (n.trigger === "mouseover") {
        i.mouseover(function () {
            l(this)
        })
    } else {
        if (n.trigger === "click") {
            i.click(function () {
                l(this)
            })
        }
    }

    function l(b) {
        var a = b;
        if (n.delay > 0) {
            k && clearTimeout(k);
            k = setTimeout(function () {
                    m(a)
                },
                n.delay)
        } else {
            m(a)
        }
    }

    function m(c) {
        i.removeClass("av");
        $(c).addClass("av");
        var b = $(c).attr("tab");
        o.hide();
        var d = o[b];
        d.style.display = "block";

    }
};

function __get_playtab_on_index(m) {
    //
    const def_pindex = Number($('#DEF_PLAYINDEX').text());
    return def_pindex;

    //
    alert('已弃用191111');
    var tli = document.getElementById("menu" + m).getElementsByTagName("li");
    var mli = document.getElementById("main" + m).getElementsByTagName("div");
    for (i = 0; i < tli.length; i++) {
        const pli_cnt = mli[i].getElementsByTagName("ul")[0].getElementsByTagName("li");
        if (i > 0 && pli_cnt.length) {
            return i;
        }
    }
    //
    return 0;
}


function __set_playtab_on_index(m) {
    setTab(0, __get_playtab_on_index(m));
    return null;
}

function __set_playtab_curr_on_index(m) {
    const _href_url = window.location.href;
    const _refresl = _href_url.match(/\/play\/(\d+?)\?playid=(\d+)_(\d+)/);
    if (_refresl) {
        const _iPlay = Number(_refresl[2]) - 1;
        setTab(0, _iPlay);
    } else {
        __set_playtab_on_index(0);
    }
    return null;
}


////////////////////////


function PlayHistoryClass() {
    var cookieStr, nameArray, urlArray, allVideoArray;
    this.getPlayArray = function () {
        cookieStr = document.cookie;
        var start = cookieStr.indexOf("qike123=") + "qike123=".length, end = cookieStr.indexOf("_$_|", start),
            allCookieStr = unescape(cookieStr.substring(start, end))
        if (end == -1) {
            allCookieStr = "";
            return;
        }
        allVideoArray = allCookieStr.split("_$_");
        nameArray = new Array(), urlArray = new Array();
        for (var i = 0; i < allVideoArray.length; i++) {
            var singleVideoArray = allVideoArray[i].split("^");
            nameArray[i] = singleVideoArray[0];
            urlArray[i] = singleVideoArray[1];
        }
    }
    this.viewPlayHistory = function (div) {
        var tag = document.getElementById(div), n = 15
        if (navigator.cookieEnabled) {
            var innerStr = "";
            for (var i = nameArray.length - 1; i >= 0; i--) {
                var textCount = nameArray[i].replace(/[^\x00-\xff]/g, "cc").length;
                if (textCount <= n * 2) {
                    texts = nameArray[i];
                } else {
                    texts = nameArray[i].substr(0, n) + "...";
                }
                innerStr += "<li><a target=_blank class=name href=\"" + urlArray[i] + "\"" + " title=\"" + nameArray[i] + "\">" + texts + "</a><a target=_blank class=now href=\"" + urlArray[i] + "\"" + " title=\"" + nameArray[i] + "\">立即观看</a></li>"
            }
            if (innerStr.length > 0) {
                tag.innerHTML = innerStr
            }
        } else {
            set(tag, "您浏览器关闭了cookie功能，不能为您自动保存最近浏览过的网页。")
        }
    }
    this.addPlayHistory = function (name, url) {
        var count = 10; //播放历史列表调用条数
        var code_name = escape(name) + "^", code_url = escape(url) + "_$_",
            expireTime = new Date(new Date().setDate(new Date().getDate() + 30)),
            timeAndPathStr = "|; expires=" + expireTime.toGMTString() + "; path=/";
        if (cookieStr.indexOf("qike123=") != -1 || cookieStr.indexOf("_$_|") != -1) {
            var newCookieStr = "";
            if (allVideoArray.length < count) {
                for (i in allVideoArray) {
                    if (nameArray[i] == name) continue;
                    newCookieStr += escape(nameArray[i]) + "^" + escape(urlArray[i]) + "_$_";
                }
            } else {
                for (var i = 1; i < count; i++) {
                    if (nameArray[i] == name) continue;
                    newCookieStr += escape(nameArray[i]) + "^" + escape(urlArray[i]) + "_$_";
                }
            }
            document.cookie = "qike123=" + newCookieStr + code_name + code_url + timeAndPathStr;
        } else {
            document.cookie = "qike123=" + code_name + code_url + timeAndPathStr;
        }
    }
}


//var cookiedomain="1.22";

function delCookie(name) {
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval = getCookie(name);
    if (cval != null) document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString() + ";path=/";
}

function _GC() {
    document.getElementById('playhistory').innerHTML = "<center>暂无播放历史列表...</center>";
    var expdate = new Date(1970, 1, 1);
    delCookie("qike123");
}

var PlayHistoryObj = new PlayHistoryClass()
PlayHistoryObj.getPlayArray()

function killErrors() {
    return true;
}

window.onerror = killErrors;

var topShow = false;

function showTop(n) {
    if (topShow == true) {
        switchTab('top', n, 2, "history");
    } else {
        //alert("s");
        document.getElementById("Tab_top_" + n).className = "history";
        //document.getElementById("List_top_"+n).className="dropbox_tigger dropbox_tigger_on";
        document.getElementById("List_top_" + n).style.display = "";
        topShow = false;
    }
}

function hideTop() {
    for (i = 0; i < 2; i++) {
        var CurTabObj = document.getElementById("Tab_top_" + i);
        var CurListObj = document.getElementById("List_top_" + i);
        CurTabObj.className = "history";
        CurListObj.style.display = "none";
    }
}

function turnOff() {
    $("#shadow").css("height", 10000);
    $("#turnedOn").css("position", "relative");
    $("#playbox").css("z-index", 8);
    $("#shadow").show();
    $("#turnedOff").hide();
    $("#turnedOn").show();
    $("#light-switch").css("z-index", 7);


}

function turnOn() {
    $("#turnedOff").show();
    $("#turnedOn").hide();
    $("#shadow").hide();
    $("#light-switch").css("z-index", 0);

}


///////////////////////////

function __setCookie(name, value, _in_days) {
    var Days = _in_days;
    var exp = new Date();
    exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString() + ";path=/";
}

function __setCookie_v3(name, value, _in_days) {
    var Days = _in_days;
    var exp = new Date();
    exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
    document.cookie = name + "=" + encodeURIComponent(value) + ";expires=" + exp.toGMTString() + ";path=/";
}

function setCookie2(name, value) {
    return __setCookie(name, value, 36500);
}

function __getCookie(name) {
    var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
    if (arr === document.cookie.match(reg)) {
        return unescape(arr[2]);
    } else {
        return null;
    }
}

function __getCookie_v3(name) {
    var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
    if (arr === document.cookie.match(reg)) {
        return decodeURIComponent(arr[2]);
    } else {
        return null;
    }
}

function getCookie2(name) {
    return __getCookie(name);
}


////////////////////////////////////

function FEI2(in_epi) {

    //
    var hf_epi = Number(in_epi);
    const time_curr = new Date().getTime();

    var fa_t = Number(getCookie2('fa_t'));
    if (!fa_t) {
        fa_t = time_curr;
    }

    var fa_c = Number(getCookie2('fa_c'));
    if (!fa_c) {
        fa_c = 0;
    }

    //
    if (time_curr - fa_t > (6000)) {
        fa_t = 0;
        fa_c = 0;
    }

    //
    fa_c += 1;
    fa_t = time_curr;


    //
    if (fa_c > 10) {
        fa_t = 0;
        fa_c = 0;
        //
        if (hf_epi > 1) {
            hf_epi = (time_curr % hf_epi);
            if (!hf_epi) {
                hf_epi = 1;
            }
        }
    }

    //
    __setCookie('fa_t', fa_t, 1);
    __setCookie('fa_c', fa_c, 1);

    //
    return hf_epi;
}


//////////////////////////////////


function __html_set_username() {
    var userInfo = localStorage.getItem("userInfo");
    var node = document.getElementById('comment_user');
    if (node) {
        if (userInfo !== null) {
            node.innerHTML = JSON.parse(userInfo).username
        } else {
            node.innerHTML = '登陆';
        }
    }
}

function __set_cookie_username() {
    // 请求用户名
    var xhr = new XMLHttpRequest();

    function send1(argument) {
        xhr.open('GET', '/_username', true);
        xhr.send();
    }

    send1();
    xhr.onload = function () {
        var username = this.response;
        __setCookie_v3('username', username, 30);
    };
}

var __g_detail_imform_kv_display = [];

function detail_show_full() {
    let new_sel;
    const kv_len = 12
    if (!__g_detail_imform_kv_display.length) {
        for (var i = 0; i < kv_len; i++) {
            new_sel = $('.detail_imform_kv:nth-child(' + i + ')');
            __g_detail_imform_kv_display.push(new_sel.css('display'));
            new_sel.css('display', 'inline-block')
        }
        $('.detail_imform_show_full').html('&lt;&lt;收起');
    } else {
        for (var i = 0; i < kv_len; i++) {
            new_sel = $('.detail_imform_kv:nth-child(' + i + ')');
            new_sel.css('display', __g_detail_imform_kv_display[i]);
        }
        $('.detail_imform_show_full').html('&lt;&lt;展开');
        __g_detail_imform_kv_display = [];
    }

}

