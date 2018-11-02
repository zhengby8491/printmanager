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