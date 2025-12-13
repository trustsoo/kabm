window.xAlert = function xAlert(msg){
    try{
        ubicus.app.notify('error',msg);
    }catch(e){
        alert(msg);
    }
}

window.xSubmit = function submit(settings) {
    var formObj = settings.form || null;
    
    if (formObj === null) {
        throw new Error('xSubmit은 반드시 form객체가 필요합니다.\nsettings에 form을 설정해주세요.');
    }
    
    settings = settings || {};
    
    if (settings.validation) {
        if (!settings.validation(formObj, settings)) {
            return false;
        }
    }else{
        if(formObj != null){
            try{
                if ($(formObj).validator('validate').has('.has-error').length > 0) {
                    $(formObj).find('.has-error').each(function(){
                        console.error(this, 'validation fail');
                    });
                    return false;// form is valid
                }
                
                /*
                var validator = $(formObj).validate();
                if(!validator.form()){
                    return false;
                }
                */
            }catch(e){}
        }
    }
    
    var defaultOptions = {
        loadingBar : true
    };
    
    $.extend(true, defaultOptions, settings);
    
    if (settings.beforeSend) {
        if (settings.beforeSend(formObj, defaultOptions)) {
            formObj.submit();
        } else {
            return false;
        }
    } else {
        formObj.submit();
    }
};

window.xAjax = function submitAjax(settings) {
    settings = settings || {};
    
    var formObj = settings.form || null;
    
    if (settings.validation) {
        if (!settings.validation(formObj, settings)) {
            return false;
        }
    }else{
        if(formObj != null){
            try{
                if ($(formObj).validator('validate').has('.has-error').length > 0) {
                    $(formObj).find('.has-error').each(function(){
                        console.error(this, 'validation fail');
                    });
                    
                    return false;// form is valid
                }
                
                /*
                var validator = $(formObj).validate();
                if(!validator.form()){
                    return false;
                }
                */
            }catch(e){}
        }
    }
    
    var defaultOptions = {
        loadingBar : true
    // 로딩바
    };
    
    var resultAjaxOption = {
        success : function(data, textStatus, jqXHR) {
            try {
                if (settings.success) {
                    settings.success(data, textStatus, jqXHR);
                }
            } finally {
                Loader.hide();
            }
        },
        error : function(jqXHR, textStatus, errorThrown) {
            if (settings.error) {
                try {
                    settings.error(jqXHR, textStatus, errorThrown);
                } finally {
                    Loader.hide();
                }
            } else {
                if (jqXHR.readyState !== 0) {
                    var actionName = formObj ? formObj.getAttribute("action") : defaultOptions.url;
                    var status = jqXHR.status;
                    if (status == 500) {
                        if(jqXHR.responseJSON && jqXHR.responseJSON.rmsg){
                            xAlert(jqXHR.responseJSON.rmsg);
                        }else{
                            xAlert('서비스 오류 발생[' + actionName + ']');
                        }
                    } else if (status == 404) {
                        xAlert('요청하신 서비스를 찾을수 없습니다.\n[' + actionName + ']');
                    } else {
                        xAlert('시스템 송신 오류가 발생하였습니다.\n[' + jqXHR.status + ':' + actionName + ']');
                    }
                }
            }
        },
        complete : function(jqXHR, textStatus) {
            try {
                if (settings.complete) {
                    settings.complete();
                }
            } finally {
                Loader.hide();
            }
        },
        beforeSend : function(jqXHR, opts) {
            if (defaultOptions['loadingBar']) {
                Loader.show();
            }
            
            try {
                if (settings.beforeSend) {
                    if (!settings.beforeSend(jqXHR, opts)) {
                        Loader.hide();
                        jqXHR.abort();
                    }
                }
            } finally {
                Loader.hide();
            }
        }
    };
    
    $.extend(true, defaultOptions, settings);
    $.extend(true, defaultOptions, resultAjaxOption);
    // 이후로는 defaultOptions만 사용
    
    var getAjaxOption = function() {
        var ajaxOption;
        if (formObj !== null && formObj !== undefined) {
            var $formObj = $(formObj);
            ajaxOption = {
                cache : false,
                url : $formObj.attr("action"),
                method : $formObj.attr("method")||'GET',
                data : defaultOptions.data ? defaultOptions.data : $formObj.serialize()
            };
            
            $.extend(true, ajaxOption, defaultOptions);
        } else {
            ajaxOption = {
                url : defaultOptions.url,
                data : defaultOptions.data
            };
            
            $.extend(true, ajaxOption, defaultOptions);
        }
        
        return ajaxOption;
    };
    
    return $.ajax(getAjaxOption());
};

window.Loader = (function() {
    var self = {};
    
    var timer = null;
    
    self.event = $({});
    
    var messageId = 'common-loading-message';
    var messageImgUrl = '/common/img/loading.gif';
    
    // 현재 로딩되고 있는 로더들 개수
    var nowLoading = 0;
    var blockDiv = $('<div style="width:100%;height:100%;position:fixed;z-index:2147483638;top:0;left:0;background-color:white;">').css({
        opacity : 0
    });
    
    var loaderImgWidth = 50;
    var loaderImgHeight = 50;
    var loaingDiv = '<div id="' + messageId + '" class="' + messageId + '" ';
    loaingDiv += ' style="width:' + loaderImgWidth + 'px;height:' + loaderImgHeight + 'px;margin-left:-' + (loaderImgWidth / 2) + 'px;margin-top:-' + (loaderImgHeight / 2) + 'px;';
    loaingDiv += ' top:50%;left:50%;display:none;position:absolute;z-index:1000000;text-align:center;">';
    loaingDiv += '<img src="' + messageImgUrl + '" alt="로딩중.."/>';
    loaingDiv += '</div>';
    
    function appendMessageMarkup() {
        var markup = $(loaingDiv);
        
        $('body').append(markup);
        
        markup.position({
            my : 'center',
            at : 'center',
            of : window
        });
        
        markup.show();
        
        $('body').append(blockDiv);
        
    }
    
    function checkExist() {
        
        var loader = $('#' + messageId);
        
        if (loader.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
    
    function removeMarkup() {
        $('#' + messageId).remove();
        nowLoading = 0;
        
        blockDiv.remove();
    }
    
    function showMessage() {
        
        if (nowLoading === 0) {
            removeMarkup();
            appendMessageMarkup();
        }
        
        nowLoading++;
        self.event.trigger('startloading');
    }
    
    function hideMessage(forceClosing) {
        
        if (nowLoading < 1) {
            return;
        }
        
        nowLoading--;
        
        if (nowLoading === 0 || forceClosing) {
            removeMarkup();
        }
        
        self.event.trigger('endloading');
    }
    
    self.show = function() {
        showMessage();
    };
    
    self.hide = function() {
        hideMessage();
    };
    
    self.setTimeout = function(time) {
        
        if (timer !== null) {
            clearTimeout(timer);
            timer = null;
        }
        
        timer = setTimeout(function() {
            hideMessage(true);
        }, time);
    };
    
    // default
    self.setTimeout(30 * 1000); // 1분
    
    return self;
    
})();

/**
 * ubicus.common 유틸 패키지
 * 
 * @author neoxeni neoxeni@ubicus.co.kr
 * @version 1.0
 */
(function(ubicus, $, window, document, undefined) {
    window.ubicus = ubicus;
    
    var resizeTimer = null;
    $(window).on('resize',function(){
        clearTimeout(resizeTimer);
        resizeTimer = setTimeout(function(){
            for( var i = 0, ic = ubicus.common.RESIZE_ARRAY.length; i < ic; i++ ){
                try{
                    ubicus.common.RESIZE_ARRAY[i]();
                }catch(e){
                    console.log('window resize in ubicus.common',e);
                    ubicus.common.RESIZE_ARRAY.splice(i,1);
                }
            }
        },400);
    });
    
    var module = ubicus.common = {
        RESIZE_ARRAY : [],
        
        addResizeFunction : function(func){
            ubicus.common.RESIZE_ARRAY.push(func);
        },
        
        escapeXSS : function escapeXSS(val) {
            return val.replace(/&#40;/g,'(').replace(/&#41;/g,')').replace(/&/g,'').replace(/=/g,'').replace(/:/g,'').replace(/\?/g,'').replace(/\//g,'').replace(/</g,'').replace(/>/g,'').replace(/\'/g,'').replace(/\"/g,'').replace();
        },

        escapeHTML : function escapeHTML(val) {
            return val.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;').replace(/\'/g,'&#039;').replace(/\"/g,'&quot;').replace();   
        },

        unescapeHTML : function unescapeHTML(val) {
            return val.replace(/&amp;/g,'&').replace(/&lt;/g,'<').replace(/&gt;/g,'>').replace(/&#039;/g,'\'').replace(/&quot;/g,'\"');
        },
        
        /**
         * serialize로 만들어진 문자열의 값을 변화 (ex:콤마제거, 달력 / 제거)
         * 
         * @param {String} str 체크할 문자열
         * @return{boolean} str이 문자열인지 여부
         */
        isHTML : function hasHTML(str){
            if(str == null || str == ''){
                return false;
            }
            var a = document.createElement('div');
            a.innerHTML = str;

            for (var c = a.childNodes, i = c.length; i--; ) {
              if (c[i].nodeType == 1) return true; 
            }

            return false;
        },
        
        setIframeContent : function($iframe,content){
            $iframe = $($iframe);
            if($iframe.data('content') === undefined){
                $iframe.data('content',content);
                $iframe.empty();
            }else{
                content = $iframe.data('content');
            }
            
            if(content != undefined && content != ''){
                var ifrm = $iframe[0];
                //val = val.replace(/(<a href=["|']hts[^>]*>)([^<]+)(<\/a>)/g, '$2');
                ifrm = ifrm.contentWindow || ifrm.contentDocument.document || ifrm.contentDocument;
                ifrm.document.open();
                ifrm.document.write($.trim(content));
                var style = document.createElement('style');
                style.textContent =
                    'html, body {' +
                    '    white-space: pre-wrap;' +
                    '    word-wrap: break-word;' +
                    '    margin: 0;' +
                    '    padding: 0;' +
                    '    height: 100%;' +
                    '}' 
                ;
                
                ifrm.document.head.appendChild(style);
                ifrm.document.close();
                
                var resizeIframe = function resizeIframe(iframe) {
                    var padding = 0;
                    if (iframe.contentWindow.document.body.scrollHeight < (window.innerHeight - padding))
                        iframe.height = iframe.contentWindow.document.body.scrollHeight + "px";
                    else
                        iframe.height = (window.innerHeight - padding) + "px";
                }
                
                resizeIframe($iframe[0]);
            }
        },
            
        /**
         * serialize로 만들어진 문자열의 값을 변화 (ex:콤마제거, 달력 / 제거)
         * 
         * @param {String} name 변경할 필드 이름
         * @param {String} value 변경값
         * @param {String} oridata serialize로 만들어진 원본 문자열
         * @return{String} name에 해당하는 필드의 값을 value로 변경한 serialize된 문자열
         */
        changeValue : function(name, value, oridata) {
            try {
                var exp = "(" + name + "=*[^\&]*.*?)";
                var changedValue = oridata.replace(new RegExp(exp), name + "=" + value);
                return changedValue;
            } catch (e) {
                return oridata;
            }
        },
        
        /**
         * serialize로 만들어진 문자열의 값을 변화 (ex:콤마제거, 달력 / 제거)
         * 
         * @param {String} name 삭제할 필드 이름
         * @param {String} oridata serialize로 만들어진 원본 문자열
         * @return{String} name에 해당하는 필드의 값을 제거한 serialize된 문자열
         */
        removeValue : function(name, oridata) {
            try {
                var exp = "(" + name + "=*[^\&]*.*?)";
                var changedValue = oridata.replace(new RegExp(exp), '');
                return changedValue;
            } catch (e) {
                return oridata;
            }
        },
        
        /**
         * 오늘 날짜를 구해서 리턴한다. (구분인자가 정의되지 않으면 yyyyMMdd 포멧으로, 인자가 정의되면
         * yyyy(구분)MM(구분)dd 형식으로 리턴 (ex: "/" -> yyyy/MM/dd
         * 
         * @param {String} delim 날짜 포맷 구분자 , / - . 등등
         * @return{String} 오늘 날짜를 delim 으로 포맷팅한 문자열
         */
        getToday : function(delim) {
            var today = new Date();
            var year = today.getFullYear();
            var month = today.getMonth();
            var day = today.getDate();
            
            if ((month + 1) < 10) {
                month = "0" + (month + 1);
            } else {
                month = month + 1;
            }
            
            if (day < 10) {
                day = "0" + day;
            }
            
            if (delim != undefined) {
                return year + delim + month + delim + day;
            } else {
                return year + "" + month + "" + day;
            }
        },
        
        /**
         * 현재 시간을 delim에 맞는 문자열로 포맷팅하여 리턴한다. 102530 또는 10:25:30
         * 
         * @param {String} delim 시간 포맷 구분자 "" 또는 :
         * @return{String} delim으로 포맷팅된 현재 시간 문자열
         */
        getCurrentTime : function(delim) {
            var today = new Date();
            var hour = today.getHours();
            var min = today.getMinutes();
            var sec = today.getSeconds();
            
            if (hour < 10) {
                hour = '0' + hour;
            }
            if (min < 10) {
                min = '0' + min;
            }
            if (sec < 10) {
                sec = '0' + sec;
            }
            
            if (delim != undefined) {
                return hour + delim + min + delim + sec;
            } else {
                return hour + "" + min + "" + sec;
            }
        },
        
        /**
         * 시작일과 종료일간의 일수 차이를 계산하여 리턴
         * 
         * @param {String} start 날짜 형식의 문자열 의 시작일
         * @param {String} end 날짜 형식의 문자열 의 종료일
         * @return{Number} 일수 차이
         */
        getDiffDay : function(start, end) {
            var startDate = new Date(wts.util.formatDate(start, '/'));
            var endDate = new Date(wts.util.formatDate(end, '/'));
            var diffTime = endDate.getTime() - startDate.getTime();
            var diffDay = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
            
            return diffDay;
        },
        
        /**
         * 데이트 형식의 문자열에 delim으로 구분된값으로 변형하여 리턴. 20160412 -> 2016/04/12
         * 
         * @param {String} date 날짜 형식의 문자열
         * @param {String} delim 날짜 포맷 구분자 , / - . 등등
         * @return{String} delim으로 포맷팅된 날짜 문자열
         */
        formatDate : function(date, delim) {
            date = date.replace(/[,\/\.-]/g, '');
            delim = delim === undefined ? '.' : delim;
            
            var m1 = date.match(/^(\d{4})(\d{2})(\d{2})$/);
            var m2 = date.match(/^(\d{4})(\d{2})$/);
            
            var m = m1 || m2;
            
            if (!m) {
                return date;
            }
            
            var ret = '';
            for (var i = 1, max = m.length; i < max; i++) {
                ret += (delim + m[i]);
            }
            
            ret = ret.substring(1);
            
            return (m) ? ret : date;
        },
        
        /**
         * 숫자값에 콤마(,) 추가한다. 두번째 인자로 ele를 넣는경우 ele에 세팅 해준다.
         * 
         * @param {String} num ,가 처리되지 않은 숫자값
         * @param {element} ele input text element
         * @return{String} ,를 추가한 문자열
         */
        addComma : function(num, ele) {
            var minus = false;
            var isPoint = false;
            var pointNum = '';
            
            if (num === undefined) {
                num = '';
            }
            
            num = (num + '');
            
            if (num.charAt(0) == '-') {
                minus = true;
            }
            
            if (num.indexOf('.') > 0) {
                var numStrs = num.split('.');
                num = numStrs[0];
                pointNum = '.' + numStrs[1];
            }
            
            num = num.replace(/[^0-9]/g, '');// 숫자만 남겨놓고 모두 제거
            
            if (num.length < 4) {
                if (minus === true) {
                    return '-' + num + pointNum;
                } else {
                    return num + pointNum;
                }
                
            }
            
            var ret = num.replace(/\B(?=(\d{3})+(?!\d))/g, ",") + pointNum;
            
            if (minus === true) {
                ret = '-' + ret;
            }
            
            if (ele) {
                ele.value = ret;
            }
            
            return ret;
        },
        
        /**
         * xls의 반올림 방식 구현
        */
        roundXL : function(n, digits) {
          if (digits >= 0) return parseFloat(n.toFixed(digits)); 

          digits = Math.pow(10, digits); 
          var t = Math.round(n * digits) / digits;

          return parseFloat(t.toFixed(0));
        },
        
        /**
         * FileSize를 KB 단위로 변환하여 리턴한다.
         * @param {Integer} size 파일의 사이즈
         * @return{String} KB 단위로 변환된 값
        */
        getFileSizeKB : function(size){
            return module.addComma(module.roundXL(size/1024, 0))+" KB";
        },
        
        /**
         * 숫자값에 콤마 제거
         * 
         * @param {String} num 문자열 숫자값
         * @return{String} 콤마가 제거된 문자 숫자값
         */
        removeComma : function(num) {
            return num.replace(/\,/g, '');
        },
        
        /**
         * input text에 키가 눌릴때마다 comma를 찍어주는 이벤트를 부여한다.
         * 
         * @param {element}input input type text element
         */
        addCommaWhenKeydown : function(input) {
            input.on("keyup keydown", function(event) {
                var eventType = event.type;
                var keyCode = event.keyCode;
                
                // 숫자,키패드 ,(백스페이스, delete, (dot) , (comma) , (keypad dot)
                if ((keyCode > 47 && keyCode < 58) || (keyCode > 95 && keyCode < 106) || (keyCode == 8 || keyCode == 46) || keyCode == 110) {
                    if (eventType == "keyup") {
                        // this.value =
                        // numberWithCommas(this.value,this.getAttribute('fix'));
                        this.value = wts.util.addComma(this.value);
                    }
                } else {
                    if (eventType == "keydown") {
                        if (keyCode != 37 && keyCode != 39 && keyCode != 9 && keyCode != 16) {// 좌우
                            // 방향키,TAB,SHIFT
                            event.preventDefault();
                            event.stopPropagation();
                        }
                    }
                }
            });
        },
        
        /**
         * 문자열이 prefix로 시작되는지 체크
         * 
         * @param {String} str 검사할 문자열
         * @param {String} prefix 시작 문자열
         * @return{boolean} 시작하는지 안하는지 여부
         */
        startsWith : function(str, prefix) {
            if (str.startsWith !== undefined) {
                return str.startsWith(prefix);
            } else {
                return new RegExp('^' + prefix).test(str);
            }
        },
        
        /**
         * element를 기준으로 nodeName에 해당하는 부모 element를 리턴
         * 
         * @param {element} node 해당 element node
         * @param {String} pNodeName 부모노드의 이름 (대문자기준 TD, TBODY, BODY)
         * @return {element} nodeName에 해당하는 부모 Element
         */
        getParentNode : function(node, pNodeName) {
            var pNodeEle = node;
            while (pNodeEle.nodeName != pNodeName) {
                pNodeEle = pNodeEle.parentNode;
                if (pNodeEle.nodeName == 'BODY') {// 무한루프 방지
                    break;
                }
            }
            
            return pNodeEle;
        },
        
        /**
         * iframe이 로드된 이후(렌더링 시간을 위해 500ms delay) 높이를 맞춰준다.
         * 
         * @param {element} iframe 변경할 iframe object
         */
        iframeResize : function(iframe) {
            setTimeout(function() {
                var iframeHeight = (iframe).contentWindow.document.body.scrollHeight;
                iframe.setAttribute('height', (iframeHeight + 20) + 'px');
            }, 500);
        },
        
        /**
         * 저장된 브라우저 cookie중 name에 해당하는 값을 가져온다.
         * 
         * @param {String} key 가져올 쿠키의 key값
         * @return {String} key에 해당하는 value값
         */
        getCookie : function(key) {
            
            var localStorageIsAvailable = $.jStorage.storageAvailable();
            var localStorageHasNoKey    = false;
            var returnValue = "";
            
            if(localStorageIsAvailable)
            {
                returnValue = $.jStorage.get(key);
                
                if(returnValue === null)
                {
                    returnValue             = "";
                    localStorageHasNoKey    = true;
                }
            }
            
            if(localStorageHasNoKey)
            {
                var nameOfCookie = key + "=";
                var x = 0;
                while (x <= document.cookie.length) {
                    var y = (x + nameOfCookie.length);
                    if (document.cookie.substring(x, y) == nameOfCookie) {
                        if ((endOfCookie = document.cookie.indexOf(";", y)) == -1)
                            endOfCookie = document.cookie.length;
                        return unescape(document.cookie.substring(y, endOfCookie));
                    }
                    x = document.cookie.indexOf(" ", x) + 1;
                    if (x == 0)
                        break;
                }
            }
            
            return returnValue;
        },
        
        /**
         * 브라우저 쿠키 세팅한다.
         * 
         * @param {String} key 저장할 쿠키 이름
         * @param {String} value 저장할 쿠키 값
         * @param {Number} expiredays 만료일자
         */
        setCookie : function(key, value, expiredays) {
            var todayDate = new Date();
            if (expiredays === undefined || expiredays == null) {
                expiredays = 7;
            }
            
            var localStorageIsAvailable = $.jStorage.storageAvailable();
            
            if(localStorageIsAvailable)
            {
                var TTL = 24 * 60 * 60 * expiredays;
                $.jStorage.set(key,value);
                $.jStorage.setTTL(key,TTL);
            }
            else
            {
                var cookiePath = location.pathname;
                cookiePath = '/';// IE Edge에서 설정이 안됨.. 무조건 root여야 하는듯
               
                todayDate.setDate(todayDate.getDate() + expiredays);
                document.cookie = key + "=" + escape(value) + "; path=" + cookiePath + "; expires=" + todayDate.toGMTString() + ";";
            }
        },
        
        /**
         * queryString 문자열의 값을 object로 반환
         * 
         * @param {String} queryString a=1&b=2형태의 string 문자열
         * @return {Object} key/value 형태의 map
         */
        queryStringToObject : function(queryString) {
            var obj = {};
            var querySplit = queryString.split('&');
            for (var i = 0, ic = querySplit.length; i < ic; i++) {
                var queryKeyVal = querySplit[i].split('=');
                if (queryKeyVal[1] == 'true' || queryKeyVal[1] == 'false') {
                    queryKeyVal[1] = queryKeyVal[1] == 'true' ? true : false;
                } else {
                    queryKeyVal[1] = decodeURIComponent(queryKeyVal[1]);
                }
                obj[queryKeyVal[0]] = queryKeyVal[1];
            }
            
            return obj;
        },
        
        /**
         * 중복되지 않는 유니크한 아이디를 생성하여 리턴한다.
         * 
         * @return {String} 유니크 아이디
         */
        UUID : function() {
            var d = new Date().getTime();
            return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
                var r = (d + Math.random() * 16) % 16 | 0;
                d = d / 16 | 0;
                return (c == 'x' ? r : (r & 0x7 | 0x8)).toString(16);
            });
        },
        
        /**
         * 라디오의 선택된 값을 리턴한다.
         * 
         * @param {element} radio 라디오 element ex) f.radio
         * @return {String} 선택된 라디오 element의 value값
         */
        getRadioValue : function(radio) {
            return $(radio).filter(':checked').val();
        },
        
        sample : function(anyParams) {
            
        }
    };
    
})(window.ubicus || {}, jQuery, this, this.document);
