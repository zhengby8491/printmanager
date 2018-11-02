/**
 * Author:		   think
 * Create:	 	   2017年10月31日 下午2:42:39
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 */

$(function()
{

	init(); // 初始化

	/**
	* 
	* 初始化功功能、按钮事件
	* @since 1.0, 2017年10月17日 下午6:53:43, think
	*/
	function init()
	{
		initBtn(); // 初始化初始化功能、按钮事件
		initCustomFormula(); // 初始化自定义报价公式
	}

	/**
	 * 
	 * 初始化初始化功能、按钮事件
	 * @since 1.0, 2017年10月31日 下午6:53:43, think
	 */
	function initBtn()
	{
		// 保存
		$("#btnSave").click(function()
		{
			if (!Helper.validfield.validateFieldText($("#formulaText"), "请填写计算公式"))
			{
				return false;
			}

			if (Helper.isEmpty($("#custom_formula").val()))
			{
				Helper.message.warn("请填写自定义公式");
				return false;
			}

			parent.procedureCustom($("#formulaText").val(), $("#custom_formula").val());
			Helper.popup.close();
		});
		// 取消
		$("#btnCancel").click(function()
		{
			Helper.popup.close();
		});
	}

	/**
	 * 初始化自定义公式组件
	 * CodeMirror属于在线编辑器，详细请查询API
	 * 
	 * @since 1.0, 2017年10月31日 下午3:44:04, think
	 */
	function initCustomFormula()
	{
		// 先设置值（获取父类的值）
		var custom = parent.getCustom();
		if(custom)
		{
			$("#formulaText").val(custom.formulaText);
			$("#code, #custom_formula").val(custom.formula);
		}
		
		// 自定义公式组件
		var editor = CodeMirror.fromTextArea(document.getElementById("code"), {
			mode : "javascript",
			theme : "default",
			matchBrackets : true,
			lineNumbers : true,
			gutters : [ "CodeMirror-linenumbers", "breakpoints" ]
		});
		editor.on("change", function()
		{
			$("#custom_formula").val(editor.getValue().replace(/\‘/g, "'").replace(/\’/g, "'").replace(/\：/g, ":").replace(/\“/g, "\"").replace(/\”/g, "\"").replace(/\；/g, ";"));
			refreshFormula(editor);
		});
		// 计算参数
		$("#custom_params").find("li").click(function()
		{
			editor.replaceSelection('[' + $(this).text() + ']');
			refreshFormula(editor);
			editor.focus();
		});
		// 计算器
		$("#calculator").find("input").click(function()
		{
			editor.replaceSelection($(this).val() + '');
			refreshFormula(editor);
			editor.focus();
		});
		// 检查公式
		$("#check").click(function()
		{
			checkFormula($("#custom_formula").val());
		})
	}

	/**
	 * 刷新公式
	 * @since 1.0, 2017年10月31日 上午11:49:45, think
	 */
	function refreshFormula(editor)
	{
		$("#custom_formula").val(editor.getValue());
	}

	/**
	 * 检查公式
	 * @since 1.0, 2017年10月31日 上午11:49:45, think
	 */
	function checkFormula(str)
	{
		if (str.trim() != "")
		{
			var sel_params = getParamsByBraket(str);
			var custom_params = [];
			$("#custom_params li").each(function()
			{
				custom_params.push($(this).text());
			});
			var flag = true;
			for (var i = 0; i < sel_params.length; i++)
			{
				if ($.inArray(sel_params[i], custom_params) == -1)
				{
					flag = false;
					$("#check_formula_tip").text('参数   "' + sel_params[i] + '" 不存在!').show();
					$("#check_result").val("false");
					return;
				}
			}
			if ((getLength(str, "{") + getLength(str, "}")) % 2 != 0 || (getLength(str, "(") + getLength(str, ")")) % 2 != 0)
			{
				$("#check_formula_tip").text('语法有误!').show();
				$("#check_result").val("false");
				return;
			}
			$("#check_formula_tip").text('校验通过!').show();
			$("#check_result").val("true");
		} else
		{
			$("#check_formula_tip").text('请输入公式!').show();
		}
	}

	/**
	 * 检查公式 - 获取计价参数
	 * @since 1.0, 2017年10月31日 上午11:49:45, think
	 */
	function getParamsByBraket(str)
	{
		var sel_params = [];
		var leftIndex = str.indexOf("[");
		var rightIdnex = -1;
		if (leftIndex >= 0)
		{
			rightIndex = str.indexOf("]", leftIndex);
			if (rightIndex >= 0)
			{
				new_str = str.substring(0, leftIndex) + str.substring(rightIndex, str.length);
				sel_params.push(str.substring(leftIndex + 1, rightIndex))
				getParamsByBraket(new_str)
			}
		}
		return sel_params;
	}

	/**
	 * 
	 * 检查公式 - 计算字符出现个数
	 * @since 1.0, 2017年10月31日 下午7:11:02, think
	 */
	function getLength(str, c)
	{
		var reg = eval('/\\' + c + '/g');
		if (reg.test(str))
		{
			return str.match(reg).length;
		} else
		{
			return 0;
		}
	}
});