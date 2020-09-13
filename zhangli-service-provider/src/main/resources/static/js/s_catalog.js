

///////////

function __trim_right(_in_str, _in_cch){
    var _hf_i_end = undefined;
    for (var i = (_in_str.length-1); i > 0; i--){
        if(_in_cch != _in_str[i]){
            break;
        }
        _hf_i_end = i;
    }

    //
    var _hf_str = _in_str;
    if(_hf_i_end != undefined){
        _hf_str = _hf_str.slice(0, _hf_i_end);
    }

    //
    return _hf_str;
}


////////

function __get_url_param(_in_url, _in_name){
    var _findex = _in_url.indexOf('?');
    if(_findex >= 0){
        var _urlpath = _in_url.slice(_findex + 1);
        var _params = _urlpath.split('&');
        for (var i = 0; i < _params.length; i++){
            var _s_par = _params[i].split('=');
            if(_s_par.length > 1){
                if(_in_name == _s_par[0]){
                    return _s_par[1];
                }
            }
        }
    }

    //
    return null;
}


/////////////////

function __del_url_param(_in_url, _in_name){
    var _hf_url = _in_url;
    var _findex = _in_url.indexOf('?');
    if(_findex >= 0){
        _hf_url = _in_url.slice(0, _findex + 1);
        var _urlpath = _in_url.slice(_findex + 1);
        var _params = _urlpath.split('&');
        for (var i = 0; i < _params.length; i++){
            var _s_par = _params[i].split('=');
            if(_in_name != _s_par[0]){
                _hf_url += ('&' + _s_par[0] + '=');
                if(_s_par.length > 1){
                    _hf_url += _s_par[1];
                }
            }
        }

        //
        _hf_url = _hf_url.replace('?&', '?');
        _hf_url = __trim_right(_hf_url, '?');
    }

    //
    return _hf_url;
}


/////////////////////

function __set_url_param(_in_url, _in_name, _in_value){
    var _hf_url = __del_url_param(_in_url, _in_name);
    if(String(_in_value)){
        const _XX = (_hf_url.indexOf('?') > 0 ? '&' : '?');
        _hf_url += (_XX + _in_name + '=' + _in_value);
    }

    //
    return _hf_url;
}


//////////////////

function __s_get_href(_in_name, _in_pval, _in_curr_url){
    //
    var hf_url = _in_curr_url.replace(/-AGE-RIP-/g, '-AGERIP-');

    //
    const names = [null,'genre','year','letter','label','resource','order',null,'region','season','status'];
    var re_resl = hf_url.match(/\/catalog\/([^?-]+)-([^?-]+)-([^?-]+)-([^?-]+)-([^?-]+)-([^?-]+)-([^?-]+)-*([^?-]*)-*([^?-]*)-*([^?-]*)/);
    if(names.length != re_resl.length){
        alert('if(names.length != re_resl.length){');
    }

    //
    re_resl[5] = re_resl[5].replace(/AGERIP/g, 'AGE-RIP');

    //
    re_resl[7] = 1; //第1页

    //
    const old_len = 8;
    var end_index = 8;
    var set_index = 0;
    var is_li_on = false;
    for (var i = 1; i < names.length; i++){
        //
        if('' == re_resl[i]){
            re_resl[i] = 'all';
        }
        //
        if(names[i] == _in_name){
            var new_pval = _in_pval;
            if('全部' == new_pval){ new_pval = 'all'; }
            if('更新时间' == new_pval){ new_pval = 'time'; }
            if('名称' == new_pval){ new_pval = 'name'; }
            is_li_on = (decodeURI(re_resl[i]) == new_pval);
            re_resl[i] = new_pval;
            set_index = i;
        }
        //
        if(i >= old_len){
            if('all' != re_resl[i]){
                end_index = 11;
            }
        }
    }

    //
    if(!set_index){
        alert('if(!set_index){');
        return null;
    }

    //
    var hf_href = re_resl.slice(1, end_index).join('-');
    return [hf_href, is_li_on];
}


function __yx_write_catalog_sub(_in_labels, _in_curr_url){
    var _html_li = '<li>{1}</li>';
    var _html_li_span = '<span>{1}：</span>'.replace('{1}', _in_labels[1]);
    var _html_li_ul = '<ul class=\"search-tag\">{1}</ul>';
    var _html_li_ul_li = '';
    const _hf_iLabel = 2;
    for (var i = _hf_iLabel; i < _in_labels.length; i++){
        var _li = '<li {3}><a href=\"{1}\">{2}{4}</a></li>\r\n';

        //
        var hf_resl = __s_get_href(_in_labels[0], _in_labels[i], _in_curr_url);
        var _li_href = hf_resl[0];
        _li = _li.replace('{1}', _li_href);
        _li = _li.replace('{2}', _in_labels[i]);

        //
        if(hf_resl[1]){
            _li = _li.replace('{3}', 'class=on');
        }
        else {
            _li = _li.replace('{3}', '');
        }


        //
        // 特殊修正 -----------
        if('year' == _in_labels[0] && (2000 == _in_labels[i])){
            _li = _li.replace('{4}', '以前');
        }
        else if('season' == _in_labels[0] && (i > _hf_iLabel)){
            _li = _li.replace('{4}', '月');
        }
        else {
            _li = _li.replace('{4}', '');
        }

        //
        _html_li_ul_li += _li;
    }


    //
    _html_li_ul = _html_li_ul.replace('{1}', _html_li_ul_li);
    _html_li = _html_li.replace('{1}', _html_li_span + _html_li_ul);

    //
    document.write(_html_li);
}

//////////

(function __yx_write_catalog_table(){
    const _curr_url = window.location.href;

    //
    var _labels = false;

    _labels = ["region", "地区", "全部", "日本", "中国", "欧美"];
    __yx_write_catalog_sub(_labels, _curr_url);

    _labels = ["genre", "版本", "全部", "TV", "剧场版", "OVA"];
    __yx_write_catalog_sub(_labels, _curr_url);

    _labels = ["letter", "首字母", "全部", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"];
    __yx_write_catalog_sub(_labels, _curr_url);

    _labels = ["year", "年份", "全部", "2020", "2019", "2018", "2017", "2016", "2015", "2014", "2013", "2012", "2011", "2010", "2009", "2008", "2007", "2006", "2005", "2004", "2003", "2002", "2001", "2000"];
    __yx_write_catalog_sub(_labels, _curr_url);

    _labels = ["season", "季度", "全部", "1", "4", "7", "10"];
    __yx_write_catalog_sub(_labels, _curr_url);

    _labels = ["status", "状态", "全部", "连载", "完结", "未播放"];
    __yx_write_catalog_sub(_labels, _curr_url);

    _labels = ["label", "类型", "全部", "搞笑", "运动", "励志", "热血", "战斗", "竞技", "校园", "青春", "爱情", "冒险", "后宫", "百合", "治愈", "萝莉", "魔法", "悬疑", "推理", "奇幻", "科幻", "游戏", "神魔", "恐怖", "血腥", "机战", "战争", "犯罪", "历史", "社会", "职场", "剧情", "伪娘", "耽美", "童年", "教育", "亲子", "真人", "歌舞", "肉番", "美少女", "轻小说", "吸血鬼", "乙女向", "泡面番", "欢乐向"];
    __yx_write_catalog_sub(_labels, _curr_url);

    _labels = ["resource", "资源", "全部", "BDRIP", "AGE-RIP"];
    __yx_write_catalog_sub(_labels, _curr_url);

    _labels = ["order", "排序", "更新时间", "名称", "点击量"];
    __yx_write_catalog_sub(_labels, _curr_url);

    //

})();


