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