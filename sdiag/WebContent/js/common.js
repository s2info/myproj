///=============================================================
///
///String 프로토타입
///
///=============================================================

///상용법 (var a = ''.Empty() -- a 변수가 빈 문자열로 초기화 됨.)
String.prototype.Empty = function () {
    return ('');
};

//비밀번호 체크 문자, 특수문자, 숫자 포함 8~20자리
String.prototype.IsPasswordCheck = function () {
    if (this.IsNullOrEmpty(this)) return false;

    //var rx = /^.*(?=^.{8,20}$)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$/;
    var rx = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,20}$/;

    return rx.test(this);

};

///escapeHtml
String.prototype.GetVaildString = function () {
    var entityMap = {
        "&": "&amp;",
        "<": "&lt;",
        ">": "&gt;",
        '"': '&quot;',
        "'": '&#39;',
        "/": '&#x2F;'
    };
    return this.replace(/[&<>"'\/]/g, function (s) {
        return entityMap[s];
    });
  
};

///캐리지리턴문자 포함한 바이트 단위로 문자열 길이 반환
String.prototype.ByteCount = function () {
    var bytesOfContent = 0;

    for (var i = 0; i < this.length; i++) {
        var theChar = this.charAt(i);
        var escChar = escape(theChar);

        if (escChar.length > 4 || theChar == '·' || theChar == '°') bytesOfContent += 2; /* 특수문자, 한글인 경우 2 Byte 로 카운트합니다. */
        else bytesOfContent += 1;
    }

    return bytesOfContent;
};

///캐리지리턴문자 제외한 바이트 단위로 문자열 길이 반환 
String.prototype.ByteLength = function () {
    var bytesOfContent = 0;

    for (var i = 0; i < this.length; i++) {
        var theChar = this.charAt(i);
        var escChar = escape(theChar);

        if (escChar.length > 4 || theChar == '·' || theChar == '°') bytesOfContent += 2;
        else if (theChar != '\r') bytesOfContent += 1;
    }

    return bytesOfContent;
};

///바이트 단위로 문자열 길이를 계산한 후('\r' 제외). 제수(divisor)로 나눈 몫, 나머지가 있으면 (몫+1) 한 결과 반환
///인자가 없으면 divisor = 80 (sms 1건 기본 문자수) 사용
String.prototype.ByteGuns = function () {
    var guns = 0;
    var divisor = arguments.length > 0 ? arguments[0] : 80;
    var length = this.ByteLength();

    if (divisor > 0)
        guns = Math.ceil(length / divisor);
    else
        alert("0 이하의 수로는 나눌 수 없습니다.");

    return guns;
};

///지정 자리수(첫 인자) 마다 지정 문자(두번째 인자) 찍기
///인자가 없으면 3자리 마다 ',' 를 넣습니다.
///첫 인자는 있고 두번째 인자를 지정하지 않으면 ',' 를 넣습니다.
String.prototype.CommaString = function () {
    str = this.replace(/,/g, '');
    var divisor = (arguments.length > 0) ? arguments[0] : 3;
    var insertChar = (arguments.length > 1) ? arguments[1] : ",";

    var cnt = 0;
    var tmp = "";
    for (var i = str.length - 1; i >= 0; i--) {
        var ch = str.charAt(i);

        tmp += ch;

        if (i > 0 && ++cnt % divisor == 0)
            tmp += insertChar;
    }

    var returnValue = "";

    for (var i = tmp.length - 1; i >= 0; i--)
        returnValue += tmp.charAt(i);

    return returnValue;
};

///이메일 형식체크
String.prototype.IsEmail = function () {
    ///regular expression 지원 여부 점검
    var supported = 0;

    if (window.RegExp) {
        var tempStr = "a";
        var tempReg = new RegExp(tempStr);
        if (tempReg.test(tempStr)) supported = 1;
    }

    if (!supported)
        return (this.indexOf(".") > 2) && (this.indexOf("@") > 0);

    var rx = new RegExp("^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
    return rx.test(this);
};

///특수문자 체크
String.prototype.IsSpecialLetter = function () {
    if (this.IsNullOrEmpty(this)) return false;

    var rx =/[`~!@#$%^*|\\\'\"\/?]/gi;
    return rx.test(this);
};

///널 또는 비어있느지 체크
String.prototype.IsNullOrEmpty = function () {
    var rx = new RegExp("^$");
    return (rx.test(this) || this == null);
};

///비어있거나 공백문자로만 존재하는지 여부
String.prototype.IsSpace = function () {
    var rx = new RegExp("\\S+", "g");
    return (this.IsNullOrEmpty(this)) || (this.length > 0 && !rx.test(this));
};

///공백문자(스페이스)로만 존재하는지 여부
String.prototype.OnlySpace = function () {
    var rx = new RegExp("\\S+", "g");
    return this.length > 0 && !rx.test(this);
};

///공백문자가 하나라도 존재하는지 여부
String.prototype.ExistSpace = function () {
    if (this.IsNullOrEmpty()) return true;

    var rx = new RegExp("\\s+", "g");

    return rx.test(this);
};

///날짜형태 체크 YYYYMMDD 년월일 구분자 없거나 또는 3가지 구분자(.-/) 사용가능 
String.prototype.IsDateFormat = function (format) {
    //인자 format : 예약
    if (this.IsNullOrEmpty(this)) return false;

    var rx = null;

    rx = new RegExp("[^\\d\\-\\/\\.]+", "g");

    if (rx.test(this))
        return false;

    rx = new RegExp("[^\\d]+", "g");

    return this.replace(rx, "").IsYYYYMMDD();
};

///숫자로 구성된 문자열여부 체크
String.prototype.IsNumber = function () {
    ///var rx = new RegExp("\\D+", "g");
    ///return !rx.test(this) && this.length > 0;
	regexp = /[^0-9]/gi;
    return !regexp.test(this) && this.length > 0;
};

///숫자로 구성된 문자열여부 체크
String.prototype.isnumber = function () {
    ///var rx = new RegExp("\\D+", "g");
    ///return !rx.test(this) && this.length > 0;
	regexp = /[^0-9]/gi;
    return !regexp.test(this) && this.length > 0;
};

///정수형 체크(양수, 음수)
String.prototype.IsNaNumber = function () {
    var rx = new RegExp("[^\\d\\-]+", "g");
    return !rx.test(this) && this.length > 0;
};

///핸드폰 체크 - arguments[0] : 핸드폰 구분자 (예, -)
String.prototype.IsMobile = function () {
    var arg = arguments[0] ? arguments[0] : "";
    return eval("(/01[016789]" + arg + "[1-9]{1}[0-9]{2,3}" + arg + "[0-9]{4}$/).test(this)");
};

///전화번호 체크 - arguments[0] : 전화번호 구분자 (예, -)
String.prototype.IsPhone = function () {
    var arg = arguments[0] ? arguments[0] : "";
    return eval("(/(02|0[3-9]{1}[0-9]{1})" + arg + "[1-9]{1}[0-9]{2,3}" + arg + "[0-9]{4}$/).test(this)");
};

///날짜검사
String.prototype.IsYYYYMMDD = function () {
    if (this.IsNullOrEmpty(this)) return false;
   
    var rx = new RegExp("^[12][0-9]{3}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$");

    if (!rx.test(this)) return false;

    var days = null;
    var year = parseFloat(this.substr(0, 4));
    var month = parseFloat(this.substr(4, 2));
    var day = parseFloat(this.substr(6, 2));

    if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) days = 31;
    else if (month == 4 || month == 6 || month == 9 || month == 11) days = 30;
    else if (month == 2 ) {
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)
            days = 29;
        else
            days = 28;
    }

    return (day <= days);
};

String.prototype.trim = function () {
    return this.replace(/(^\s*)|(\s*$)/g, "");
};

String.prototype.Trim = function () {
    return this.replace(/(^\s*)|(\s*$)/g, "");
};

String.prototype.Ltrim = function () {
    return this.replace(/(^\s*)/, "");
};

String.prototype.LTrim = function () {
    return this.replace(/(^\s*)/, "");
};

String.prototype.Rtrim = function () {
    return this.replace(/(\s*$)/, "");
};

String.prototype.RTrim = function () {
    return this.replace(/(\s*$)/, "");
};

///x : 문자열이면 커리에서 변수명이 문자열(x)와 같은 변수의 값을 반환 또는 
///x : JSON 객체배열 (예, [{'name':'a','value':'2'},{'name':'b','value':'5'},...]) 이면 커리에 추가하거나 또는 존재하면 대체 함.
///r : query 문자열 반환 (예, ?a=2&b=5&...)
String.prototype.GetQuery = function (x) {
    var path = this.split("?");
    var oldQuerys = (path.length > 1) ? path[1].split("&") : path[0].split("&");
    var newQuerys;

    if (arguments.length == 0 || x == null)
        return (oldQuerys.length > 0 ? '?' + oldQuerys.join("&") : ''.Empty());

    if ((typeof x).toLowerCase() == 'string') {
        newQuerys = ''.Empty();

        for (var i = 0; i < oldQuerys.length; i++)
            if (oldQuerys[i] != null && oldQuerys[i].split("=").length == 2 && oldQuerys[i].split("=")[0].toLowerCase() == x.toLowerCase()) {
                newQuerys = oldQuerys[i].split("=")[1];
                break;
            }
    }
    else {
        newQuerys = [];

        for (var k = 0; k < x.length; k++) {
            if (x[k] == null)
                continue;

            if (!('name' in x[k] && 'value' in x[k]))
                continue;

            for (var i = 0; i < oldQuerys.length; i++)
                if (oldQuerys[i] == null || oldQuerys[i].IsNullOrEmpty() || oldQuerys[i].IsSpace() || oldQuerys[i].split("=").length != 2 || oldQuerys[i].split("=")[0].toLowerCase() == x[k].name.toLowerCase()) {
                    oldQuerys.splice(i, 1);
                    --i;
                }

            newQuerys.push(x[k].name + '=' + x[k].value);
        }

        for (var i = 0; i < oldQuerys.length; i++)
            newQuerys.push(oldQuerys[i]);
    }

    return ((typeof x).toLowerCase() == 'string' ? newQuerys : newQuerys.length > 0 ? '?' + newQuerys.join("&") : ''.Empty());
};

///x : 문자열이면 커리에서 변수명이 문자열(x)와 같은 변수제거 또는 
///x : 문자열의 배열이면 (예, ['a','b',...]) 이면 커리에서 변수명이 일치하면 제거 함.
///r : 제거 후 나머지 query 문자열 반환 (예, ?a=2&b=5&...)
String.prototype.RemoveQuery = function (x) {
    var path = this.split("?");
    var oldQuerys = (path.length > 1) ? path[1].split("&") : path[0].split("&");

    if (oldQuerys == null || oldQuerys.length == 0)
        return ''.Empty();

    if ((typeof x).toLowerCase() == 'string') {
        for (var i = 0; i < oldQuerys.length; i++)
            if (oldQuerys[i] == null || oldQuerys[i].IsNullOrEmpty() || oldQuerys[i].IsSpace() || oldQuerys[i].split("=").length != 2 || oldQuerys[i].split("=")[0].toLowerCase() == x.toLowerCase()) {
                oldQuerys.splice(i, 1);
                --i;
            }
    }
    else {
        for (var k = 0; k < x.length; k++) {
            for (var i = 0; i < oldQuerys.length; i++)
                if (oldQuerys[i] == null || oldQuerys[i].IsNullOrEmpty() || oldQuerys[i].IsSpace() || oldQuerys[i].split("=").length != 2 || oldQuerys[i].split("=")[0].toLowerCase() == x[k].toLowerCase()) {
                    oldQuerys.splice(i, 1);
                    --i;
                }
        }
    }

    return (oldQuerys.length > 0 ? '?' + oldQuerys.join("&") : ''.Empty());
};

///=============================================================
///
///Number 프로토타입
///
///=============================================================
///digit 자리수 마다 ',' 찍기
///digit = null 또는 전달인자가 하나이면 3 자리(천단위) 마다 ',' 
Number.prototype.CommaString = function () {
    var digit = arguments[0];
    var divisor = (digit != null && digit > 1) ? parseFloat(digit) : 3;
    var str = String(this).split(".");
    var decimalPoint = (str.length > 1) ? "." + parseInt(str[1]) : "";
    var tmp = "";


    var cnt = 0;
    for (var i = str[0].length - 1; i >= 0; i--) {
        var ch = str[0].charAt(i);

        tmp += ch;

        if (i > 0 && ++cnt % divisor == 0)
            tmp += ",";
    }

    var returnValue = "";

    for (var i = tmp.length - 1; i >= 0; i--)
        returnValue += tmp.charAt(i);

    returnValue += decimalPoint;

    return returnValue;
};

/* 메인팝업 */
//팝업 쿠키
function Get_PupupCookie(name) {
    var nameOfCookie = name + "=";
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
    return "";
}

function Set_PopupCookie(name, value, expiredays) {
    var todayDate = new Date();
    todayDate.setDate(todayDate.getDate() + expiredays);
    document.cookie = name + "=" + escape(value) + "; path=/; expires=" + todayDate.toGMTString() + ";";

}
/* 메인팝업 end */


