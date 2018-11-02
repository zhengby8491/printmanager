/**
 * Author:       THINK
 * Create:       2018年02月14日 上午9:38:09
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 */
// 印管家项目JS核心工具包
// FrameWork基本信息
// console.log(navigator.userAgent);
var Helper = {};
Helper.author = "zjt";
Helper.version = "1.0 Beta";
Helper.cdate = "2016-01-01";
Helper.iswindow = /window/i.test(navigator.userAgent);
Helper.isie = /msie|rv/i.test(navigator.userAgent);
Helper.isff = /firefox/i.test(navigator.userAgent);
Helper.isop = /opera/i.test(navigator.userAgent);
Helper.ischrome = /Chrome/i.test(navigator.userAgent);
Helper.ismozilla = /Mozilla/i.test(navigator.userAgent);
Helper.browser = {
	name : Helper.isie ? "IE" : Helper.isff ? "Firefox" : Helper.isop ? "Opear" : Helper.ischrome ? "Chrome" : Helper.ismozilla ? "Mozilla " : "other",
	version : navigator.userAgent.match(/(?:rv|msie|firefox|opera|chrome|mozilla)[:| |\/]([\d\.]+)/i)[1]
};
Helper.isie6 = Helper.isie && Helper.browser.version == "6.0";

// 项目根路径
Helper.basePath = "";

/**==============================================
 *  扩展String
 * ============================================== **/
// 删除两边的空格
String.prototype.trim = function()
{
	return this.replace(/(^\s*)|(\s*$)/g, "");
};
// 删除左边的空格
String.prototype.ltrim = function()
{
	return this.replace(/(^\s*)/g, "");
};
// 删除右边的空格
String.prototype.rtrim = function()
{
	return this.replace(/(\s*$)/g, "");
};
// 删除右边的空格
String.prototype.toBoolean = function()
{
	if (this.trim() == 'true')
	{
		return true;
	} else
	{
		return false;
	}
};
// 从头检测
String.prototype.startWith = function(str)
{
	var reg = new RegExp("^" + str);
	return reg.test(this);
};
// 从尾检测
String.prototype.endWith = function(str)
{
	var reg = new RegExp(str + "$");
	return reg.test(this);
};

/**==============================================
 *  扩展Date
 * ============================================== **/
// 格式化时间功能,例：yyyy-MM-dd hh:mm:ss:SSS
Date.prototype.format = function(format)
{
	if (!format)
	{
		format = "yyyy-MM-dd hh:mm:ss";
	}
	var _oDatePre = {
		"y+" : this.getFullYear(),// Year
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"S+" : this.getMilliseconds()
	// millisecond
	};
	for ( var _s in _oDatePre)
	{
		if (new RegExp("(" + _s + ")").test(format))
		{
			var _sValue = _oDatePre[_s];
			var _sLength = RegExp.$1.length;
			var _sFormatValue = "";
			if (_sValue.toString().length < _sLength)
			{
				for (var i = 0; i < _sLength - _sValue.toString().length; i += 1)
				{
					_sFormatValue += "0";
				}
			}
			_sFormatValue += _sValue.toString();
			format = format.replace(RegExp.$1, _sFormatValue);
		}
	}
	return format;
};
// 日期增加函数
Date.prototype.add = function(type, num)
{
	switch (type)
	{
	case "h":
		return new Date(Date.parse(this) + (3600000 * num));
	case "d":
		return new Date(Date.parse(this) + (86400000 * (num + 1)));
	case "w":
		return new Date(Date.parse(this) + ((86400000 * 7) * num) + 86400000);
	case "m":
		return new Date(this.getFullYear(), (this.getMonth()) + num, this.getDate(), this.getHours(), this.getMinutes(), this.getSeconds());
	case "y":
		return new Date((this.getFullYear() + num), this.getMonth(), this.getDate(), this.getHours(), this.getMinutes(), this.getSeconds());
	}
};

/**==============================================
 *  扩展Number
 * ============================================== **/
Number.prototype.add = function(arg)
{
	return Helper.math.add(this, arg);
};
Number.prototype.subtr = function(arg)
{
	return Helper.math.subtr(this, arg);
};
Number.prototype.mul = function(arg)
{
	return Helper.math.mul(this, arg);
};
Number.prototype.div = function(arg)
{
	return Helper.math.div(this, arg);
};
Number.prototype.trunc = function()
{
	return Helper.math.trunc(this);
};
Number.prototype.ceil = function()
{
	return Helper.math.ceil(this);
};
Number.prototype.floor = function()
{
	return Helper.math.floor(this);
};
Number.prototype.round = function()
{
	return Helper.math.round(this);
};
Number.prototype.tomoney = function()
{
	return Helper.math.money(this);
};
Number.prototype.roundFixed = function(arg)
{
	return Helper.math.roundFixed(this, arg);
};

/**==============================================
 *  扩展Array
 * ============================================== **/
// 查找元素在数组中的位置
Array.prototype.indexOf = function(e)
{
	for (var i = 0, j; j = this[i]; i++)
	{
		if (j == e)
		{
			return i;
		}
	}
	return -1;
};
// 扩展数组对象，查询元素是否存在
Array.prototype.contains = function(oObj)
{
	var _THIS = this;
	for (var i = 0; i < _THIS.length; i += 1)
	{
		if (_THIS[i] === oObj)
		{
			return true;
		}
	}
	return false;
};
Array.prototype.containCount = function(oObj)
{
	var _THIS = this;
	var count = 0;
	for (var i = 0; i < _THIS.length; i += 1)
	{
		if (_THIS[i] === oObj)
		{
			count += 1;
		}
	}
	return count;
};
Array.prototype.copy = function()
{
	var arrs = [];
	for (var i = 0; i < this.length; i += 1)
	{
		arrs.push(this[i]);
	}
	return arrs;
};
Array.prototype.sum = function()
{
	var count = Number(0);
	for (var i = 0; i < this.length; i++)
	{
		count = count.add(Number(this[i]));
	}
	return count;
};
/*
 * 方法:Array.removeByValue(value) 功能:删除数组中第一个值与value相同的元素. 参数:value元素的值.
 * 返回:在原数组上修改数组.
 */
Array.prototype.removeByValue = function(value)
{
	for (var i = 0; i < this.length; i++)
	{
		if (this[i] == value)
		{
			this.remove(i);
			return true;
		}
	}
	return true;
};
/*
 * 方法:Array.baoremove(dx) 功能:删除数组元素. 参数:dx为元素的下标. 返回:在原数组上修改数组.
 */
Array.prototype.remove = function(dx)
{
	if (isNaN(dx) || dx > this.length)
	{
		return false;
	}
	this.splice(dx, 1);
	return true;
};
// 一维数组的排序
// type 参数
// 0 字母顺序（默认）
// 1 大小 比较适合数字数组排序
// 2 拼音 适合中文数组
// 3 乱序 有些时候要故意打乱顺序，呵呵
// 4 带搜索 str 为要搜索的字符串 匹配的元素排在前面
Array.prototype.sortBy = function(type, str)
{
	switch (type)
	{
	case 0:
		this.sort();
		break;
	case 1:
		this.sort(function(a, b)
		{
			return a - b;
		});
		break;
	case 2:
		this.sort(function(a, b)
		{
			return a.localeCompare(b)
		});
		break;
	case 3:
		this.sort(function()
		{
			return Math.random() > 0.5 ? -1 : 1;
		});
		break;
	case 4:
		this.sort(function(a, b)
		{
			return a.indexOf(str) == -1 ? 1 : -1;
		});
		break;
	default:
		this.sort();
	}
};
// 数字操作
Helper.math = {

	// 加法函数,arg1加上arg2的精确结果
	add : function(arg1, arg2)
	{
		var r1, r2, m;
		try
		{
			r1 = arg1.toString().split(".")[1].length
		} catch (e)
		{
			r1 = 0
		}
		try
		{
			r2 = arg2.toString().split(".")[1].length
		} catch (e)
		{
			r2 = 0
		}
		m = Math.pow(10, Math.max(r1, r2))
		return (arg1 * m + arg2 * m) / m
	},
	// 减法函数，用来得到精确的减法结果
	subtr : function(arg1, arg2)
	{
		var r1, r2, m, n;
		try
		{
			r1 = arg1.toString().split(".")[1].length
		} catch (e)
		{
			r1 = 0
		}
		try
		{
			r2 = arg2.toString().split(".")[1].length
		} catch (e)
		{
			r2 = 0
		}
		m = Math.pow(10, Math.max(r1, r2));
		// last modify by deeka
		// 动态控制精度长度
		n = (r1 >= r2) ? r1 : r2;
		return Number(((arg1 * m - arg2 * m) / m).toFixed(n));
	},
	// 乘法函数，用来得到精确的乘法结果
	// 说明：javascript的乘法结果会有误差，在两个浮点数相乘的时候会比较明显。这个函数返回较为精确的乘法结果。
	// 调用：accMul(arg1,arg2)
	// 返回值：arg1乘以arg2的精确结果
	mul : function(arg1, arg2)
	{
		var m = 0, s1 = arg1.toString(), s2 = arg2.toString();
		try
		{
			m += s1.split(".")[1].length
		} catch (e)
		{
		}
		try
		{
			m += s2.split(".")[1].length
		} catch (e)
		{
		}
		return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m)
	},
	// 除法函数，用来得到精确的除法结果
	// 说明：javascript的除法结果会有误差，在两个浮点数相除的时候会比较明显。这个函数返回较为精确的除法结果。
	// 调用：accDiv(arg1,arg2)
	// 返回值：arg1除以arg2的精确结果
	div : function(arg1, arg2)
	{
		var t1 = 0, t2 = 0, r1, r2;
		try
		{
			t1 = arg1.toString().split(".")[1].length
		} catch (e)
		{
		}
		try
		{
			t2 = arg2.toString().split(".")[1].length
		} catch (e)
		{
		}
		with (Math)
		{
			r1 = Number(arg1.toString().replace(".", ""))
			r2 = Number(arg2.toString().replace(".", ""))
			return (r1 / r2) * pow(10, t2 - t1);
		}
	},
	// 丢弃小数部分
	trunc : function(arg)
	{
		return parseInt(arg);

	},
	// 向上取整
	ceil : function(arg)
	{
		return Math.ceil(arg);
	},
	// 向下取整
	floor : function(arg)
	{
		return Math.floor(arg);
	},
	// 四舍五入
	round : function(arg)
	{
		return Math.round(arg);
	},
	// 转换成money
	money : function(arg)
	{
		if (arg.toString().indexOf(".") > 0)
		{
			return Number(Number(arg).toFixed(2));
		} else
		{
			return Number(arg);
		}
	},
	// 优化toFixed，直接使用toFixed会有部分小数四舍五入错误
	// arg1=当前数值 ，arg2=保留小数位
	roundFixed : function(arg1, arg2)
	{
		if (arg2 == null)
		{
			arg2 = 0;
		}
		return ((Number(arg1) * Math.pow(10, arg2)).round() / Math.pow(10, arg2)).toFixed(arg2);
	}
}
// 转换操作
Helper.Conver = {

};
// 继承机制(该继承方式只会继承父类中的原型，不会继承父类自己本身的属性和方法)
Helper.inherits = function(fSubclass, fSuperclass)
{
	var F = function()
	{
	};
	F.prototype = fSuperclass.prototype;
	fSubclass.prototype = new F();
	fSubclass.prototype.constructor = fSuperclass;// 子类中保存父类的信息,主要用于子类原型重写父类原型方法后,可以调用到父类原型方法
	fSubclass.prototype.selfConstructor = fSubclass;// 保存自己类信息
};

Helper.Index = function()
{
	var index = 1000;
	this.getIndex = function()
	{
		return index += 1;
	};
};

Helper.Map = function()
{
	this.obj = {};
	this.prefix = "imap_key_prefix_";
	this.length = 0;
};
Helper.Map.prototype.put = function(key, value)
{
	if (!this.hasKey(key))
	{
		this.length += 1;
	}
	this.obj[this.prefix + key] = value;
};
Helper.Map.prototype.get = function(key)
{
	return this.obj[this.prefix + key];
};
Helper.Map.prototype.remove = function(key)
{
	if (this.hasKey(key))
	{
		var _oValue = this.get(key);
		delete this.obj[this.prefix + key];
		this.length -= 1;
		return _oValue;
	}
};
Helper.Map.prototype.clear = function()
{
	for ( var fo in this.obj)
	{
		if (this.obj.hasOwnProperty(fo))
		{
			delete this.obj[fo];
		}
	}
	this.length = 0;
};
Helper.Map.prototype.hasKey = function(key)
{
	var flag = this.obj.hasOwnProperty(this.prefix + key);
	return flag;
};
Helper.Map.prototype.size = function()
{
	return this.length;
};
Helper.Map.prototype.values = function()
{
	var _aArrays = [];
	for ( var fo in this.obj)
	{
		if (this.obj.hasOwnProperty(fo) && fo.substring(0, this.prefix.length).toLowerCase() === this.prefix)
		{
			_aArrays.push(this.obj[fo]);
		}
	}
	return _aArrays;
};
Helper.Map.prototype.keys = function()
{
	var _aArrays = [];
	for ( var fo in this.obj)
	{
		if (this.obj.hasOwnProperty(fo) && fo.substring(0, this.prefix.length).toLowerCase() === this.prefix)
		{
			_aArrays.push(fo.substr(this.prefix.length));
		}
	}
	return _aArrays;
};

Helper.Timmer = function(p)
{
	this.milli = p.milli;// 执行间隔,毫秒数
	this.count = p.count || -1;// -1表示无限执行
	this.call = p.call;// 执行的函数
	this.timmer = null;
	this.doCount = 0;// 已经执行的次数
	this.isRun = false;// 是否正在执行中
	var THIS = this;
	this.start = function()
	{
		if (!THIS.isRun && THIS.doCount === 0)
		{
			THIS.isRun = true;
			THIS.timmer = setInterval(function()
			{
				if (THIS.count > 0 && THIS.doCount >= THIS.count)
				{
					THIS.stop();
					return;
				} else
				{
					p.call();
					THIS.doCount += 1;
				}
			}, THIS.milli);
		}
	};
	this.stop = function()
	{
		if (THIS.timmer)
		{
			clearInterval(THIS.timmer);
		}
		THIS.doCount = 0;
		THIS.isRun = false;
		THIS.timmer = null;
	};
};

/**
 * 缓存类，该类仅供页面查询缓存使用，可以设置缓存的过期时间
 */
Helper.BaseCache = function(p)
{
	p = p || {};
	this.maxCacheTimeMM = p.maxCacheTimeMM || -1;// 页面缓存最大毫秒数,小于0则缓存永远不过期
	this.cacheMap = new Helper.Map();
};
Helper.BaseCache.prototype.addCache = function(key, data)
{
	var obj = {
		value : data
	};
	if (this.maxCacheTimeMM > 0)
	{
		obj.timeMM = new Date().getTime();
	}
	this.cacheMap.put(key, obj);
};
Helper.BaseCache.prototype.removeCache = function(key)
{
	this.cacheMap.remove(key);
};
Helper.BaseCache.prototype.clearCache = function()
{
	this.cacheMap.clear();
};
Helper.BaseCache.prototype.getCache = function(key)
{
	var data = this.cacheMap.get(key);
	if (!data)
		return undefined;
	if (this.maxCacheTimeMM <= 0)
	{
		return data.value;
	} else
	{
		if (new Date().getTime() <= (data.timeMM + this.maxCacheTimeMM))
		{
			return data.value;
		} else
		{
			this.removeCache(key);
			return undefined;
		}
	}
};

// 公用消息显示方法
Helper.message = {
	open : function(o)
	{
		layer.open(o);
	},
	tips : function(msg, follow, options)
	{
		layer.tips(msg, follow, options || ({
			tips : [ 1, '#0FA6D8' ]
		// 还可配置颜色
		}))
	},
	alert : function(msg, fun)
	{
		layer.msg(msg, fun);
	},
	suc : function(msg)
	{
		layer.msg(msg, {
			icon : 1
		});
	},
	err : function(msg)
	{
		layer.msg(msg, {
			icon : 2
		});
	},
	ask : function(msg)
	{
		layer.msg(msg, {
			icon : 3
		});
	},
	lock : function(msg)
	{
		layer.msg(msg, {
			icon : 4
		});
	},
	warn : function(msg)
	{
		layer.msg(msg, {
			icon : 5
		});
	},
	fail : function(msg)
	{
		layer.msg(msg, {
			icon : 6
		});
	},
	confirm : function(text, yes, cancel)
	{
		layer.confirm(text, {
			icon : 3
		}, yes, cancel);
	},
	prompt : function(title, value, fun)
	{
		layer.prompt({
			title : title,
			value : value,
			formType : 2,
			yes : fun
		});
	},
	view : function(msg)
	{
		layer.alert(msg);
	}
}

Helper.popup = {
	/* 弹出层 */
	/*
	 * 参数解释： title 标题 url 请求的url id 需要操作的数据id w 弹出层宽度（缺省调默认值） h 弹出层高度（缺省调默认值）
	 */
	show : function(title, url, w, h, full)
	{
		if (title == null || title == '')
		{
			title = false;
		}
		;
		if (url == null || url == '')
		{
			url = "404.html";
		}
		;
		if (w == null || w == '')
		{
			w = 800;
		}
		;
		if (h == null || h == '')
		{
			h = ($(window).height() - 50);
		}
		;
		var o = layer.open({
			type : 2,
			area : [ w + 'px', h + 'px' ],
			fix : false, // 不固定
			maxmin : true,
			shade : [ 0.4, "#CECFD8" ],
			title : title,
			content : url
		});
		if (full)
		{
			layer.full(o);
		}
	},
	/* 关闭弹出框口 */
	close : function()
	{
		var index = parent.layer.getFrameIndex(window.name);
		parent.layer.close(index);
	}
}

/**
 * 基础ajax访问类，该类用于页面基础ajax，统一错误处理功能，FROM提交功能
 */
Helper.Remote = {
	// from 增加参数提交
	fromSubmit : function(form, params)
	{// form 表单ID. params ajax参数
		var pp = {
			error : function(XMLHttpRequest, textStatus, errorThrown)
			{
				layer.open({
					type : 1,
					title : "出错啦！",
					area : [ '95%', '95%' ],
					content : "<div id='layerError' style='color:red'>" + XMLHttpRequest.responseText + "</div>"
				});
			}
		};
		$.extend(pp, params);
		$(form).ajaxSubmit(pp);
	},
	// 简单POST
	post : function(url)
	{
		var async = false;
		var params = null;
		var success = null;
		var error = null;
		var len = arguments.length;
		if (len == 5)
		{// url,params,success,error,async
			params = arguments[1];
			success = arguments[2];
			error = arguments[3];
			async = arguments[4];
		} else if (len == 4)
		{// url,params,success,error
			params = arguments[1];
			success = arguments[2];
			error = arguments[3];
		} else if (len == 3)
		{
			if (typeof (arguments[1]) === "function")
			{// url,success,error
				success = arguments[1];
				error = arguments[2];
			} else
			{// url,params,success
				params = arguments[1];
				success = arguments[2];
			}
		} else if (len == 2)
		{
			if (typeof (arguments[1]) === "function")
			{// url,success
				success = arguments[1];
			} else
			{// url,params
				params = arguments[1];
			}
		}
		$.ajax({
			type : "post",
			url : url,
			data : params,
			async : async,// 取消异步
			dataType : "json",
			beforeSend : function()
			{
				layer.load(1);
			},
			complete : function()
			{
				layer.closeAll('loading');
			},
			success : success,
			error : error
		});
	},
	getJson : function(url, params)
	{
		var result = null;
		$.ajax({
			type : "get",
			url : url,
			data : params,
			async : false,// 取消异步
			dataType : "json",
			success : function(data)
			{
				result = data;
			}
		});
		return result;
	},
	request : function(obj)
	{
		$.ajax({
			type : obj.method || "POST",
			url : obj.url,
			data : JSON.stringify(obj.data || {}),// 将form序列化成JSON字符串
			dataType : obj.dataType || "json",
			contentType : obj.contentType || 'application/json;charset=utf-8', // 设置请求头信息
			async : obj.async || true,// 默认异步请求
			success : obj.success,
			error : obj.error || (function(data)
			{
				Helper.message.warn("请求错误")
			}),
			beforeSend : function()
			{
				layer.load(1);
			},
			complete : function()
			{
				layer.closeAll('loading');
			}
		});
	}
};

// 公用工具方法
Helper.tools = {
	/* 生成一个min到max的随机整数 */
	getRandomNum : function(min, max)
	{
		return Math.round((max - min) * Math.random() + min);
	},

	combine : function(result, data, list, count, low)
	{
		if (count == 0)
		{
			result.push(list.copy());
		} else
		{
			for (var i = low; i < data.length; i += 1)
			{
				list.push(data[i]);
				Helper.tools.combine(result, data, list, count - 1, i + 1);
				list.pop();
			}
		}
	},
	/**
	 * method 获得文本框内的字符长度，1中文字符等于2长度
	 */
	getCharCount : function(str)
	{
		var sum = 0;
		for (var i = 0, len = str.length; i < len; i++)
		{
			if ((str.charCodeAt(i) >= 0) && (str.charCodeAt(i) <= 255))
			{
				sum = sum + 1;
			} else
			{
				sum = sum + 2;
			}
		}
		return sum;
	},
	getFormatMoney : function(num, n)
	{
		if (num && n)
		{
			num = parseFloat(num);
			num = String(num.toFixed(n));
			var re = /(-?\d+)(\d{3})/;
			while (re.test(num))
			{
				num = num.replace(re, "$1,$2");
			}
			return num;
		} else
		{
			return "0.00";
		}
	},
	// [1,2,2] 去掉重复[1,2]
	getUnsameNumber : function(codes)
	{
		var numbers = [];
		for (var i = 0; i < codes.length; i += 1)
		{
			if (!numbers.contains(codes[i]))
			{
				numbers.push(codes[i]);
			}
		}
		return numbers;
	},
	// 一位数组转化为二维数组
	changeArray : function(arr)
	{
		var targetArr = new Array();
		for (var i = 0; i < arr.length; i++)
		{
			targetArr[i] = new Array();
			targetArr[i][0] = arr[i];
		}
		return targetArr;
	},
	// 把1位的数字号码转换为2位
	convert : function(nCode)
	{
		if (nCode.toString().length === 1)
		{
			return "0" + nCode.toString();
		} else
		{
			return nCode.toString();
		}
	},
	// 冒泡排序
	sort : function(aArray)
	{
		if (!aArray)
			return;
		for (var i = 0; i <= aArray.length; i++)
		{
			for (var j = 0; j < aArray.length - i - 1; j += 1)
			{
				if (Helper.doms.parseInt(aArray[j], 10) > Helper.doms.parseInt(aArray[j + 1], 10))
				{
					var _temp = aArray[j];
					aArray[j] = aArray[j + 1];
					aArray[j + 1] = _temp;
				}
			}
		}
	},

	/**
	 * html标签转义
	 */
	htmlspecialchars : function(str)
	{
		var s = "";
		if (str.length == 0)
			return "";
		for (var i = 0; i < str.length; i++)
		{
			switch (str.substr(i, 1))
			{
			case "<":
				s += "&lt;";
				break;
			case ">":
				s += "&gt;";
				break;
			case "&":
				s += "&amp;";
				break;
			case " ":
				if (str.substr(i + 1, 1) == " ")
				{
					s += " &nbsp;";
					i++;
				} else
					s += " ";
				break;
			case "\"":
				s += "&quot;";
				break;
			case "\n":
				s += "";
				break;
			default:
				s += str.substr(i, 1);
				break;
			}
		}
	}

};

// dom操作
Helper.doms = {
	id : function(id, target)
	{
		if (id)
		{
			target = target || "self";
			return window[target].document.getElementById(id);
		} else
		{
			return undefined;
		}
	},
	getValueInt : function(id)
	{
		var value = Helper.id(id).value.trim();
		if (value === "")
		{
			return 0;
		} else
		{
			return Helper.doms.parseInt(value);
		}
	},
	getHtmlInt : function(id)
	{
		var value = $("#" + id).text().trim();
		if (value === "")
		{
			return 0;
		} else
		{
			return Helper.doms.parseInt(value);
		}
	},
	paramsHtml : function(item)
	{
		var _sValue = location.search.match(new RegExp("[\?\&]" + item + "=([^\&]*)(\&?)", "i"));
		return _sValue ? _sValue[1] : _sValue;
	},
	numberKeyupEmpty : function(dom, success)
	{
		dom.onkeyup = function()
		{
			if (dom.value !== "")
			{
				dom.value = this.value.replace(/[^\d]/g, ""); // 清除“数字”以外的字符
			}
			if (typeof (success) === 'function')
			{
				success(dom);
			}
		};
		dom.onblur = function()
		{
			if (typeof (success) === 'function')
			{
				success(dom);
			}
		};
	},
	numberKeyup : function(dom, success)
	{
		dom.onkeyup = function()
		{
			if (dom.value !== "")
			{
				dom.value = this.value.replace(/[^\d]/g, ""); // 清除“数字”以外的字符
				if (this.value === "0")
				{
					this.value = 1;
				} else if (this.value.trim() === "")
				{
					this.value = 1;
				}
				if (typeof (success) === 'function')
				{
					success(dom);
				}
			}
		};
		dom.onblur = function()
		{
			if (dom.value === "")
			{
				dom.value = 1;
				if (typeof (success) === 'function')
				{
					success(dom);
				}
			}
		};
	},
	numberKeyupMinMax : function(dom, min, max, success)
	{
		dom.onkeyup = function()
		{
			if (dom.value !== "")
			{
				dom.value = this.value.replace(/[^\d]/g, ""); // 清除“数字”以外的字符
				var _nValue = Helper.doms.parseInt(this.value, 10);
				if (_nValue < min)
				{
					// this.value = min;
				} else if (_nValue > max)
				{
					this.value = max;
				}
				if (typeof (success) === 'function')
				{
					success(dom);
				}
			}
		};
		dom.onblur = function()
		{
			if (dom.value === "")
			{
				dom.value = min;
				if (typeof (success) === 'function')
				{
					success(dom);
				}
			} else
			{
				var _nValue = Helper.doms.parseInt(this.value, 10);
				if (_nValue < min)
				{
					dom.value = min;
					if (typeof (success) === 'function')
					{
						success(dom);
					}
				}
			}
		};
	},
	enterSub : function(domId, success)
	{
		Helper.id(domId).onkeydown = function(event)
		{
			event = (event == null) ? window.event : event;
			if (event.keyCode == 13)
			{
				success();
			}
		};
	},
	parseInt : function(value, radio)
	{
		if (value.toString().trim() === "")
		{
			return 0;
		} else
		{
			return parseInt(value, radio ? radio : 10);
		}
	},
	/* textarea 字数限制 */
	textarealength : function(obj, maxlength)
	{
		var v = $(obj).val();
		var l = v.length;
		if (l > maxlength)
		{
			v = v.substring(0, maxlength);
			$(obj).val(v);
		}
		$(obj).parent().find(".textarea-numberbar").text(v.length + "/" + maxlength);
	}
};

Helper.select = {

	/**
	 * 移动两个select中的元素
	 * 
	 * @param selectBoxId
	 *          减少元素的控件
	 * @param resultBoxId
	 *          添加元素的控件
	 */
	moveSelectItem : function(selectBoxId, resultBoxId)
	{
		/* 权限列表box */
		var __selectBox = Helper.id(selectBoxId);
		/* 结果列表box */
		var __resultBox = Helper.id(resultBoxId);
		if (__selectBox.selectedIndex == -1)
		{
			alert("请选择您要操作的数据！");
			return;
		}
		var __option_items_index = new Array();
		for (var i = 0; i < __selectBox.options.length; i++)
		{
			if (__selectBox.options[i].selected)
			{
				__option_items_index.push(i);
			}
		}
		/* 必须从数组末端往前端删除，因为删除select的一个选项时，它内部的索引会改变 */
		for (var i = __option_items_index.length - 1; i >= 0; i--)
		{
			var __item = __selectBox.options[__option_items_index[i]];
			Helper.select.addItems(__resultBox, __item.value, __item.innerHTML);
			Helper.select.removeItems(__selectBox, __option_items_index[i]);
		}
	},

	/**
	 * select控件添加选项
	 * 
	 * @param currentBox
	 *          当前控件对象
	 * @param value
	 *          控件值
	 * @param label
	 *          控件显示值
	 */
	addItems : function(currentBox, value, label, color)
	{
		var __option = document.createElement("option");
		__option.value = value;
		__option.innerHTML = label;
		if (color)
		{
			__option.style.color = color;
		}
		currentBox.appendChild(__option);
	},

	/**
	 * 删除select选项
	 * 
	 * @param currentBox
	 *          当前select控件
	 * @param index
	 *          删除的索引
	 */
	removeItems : function(currentBox, index)
	{
		currentBox.remove(index);
	},

	/**
	 * 删除selectbox所有option
	 * 
	 * @param currentBox
	 *          select控件对象
	 */
	removeAllItems : function(currentBox)
	{
		if (currentBox.options <= 0)
			return;
		for (var i = currentBox.options.length - 1; i >= 0; i--)
		{
			currentBox.remove(i);
		}
	}

};

// 字符校验
Helper.validata = {
	// 判断是否为null
	isNull : function(value)
	{
		if (value === undefined || value === null)
		{
			return true;
		}
		return false;
	},
	isNotNull : function(value)
	{
		return !Helper.isNull(value);
	},
	isEmpty : function(value)
	{
		if (Helper.isNull(value))
		{
			return true;
		} else if (typeof (value) == "string")
		{
			if (value.trim() === "" || value === "undefined" || value === "[]" || value === "{}")
			{
				return true;
			}
		} else if (typeof (value) === "object")
		{
			return value.length <= 0 ? true : false;
		} else
		{
			return false;
		}
	},
	isNotEmpty : function(value)
	{
		return !Helper.isEmpty(value);
	},
	getByteLength : function(text)
	{
		if (Helper.isNull(text))
		{
			return 0;
		}
		return text.replace(/[^\u0000-\u00ff]/g, "aa").length;
	},
	// 验证中文
	isChinese : function(obj)
	{
		r = /[^\u4E00-\u9FA5]/g;
		return !r.test(obj);
	},
	isUsername : function(obj)
	{
		r = /^[A-Za-z0-9\u4E00-\u9FA5]+$/;
		return r.test(obj);
	},
	// --身份证号码验证-支持新的带x身份证
	isIdCardNo : function(pId)
	{
		if (pId.length != 15 && pId.length != 18)
			return false;
		var Ai = pId.length == 18 ? pId.substring(0, 17) : pId.slice(0, 6) + "19" + pId.slice(6, 16);
		if (!/^\d+$/.test(Ai))
			return false;
		var yyyy = Ai.slice(6, 10), mm = Ai.slice(10, 12) - 1, dd = Ai.slice(12, 14);
		var d = new Date(yyyy, mm, dd), year = d.getFullYear(), mon = d.getMonth(), day = d.getDate(), now = new Date();
		if (year != yyyy || mon != mm || day != dd || d > now || !Helper.validata.isValidData(dd, mm, yyyy))
			return false;
		return Helper.validata.isIdcardCheckno(pId);
	},
	// 身份证最后一位验证码验证
	isIdcardCheckno : function(idNo)
	{
		if (idNo.length == 15)
			return true;
		else
		{
			var a = [];
			for (var i = 0; i < idNo.length - 1; i++)
			{
				a[a.length] = idNo.substring(i, i + 1);
			}
			var w = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ]; //
			var sum = 0; // 和
			var model = 0; // 模
			var result; // 结果
			var map = [ 1 ]; // 映射
			// 对应替换
			map[0] = 1;
			map[1] = 0;
			map[2] = 'X';
			map[3] = 9;
			map[4] = 8;
			map[5] = 7;
			map[6] = 6;
			map[7] = 5;
			map[8] = 4;
			map[9] = 3;
			map[10] = 2;
			// 求和
			for (var i = 0; i < a.length; i++)
			{
				sum += w[i] * a[i];
			}
			// 取模
			model = sum % 11;
			// 对应替换
			result = map[model];
			if (idNo.substring(17, 18) != result)
				return false;
			else
				return true;
		}
	},
	isValidDate : function(day, month, year)
	{
		if (month == 1)
		{
			var leap = (year % 4 == 0 || (year % 100 == 0 && year % 400 == 0));
			if (day > 29 || (day == 29 && !leap))
			{
				return false;
			}
		}
		return true;
	},

	/**
	 * 判断是否年满18
	 */
	suiTableAge : function(pId)
	{
		var Ai = pId.length == 18 ? pId.substring(0, 17) : pId.slice(0, 6) + "19" + pId.slice(6, 16);
		var yyyy = Ai.slice(6, 10), mm = Ai.slice(10, 12) - 1, dd = Ai.slice(12, 14);
		var time = new Date();

		// 满足18岁的最低日期
		time.setFullYear(time.getFullYear() - 18);
		var now_ms = time.getTime();

		// 身份证日期
		time.setFullYear(yyyy, mm, dd);
		var idcard_ms = time.getTime();

		return (now_ms >= idcard_ms);
	},

	isMobile : function(m)
	{
		var pattern = /^1[0-9]{10}$/;
		return pattern.test(m);
	},

	isEmail : function(e)
	{
		var pattern = /^[_a-zA-Z0-9\-]+(\.[_a-zA-Z0-9\-]*)*@[a-zA-Z0-9\-]+([\.][a-zA-Z0-9\-]+)+$/;
		return pattern.test(e);
	},

	isInteger : function(s)
	{
		var exp = new RegExp(/^\d+$/);
		return exp.test(s);
	},

	isMoney : function(s)
	{
		var exp = new RegExp(/^\d+(.\d{2})?$/);
		return exp.test(s);
	},

	isTelephone : function(str)
	{
		var reg = /^([0-9]|[\-])+$/g;
		if (str.length < 7 || str.length > 18)
		{
			return false;
		} else
		{
			return reg.test(str);
		}
	},
	/**
	 * 
	 * 验证最大尺寸（xxxx*xxxx）
	 * @since 1.0, 2017年10月19日 下午6:52:15, think
	 */
	isSize : function(s)
	{
		var exp = new RegExp(/^[1-9][0-9]{0,5}\*[1-9][0-9]{0,5}$/);
		return exp.test(s)
	},

	/**
	 * 验证最小尺寸（可以填写为 0*0）
	 * @since 1.0, 2017年11月30日 上午9:23:44, zhengby
	 */
	isSizeMin : function(s)
	{
		var exp = new RegExp(/^[0-9]{0,5}\*[0-9]{0,5}$/);
		return exp.test(s)
	},

	/**
	 * 
	 * 验证材料尺寸（xxxx*xxxx）
	 * 材料尺寸支持 xxx,xxx*xxx,xxx*xxx*xxx
	 * @since 1.0, 2017年10月19日 下午6:52:15, think
	 */
	isMaterialSize : function(s)
	{
		var exp1 = new RegExp(/^[0-9]+([.]{1}[0-9]{1,4})?$/);
		var exp2 = new RegExp(/^[0-9]+([.]{1}[0-9]{1,4})?\*[0-9]+([.]{1}[0-9]{1,4})?$/);
		var exp3 = new RegExp(/^[0-9]+([.]{1}[0-9]{1,4})?\*[0-9]+([.]{1}[0-9]{1,4})?\*[0-9]+([.]{1}[0-9]{1,4})?$/);
		var result1 = exp1.test(s);
		var result2 = exp2.test(s);
		var result3 = exp3.test(s);

		return (result1 || result2 || result3);
	},

	validataUname : function(username)
	{
		var _sErrorMessage = undefined;
		var leng = Helper.validata.getByteLength(username);
		if (Helper.isEmpty(username))
		{
			_sErrorMessage = "用户名不能为空";
		} else if (!Helper.validata.isUsername(username))
		{
			_sErrorMessage = "用户名含有非法字符";
		} else if (leng < 3 || leng > 16)
		{
			_sErrorMessage = "用户名字符长度应为3-16之间";
		}
		return _sErrorMessage;
	},
	validataPass : function(password)
	{
		var _sErrorMessage = undefined;
		var leng = Helper.validata.getByteLength(password);
		if (password === "")
		{
			_sErrorMessage = "密码不能为空";
		} else if (leng < 6 || leng > 16)
		{
			_sErrorMessage = "密码字符长度应为6-16之间";
		}
		return _sErrorMessage;
	},
	validataEmail : function(email)
	{
		var _sErrorMessage = undefined;
		if (email === "")
		{
			_sErrorMessage = "邮箱地址不能为空";
		} else if (!Helper.validata.isEmail(email))
		{
			_sErrorMessage = "邮箱格式不正确";
		} else if (email.length > 50)
		{
			_sErrorMessage = "邮箱地址过长";
		}
		return _sErrorMessage;
	},
	validataMobile : function(mobile)
	{
		var _sErrorMessage = undefined;
		if (mobile === "")
		{
			_sErrorMessage = "手机号码不能为空";
		} else if (!Helper.validata.isMobile(mobile))
		{
			_sErrorMessage = "手机号码格式不正确";
		}
		return _sErrorMessage;
	}
};

/**
 * 验证字段
 */
Helper.validfield = {
	/**
	 * 
	 * 表单验证字段并提示信息
	 * @param $field
	 * @param message
	 * @returns {Boolean}
	 * @since 1.0, 2017年10月26日 下午3:19:55, think
	 */
	validateFieldText : function($field, message)
	{
		if (Helper.isEmpty($field.val()))
		{
			Helper.message.tips(message, $field);
			$field.focus();
			return false;
		}
		return true;
	},
	/**
	 * 
	 * 表单验证整型字段并提示信息
	 * @param $field
	 * @param message
	 * @returns {Boolean}
	 * @since 1.0, 2017年10月26日 下午3:19:55, think
	 */
	validateFieldIntegerText : function($field, message)
	{
		if (!Helper.validata.isInteger($field.val()))
		{
			Helper.message.tips(message, $field);
			$field.focus();
			return false;
		}
		return true;
	},
	/**
	 * 
	 * 表单验证xxx*xxx字段并提示信息
	 * @param $field
	 * @param message
	 * @returns {Boolean}
	 * @since 1.0, 2017年10月26日 下午3:19:55, think
	 */
	validateFieldSizeText : function($field, message)
	{
		if (!Helper.validata.isSize($field.val()))
		{
			Helper.message.tips(message, $field);
			$field.focus();
			return false;
		}
		return true;
	},

	/**
	 * 
	 * 表单验证字段并提示信息
	 * @param $field
	 * @param eqmsg
	 * @param message
	 * @returns {Boolean}
	 * @since 1.0, 2017年10月26日 下午3:19:55, think
	 */
	validateFieldEqText : function($field, eqmsg, message)
	{
		if (eqmsg == $field.val())
		{
			Helper.message.tips(message, $field);
			$field.focus();
			return false;
		}
		return true;
	},

	/**
	 * 
	 * 表单验证xxx*xxx字段并提示信息
	 * @param $field
	 * @param message
	 * @returns {Boolean}
	 * @since 1.0, 2017年10月26日 下午3:19:55, think
	 */
	validateFieldMaterialSizeText : function($field, message)
	{
		if (!Helper.validata.isMaterialSize($field.val()))
		{
			Helper.message.tips(message, $field);
			$field.focus();
			return false;
		}
		return true;
	}
};

// 系统基础类
Helper.basic = {
	infoArray : function(type, field, values)
	{
		var result = Helper.cache.getCache("infoArray_" + type + "_" + field + "_" + values);
		if (!result)
		{
			$.ajax({
				type : "get",
				url : Helper.basePath + "/basicInfoList",
				data : {
					'type' : type,
					'field' : field,
					'values' : values
				},
				async : false,// 取消异步
				dataType : "json",
				success : function(data)
				{
					result = data;
				}
			});
			Helper.cache.addCache("infoArray_" + type + "_" + field + "_" + values, result);
		}
		return result;
	},
	info : function(type, id)
	{
		var result = Helper.cache.getCache("info_" + type + "_" + id);
		if (!result)
		{
			$.ajax({
				type : "get",
				url : Helper.basePath + "/basicInfo",
				data : {
					'type' : type,
					'id' : id
				},
				async : false,// 取消异步
				dataType : "json",
				success : function(data)
				{
					result = data;
				}
			});
			Helper.cache.addCache("info_" + type + "_" + id, result);
		}
		return result;
	},
	// 获取汇率对象（currencyType：兑换币别）
	getExchangeRate : function(currencyType)
	{
		var result = Helper.cache.getCache("getExchangeRate_" + currencyType);
		if (!result)
		{
			$.ajax({
				type : "get",
				url : Helper.basePath + "/getExchangeRate",
				data : {
					'currencyType' : currencyType
				},
				async : false,// 取消异步
				dataType : "json",
				success : function(data)
				{
					result = data;
				}
			});
			Helper.cache.addCache("getExchangeRate_" + currencyType, result);
		}
		return result;
	},
	// 获取枚举对象文本
	getEnumText : function(className, name, property)
	{
		var result = Helper.cache.getCache("getEnumText_" + className + "_" + name + "_" + className);
		if (!result)
		{
			$.ajax({
				type : "get",
				url : Helper.basePath + "/public/getEnumText",
				data : {
					'className' : className,
					'name' : name,
					'property' : property
				},
				async : false,// 取消异步
				dataType : "json",
				success : function(data)
				{
					result = data;
				}
			});
			Helper.cache.addCache("getEnumText_" + className + "_" + name + "_" + className, result);
		}
		return result;
	},
	// 是否有权限
	hasPermission : function(permission)
	{
		var result = Helper.cache.getCache("hasPermission_" + permission);
		if (!result)
		{
			$.ajax({
				type : "get",
				url : Helper.basePath + "/hasPermission",
				data : {
					'permission' : permission
				},
				async : false,// 取消异步
				dataType : "json",
				success : function(data)
				{
					result = data;
				}
			});
			Helper.cache.addCache("hasPermission_" + permission, result);
		}
		return result;
	},
	// 是否有报价系统
	hasOfferPermission : function(offerType, permission)
	{
		var result = Helper.cache.getCache("hasOfferPermission_" + permission);
		if (!result)
		{
			$.ajax({
				type : "get",
				url : Helper.basePath + "/hasOfferPermission",
				data : {
					'offerType' : offerType,
					'permission' : permission
				},
				async : false,// 取消异步
				dataType : "json",
				success : function(data)
				{
					result = data;
				}
			});
			Helper.cache.addCache("hasOfferPermission_" + permission, result);
		}
		return result;
	},
};

Helper.i18n = {
	getMsg : function(id)
	{
		var $msg = $("#" + id).data("msg");
		if ($msg.trim() == "" || $msg.startWith("i18n.jsp."))
		{
			$msg = $("#" + id).data("dft");
		}
		return $msg;
	}
};

Helper.Date = {
	diff : function(sDate1, sDate2)
	{ // sDate1和sDate2是2002-12-18格式
		s2 = (new Date(sDate2).getTime());
		s1 = (new Date(sDate1.replace(/-/g, "/"))).getTime();
		var days = (s1) - (s2);
		var time = Math.floor(days / (1000 * 60 * 60 * 24)) + 1;
		return time;
	}
};


(function($)
{
	/* 加载打印模板 */
	window.templateItem = {};
	$.fn.loadTemplate = function(billType, dataUrl)
	{
		var url = Helper.basePath + '/template/listWhitTitle?billType=' + billType;
		var _this = this;
		Helper.request({
			url : url,
			success : function(data)
			{
				if (data.success)
				{
					var ul = '<ul class="dropdown-menu" role="menu">';
					$.each(data.obj, function(key, value)
					{
						ul += ' <li><a title="' + value + '" href="' + Helper.basePath + '/sys/template/print?url=' + dataUrl + '&templateId=' + key + '" target="_blank">' + value + '</a></li>';
					});
					ul += '</ul>';
					$(_this).find(".template_div").append(ul);
				} else
				{
					var ul = '<ul class="dropdown-menu" role="menu">';
					ul += '<li>暂时没有模板</li>';
					ul += '</ul>';
					$(_this).find(".template_div").append(ul);
				}

			}
		});
	};
	$.fn.select = function(url, params, callBack)
	{
		var _THIS = $(this);
		$.ajax({
			type : "get",
			url : Helper.basePath + url,
			data : params,
			async : true,
			dataType : "json",
			success : function(data)
			{
				_THIS.empty();
				$.each(data, function(index, item)
				{
					_THIS.append('<option value="' + item[1] + '">' + item[0] + '</option>');
				});
				callBack();
			}
		});
	};
	$.fn.formToJson = function()
	{
		var jsonData1 = {};
		var serializeArray = this.serializeArray();
		// 先转换成{"id": ["12","14"], "name": ["aaa","bbb"],
		// "pwd":["pwd1","pwd2"]}这种形式
		$(serializeArray).each(function()
		{
			if (jsonData1[this.name] !== undefined)
			{
				if ($.isArray(jsonData1[this.name]))
				{
					jsonData1[this.name].push(Helper.isEmpty(this.value) ? null : this.value);
				} else
				{
					jsonData1[this.name] = [ jsonData1[this.name], this.value ];
				}
			} else
			{
				jsonData1[this.name] = Helper.isEmpty(this.value) ? null : this.value;
			}
		});

		// console.log(jsonData1);
		// console.log(JSON.stringify(jsonData1));
		// 再转成[{"id": "12", "name": "aaa", "pwd":"pwd1"},{"id": "14", "name":
		// "bb", "pwd":"pwd2"}]的形式
		var vCount = 0;
		// 计算json内部的数组最大长度
		for ( var item in jsonData1)
		{
			var temp = jsonData1[item];
			if ($.isArray(temp))
			{// 数组拆分
				var itemNameArray = item.split(".");
				var subName = itemNameArray[0];
				var subProperty = itemNameArray[1];
				if (!jsonData1[subName])
				{
					jsonData1[subName] = [];
				}
				for (var i = 0; i < temp.length; i++)
				{
					if (!jsonData1[subName][i])
					{
						jsonData1[subName][i] = {};
					}
					jsonData1[subName][i][subProperty] = Helper.isEmpty(temp[i]) ? null : temp[i];
				}
				delete jsonData1[item];// 移除原有属性
			} else
			{
				if (item.split(".").length > 1)
				{
					var _itemNameArray = item.split(".");
					var _subName = _itemNameArray[0];
					var _subProperty = _itemNameArray[1];
					if (!jsonData1[_subName])
					{
						jsonData1[_subName] = [];
						jsonData1[_subName][0] = {};
					}
					jsonData1[_subName][0][_subProperty] = Helper.isEmpty(temp) ? null : temp;
					delete jsonData1[item];// 移除原有属性
				}
			}
		}
		return jsonData1;
	};
})(jQuery);

// 全局缓存对像
Helper.cache = new Helper.BaseCache();
// 简写
Helper.id = Helper.doms.id;
Helper.getValueInt = Helper.doms.getValueInt;
Helper.getHtmlInt = Helper.doms.getHtmlInt;
Helper.paramsHtml = Helper.doms.paramsHtml;
Helper.isNull = Helper.validata.isNull;
Helper.isNotNull = Helper.validata.isNotNull;
Helper.isEmpty = Helper.validata.isEmpty;
Helper.isNotEmpty = Helper.validata.isNotEmpty;
Helper.post = Helper.Remote.post;
Helper.request = Helper.Remote.request;

// 公共初始化
$(function()
{
	// 所有需要用到字数限制的对象 新增class名为 input-txt_onlymemos
	$(".input-txt_onlymemos").each(function()
	{
		Helper.doms.textarealength(this, 100)
	})
})
/*
 * =============标签栏js=============
 */
/* 定义全局变量num和hide_nav */
var num = 0, oUl = $("#min_title_list"), hide_nav = $("#tabNav");

/* 定义变量获取对象 */
function min_titleList()
{
	var topWindow = $(window.parent.document);
	var show_nav = topWindow.find("#min_title_list");
	var aLi = show_nav.find("li");
}

/* 获得左侧菜单栏标签，在右边tab栏判断添加tab */
function admin_tab(obj)
{
	if ($(obj).attr('_href'))
	{
		var bStop = false;
		var bStopIndex = 0;
		var _href = $(obj).attr('_href');
		var _titleName = $(obj).attr("data-title");
		var _refresh = $(obj).attr('refresh');
		var topWindow = $(window.parent.document);
		var show_navLi = topWindow.find("#min_title_list li");
		/* 遍历顶部整个li列表，判断是否存在左边菜单选项 */
		show_navLi.each(function()
		{
			if ($(this).find('span').attr("title") == _titleName)
			{
				bStop = true;
				bStopIndex = show_navLi.index($(this));
				return false;
			}
		});
		if (!bStop)
		{
			creatIframe(_href, _titleName, _refresh);
			min_titleList();
		}
		/* 给点击标签li赋active 显示对应标签的frame */
		else
		{
			show_navLi.removeClass("active").eq(bStopIndex).addClass("active");
			show_navLi.eq(bStopIndex).find('span').html(_titleName);
			var iframe_box = topWindow.find("#iframe_box");
			/* 火狐display:none元素获取不到兼容写法 */
			/*
			 * iframe_box.find(".show_iframe").hide().eq(bStopIndex).show().find("iframe").attr("src",
			 * _href);
			 */
			iframe_box.find(".show_iframe").eq(bStopIndex).show().find("iframe").attr("src", _href);
			iframe_box.find(".show_iframe").eq(bStopIndex).siblings(".show_iframe").hide();
		}
	}
}

/* 给标签栏添加新的标签 */
function creatIframe(href, titleName, refresh)
{
	var topWindow = $(window.parent.document);
	var show_nav = topWindow.find('#min_title_list');
	show_nav.find('li').removeClass("active");
	var iframe_box = topWindow.find('#iframe_box');
	/* 为标签栏创建新li标签 */
	show_nav.append('<li class="active left-border"  onselectstart="return false" ><span data-href="' + href + '" title="' + titleName + '">' + titleName + '</span><i></i><em class="icon icon-cross"></em></li>');
	var taballwidth = 0, $tabNav = topWindow.find(".acrossTab"), $tabNavWp = topWindow.find(".tabNav-wp"), $tabNavitem = topWindow.find(".acrossTab li");
	/* 判断如果不存在标签，则返回 */
	if (!$tabNav[0])
	{
		return
	}
	/* 遍历整个标签栏中的标签，计算标签总长度 */
	$tabNavitem.each(function(index, element)
	{
		taballwidth += Number(parseFloat($(this).width() + 32));
	});
	$tabNav.width(taballwidth + 25);
	/* iframe */
	var iframeBox = iframe_box.find('.show_iframe');
	iframeBox.hide();
	/* 创建并添加新的iframe */
	iframe_box.append('<div class="show_iframe"><div class="loading"></div><iframe every-refresh="' + refresh + '" frameborder="0" src=' + href + '></iframe></div>');
	var showBox = iframe_box.find('.show_iframe:visible');// 找出iframe可见的父级div
	showBox.find('iframe').load(function()
	{
		showBox.find('.loading').hide();// 找出新添加的iframe，隐藏载入动画;
	});
	tabvisible();
}

/* 标签被遮挡处理,使新添标签左移，让用户可见 */
function tabvisible()
{
	var taballwidth = 0, count = 0, $tabNav = hide_nav.find(".acrossTab"), $tabNavitem = hide_nav.find(".acrossTab li"), $tabNavitem_active = hide_nav.find(".acrossTab li.active"), $tabNavWp = hide_nav.find(".tabNav-wp");
	$tabNavitem.each(function(index, element)
	{
		taballwidth += Number(parseFloat($(this).width() + 32))
	});
	/* 获取整个标签栏div的长度 */
	var w = $tabNavWp.width();
	/* 当标签总长度大于标签栏的总长度时，全部标签左移 */
	if (taballwidth > (w - 45))
	{
		var tabnum = Number((taballwidth - w + 70 + $tabNavitem_active.width()) * (-1));
		$tabNav.css('left', tabnum + 'px');
	}
}

/* 关闭最右tab显示上一个Iframe和tab,即当关闭最右边的li时,显示上一个li,iframe也显示对应的 */
function removeIframe(obj)
{
	var aCloseIndex = obj.index();
	var iframe_box = $("#iframe_box");
	if (aCloseIndex > 0)
	{
		obj.remove();
		$('#iframe_box').find('.show_iframe').eq(aCloseIndex).remove();
		num == 0 ? num = 0 : num--;
		if ($("#min_title_list li.active").size() == 0)
		{
			$("#min_title_list li").removeClass("active").eq(aCloseIndex - 1).addClass("active");
			iframe_box.find(".show_iframe").hide().eq(aCloseIndex - 1).show();
		}
		/* 标签栏长度发生改变时触发此函数 */
		tabNavallwidth();
	} else
	{
		return false;
	}

	/*
	 * var topWindow = $(window.parent.document); var aCloseIndex =
	 * $(obj).parent().index(); var tab = topWindow.find(".acrossTab li"); var
	 * iframe = topWindow.find('#iframe_box .show_iframe');
	 * tab.eq(aCloseIndex).remove(); iframe.eq(aCloseIndex).remove(); num == 0?num =
	 * 0:num--; if($("#min_title_list li.active").size()==0)
	 * {//如果没有已激活的则显示上一个iframe标签 tab.removeClass('active');
	 * tab.eq(i-1).addClass("active"); iframe.hide(); iframe.eq(i-1).show(); }
	 */
}

/* 获取顶部选项卡总长度,判断触发点击事件(左右翻标签) */
function tabNavallwidth()
{
	var taballwidth = 0, $tabNav = hide_nav.find(".acrossTab"), $tabNavWp = hide_nav.find(".tabNav-wp"), $tabNavitem = hide_nav.find(".acrossTab li"), $tabNavmore = hide_nav.find(".tab-swith-btn");
	if (!$tabNav[0])
	{
		return
	}
	$tabNavitem.each(function(index, element)
	{
		taballwidth += Number(parseFloat($(this).width() + 32))
	});
	var w = $tabNavWp.width();
	if (taballwidth > (w - 90))
	{
		/* 点击下翻按钮 */
		$('.next-tab').click(function()
		{
			num == oUl.find('li').length - 1 ? num = oUl.find('li').length - 1 : num++;//
			toNavPos();
		});
		/* 点击上翻按钮 */
		$('.prev-tab').click(function()
		{
			num == 0 ? num = 0 : num--;
			toNavPos();
		});
	} else
	{
		if ($tabNav.offset().left == 220)
		{
			$('.tab-swith-btn').off('click');
		}
	}
}

/* 停止所有动画标签动画并开始当前动画 */
function toNavPos()
{
	oUl.stop().animate({
		'left' : -num * 100
	}, 100);
}

/**
 * 页签右建事件
 */
function createMouseDownMenuData(obj)
{
	obj.unbind("mousedown");
	obj.one("mousedown", (function(e)
	{
		if (e.which == 3)
		{// 右键事件
			var _THIS = $(this);
			var SELF = "self";
			var OTHER = "other";
			var LEFT = "left";
			var RIGHT = "right";
			var ALL = "all";
			var opertionn = {
				name : "",
				offsetX : 2,
				offsetY : 2,
				textLimit : 10,
				beforeShow : $.noop,
				afterShow : $.noop
			};
			var refreshSelf = function()
			{
				var liCurrIndex = $("#min_title_list li").index(_THIS);// 当前页签的索引（从0开始）
				var _iframe = $('#iframe_box').find('.show_iframe').eq(liCurrIndex).find("iframe");
				_iframe[0].contentWindow.location.reload();
				// _iframe.attr('src', _iframe.attr('src'));
			}
			var closeByType = function(type)
			{
				var liCurrIndex = $("#min_title_list li").index(_THIS);// 当前页签的索引（从0开始）
				var liLength = $("#min_title_list li").length;
				switch (type)
				{
				case SELF:
					_THIS.remove();
					$('#iframe_box').find('.show_iframe').eq(liCurrIndex).remove();
					num == 0 ? num = 0 : num--;
					break;
				case OTHER:
					for (var i = 0; i < liLength; i++)
					{
						if (i != 0)
						{
							if (i < liCurrIndex)
							{
								$("#min_title_list li").eq(1).remove();
								$('#iframe_box').find('.show_iframe').eq(1).remove();
							} else if (i > liCurrIndex)
							{
								$("#min_title_list li").eq(2).remove();
								$('#iframe_box').find('.show_iframe').eq(2).remove();
							}
						}
					}
					num = 1;
					break;
				case LEFT:
					for (var k = 0; k < liCurrIndex; k++)
					{
						if (k != 0)
						{
							$("#min_title_list li").eq(1).remove();
							$('#iframe_box').find('.show_iframe').eq(1).remove();
							num == 0 ? num = 0 : num--;
						}
					}
					break;
				case RIGHT:
					for (var x = liCurrIndex; x < liLength; x++)
					{
						$("#min_title_list li").eq(liCurrIndex + 1).remove();
						$('#iframe_box').find('.show_iframe').eq(liCurrIndex + 1).remove();
						num == 0 ? num = 0 : num--;
					}
					break;
				case ALL:
					for (var y = 0; y < liLength; y++)
					{
						if (y != 0)
						{
							$("#min_title_list li").eq(1).remove();
							$('#iframe_box').find('.show_iframe').eq(1).remove();
						}
					}
					num = 0;
					break;
				}

				if (!$("#min_title_list li").hasClass("active"))
				{
					$("#min_title_list li").removeClass("active").eq(-1).addClass("active");
					$("#iframe_box").find(".show_iframe").hide().eq(-1).show();
				}
				tabNavallwidth();
			}

			var liCurrIndex = $("#min_title_list li").index($(this));// 当前页签的索引（从0开始）
			var liLength = $("#min_title_list li").length;
			function createMenuData()
			{

				var refreshSelf_tab = {
					text : "刷新",
					func : function()
					{
						refreshSelf();
					}
				};
				var closeSelf_tab = {
					text : "关闭",
					func : function()
					{
						closeByType(SELF);
					}
				};
				var closeOther_tab = {
					text : "关闭其它",
					func : function()
					{
						closeByType(OTHER);
					}
				};
				var closeLeft_tab = {
					text : "关闭左侧",
					func : function()
					{
						closeByType(LEFT);
					}
				};
				var closeRight_tab = {
					text : "关闭右侧",
					func : function()
					{
						closeByType(RIGHT);
					}
				};
				var closeAll_tab = {
					text : "关闭全部",
					func : function()
					{
						closeByType(ALL);
					}
				};
				if (liCurrIndex == 0)
				{
					return [ [ refreshSelf_tab ] ];
				} else
				{
					if (liLength == 2)
					{// 只有自己
						return [ [ refreshSelf_tab ], [ closeSelf_tab ] ];
					} else if (liCurrIndex == 1)
					{// 自己是第一个节点
						return [ [ refreshSelf_tab ], [ closeSelf_tab, closeRight_tab ], [ closeAll_tab ] ];
					} else if (liCurrIndex > 1 && (liCurrIndex < liLength - 1))
					{// 自己是中间节点
						return [ [ refreshSelf_tab ], [ closeSelf_tab, closeOther_tab, closeLeft_tab, closeRight_tab ], [ closeAll_tab ] ];
					} else
					{// 自己是末节点
						return [ [ refreshSelf_tab ], [ closeSelf_tab, closeLeft_tab ], [ closeAll_tab ] ];
					}
					return null;
				}

			}
			;
			_THIS.smartMenu(createMenuData(), opertionn);

		}
	}));
}

// 关闭当前标签并显示对应标题的页签
function closeTabAndJump(active_title, url)
{
	// 查找当前业签
	var _li_index = $(window.parent.document).find("#min_title_list").find("li.active").index();
	if (url)
	{
		admin_tab($("<a _href='" + url + "' data-title='" + active_title + "' />"));
	} else
	{
		// 显示被激活的业签
		$(window.parent.document).find("#min_title_list").find("li span[title='" + active_title + "']").parent().click();
	}
	// 关闭当前业签
	$(window.parent.document).find("#min_title_list").find("li:eq(" + _li_index + ") em").click();
}
// 图片缩放方法
function imgShow(outerdiv, innerdiv, bigimg, _this)
{
	var src = _this.attr("src");// 获取当前点击的pimg元素中的src属性
	$(bigimg).attr("src", src);// 设置#bigimg元素的src属性

	/* 获取当前点击图片的真实大小，并显示弹出层及大图 */
	$("<img/>").attr("src", src).load(function()
	{
		var windowW = $(window).width();// 获取当前窗口宽度
		var windowH = $(window).height();// 获取当前窗口高度
		var realWidth = this.width;// 获取图片真实宽度
		var realHeight = this.height;// 获取图片真实高度
		var imgWidth, imgHeight;
		var scale = 0.8;// 缩放尺寸，当图片真实宽度和高度大于窗口宽度和高度时进行缩放

		if (realHeight > windowH * scale)
		{// 判断图片高度
			imgHeight = windowH * scale;// 如大于窗口高度，图片高度进行缩放
			imgWidth = imgHeight / realHeight * realWidth;// 等比例缩放宽度
			if (imgWidth > windowW * scale)
			{// 如宽度扔大于窗口宽度
				imgWidth = windowW * scale;// 再对宽度进行缩放
			}
		} else if (realWidth > windowW * scale)
		{// 如图片高度合适，判断图片宽度
			imgWidth = windowW * scale;// 如大于窗口宽度，图片宽度进行缩放
			imgHeight = imgWidth / realWidth * realHeight;// 等比例缩放高度
		} else
		{// 如果图片真实高度和宽度都符合要求，高宽不变
			imgWidth = realWidth;
			imgHeight = realHeight;
		}
		$(bigimg).css("width", imgWidth);// 以最终的宽度对图片缩放

		var w = (windowW - imgWidth) / 2;// 计算图片与窗口左边距
		var h = (windowH - imgHeight) / 2;// 计算图片与窗口上边距
		$(innerdiv).css({
			"top" : h,
			"left" : w
		});// 设置#innerdiv的top和left属性
		$(outerdiv).fadeIn("fast");// 淡入显示#outerdiv及.pimg
	});

	$(outerdiv).click(function()
	{// 再次点击淡出消失弹出层
		$(this).fadeOut("fast");
	});
}
// 在单据模块中点击单据编号，可以直接打开单据编号对应的单据模块的查看界面
function jumpTo(url, title)
{
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}

/**
 * 根据单号转换url
 * @since 1.0, 2017年12月29日 上午9:08:20, zhengby
 */
function billNoTransToUrl(billNo)
{
	if (billNo == null || billNo == "")
	{
		return;
	}
	var url = "";
	var title = "";
	// 生产工单
	if (/^MO/g.test(billNo))
	{
		url = Helper.basePath + '/produce/work/toView/' + billNo;
		title = "生产工单";
	}
	// 销售订单
	else if (/^SO/g.test(billNo))
	{
		url = Helper.basePath + '/sale/order/view/' + billNo;
		title = "销售订单";
	}
	// 采购订单
	else if (/^PO/g.test(billNo))
	{
		url = Helper.basePath + '/purch/order/view/' + billNo;
		title = "采购订单";
	}
	// 采购退货单
	else if (/^PR/g.test(billNo))
	{
		url = Helper.basePath + '/purch/refund/view/' + billNo;
		title = "采购退货单";
	}
	// 采购入库单
	else if (/^PN/g.test(billNo))
	{
		url = Helper.basePath + '/purch/stock/view/' + billNo;
		title = "采购入库单";
	}
	// 发外加工单
	else if (/^OP/g.test(billNo))
	{
		url = Helper.basePath + '/outsource/process/toView/' + billNo;
		title = "发外加工单";
	}
	// 报价单
	else if (/^QU/g.test(billNo))
	{
		url = Helper.basePath + '/offer/view/no/' + billNo;
		title = "报价单";
	}
	return '<a class="jump-to" title="点击链接" onclick="jumpTo(&quot;' + url + '&quot;,&quot;' + title + '&quot;)">' + billNo + '</a>';
}

/**
 * 根据源单单号+id转换url
 * @since 1.0, 2017年12月29日 上午10:22:13, zhengby
 */
function idTransToUrl(id, billNo)
{
	if (id == null || id == "")
	{
		return;
	}
	var url = "";
	var title = "";
	// 销售退货单
	if (/^IR/g.test(billNo))
	{
		url = Helper.basePath + '/sale/return/view/' + id;
		title = "销售退货单";
	}
	// 销售送货单
	else if (/^IV/g.test(billNo))
	{
		url = Helper.basePath + '/sale/deliver/view/' + id;
		title = "销售送货单";
	}
	// 发外到货单
	else if (/^OA/g.test(billNo))
	{
		url = Helper.basePath + '/outsource/arrive/view/' + id;
		title = "发外到货单";
	}
	// 发外退货单
	else if (/^OR[0-9]/g.test(billNo))
	{
		url = Helper.basePath + '/outsource/return/view/' + id;
		title = "发外退货单";
	}
	// 发外对账单
	else if (/^OC/g.test(billNo))
	{
		url = Helper.basePath + '/outsource/reconcil/view/' + id;
		title = "发外对账";
	}
	// 采购对账单
	else if (/^PK/g.test(billNo))
	{
		url = Helper.basePath + '/purch/reconcil/view/' + id;
		title = "采购对账";
	}
	// 销售对账单
	else if (/^SK/g.test(billNo))
	{
		url = Helper.basePath + '/sale/reconcil/view/' + id;
		title = "销售对账";
	}
	// 报价单
	else if (/^QU/g.test(billNo))
	{
		url = Helper.basePath + '/offer/view/' + id;
		title = "报价单";
	}
	// 产量上报
	else if (/^DY/g.test(billNo))
	{
		url = Helper.basePath + '/produce/report/view/' + id;
		title = "产量上报";
	}
	// 生产领料
	else if (/^MR/g.test(billNo))
	{
		url = Helper.basePath + '/stockmaterial/take/view/' + id;
		title = "生产领料";
	}
	// 生产补料
	else if (/^SM[0-9]/g.test(billNo))
	{
		url = Helper.basePath + '/stockmaterial/supplement/view/' + id;
		title = "生产补料";
	}
	// 生产退料
	else if (/^RM/g.test(billNo))
	{
		url = Helper.basePath + '/stockmaterial/return/view/' + id;
		title = "生产退料";
	}
	// 材料其他入库
	else if (/^SMI/g.test(billNo))
	{
		url = Helper.basePath + '/stockmaterial/otherin/view/' + id;
		title = "材料其他入库";
	}
	// 材料其他出库
	else if (/^SMO/g.test(billNo))
	{
		url = Helper.basePath + '/stockmaterial/otherout/view/' + id;
		title = "材料其他出库";
	}
	// 材料库存调整
	else if (/^MA/g.test(billNo))
	{
		url = Helper.basePath + '/stockmaterial/adjust/view/' + id;
		title = "材料库存调整";
	}
	// 材料库存调拨
	else if (/^MT/g.test(billNo))
	{
		url = Helper.basePath + '/stockmaterial/transfer/view/' + id;
		title = "材料库存调拨";
	}
	// 材料库存盘点
	else if (/^MI/g.test(billNo))
	{
		url = Helper.basePath + '/stockmaterial/inventory/view/' + id;
		title = "材料库存盘点";
	}
	// 材料分切
	else if (/^CT/g.test(billNo))
	{
		url = Helper.basePath + '/stockmaterial/split/view/' + id;
		title = "材料分切";
	}
	// 成品入库
	else if (/^IS/g.test(billNo))
	{
		url = Helper.basePath + '/stockproduct/in/view/' + id;
		title = "成品入库";
	}
	// 成品其它入库
	else if (/^SPI/g.test(billNo))
	{
		url = Helper.basePath + '/stockproduct/otherin/view/' + id;
		title = "成品其它入库";
	}
	// 成品其它出库
	else if (/^SPO/g.test(billNo))
	{
		url = Helper.basePath + '/stockproduct/otherout/view/' + id;
		title = "成品其它出库";
	}
	// 成品库存调整
	else if (/^PA/g.test(billNo))
	{
		url = Helper.basePath + '/stockproduct/adjust/view/' + id;
		title = "成品库存调整";
	}
	// 成品库存调拨
	else if (/^PT/g.test(billNo))
	{
		url = Helper.basePath + '/stockproduct/transfer/view/' + id;
		title = "成品库存调拨";
	}
	// 成品库存盘点
	else if (/^PI/g.test(billNo))
	{
		url = Helper.basePath + '/stockproduct/inventory/view/' + id;
		title = "成品库存盘点";
	}
	// 付款单
	else if (/^RV/g.test(billNo))
	{
		url = Helper.basePath + '/finance/payment/view/' + id;
		title = "付款单";
	}
	// 收款单
	else if (/^RC/g.test(billNo))
	{
		url = Helper.basePath + '/finance/receive/view/' + id;
		title = "收款单";
	}
	// 付款核销单
	else if (/^WRV/g.test(billNo))
	{
		url = Helper.basePath + '/finance/writeoffPayment/view/' + id;
		title = "付款核销单";
	}
	// 收款核销单
	else if (/^WRC/g.test(billNo))
	{
		url = Helper.basePath + '/finance/writeoffReceive/view/' + id;
		title = "收款核销单";
	}
	// 其它付款单
	else if (/^OV/g.test(billNo))
	{
		url = Helper.basePath + '/finance/otherPayment/view/' + id;
		title = "其它付款单";
	}
	// 其它收款单
	else if (/^ORC/g.test(billNo))
	{
		url = Helper.basePath + '/finance/otherReceive/view/' + id;
		title = "其它收款单";
	}
	// 材料期初单
	else if (/^BM/g.test(billNo))
	{
		url = Helper.basePath + '/begin/material/view/' + id;
		title = "材料期初单";
	}
	// 产品期初单
	else if (/^BP/g.test(billNo))
	{
		url = Helper.basePath + '/begin/product/view/' + id;
		title = "产品期初单";
	}
	// 代工单
	else if (/^EO/g.test(billNo))
	{
		url = Helper.basePath + '/oem/order/view/' + id;
		title = "代工单";
	}
	// 代工送货单
	else if (/^ED/g.test(billNo))
	{
		url = Helper.basePath + '/oem/deliver/view/' + id;
		title = "代工送货";
	}
	// 代工退货单
	else if (/^ER/g.test(billNo))
	{
		url = Helper.basePath + '/oem/return/view/' + id;
		title = "代工退货";
	}
	// 代工对账单
	else if (/^EC/g.test(billNo))
	{
		url = Helper.basePath + '/oem/reconcil/view/' + id;
		title = "代工对账";
	}
	// 客户期初
	else if (/^BC/g.test(billNo))
	{
		url = Helper.basePath + '/begin/customer/view/' + id;
		title = "客户期初";
	} 
	// 供应商期初
	else if (/^BS/g.test(billNo))
	{
		url = Helper.basePath + '/begin/supplier/view/' + id;
		title = "供应商期初";
	}
	// 财务调整单
	else if (/^FA/g.test(billNo))
	{
		url = Helper.basePath + '/finance/adjust/view/' + id;
		title = "财务调整单";
	} else
	{
		return billNo;
	}

	return '<a class="jump-to" title="点击链接" onclick="jumpTo(&quot;' + url + '&quot;,&quot;' + title + '&quot;)">' + billNo + '</a>';
}

/**
 * 表格列拖动的公共方法，依赖jQuery插件与colResizable插件
 * @since 1.0, 2018年10月16日 上午9:54:22, zhengxchn@163.com
 */
var table_ColDrag = function(jQuery_$, params){
	if (jQuery_$ == undefined || jQuery_$ == null)
	{
		return false;
	}
	if (params == undefined|| params == null)
	{
		params = {};
	}
	jQuery_$.colResizable({disable: true});		// 先废除
	jQuery_$.colResizable({
		minWidth: params.minWidth,				// 一个单元格最小宽度，默认15
		resizeMode: params.resizeMode,		// 默认'fit'
		onResize: params.resize						// 拖动后触发的方法
	})
}

/**
 * bootstrap_table表格列拖动方法
 * @since 1.0, 2018年10月16日 上午9:54:22, zhengxchn@163.com
 */
var bootstrapTable_ColDrag = function(jQuery_$, params){
	if (jQuery_$ == undefined || jQuery_$ == null)
	{
		return false;
	}
	if (params == undefined|| params == null)
	{
		params = {};
	}
	params.resize = function(){
		jQuery_$.bootstrapTable('resetView');					// 每次拖动后重置bootstrapTable表格，用于重置表格的高度和宽度
	}
	table_ColDrag(jQuery_$, params);
}

/**
 * 为某一个标签下的所有未能列拖动的table标签添加列拖动效果，
 * 注意:
 *     该方法是筛选class不为"JColResizer"的Table标签
 * @since 1.0, 2018年10月18日 下午16:34:07, zhengxchn@163.com
 */
var allTable_ColDrag = function(jQuery_$, params){
	if (jQuery_$ == undefined || jQuery_$ == null)
	{
		return false;
	}
	if (params == undefined|| params == null)
	{
		params = {};
	}
	
	var tagName = jQuery_$.get(0).tagName;						// 检查当前的标签是否是Table标签
	if (tagName == "TABLE")
	{
		var coldrag = jQuery_$.attr("coldrag");
		if (coldrag != "false" && !jQuery_$.is(":hidden") && !jQuery_$.hasClass("JColResizer"))
		{
			// table没有coldrag属性，不是隐藏标签， class不包含JColResizer
			table_ColDrag(jQuery_$, params);
		}
	}
	
	var tables = jQuery_$.find("table");						// 查询该标签下的所有table
	$.each(tables, function(index, item)
	{
		var coldrag = $(item).attr("coldrag");
		if (coldrag == "false" || $(item).is(":hidden") || $(item).hasClass("JColResizer"))
		{
			// table有coldrag属性或是隐藏标签或包含JColResizer
			return true;
		}
		table_ColDrag($(item), params);
	});
}

/**
 * 默认开启表格列拖动方法
 * 适用于普通table和id为"bootTable"的bootstrap_table表格）
 * @since 1.0, 2018年10月16日 下午14:37:22, zhengxchn@163.com
 * */
$(function()
{
	var tables = $("body").find("table");			// 寻找页面上的table
	$.each(tables, function(index, item)
	{
		if ($(item).is(":hidden"))							// 隐藏table，跳过
		{
			return true;
		}
		
		var coldrag = $(item).attr("coldrag");	// coldrag用于关闭列拖动，在table标签加上coldrag="false"
		if (coldrag == "false")
		{
			return true;
		}
		
		var id = $(item).attr("id");						// 获取table的id属性
		if (id == "bootTable")									// table是bootstrap_table
		{
			$("#" + id).on('load-success.bs.table', function()			// bootstrap_table加载完后触发
			{
				bootstrapTable_ColDrag($("#" + id));
			})
			return true;
		}
		
		table_ColDrag($(item));																		// 普通table
	});
})
$(document).ready(function()
{
	try
	{
		// 链接去掉虚框1
		$("a").bind("focus", function()
		{
			if (this.blur)
			{
				this.blur()
			}
		});
		// 所有下拉框使用select2
		$(".hy_select2").select2({
			language : "${locale }",
			minimumResultsForSearch : 8,
		});
		/* 表格列拖动-针对没用bootstrap-table的表格 */
		$(".table_resizable").colResizable({
			resizeMode : 'overflow'
		});
	} catch (e)
	{
		// blank
	}
	// 回车键提交表单
	$(document).keyup(function(event)
	{
		if (event.keyCode == 13)
		{
			// 优先级：搜索按钮->ok按钮->报价按钮->表单提交按钮
			if ($("#btn_search").length > 0)
			{
				$("#btn_search").trigger("click");
			} else if ($("#btn_ok").length > 0)
			{
				$("#btn_ok").trigger("click");
			} else if ($("#offer_btn").length > 0)
			{
				$("#offer_btn").trigger("click");
			} else
			{
				$("input[type=submit]").trigger("click");
			}
		}
	});
	// 设置可编辑下拉框
	$(document).on("change", ".slt_val_change", function()
	{
		$(this).next().val($(this).find("option:selected").text());
	})

	/* 全选-选中所有复选框 */
	$(document).on('click', 'thead input[type=checkbox]', function()
	{
		if ($(this).attr('checked') != 'checked')
		{
			$(this).attr('checked', 'checked');
			$(this).parentsUntil('table').next().children().addClass('chang_color').find('input[type=checkbox]').prop('checked', true);
		} else if ($(this).attr('checked') == 'checked')
		{
			$(this).removeAttr('checked');
			$(this).parentsUntil('table').next().children().removeClass('chang_color').find('input[type=checkbox]').removeAttr('checked');
		}
	});

	// 电话号码的控制
	$(document).on('keypress', ".constraint_tel", function()
	{
		return (/\d|\-/.test(String.fromCharCode(event.keyCode)));
	});
	// 电话号码的控制
	$(document).on('blur', ".constraint_tel", function()
	{
		$(this).val($(this).val().trim());
		if (Helper.isEmpty($(this.val())) || Helper.validata.isTelephone($(this).val()))
		{

		} else
		{
			Helper.message.tips("电话输入非法", this);
			$(this).focus();// 触发再次获得焦点事件
		}
	});
	// 手机号码的控制
	$(document).on('keypress', ".constraint_mobile", function()
	{
		return (/\d/.test(String.fromCharCode(event.keyCode)));
	});
	// 手机号码的控制
	$(document).on('blur', ".constraint_mobile", function()
	{
		$(this).val($(this).val().trim());
		if (Helper.validata.isMobile($(this).val()))
		{

		} else
		{
			if (Helper.isNotEmpty($(this).val()))
			{
				Helper.message.tips("手机号码输入非法", this);
				$(this).focus();// 触发再次获得焦点事件
			}
		}
	});
	// 邮箱的控制
	$(document).on('blur', ".constraint_email", function()
	{
		$(this).val($(this).val().trim());
		if (Helper.validata.isEmail($(this).val()))
		{

		} else
		{
			if (Helper.isNotEmpty($(this).val()))
			{
				Helper.message.tips("邮箱输入非法", this);
				$(this).focus();// 触发再次获得焦点事件
			}

		}
	});
	// 数字的键盘输入控制
	$(document).on('keypress', ".constraint_number,.constraint_negative", function()
	{
		return (/\d/.test(String.fromCharCode(event.keyCode)));
	});
	// 数字、负号键盘输入控制
	$(document).on('keypress', ".constraint_positive", function()
	{
		return (/\d|\-/.test(String.fromCharCode(event.keyCode)));
	});
	// 数字、小数控制
	$(document).on('blur', ".constraint_number,.constraint_decimal", function()
	{
		$(this).val($(this).val().trim());
		if ($(this).val().trim() == "")
		{
			$(this).val(0);
			return;
		}
		if (/^\-?\d+(\.\d+)?$/.test($(this).val()))
		{
			if (Number($(this).val()) == 0)
			{
				$(this).val(0);
			}
		} else
		{
			$(this).val(0);
			$(this).blur();// 触发再次失去焦点事件
			Helper.message.tips("只能输入数字、小数", this);
			$(this).focus();
		}
		$(this).val(Number($(this).val()));
	});
	// 正数控制
	$(document).on('blur', ".constraint_negative", function()
	{
		$(this).val($(this).val().trim());
		if ($(this).val().trim() == "")
		{
			$(this).val(0);
			return;
		}
		if (/^\d+$/.test($(this).val()))
		{
			$(this).val(Number($(this).val()));
		} else if (/^[1-9]\d*\.\d*|0\.\d*[1-9]\d*$/.test($(this).val()))
		{
			$(this).val(Number($(this).val()));
		} else
		{
			// Helper.message.warn("只能输入数字");
			$(this).val(0);
			$(this).blur();// 触发再次失去焦点事件
			Helper.message.tips("只能输入正数", this);
			$(this).focus();
		}
		$(this).val(Number($(this).val()));
	});
	// 尺寸格式11*11
	$(document).on('keypress', ".constraint_style", function()
	{
		return (/\d|\*|[.]{1}/.test(String.fromCharCode(event.keyCode)));
	});
	// 尺寸格式11*11
	$(document).on('blur', ".constraint_style", function()
	{
		if (Helper.isNotEmpty($(this).val()))
		{
			if (!/^[0-9]+([.]{1}[0-9]{1,4})?\*[0-9]+([.]{1}[0-9]{1,4})?$/.test($(this).val()))
			{
				Helper.message.tips("规格错误", this);
				$(this).focus();
			}
		}
	});
	// 点击table之外区域触发
	$(document).bind("click", function(e)
	{
		if ($(e.target).closest("table").length == 0)
		{
			// 点击table之外，则触发
			$("tr").removeClass("tr_active");
		}
	})
	// 负数控制
	$(document).on('blur', ".constraint_positive", function()
	{
		$(this).val($(this).val().trim());
		if ($(this).val() == "")
		{
			$(this).val(-1);
			return;
		}
		if (/^\-?\d+$/.test($(this).val()))
		{
			$(this).val($(this).val().replace("-", ""));// 删除'-'
			if (Number($(this).val()) == 0)
			{
				$(this).val(-1);
			} else
			{
				$(this).val("-" + Math.abs(Number($(this).val())));// 在最前面加入'-'
			}
		} else
		{
			// Helper.message.warn("只能输入数字");
			// $(this).val(-1);
			// $(this).blur();//触发再次失去焦点事件

			Helper.message.tips("只能输入负数", this);
			$(this).focus();
		}
	});
	// 数字、小数点、 负号的键盘输入控制
	$(document).on('keypress', ".constraint_decimal", function()
	{
		return (/\d|\-|\./.test(String.fromCharCode(event.keyCode)));
	});
	// 数字、小数点 的键盘输入控制
	$(document).on('keypress', ".constraint_decimal_negative", function()
	{
		return (/\d|\./.test(String.fromCharCode(event.keyCode)));
	})
	// 数字、负号、小数点键盘输入控制
	$(document).on('keypress', ".constraint_decimal_positive", function()
	{
		return (/\d|\-|\./.test(String.fromCharCode(event.keyCode)));
	});

	// 正数、正小数控制
	$(document).on('blur', ".constraint_decimal_negative", function()
	{
		$(this).val($(this).val().trim());
		if ($(this).val() == "")
		{
			$(this).val(0);
			return;
		}
		if (/^\d+(\.\d+)?$/.test($(this).val()))
		{
			$(this).val(Number($(this).val()));
		} else
		{
			$(this).val(0);
			$(this).blur();// 触发再次失去焦点事件

			Helper.message.tips("只能输入正数、小数", this);
			$(this).focus();

		}
	});
	// 负数控制
	$(document).on('blur', ".constraint_decimal_positive", function()
	{
		$(this).val($(this).val().trim());
		if ($(this).val() == "")
		{
			$(this).val(-1);
			return;
		}
		if (/^\-?\d+(\.\d+)?$/.test($(this).val()))
		{
			$(this).val($(this).val().replace("-", ""));// 删除'-'
			$(this).val("-" + Math.abs(Number($(this).val())));// 在最前面加入'-'
		} else
		{
			// Helper.message.warn("只能输入数字和小数");
			$(this).val(-1);
			$(this).blur();// 触发再次失去焦点事件
			Helper.message.tips("只能输入负数、小数", this);
			$(this).focus();
		}
	});
	$(document).on("click", ".table-container .addrow", function()
	{
		add_tablerow(this);
	});
	$(document).on("click", ".table-container .deleterow", function()
	{
		delete_tablerow(this);
	});
	// 备注编辑
	$(document).on("click", ".memo", function()
	{
		var this_ = $(this);
		var text = this_.context.alt;
		if (text == '')
		{
			text = "请填写备注";
		} else
		{
			text = "请填写" + text;
		}
		Helper.message.prompt(text, this_.val(), function(index, layero)
		{
			var text = layero.find(".layui-layer-input").val();
			layer.close(index);
			this_.val(text);
		})
	})
	// 备注查看
	$(document).on("click", ".memoView", function()
	{
		var this_ = $(this);
		Helper.message.view(this_.html() || this_.val())
	})
	// 其他查看（跟备注查看一样，只是为了区别）
	$(document).on("click", ".otherView", function()
	{
		var this_ = $(this);
		Helper.message.view(this_.html())
	})
	// bootTable悬浮显示title
	$(document).on("mouseenter", "#bootTable tbody td", function()
	{
		$(this).attr("title", $(this).text());
	})
	$(document).on("mouseenter", ".border-table tbody td,#product_table tbody td,#partList_div tbody td", function()
	{
		var inputval;
		if ($(this).children().length == 0)
		{
			inputval = $(this).text();
		} else
		{
			inputval = $(this).find("input:visible").val();
			if (!inputval)
			{
				inputval = $(this).find("select").find("option:selected").text();
			}
		}
		$(this).attr("title", inputval);
	})
	// 监听点击查看大图
	$("table").on('click', '.pimg', function()
	{
		var _this = $(this);// 将当前的pimg元素作为_this传入函数
		imgShow("#outerdiv", "#innerdiv", "#bigimg", _this);
	});
	/* 初始化frame肤色 */
	function initSkin()
	{
		var cssType = $.cookie("ygj_skin"), topDoc = $(window.parent.document), frameskin = $("#frameskin"), title = topDoc.find("title").html(), topskin = topDoc.find("#topskin"), head = topDoc.find("head");
		skin_default_i = topDoc.find(".skin_default"), skin_brown_i = topDoc.find(".skin_brown"), skin_blue_i = topDoc.find(".skin_blue");
		if (cssType == "" || cssType == null || cssType == "default")
		{
			skin_default_i.addClass("active");
			skin_blue_i.removeClass("active");
			skin_brown_i.removeClass("active");
			frameskin.attr("href", Helper.ctxHYUI + '/themes/default/style.css?v=' + Helper.v);
		} else if (cssType == "brown")
		{
			skin_brown_i.addClass("active");
			skin_default_i.removeClass("active");
			skin_blue_i.removeClass("active");
			frameskin.attr("href", Helper.ctxHYUI + '/themes/brown/style.css?v=' + Helper.v);
		} else if (cssType == "blue")
		{
			skin_blue_i.addClass("active");
			skin_default_i.removeClass("active");
			skin_brown_i.removeClass("active");
			frameskin.attr("href", Helper.ctxHYUI + '/themes/blue/style.css?v=' + Helper.v);
		}
	}
	initSkin();
	/* 切换frame皮肤 */
	$(window.parent.document).find(".skin_list").children("ul").children("li").children("span").click(function()
	{
		var cssType = $(this).siblings("input").val(), frameskin = $("#frameskin");
		frameskin.attr("href", Helper.ctxHYUI + '/themes/' + cssType + '/style.css?v=' + Helper.v);
	})
});

function redirect(title, url)
{
	admin_tab($("<a _href='" + Helper.basePath + url + "' data-title='" + title + "' />"));
}

// 处理键盘事件
function doKey(e)
{
	var ev = e || window.event; // 获取event对象
	var obj = ev.target || ev.srcElement; // 获取事件源
	var t = obj.type || obj.getAttribute('type'); // 获取事件源类型
	if (ev.keyCode == 8 && t != "password" && t != "text" && t != "textarea")
	{
		return false;
	}

}
// 禁止后退键 作用于Firefox、Opera
document.onkeypress = doKey;
// 禁止后退键 作用于IE、Chrome
document.onkeydown = doKey;
// 单据详情主表数据填充方法
function viewResponseHandler(data)
{
	// console.log(data);
	// 按钮控制
	$("#forceComplete" + data.isForceComplete).show();
	$("#isCheck" + data.isCheck).show();
	$(".isFlowCheck" + data.isCheck).show();
	// 控制盖章
	if (data.isCheck == 'YES')
	{
		$(".review").show();
	} else
	{
		$(".review").hide();
	}
	// 生成按钮控制
	if (data.isForceComplete == "YES" || data.isCheck == "NO")
	{
		$(".isFlowCheckYES").hide();
	} else
	{
		$(".isFlowCheckYES").show();
	}

	// 循环赋值input
	$("input[type=text],input[type=hidden]").each(function(index, item)
	{
		var inputName = item.id;
		var value = data[inputName];
		if (inputName == undefined || inputName == null || value == undefined || value == null)
		{
			return;
		}
		if ($(item).attr('pattern') != undefined && $(item).attr('pattern') == 'yyyy-MM-dd')
		{
			$(item).val(new Date(value).format('yyyy-MM-dd'));
			return;
		}
		$(item).val(value);
	});
	// 循环赋值textarea
	$("textarea").each(function(index, item)
	{
		var inputName = item.id;
		var value = data[inputName];
		$(item).html(value);
	});
	return {
		rows : data.detailList,
		total : eval(data.detailList).length
	};
}
/* 加载同事 */
function loadColleague()
{
	var url = Helper.basePath + "/basic/employee/findSameEmployee?userId=${user.id }";
	Helper.request({
		url : url,
		success : function(data)
		{
			if (data.success)
			{
				var div = '<div>';
				$.each(data.obj, function(index, item)
				{
					div += '<label><input type="checkbox" name="employeeIds" value="' + item.id + '" ' + item.checked + '>' + item.name + '</label>';
				});
				div += '</div>';
				$("#colleague").empty();
				$("#colleague").append(div);
				/*
				 * var arrayDiv=new Array() var arrayInput=new Array()
				 * $.each(data.obj,function (index,item){
				 * if(!arrayDiv.contains(item.departmentName)){
				 * arrayDiv.push(item.departmentName); arrayInput.push('<label><input
				 * type="checkbox" name="employeeIds" value="'+item.id+'"
				 * '+item.checked+'>'+item.name+'</label>'); }else{ var
				 * index=arrayDiv.indexOf(item.departmentName);
				 * 
				 * arrayInput[index]=arrayInput[index]+'<label><input type="checkbox"
				 * name="employeeIds" value="'+item.id+'" '+item.checked+'>'+item.name+'</label>'; }
				 * }); $("#colleague").empty(); $.each(arrayDiv,function (index,item){
				 * var div =item+'<div style="border-top: 1px dashed
				 * #ccc">'+arrayInput[index]+'</div>' $("#colleague").append(div); });
				 */
			}
		}
	});
};

// 模板解析数据 data: 数据 content:模板
function decodeTemplateData(data, content)
{
	var HTML = content;
	var detailMap = {};
	for ( var key in data)
	{
		if (key.indexOf("List") > 0)
		{
			var preKey = key.replace("List", "");
			var indexValue = HTML.indexOf("{" + preKey + ".");
			if (indexValue > -1)
			{
				detailMap[indexValue + '_' + preKey] = data[key];
			}
			continue;
		}
		if (key.indexOf("Time") > 0)
		{
			HTML = HTML.replace(eval("/{" + key + "}/gi"), new Date(data[key]).format("yyyy-MM-dd"));
			continue;
		}
		HTML = HTML.replace(eval("/{" + key + "}/gi"), data[key]);
	}
	// 金额转大写SAY()函数
	var fnHTML = HTML.match(/SAY\([0-9.]+\)/gi);

	if (fnHTML != null)
	{
		$.each(fnHTML, function(index, item)
		{
			var num = item.toString().match(/[0-9.]+/gi);
			HTML = HTML.replace(eval("/SAY\\(" + num + "\\)/gi"), digitUppercase(num));
		});
	}
	HTML = HTML.replace(eval("/null/gi"), '');
	var tableMap = {};
	// 排序后数据和页面对应 start
	var sortDetailMap = {};
	var keys = new Array();
	$.each(detailMap, function(key, detail)
	{
		keys.push(key);
	});
	$.each(keys.sort(sortNumber), function(index, key)
	{
		sortDetailMap[key.split("_")[1]] = detailMap[key];
	});
	// 排序结束 end
	// console.log(sortDetailMap);
	// 截取table标签的起始位
	window.fromindex = 0;
	$.each(sortDetailMap, function(key, detail)
	{
		var startIndex = HTML.indexOf("<table", window.fromindex);
		var endIndex = HTML.indexOf("</table>", window.fromindex) + 8;
		// console.log(HTML);
		window.fromindex = endIndex;
		if (startIndex > -1)
		{
			var table = HTML.substring(startIndex, endIndex);
			var tbody = table.substring(table.indexOf("<tbody"), table.indexOf("</tbody>") + 8);
			// console.log($(tbody).hasClass(".more"))
			var TR = tbody.substring(tbody.indexOf("<tr>"), tbody.indexOf("</tr>") + 5);
			// 判断是否有追加的TR
			if ($(tbody).find("tr.more").length > 0)
			{
				TR = tbody.substring(tbody.indexOf("<tr>"), tbody.lastIndexOf("</tr>") + 5);
			}
			var tr = '';
			var outkey = key;
			// 如果无数据，则删除页面的table
			if (detail.length == 0)
			{
				tableMap[table] = "";
				return;
			}
			$.each(detail, function(index, item)
			{
				var _tr = TR;
				for ( var key in item)
				{
					if (key.indexOf("Time") > 0)
					{
						_tr = _tr.replace(eval("/{" + outkey + "." + key + "}/gi"), new Date(item[key]).format("yyyy-MM-dd"));
						continue;
					}
					_tr = _tr.replace(eval("/{" + outkey + "." + key + "}/gi"), item[key]);
					_tr = _tr.replace(eval("/{detail.statusIndex}/gi"), ++index);
				}
				_tr = _tr.replace(eval("/null/gi"), '')
				tr += _tr
				// console.log(tr);
			});
			tableMap[table] = table.replace(TR, tr);
			// HTML = HTML.replace(table, table.replace(TR, tr));
		}
	});

	// 执行替换
	$.each(tableMap, function(key, detail)
	{
		HTML = HTML.replace(key, detail);
	});
	return HTML;
};
// 数组sort方法排序条件函数
function sortNumber(a, b)
{
	return a.split("_")[0] - b.split("_")[0];
}
// HTML反转义
function HTMLDecode(text)
{
	var temp = document.createElement("div");
	temp.innerHTML = text;
	var output = temp.innerText || temp.textContent;
	temp = null;
	return output;
}

/**
 * v6.8-基础资料自定义 : 有两种快捷窗口，一种是通过下拉框选择自定义弹出创建窗口，另一种是原本的弹出框中通过增加“新增”按钮弹出框
 * type：基础资料类型，isBySel：是否下拉框选择触发，obj：触发事件对象，wareType：仓库类型, batch：批量修改（主要处理批量修改税率和仓库）
 */
var $this = {};
// 触发快捷窗口事件
function shotCutWindow(type, isBySel, obj, wareType, batch)
{
	$this.type = type;
	if (isBySel)// 1.1在下拉框选择自定义时打开新增窗口
	{

		$this.isBySel = true;
		if (typeof (obj) == 'object' && obj.val() == -1)
		{
			$this.obj = obj;
			// 当选中自定义选项时返回空白选项
			if (obj.find("option").length == 2 && obj.find("option:first").val() == -99)
			{
				obj.val(-99);
				obj.find("option:first").prop("selected", "selected");
				// 修改select2的选中项
				obj.next("span.select2-container").find(".select2-selection__rendered").text("").prop("title", "");
			}
			if (type == "DELIVERYCLASS")
			{
				if (obj.hasClass("shotcut")) // shotcut:需判断是否快捷创建（页面上比自定义创建少一些不必填字段）
				{
					Helper.popup.show('添加送货方式', Helper.basePath + '/basic/deliveryClass/createShotCut', '400', '120');
				} else
				{
					Helper.popup.show('添加送货方式', Helper.basePath + '/basic/deliveryClass/create', '400', '270');
				}
			} else if (type == "PAYMENTCLASS")
			{
				if (obj.hasClass("shotcut"))
				{
					Helper.popup.show('添加付款方式', Helper.basePath + '/basic/paymentClass/createShotCut', '400', '200');
				} else
				{
					Helper.popup.show('添加付款方式', Helper.basePath + '/basic/paymentClass/create', '400', '350');
				}
			} else if (type == "TAXRATE")
			{
				$this.batch = batch;
				Helper.popup.show('添加税收方式', Helper.basePath + '/basic/taxRate/create', '400', '300');
			} else if (type == "WAREHOUSE")
			{
				$this.wareType = wareType;
				$this.batch = batch;
				Helper.popup.show('添加仓库', Helper.basePath + '/basic/warehouse/create', '400', '300');
			} else if (type == "SETTLEMENTCLASS")
			{
				Helper.popup.show('添加结算方式', Helper.basePath + '/basic/settlementClass/create', '400', '280');
			} else if (type == "EMPLOYEE")
			{
				if (obj.hasClass("shotcut"))
				{
					Helper.popup.show('添加员工', Helper.basePath + '/basic/employee/createShotCut', '400', '240');
				} else
				{
					Helper.popup.show('添加员工', Helper.basePath + '/basic/employee/create', '670', '380');
				}
			} else if (type == "ACCOUNT")
			{
				Helper.popup.show('添加账户', Helper.basePath + '/basic/account/create', '700', '300');
			}
		}
	} else
	// 2.1在原本的选择框里增加“新增”按钮
	{
		if (type != null)
		{
			$this.isBySel = false;
		}
		if (type == 'WORK')
		{
			Helper.popup.show('添加机台信息', Helper.basePath + '/basic/machine/create', '700', '325');
		} else if (type == 'ACCOUNT')
		{
			Helper.popup.show('添加账户', Helper.basePath + '/basic/account/create', '700', '300');
		} else if (type == 'EMPLOYEE')
		{
			Helper.popup.show('添加员工', Helper.basePath + '/basic/employee/create', '700', '400');
		}
	}
};

// 回调方法
function fillSelection(data)
{
	if ($this.isBySel)
	{ // 1.2 回调方法 回填下拉框值
		var option = "";
		if ($this.type == "ACCOUNT")
		{
			option = "<option value=\"" + data.id + "\">" + data.bankNo + "</option>";
		} else
		{
			option = "<option value=\"" + data.id + "\">" + data.name + "</option>";
		}
		$this.obj.find("option:last").before(option);
		$this.obj.find("option:nth-last-child(2)").prop("selected", "selected");
		$this.obj.next("span").find("span.select2-selection__rendered").prop("title", data.name).text(data.name);
		if ($this.obj.find("option").length > 2 && $this.obj.find("option:first").val() == -99)
		{
			$this.obj.find("option:first").remove();
		}
		$this.obj.trigger("change"); // 回调后要触发change事件，因为部分字段可能关联其他的计算
		if ($this.batch) // 批量修改详情表里的税收/仓库
		{
			var $selects;
			if ($this.batch == "TAXRATE")
			{
				$selects = $("#detailList tbody select[name='detailList.taxRateId']");
			} else if ($this.batch == "WAREHOUSE")
			{
				$selects = $("#detailList tbody select[name='detailList.warehouseId']");
			}
			$.each($selects, function(index, item)
			{
				$(item).find("option:last").before(option);
				$(item).find("option:nth-last-child(2)").prop("selected", "selected");
				$(item).next("span").find("span.select2-selection__rendered").prop("title", data.name).text(data.name);
				// 当下拉框选项个数大于2时，去除空白选项
				if ($(item).find("option").length > 2 && $(item).find("option:first").val() == -99)
				{
					$(item).find("option:first").remove();
				}
				$(item).trigger("change"); // 回调后要触发change事件，因为部分字段可能关联其他的计算
			})
		}
	} else
	{ // 2.2 回调方法
		$("#bootTable").bootstrapTable("refreshOptions", {
			pageNumber : 1
		});
	}
}

// 全局监听事件
// 设置默认值，以备选择了自定义但是没输入的情况
$(document).on("mouseover", ".form-container .ui-combo-wrap,.form-container .ui-combo-noborder,.div_select_wrap,#detailList tr td,td.newadd_txt", function() // 主表
{
	var select = $(this).find("select");
	if ($(select) && !$(select).data("preval") && $(select).val() && $(select).val() != -1 && $(select).val() != -99)
	{
		$(select).attr("data-preval", $(select).val());
	}
})
// 结算方式
$(document).on("change", ".form-container select[name='settlementClassId']", function()
{
	shotCutWindow('SETTLEMENTCLASS', true, $(this));
})
// 付款方式
$(document).on("change", ".form-container select[name='paymentClassId']", function()
{
	shotCutWindow('PAYMENTCLASS', true, $(this));
})
// 送货方式
$(document).on("change", ".form-container select[name='deliveryClassId']", function()
{
	shotCutWindow('DELIVERYCLASS', true, $(this));
})
// 员工
$(document).on("change", ".form-container select[name='employeeId']", function()
{
	shotCutWindow('EMPLOYEE', true, $(this));
})
// 发 料 人
$(document).on("change", ".form-container select[name='sendEmployeeId']", function()
{
	shotCutWindow('EMPLOYEE', true, $(this));
})
// 领 料 人
$(document).on("change", ".form-container select[name='receiveEmployeeId']", function()
{
	shotCutWindow('EMPLOYEE', true, $(this));
})
// 退 料 人
$(document).on("change", ".form-container select[name='returnEmployeeId']", function()
{
	shotCutWindow('EMPLOYEE', true, $(this));
})
// 账号
$(document).on("change", ".form-container select[name='accountId']", function()
{
	shotCutWindow('ACCOUNT', true, $(this));
})
// 基础资料中：税收
$(document).on("change", ".div_select_wrap select[name='taxRateId']", function()
{
	shotCutWindow('TAXRATE', true, $(this));
})
// 基础资料中：员工
$(document).on("change", ".div_select_wrap select[name='employeeId']", function()
{
	shotCutWindow('EMPLOYEE', true, $(this));
})
// 基础资料中：结算方式
$(document).on("change", ".div_select_wrap select[name='settlementClassId']", function()
{
	shotCutWindow('SETTLEMENTCLASS', true, $(this));
})
// 基础资料中：付款方式deliveryClassId
$(document).on("change", ".div_select_wrap select[name='paymentClassId']", function()
{
	shotCutWindow('PAYMENTCLASS', true, $(this));
})
// 基础资料中：送货方式
$(document).on("change", ".div_select_wrap select[name='deliveryClassId']", function()
{
	shotCutWindow('DELIVERYCLASS', true, $(this));
})
// 保存时去除自定义选择项,返回最初默认值
function fixEmptyValue()
{
	var selectors = "#settlementClassId,#paymentClassId,#deliveryClassId,#employeeId,#inWarehouseId,#outWarehouseId,#taxRateId" + ",#sendEmployeeId,#receiveEmployeeId,#returnEmployeeId,#inventoryEmployeeId,#accountId,#warehouseId";
	$(selectors).each(function()
	{
		if ($(this).val() == -1)
		{
			$(this).val($(this).data("preval"));
		}
	})
	// 去除详情表的自定义项
	$("#detailList tbody select").each(function()
	{
		if ($(this).val() == -1)
		{
			$(this).val($(this).data("preval"));
			$(this).trigger("change");
		}
	})
}

$(function()
{
	/* 加载菜单图片 */
	function LoadMenuImg()
	{
		var menuIMG = $(".menu_img");
		$(".item").hover(function()
		{
			var menuTitle = $(this).find(".item-tit").text()
			$(this).find(".menu_img").attr("src", Helper.staticPath + "/layout/images/menu/" + menuTitle + "_h.png");
			$(this).find(".menu_arrow").show();
		}, function()
		{
			var menuTitle = $(this).find(".item-tit").text()
			$(this).find(".menu_img").attr("src", Helper.staticPath + "/layout/images/menu/" + menuTitle + ".png");
			$(this).find(".menu_arrow").hide();
		})
	}
	LoadMenuImg();

	/* 按时间问候 */
	function timeGreeting()
	{
		var dd = new Date();
		var st = $('.timeGreeting');
		var hour = dd.getHours();// 获取当前时
		if (hour > 0 && hour <= 6)
		{
			st.html("夜猫子，该休息了！");
		} else if (hour > 6 && hour <= 8)
		{
			st.html("上午好！ ");
		} else if (hour > 8 && hour <= 11)
		{
			st.html("早上好！ ");
		} else if (hour > 11 && hour <= 13)
		{
			st.html("中午好！ ");
		} else if (hour > 13 && hour <= 17)
		{
			st.html("下午好！ ");
		} else if (hour > 17 && hour <= 18)
		{
			st.html("傍晚好！ ");
		} else if (hour > 18 && hour <= 24)
		{
			st.html("晚上好！ ");
		}
	}
	timeGreeting();

	/* 浏览器窗口大小改变时-改变菜单图标大小 */
	window.onresize = function()
	{
		changeSize();
	}
	/* 页面加载时随浏览器窗口大小-改变菜单图标大小 */
	changeSize();
	function changeSize()
	{
		/*
		 * var h = document.documentElement.clientHeight;//获取页面可见高度 alert(h) if(h<615 &&
		 * h>500){ var num = parseInt((615-h)/7); $('.item
		 * i').css({"width":(38-num)+"px","height":(38-num)+"px"}); } else if(h>615 ||
		 * h==615){ $('.item i').css({"width":"30px","height":"30px"}); }
		 */
	}

	/* 二级菜单 */
	function subMenu()
	{
		var submenu = $(".submenu");
		var length = submenu.length;
		for (var i = 0; i < length; i++)
		{
			var menu = submenu.eq(i);
			switch (menu.data("title"))
			{
			case "销售管理":
			{
				menu.css({
					"width" : "440px"
				});
				break;
			}
			case "生产管理":
			{
				menu.css({
					"width" : "240px"
				});
				break;
			}
			case "采购管理":
			{
				menu.css({
					"width" : "440px"
				});
				break;
			}
			case "发外管理":
			{
				menu.css({
					"width" : "440px"
				});
				break;
			}
			case "库存管理":
			{
				menu.css({
					"width" : "630px"
				});
				break;
			}
			case "财务管理":
			{
				menu.css({
					"width" : "440px"
				});
				break;
			}
			case "基础设置":
			{
				menu.css({
					"width" : "580px"
				});
				break;
			}
			case "系统管理":
			{
				menu.css({
					"width" : "330px"
				});
				break;
			}
			case "代工管理":
			{
				menu.css({
					"width" : "440px"
				});
				break;
			}
			default:
			{
				menu.css({
					"width" : "330px"
				});
			}
			}
		}
		if (length <= 3)
		{
			for (var i = 0; i <= length; i++)
			{
				var menu = submenu.eq(i);
				menu.css({
					"top" : "0"
				});
			}
		} else if (length > 3 && length <= 6)
		{
			for (var i = 0; i < 3; i++)
			{
				var menu = submenu.eq(i);
				menu.css({
					"top" : "0"
				});
			}
			for (var i = 3; i < length; i++)
			{
				var height = submenu.eq(i).height();
				var menu = submenu.eq(i);
				if (height < 180)
				{
					menu.css({
						"bottom" : "0"
					});
				} else if (height >= 180)
				{
					menu.css({
						"bottom" : -1 * 0.5 * height + "px"
					});
				}
			}
		} else if (length > 6 && length <= 11)
		{
			for (var i = 0; i < 3; i++)
			{
				var menu = submenu.eq(i);
				menu.css({
					"top" : "0"
				});
			}
			for (var i = 3; i < 7; i++)
			{
				var height = submenu.eq(i).height();
				var menu = submenu.eq(i);
				if (height < 180)
				{
					menu.css({
						"bottom" : "0"
					});
				} else if (height >= 180)
				{
					menu.css({
						"bottom" : -1 * 0.5 * height + "px"
					});
				}
			}
			submenu.eq(7).css({
				"bottom" : "0"
			});
			submenu.eq(8).css({
				"bottom" : "0"
			});
			submenu.eq(9).css({
				"bottom" : "0"
			});
			submenu.eq(10).css({
				"bottom" : "0"
			});
		}

		/* 移除最后字段的竖线和下边框虚线 */
		$(".submenu").find("dl:last").css("border-bottom", "none");
		$(".submenu_item").find("dd:last").find("span").remove();

	}
	subMenu();

	/* 初始化导航栏菜单 */
	$(".links_list li").each(function()
	{
		var boxWidth = $("#" + $(this).attr("src")).innerWidth() * (-0.5) + 10;
		$(this).powerFloat({
			eventType : "hover",
			targetAttr : "src",
			reverseSharp : true,
			offsets : {
				x : boxWidth,
				y : -8
			},
			container : $(this).children(".box_container"),
		});
	})
	/* 悬浮显示子菜单控制 */
	$(".nav.menu-list .item").hover(function()
	{
		$(this).children(".submenu").fadeIn(250);
	}, function()
	{
		$(this).children(".submenu").fadeOut(10);
	})
	/* 广告跑马灯 */
	$(".marquee").css({
		"left" : $(".main_title").width() + 100,
		"right" : $(".topnav_right").width()
	}).marquee({
		speed : 40,
		gap : 500,
		delayBeforeStart : 1000,
		direction : 'left',
		// duplicated: true,这条会影响IE无限滚动
		pauseOnHover : true
	});
	/* 在线提问 */
	$("#online_ask").on("click", function(e)
	{
		var url = "${ctx}/sys/service/system_notice";
		var title = "服务支持";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"))
	})
	/* 新手指南 */
	$("#new_guide").on("click", function(e)
	{
		var url = Helper.basePath + "/guide/begin_guide";
		var title = "新手指南";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"))
	})
	/* 关于 */
	$("#about").on("click", function(e)
	{
		$('.about').stop(true, true);
		$('.about_mask').show();
		$('.about').animate({
			top : "50%",
			opacity : "0.9"
		}, 300);
		$(".clo_icon").click(function()
		{
			$('.about').stop(true, true);
			$(".about").animate({
				top : "30%",
				opacity : "0"
			}, 300);
			$('.about_mask').hide();
		})
	})
	/* 点击关闭单个标签 */
	$("#min_title_list").on("click", "li em", function()
	{
		removeIframe($(this).parent());
	});
	/* 给li双击事件，关闭当前标签 */
	$(document).on("dblclick", "#min_title_list li", function()
	{
		removeIframe($(this));
	});

	/* 单击选项卡单个tab（切换tab） */
	$(document).on("click", "#min_title_list li", function()
	{
		var bStopIndex = $(this).index();
		var iframe_box = $("#iframe_box");
		$("#min_title_list li").removeClass("active").eq(bStopIndex).addClass("active");
		var _iframe = iframe_box.find(".show_iframe").hide().eq(bStopIndex).show().find("iframe");
		if (_iframe.attr("every-refresh") == "true")
		{// 时时刷新
			_iframe.attr("src", _iframe.attr("src"));
		} else
		{
			_iframe.show();
		}
	});

	// 鼠标移动到选项卡事件（添加右键事件）
	$(document).on("mouseenter", "#min_title_list li", function()
	{
		createMouseDownMenuData($(this))
	});

	// 检查是否可以继续购买
	$("#buy").on("click", function()
	{
		$.ajax({
			type : "POST",
			async : false,
			url : Helper.basePath + "/sys/buy/isPay",
			dataType : "json",
			contentType : 'application/json;charset=utf-8', // 设置请求头信息
			success : function(data)
			{
				if (data.success)
				{
					if (data.obj)
					{
						window.open(Helper.basePath + "/pay/step1/choose");
					} else
					{
						Helper.message.warn("请先支付或取消订单，在进行操作!");
						return;
					}
				} else
				{
					// 如果登陆超时,只要重新调用location,则会自动跳到登陆页面
					window.location = "/";
				}
			},
			error : function(data)
			{

			}
		});
	});
});
