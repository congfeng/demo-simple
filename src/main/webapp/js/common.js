/**
 * 合法返回true,否则返回false
 */
function validateMobile(mobile) {
	var myreg = /^((1[3|4|5|7|8][0-9])+\d{8})$/;
	return myreg.test(mobile);
}

/**
 * 保留scale位小数
 */
function round(value, scale) {
	scale = isEmpty(scale) ? 2 : scale;
	var num = new Number(value);
	return num.toFixed(scale);
}

/**
 * 格式化为中文货币格式
 */
function fmoney(value) {
	return '￥' + round(value, 2);
}

/**
 * 获取地址栏参数
 */
function getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r != null) {
		// return unescape(r[2]);
		return decodeURIComponent(r[2]);
	}
	return null;
}

/**
 * 判断是否是微信浏览器
 */
function isWeixin() {
	var ua = navigator.userAgent.toLowerCase();
	return (ua.match(/MicroMessenger/i) == "micromessenger");
}

/**
 * 合法返回true,否则返回false
 */
function isMoney(money) {
	var myreg = /^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/;
	return myreg.test(money);
}

/**
 * 判断是否为空
 */
function isEmpty(obj) {
	return obj === "" || obj == null || obj == undefined;
}

/**
 * 判断是否为空
 */
function notEmpty(obj) {
	return !isEmpty(obj);
}

/**
 * 对Date的扩展，将 Date 转化为指定格式的String 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
 * 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 例子： dateFormat("yyyy-MM-dd
 * hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 dateFormat("yyyy-M-d h:m:s.S") ==>
 * 2006-7-2 8:9:4.18
 * 
 * @param fmt
 * @returns
 */
function dateFormat(date, fmt) {
	var o = {
		"M+" : date.getMonth() + 1, // 月份
		"d+" : date.getDate(), // 日
		"h+" : date.getHours(), // 小时
		"m+" : date.getMinutes(), // 分
		"s+" : date.getSeconds(), // 秒
		"q+" : Math.floor((date.getMonth() + 3) / 3), // 季度
		"S" : date.getMilliseconds()
	// 毫秒
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
					: (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}

/**
 * 显示加载层
 */
function showLoading(msg) {
	return layer.open({
		type : 2,
		shadeClose : false,
		content : msg
	});
}

/**
 * 关闭层
 */
function hideLayer(index) {
	if (isEmpty(index)) {
		layer.closeAll();
	} else {
		layer.close(index);
	}
}

/**
 * 自定义 alert
 */
function showAlert(msg) {
	layer.open({
		content : msg,
		btn : [ '确定' ]
	});
}

/**
 * 提示执行
 */
function toIF(msg,fn) {
	layer.open({
		content : msg,
		btn : [ '确定', '取消' ],
		shadeClose : false,
		yes : function() {
			fn();
		},
		no : function() {
			
		}
	});
}

/**
 * 分页
 */
function Pagination(pageClick){
	this.isClear = true;
	this.pageClick = pageClick;
	this.pageUp = $("<li><a href='javascript:void(0)'>上一页</a></li>");
	this.pageDown = $("<li><a href='javascript:void(0)'>下一页</a></li>");
	$(".pagination").append(this.pageUp);
	$(".pagination").append(this.pageDown);
}

Pagination.prototype.refresh = function(pager){
	this.clear();
	var pageClick = this.pageClick;
	if(pager.firstPage){
		this.pageUp.addClass("disabled");
	}else{
		this.pageUp.click(function(){
			pageClick(pager.prePage);
		});
	}
	if(pager.lastPage){
		this.pageDown.addClass("disabled");
	}else{
		this.pageDown.click(function(){
			pageClick(pager.nextPage);
		});
	}
	var pageDown = this.pageDown;
	$.each(pager.pageIndexs,function(i,p){
		if(p == pager.pageNo){
			pageDown.before("<li class='page_no active'><a href='javascript:void(0)'>"+p+"</a></li>");
		}else{
			var pn = $("<li class='page_no'><a href='javascript:void(0)'>"+p+"</a></li>");
			pn.click(function(){
				pageClick(p);
			});
			pageDown.before(pn);
		}
	});
	this.isClear = false;
}

Pagination.prototype.clear = function clear(){
	if(this.isClear){
		return ;
	}
	$(".page_no").remove();
	this.pageUp.removeClass("disabled");
	this.pageDown.removeClass("disabled");
	this.pageUp.unbind('click');
	this.pageDown.unbind('click');
	this.isClear = true;
}
