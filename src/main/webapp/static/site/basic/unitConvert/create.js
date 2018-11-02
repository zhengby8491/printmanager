/**
 * Author:		   think
 * Create:	 	   2018年1月3日 上午9:36:23
 * Copyright: 	 Copyright (c) 2018
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 */

$(function()
{
	$(".cl input[type='button']").click(function()
	{
		var formula_text_dom = $("#formula_text");
		var str_start = formula_text_dom.val().substring(0, getTxt1CursorPosition() || formula_text_dom.val().length);// 获取光标之前的字符串
		var str_end = formula_text_dom.val().substring(getTxt1CursorPosition() || formula_text_dom.val().length, formula_text_dom.val().length);// 获取光标之后的字符串
		var formula_text = str_start + $(this).val() + str_end;
		formula_text_dom.val(formula_text);
		formulaChange();
		$("#formula_text").focus();
	});
	$("#formula_text").change(function()
	{
		formulaChange();
	});

	$("#save").click(function()
	{
		var result = checkCount();
		if (result == "Infinity" || result == "err")
		{
			Helper.message.warn("错误的公式，请检查公式是否符合数学逻辑");
			return;
		}
		if (Helper.isEmpty($("#formula_text").val()))
		{
			Helper.message.warn("公式不能为空");
			return;
		}
		if (Helper.isEmpty($("#name").val()))
		{
			Helper.message.warn("换算名称不能为空");
			return;
		}
		Helper.request({
			url : Helper.basePath + "/basic/unitConvert/save",
			data : $("#jsonForm").formToJson(),
			success : function(data)
			{
				if (data.success)
				{
					parent.location.href = Helper.basePath + "/basic/unitConvert/list";
				} else
				{
					Helper.message.warn('保存失败!' + data.message);
				}
			}
		});
	});

	$(".hy_select2").select2({
		language : "zh-CN",
		minimumResultsForSearch : 1000,
	});
})
function formulaChange()
{
	var str = $("#formula_text").val();
	str = str.replace(new RegExp("材料长", "g"), "length");
	str = str.replace(new RegExp("克重", "g"), "weight");
	str = str.replace(new RegExp("材料宽", "g"), "width");
	str = str.replace(new RegExp("材料高", "g"), "height");
	str = str.replace(new RegExp("原单位数量", "g"), "qty");
	
	$("#formula").val(str);
}
// 检查算法是否合格
function checkCount()
{
	var formula = $("#formula").val();
	formula = formula.replace(new RegExp("length", "g"), 1000);
	formula = formula.replace(new RegExp("weight", "g"), 80);
	formula = formula.replace(new RegExp("width", "g"), 1000);
	formula = formula.replace(new RegExp("height", "g"), 1000);
	formula = formula.replace(new RegExp("qty", "g"), 2);
	var result;
	try
	{
		result = eval('' + formula + '');
	} catch (e)
	{
		return "err";
	}
	return result;
}

// 获取光标在字符串中的下标方法
function getTxt1CursorPosition()
{
	try
	{
		var oTxt1 = document.getElementById("formula_text");
		var cursurPosition = -1;
		if (oTxt1.selectionStart)
		{// 非IE浏览器
			cursurPosition = oTxt1.selectionStart;
		} else
		{// IE
			var range = document.selection.createRange();
			range.moveStart("character", -oTxt1.value.length);
			cursurPosition = range.text.length;
		}
	} catch (e)
	{
		return null;
	}
	return cursurPosition;
}