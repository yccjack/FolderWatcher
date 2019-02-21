$(function () {

    menuClick();
    /*选择性加载页面，初始加载页面A*/

    $("#root li").click(function () {
        var _a = $(this).find("a");
        if ($(_a.attr("href")).html() == "") {   //判断页面是否已加载
            $(_a.attr("href")).addClass("active").siblings().removeClass("active");
            //tab页点击事件对应的页面元素置为"active"样式，其他兄弟页面移除"active"样式
            $(_a.attr("href")).load(_a.attr("tt"));
            //加载样式为"active"的页面
        }
    });
    $(".nav.nav-tabs li").get(0).click();    //默认选择加载第一个页面A


    $(".form_datetime").datetimepicker({format: 'yyyy-mm-dd hh:ii'});
    var type1 = "queryType";
    var type2 = "queryType2";
    importJsonContent(type1);
    importJsonContent(type2);
    var $timeStart = $("#startTime");
    var $endTime = $("#endTime");
    initTime();
    //设置隐藏域开始时间
    $timeStart.change(function () {
        var $myDate = new Date($timeStart.val()).Format("yyyyMMddHHmmss");

        var hidStartTime = $("#startTimeFormat");
        hidStartTime.val($myDate);
        appendFinal();
    });
    //设置隐藏域结束时间
    $endTime.change(function () {
        var $myDate = new Date($endTime.val()).Format("yyyyMMddHHmmss");
        //alert($myDate);
        var hidStartTime = $("#endTimeFormat");
        hidStartTime.val($myDate);
        // alert(hidStartTime.val())
        appendFinal();
    });

    var $queryData = $("#queryData");
    //设置复选
    var $queryType2 = $("#queryType2");
    $queryType2.change(function () {
        if ($("#methodName").html() == "Statistics") {
            handlerFX($queryType2, 1);
        } else {
            handlerFX($queryType2, 2);
        }


    });
    $("#queryData").change(function () {
        appendFinal();
    });
    $("#rel_queryType2").change(function () {
        appendFinal();
    });
    $("#queryType2").change(function () {
        appendFinal();
    });
    $("#ip1").change(function () {
        appendFinal();
    });
    $("#ip2").change(function () {
        appendFinal();
    });
    $("#IMEI").change(function () {
        appendFinal();
    });
    $("#account").change(function () {
        appendFinal();
    });

    //提交
    $("#sub").click(function () {
        var wsdl = $("#wsdl").val();
        if (!isNotEmpty(wsdl)) {
            alert("前台wsdl地址必填");
            return false;
        }
        var requestJson = {"param": $("#customer").val(), "wsdl": wsdl, "method": $("#methodName").html()};
        ajaxRequest(requestJson, $("#resultQueryData"), 1);
    });
    $("#subStatus").click(function () {
        var wsdl = $("#wsdl").val();
        var requestJson = {"param": $("#queryId").val(), "wsdl": wsdl, "method": "queryStatus"};
        ajaxRequest(requestJson, $("#resultStatus"), 0);
    });
});

function importJsonContent(type) {
    $.getJSON("js/userJs/serviceType.json", function (data) {
        var jsontip;
        var strHtml = "";
        if (type == "queryType") {
            jsontip = $("#queryData");
        } else if (type == "queryType2") {
            jsontip = $("#queryType2");
            strHtml +="<option></option>";
        }

        //存储数据的变量
        //清空内容
        for (var key in data) {
            var data2 = data[key];
            for (var key in data2) {
                var data3 = data2[key];
                for (var key in data3) {
                    var data4 = data3[key];
                    if (key == type) {
                        for (var key in data4) {
                            strHtml += "<option value='" + key + "'>" + key + ":" + data4[key] + "</option>";
                        }
                    }
                }
            }
        }
        jsontip.append(strHtml);
        //  $jsontip.html(strHtml);
        //显示处理后的数据
    })
};

//时间转换函数 yyyy:MM:dd HH:mm:ss
Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};

var arrayString = new Array("11", "13", "21", "22", "24", "25", "23");
var arrayFile = new Array("51", "31", "32", "33", "34", "41", "52");
var arrayStatisString = new Array("1", "2", "3", "4");

function handlerFX(content, type) {
    var html = "";
    var $rel_queryType2 = $("#rel_queryType2");
    $rel_queryType2.empty();
    if (content.val().search("返回值") != -1 && type == 2) {
        for (var i = 0; i < arrayString.length; i++) {
            html += "<option>" + arrayString[i] + "</option>"

        }
        $("#rel_queryType2").append(html)
    } else if (content.val().search("文件") != -1 && type == 2) {
        for (var i = 0; i < arrayFile.length; i++) {
            html += "<option>" + arrayFile[i] + "</option>"

        }
        $("#rel_queryType2").append(html)
    } else if (content.val().search("返回值") != -1 && type == 1) {
        for (var i = 0; i < arrayStatisString.length; i++) {
            html += "<option>" + arrayStatisString[i] + "</option>"

        }
        $("#rel_queryType2").append(html)
    }

};

//拼接结果
function appendFinal() {

    someTypeIsReady()


};

function someTypeIsReady() {
    var queryData = $("#queryData").val();
    var rel_queryType2 = $("#rel_queryType2").val();
    var IMEI = $("#IMEI").val();
    var account = $("#account").val();
    var startTimeFormat = $("#startTimeFormat").val();
    var endTimeFormat = $("#endTimeFormat").val();
    var customer = $("#customer");
    var ip1 = $("#ip1").val();
    var ip2 = $("#ip2").val();
    var methodName = $("#methodName").html();
    var final = '';
    if (methodName.trim() != "QueryData") {
        if (isNotEmpty(rel_queryType2) && isNotEmpty(IMEI) && isNotEmpty(account) && isNotEmpty(startTimeFormat) &&
            isNotEmpty(endTimeFormat)) {
            if (isNotEmpty(ip1) && isNotEmpty(ip2)) {
                final = "<QueryXml QueryType2= '" + rel_queryType2 + "'Account='" + account + "'\n" +
                    " QueryContent='查询号码" + ip1 + "和号码" + ip2 + ",在时间段" + startTimeFormat + "至" + endTimeFormat + "内,移动GMS业务中出现的中间人\n'" +
                    " BeginTime='" + startTimeFormat + "' EndTime='" + endTimeFormat + "' PhoneNumber1='" + ip1 + "' PhoneNumber2='" + ip2 + "'>\n" +
                    "</QueryXml>";
            } else {

                final = "<QueryXml QueryType2 = '" + rel_queryType2 + "' Account='" + account + "'\n" +
                    "QueryContent='查询IMEI为" + IMEI + ",在时间段" + startTimeFormat + "至" + endTimeFormat + "内,移动GSM业务中所有的通话记录'\n" +
                    "Conditions = 'BeginTime," + startTimeFormat + ";EndTime," + endTimeFormat + ";IMEI" + IMEI + "' >\n" +
                    "</QueryXml>";

            }
            customer.val(final);
        } else {
            return 0;
        }
    } else {
        if (isNotEmpty(queryData) && isNotEmpty(rel_queryType2) && isNotEmpty(IMEI) && isNotEmpty(account) && isNotEmpty(startTimeFormat) &&
            isNotEmpty(endTimeFormat)) {
            if (isNotEmpty(ip1) && isNotEmpty(ip2)) {
                final = "<QueryXml ServiceType='" + queryData + "' QueryType2= '" + rel_queryType2 + "'Account='" + account + "'\n" +
                    " QueryContent='查询号码" + ip1 + "和号码" + ip2 + ",在时间段" + startTimeFormat + "至" + endTimeFormat + "内,移动GMS业务中出现的中间人\n'" +
                    " BeginTime='" + startTimeFormat + "' EndTime='" + endTimeFormat + "' PhoneNumber1='" + ip1 + "' PhoneNumber2='" + ip2 + "'>\n" +
                    "</QueryXml>";
            } else {
                final = "<QueryXml ServiceType='" + queryData + "' QueryType2 = '" + rel_queryType2 + "' Account='" + account + "'\n" +
                    "QueryContent='查询IMEI为" + IMEI + ",在时间段" + startTimeFormat + "至" + endTimeFormat + "内,移动GSM业务中所有的通话记录'\n" +
                    "Conditions = 'BeginTime," + startTimeFormat + ";EndTime," + endTimeFormat + ";IMEI" + IMEI + "' >\n" +
                    "</QueryXml>";

            }
            customer.val(final);
        } else {
            return 0;
        }
    }

}

function isNotEmpty(obj) {
    if (typeof obj == "undefined" || obj == null || obj == "") {
        return false
    } else {
        return true;
    }
};

function ajaxRequest(paramJson, resultId, type) {
    $.ajax({
        type: "post",
        url: "queryData",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(paramJson),
        dataType: "json",
        success: function (message) {
            var resultMsg = resultId.val(JSON.stringify(message));
            if (type == 1) {
                $("#queryStatus").val(message.queryId);
            }
        },
        error: function (message) {
            alert(message);
        }
    });
};

function menuClick() {
    $("#menuId li").click(function () {
        var _a = $(this).find("a").html();

        $("#methodName").html(_a);
        if (_a != "QueryData") {
            $("#queryData").attr("disabled", "true");
        } else {
            $("#queryData").removeAttr("disabled");
        }

        if (_a == "Statistics") {
            $("#sub").removeAttr("disabled");
            $("#queryType2").empty();
            $("#rel_queryType2").empty();
            var html = "<option></option><option>返回值:1,2,3,4</option>";
            $("#queryType2").html(html);
        } else if (_a == "BasicResource") {
            $("#queryType2").empty();
            var html = "<option>返回值:1</option>";
            $("#sub").removeAttr("disabled");
            $("#queryType2").html(html);
            $("#rel_queryType2").empty();
            $("#rel_queryType2").html("<option>1</option>");
        } else if (_a == "QueryData") {
            $("#queryType2").empty();
            $("#rel_queryType2").empty();
            $("#sub").removeAttr("disabled");
            importJsonContent("queryType2");
        }else if (_a == "Alarm") {
            $("#sub").attr("disabled" , "true");
        }
        appendFinal();
    });
};

function initTime() {
    var $timeStart = $("#startTime");
    var $endTime = $("#endTime");
    var loadStartTime = new Date().Format("yyyy-MM-dd HH:mm");
    var loadStartTimeRel = new Date().Format("yyyyMMddHHmmss");
    $timeStart.val(loadStartTime);
    $("#startTimeFormat").val(loadStartTimeRel);
    $endTime.val(loadStartTime);
    $("#endTimeFormat").val(loadStartTimeRel);
};