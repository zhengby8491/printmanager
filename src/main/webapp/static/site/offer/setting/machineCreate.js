/**
 * Author:		   think
 * Create:	 	   2017年10月17日 下午5:51:15
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 */
$(function()
{
	// ================ 初始化 ================

	init(); // 初始化

	/**
	 * 
	 * 初始化功功能、按钮事件
	 * @since 1.0, 2017年10月17日 下午6:53:43, think
	 */
	function init()
	{
		initBtn(); // 初始化初始化功能、按钮事件
		initStartPrintFee(); // 初始化开机+印工费
		initCustomFormula(); // 初始化自定义报价公式
	}

	/**
	 * 
	 * 初始化初始化功能、按钮事件
	 * @since 1.0, 2017年10月17日 下午6:53:43, think
	 */
	function initBtn()
	{
		// 保存
		$("#btnSave").click(function()
		{
			formSubmit();
		});
		// 取消
		$("#btnCancel").click(function()
		{
			closeTabAndJump("")
		});
	}

	/**
	 * 
	 * 初始化开机+印工费
	 * @since 1.0, 2017年10月17日 下午6:53:43, think
	 */
	function initStartPrintFee()
	{
		// 加入专色计价
		$("#spotColorCheckbox").on("click", function()
		{
			if ($(this).prop("checked") == true)
			{
				$("#spotColorDiv, #spotColorDiv2").show();
			} else
			{
				$("#spotColorDiv, #spotColorDiv2").hide();
			}
		});

		// 加入色令计价
		$("#reamColorCheckbox").on("click", function()
		{
			if ($(this).prop("checked") == true)
			{
				$("#reamColorDiv").show();
			} else
			{
				$("#reamColorDiv").hide();
			}
		});

		// 开机+印工费切换
		$("#valuation_title_left").on("click", function()
		{
			$(this).css({
				"border-bottom" : "1px solid white",
				"background" : "white"
			});
			$("#valuation_title_right").css({
				"border-bottom" : "1px solid #aaa",
				"background" : "#f1f1f1"
			});
			$("#fixed").show();
			$("#custom").hide();
		})

		// 【普通】计价触发
		$("#machineThousandSpeedBelow").on("change", function()
		{
			changedSpeedBelow("");
		});

		// 【专色+普色】计价触发
		$("#machineSpotColorThousandSpeedBelow").on("change", function()
		{
			changedSpeedBelow("SpotColor");
		});

		// 【专色】计价触发
		$("#machineSpotColor2ThousandSpeedBelow").on("change", function()
		{
			changedSpeedBelow("SpotColor2");
		});

		// 测试
		$("#startPrintTestBtn").on("click", function()
		{
			testStartPrint();
		});
	}

	/**
	 * 
	 * 初始化自定义报价公式
	 * @since 1.0, 2017年10月17日 下午6:53:43, think
	 */
	function initCustomFormula()
	{
		// 自定义报价公式切换
		$("#valuation_title_right").on("click", function()
		{
			$(this).css({
				"border-bottom" : "0",
				"background" : "white"
			});
			$("#valuation_title_left").css({
				"border-bottom" : "1px solid #aaa",
				"background" : "#f1f1f1"
			});
			$("#custom").show();
			$("#fixed").hide();
		});

		// 初始化组定义公式组件
		initCustomFormualPart();
	}

	/**
	 * 初始化自定义公式组件
	 * CodeMirror属于在线编辑器，详细请查询API
	 * 
	 * @since 1.0, 2017年10月23日 上午9:44:04, think
	 */
	function initCustomFormualPart()
	{
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

	// ================ 业务功能 ================

	/**
	 * 
	 * （印工费）次以下 和 次以上 转换
	 * @since 1.0, 2017年10月20日 上午14:08:38, think
	 */
	function changedSpeedBelow(name)
	{
		/**
		 * 1. 获取印工费 - 次以下
		 * 2. 计算印工费 - 次以上
		 */

		// 1. 获取印工费 - 次以下
		var $machineThousandSpeedBelow = $("#machine" + name + "ThousandSpeedBelow");
		var $machineThousandSpeedAbove = $("#machine" + name + "ThousandSpeedAbove");

		// 2. 计算印工费 - 次以上
		var $speedBelow = $machineThousandSpeedBelow.val();
		if (Helper.isNotEmpty($speedBelow))
		{
			var $speedAbove = parseInt($speedBelow) + 1;
			$machineThousandSpeedAbove.val($speedAbove);
		}
	}

	/**
	 * 
	 * 【测试】计价
	 * @since 1.0, 2017年10月20日 上午10:08:38, think
	 */
	function testStartPrint()
	{
		/**
		 * 1. 验证数据
		 * 2. 后去相关对象
		 * 3. 计算数据
		 */

		// 1. 验证数据
		// 2. 后去相关对象
		var $testDiv = $("#testDiv");
		var $testTableBody = $testDiv.find("table");
		var $machineStartFee = $("#machineStartFee");
		var $machineStartSpeed = $("#machineStartSpeed");
		var $machineThousandSpeedBelow = $("#machineThousandSpeedBelow");
		var $machineThousandSpeedAbove = $("#machineThousandSpeedAbove");
		var $machineThousandSpeedBelowMoney = $("#machineThousandSpeedBelowMoney");
		var $machineThousandSpeedAboveMoney = $("#machineThousandSpeedAboveMoney");
		// 是否开启专色计价
		var $isSpotColorOpen = $("#spotColorCheckbox").prop("checked");
		// 是否开启色令计价
		var $isReamColorOpen = $("#reamColorCheckbox").prop("checked");
		var $machineReamColorStartSpeed = $("#machineReamColorStartSpeed");
		var $machineReamColorMoney = $("#machineReamColorMoney");
		var $machineReamColorCopyFee = $("#machineReamColorCopyFee");

		// 3. 计算数据
		// 显示
		$testDiv.show();
		// 清空table的tr
		$testTableBody.find("tbody").empty();

		// ======== 印数 ========
		// 初始 1000（包含印次）
		var $speed = parseInt($machineStartSpeed.val());
		// 基础 1000
		var $printSpeed = 1000;
		// 基数 1000
		var $printSpeedBase = 1000;

		// ======== 基本印工费 ========
		// 开机费
		var $startFee = parseInt($machineStartFee.val());
		// 费用
		var $fee = $startFee;
		// 开机费基数（印工费）次以下
		var $startFeeBelow = parseInt($machineThousandSpeedBelowMoney.val());
		var $startFeeAbove = parseInt($machineThousandSpeedAboveMoney.val());
		// （印工费）次以下
		var $speedBelow = parseInt($machineThousandSpeedBelow.val());
		// （印工费）次以上
		var $speedAbove = parseInt($machineThousandSpeedAbove.val());
		// 开机费系数（默认1）
		var $startFeeCount = 1;

		// ======== 色令印工费 ========
		var $startFeeReam = "";
		// 色令印数开始值
		var $reamSpeed = parseInt($machineReamColorStartSpeed.val());
		// 色令价
		var $reamMoney = parseInt($machineReamColorMoney.val());
		// 色令版费
		var $reamCopyFee = parseInt($machineReamColorCopyFee.val());
		// 开始计算色令
		var startReam = false;

		for (var i = 1; i <= 23; i++)
		{
			// 基本印工费
			// 小于 （印工费）次以下
			if ($printSpeed > $speed && $printSpeed <= $speedBelow)
			{
				$fee = $startFee + ($printSpeed - $speed) / 1000 * ($startFeeCount * $startFeeBelow);
			}
			// 大于 （印工费）次以上
			if ($printSpeed > $speed && $printSpeed > $speedAbove)
			{
				$fee = $startFee + ($printSpeed - $speed) / 1000 * ($startFeeCount * $startFeeAbove);
			}
			
			// 色令印工费
			if ($isReamColorOpen)
			{
				// 以色令印数开始值作为基础
				if ($printSpeed > $reamSpeed)
				{
					// 公式：色令价*印数/1000+版费
					$startFeeReam = $reamMoney * $printSpeed / 1000 + $reamCopyFee;
					startReam = true;
				}
			}
			
			var $tr = "";
			if(startReam === true)
			{
				$tr = "<tr><td>" + $printSpeed + "</td><td></td><td>" + $startFeeReam + "</td></tr>";
			}
			else
			{
				$tr = "<tr><td>" + $printSpeed + "</td><td>" + $fee + "</td><td></td></tr>";
			}
			$testTableBody.append($tr)

			// 基本印工费
			// 印次
			$printSpeed = $printSpeed + $printSpeedBase;
			// 大于 10000 基数为 2000
			if ($printSpeed >= 10000)
			{
				$printSpeedBase = 2000;
				$startFeeCount = 2;
			}
			// 大于20000 基数为 5000
			if ($printSpeed >= 20000)
			{
				$printSpeedBase = 5000;
				$startFeeCount = 5;
			}
		}
	}

	/**
	 * 刷新公式
	 * @since 1.0, 2017年10月23日 上午11:49:45, think
	 */
	function refreshFormula(editor)
	{
		$("#custom_formula").val(editor.getValue());
	}

	/**
	 * 检查公式
	 * @since 1.0, 2017年10月23日 上午11:49:45, think
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
	 * @since 1.0, 2017年10月23日 上午11:49:45, think
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
	 * @since 1.0, 2017年10月17日 下午7:11:02, think
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

	// ================ 表单提交 ================

	/**
	 * 
	 * 表单提交
	 * @since 1.0, 2017年10月16日 上午11:49:45, think
	 */
	function formSubmit()
	{
		// 开机费+印工费
		if ($("#custom").is(":hidden"))
		{
			formSubmitStartprintfee();
		}
		// 自定义报价公式
		else
		{
			formSubmitCustomformula();
		}
	}

	/**
	 * 
	 * 表单提交【开机+印工费】
	 * @since 1.0, 2017年10月16日 上午11:49:45, think
	 */
	function formSubmitStartprintfee()
	{
		/**
		 * 1. 验证表单数据
		 * 2. 构造提交数据
		 * 3. 提交到服务器
		 */

		// 1. 验证表单数据
		if (!formSubmitStartprintfeeValidate())
		{
			return false;
		}

		// 2. 构造提交数据
		var sm = $("#settingMachineForm").formToJson();
		var postData = {};
		if (sm && sm.machine && sm.machine.length == 1)
		{
			postData = sm.machine[0];
		}
		// 开机费+印工计价
		if (sm && sm.start && sm.start.length == 1)
		{
			$("#btnSave").attr({
				"disabled" : "disabled"
			});

			postData.offerMachineType = "START_PRINT"; // 开机费+印工计价
			postData.offerStartPrint = sm.start[0];
			// 专色转换为YES
			if (postData.offerStartPrint.joinSpotColor == "on")
			{
				postData.offerStartPrint.joinSpotColor = "YES";
			}
			// 色令转换为YES
			if (postData.offerStartPrint.joinReamColor == "on")
			{
				postData.offerStartPrint.joinReamColor = "YES";
			}
		}

		if (!postData.offerStartPrint)
		{
			return false;
		}

		// 3. 提交到服务器
		_postData(postData);
	}

	/**
	 * 
	 * 表单提交【自定义报价公式】
	 * @since 1.0, 2017年10月17日 下午7:18:29, think
	 */
	function formSubmitCustomformula()
	{
		/**
		 * 1. 验证表单数据
		 * 2. 构造提交数据
		 * 3. 提交到服务器
		 * TODO 
		 */

		// 1. 验证表单数据
		if (!customValidate())
		{
			return false;
		}

		// 2. 构造提交数据
		var sm = $("#settingMachineForm").formToJson();
		var postData = {};
		if (sm && sm.machine && sm.machine.length == 1)
		{
			postData = sm.machine[0];

			$("#btnSave").attr({
				"disabled" : "disabled"
			});

			postData.offerMachineType = "CUSTOM"; // 自定义报价
			postData.offerFormula = {};
			postData.offerFormula.formulaType = "CUSTOM";
			postData.offerFormula.formula = $("#custom_formula").val();

			// 3. 提交到服务器
			_postData(postData);
		}
	}

	/**
	 * 
	 * 提交到服务器
	 * @since 1.0, 2017年10月17日 下午7:18:29, think
	 */
	function _postData(postData)
	{
		Helper.request({
			url : Helper.basePath + "/offer/setting/saveMachine",
			data : postData,
			success : function(data)
			{
				if (data.success)
				{
					Helper.message.suc("已保存!");
					location.href = Helper.basePath + "/offer/setting/machineView/" + data.obj.id + "?type=" + postData.offerType;
				} else
				{
					Helper.message.warn('保存失败! ' + data.message);
					$("#btnSave").removeAttr("disabled");
				}
			},
			error : function(data)
			{
				Helper.message.warn('保存失败! ' + data.message);
			}
		});
	}

	// ================ 表单验证 ================

	/**
	 * 
	 * 表单验证字段并提示信息
	 * @since 1.0, 2017年10月20日 上午11:03:45, think
	 */
	function validateFieldText($field, message)
	{
		if (Helper.isEmpty($field.val()))
		{
			Helper.message.tips(message, $field);
			$field.focus();
			return false;
		}
		return true;
	}

	/**
	 * 
	 * 表单验证整型字段并提示信息
	 * @since 1.0, 2017年10月20日 上午11:03:45, think
	 */
	function validateFieldIntegerText($field, message)
	{
		if (!Helper.validata.isInteger($field.val()))
		{
			Helper.message.tips(message, $field);
			$field.focus();
			return false;
		}
		return true;
	}

	/**
	 * 
	 * 表单验证xxx*xxx字段并提示信息
	 * @since 1.0, 2017年10月20日 上午11:03:45, think
	 */
	function validateFieldSizeText($field, message)
	{
		if (!Helper.validata.isSize($field.val()))
		{
			Helper.message.tips(message, $field);
			$field.focus();
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * 表单验证xxx*xxx字段并提示信息
	 *  @since 1.0, 2017年11月30日 上午9:25:55, zhengby
	 */
	function validateFieldSizeMinText($field, message)
	{
		if (!Helper.validata.isSizeMin($field.val()))
		{
			Helper.message.tips(message, $field);
			$field.focus();
			return false;
		}
		return true;
	}
	/**
	 * 表单验证【公共】
	 * @since 1.0, 2017年10月16日 上午11:49:45, think
	 */
	function commonValidate()
	{
		// 机台名称
		if (!validateFieldText($("#machineName"), "请填写机台名称"))
		{
			return false;
		}
		// 最大上机(mm)
		if (!validateFieldText($("#machineMaxStyle"), "请填写最大上机"))
		{
			return false;
		}
		if (!validateFieldSizeText($("#machineMaxStyle"), "请填写正确的最大上机格式 xxxx*xxxx"))
		{
			return false;
		}
		// 最小上机(mm)
		if (!validateFieldText($("#machineMinStyle"), "请填写最小上机"))
		{
			return false;
		}
		if (!validateFieldSizeMinText($("#machineMinStyle"), "请填写正确的最小上机格式 xxxx*xxxx"))
		{
			return false;
		}
		// 最少印刷厚度(g)
		if (!validateFieldText($("#machineMinPrintPly"), "请填写最少印刷厚度"))
		{
			return false;
		}
		if (!validateFieldIntegerText($("#machineMinPrintPly"), "请填写正确的最少印厚度"))
		{
			return false;
		}
		// 最大印刷厚度(g)
		if (!validateFieldText($("#machineMaxPrintPly"), "请填写最大印刷厚度"))
		{
			return false;
		}
		if (!validateFieldIntegerText($("#machineMaxPrintPly"), "请填写正确的最大印厚度"))
		{
			return false;
		}
		return true;
	}

	// ================ 表单验证【开机+印工费】 ================

	/**
	 * 
	 * 表单验证【开机+印工费】验证
	 * @since 1.0, 2017年10月16日 上午11:49:45, think
	 */
	function formSubmitStartprintfeeValidate()
	{
		// 1. 验证公共表单数据
		if (!commonValidate())
		{
			return false;
		}

		// 2. 验证开机+印工费
		if (!startprintfeeValidate())
		{
			return false;
		}

		return true;
	}

	/**
	 * 
	 * 表单验证【开机+印工费】验证
	 * @since 1.0, 2017年10月16日 上午11:49:45, think
	 */
	function startprintfeeValidate()
	{
		// 【普通】验证
		if (!_startprintfeeCommonValidate(""))
		{
			return false;
		}

		// 【专色+普色】验证
		var $isSpotColorOpen = $("#spotColorCheckbox").prop("checked");
		if ($isSpotColorOpen && !_startprintfeeCommonValidate("SpotColor"))
		{
			return false;
		}
		// 【专色】验证
		if ($isSpotColorOpen && !_startprintfeeCommonValidate("SpotColor2"))
		{
			return false;
		}

		// 是否开启色令计价
		var $isReamColorOpen = $("#reamColorCheckbox").prop("checked");
		if ($isReamColorOpen)
		{
			// 【色令】开始值
			if (!validateFieldText($("#machineReamColorStartSpeed"), "请填写色令印数开始值"))
			{
				return false;
			}
			if (!validateFieldIntegerText($("#machineReamColorStartSpeed"), "请填写正确的色令印数开始值"))
			{
				return false;
			}
			// 【色令】色令价
			if (!validateFieldText($("#machineReamColorMoney"), "请填写色令价"))
			{
				return false;
			}
			if (!validateFieldIntegerText($("#machineReamColorMoney"), "请填写正确的色令价"))
			{
				return false;
			}
			// 【色令】版费
			if (!validateFieldText($("#machineReamColorCopyFee"), "请填写色令版费"))
			{
				return false;
			}
			if (!validateFieldIntegerText($("#machineReamColorCopyFee"), "请填写正确的色令版费"))
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * 
	 * 表单公共验证【开机+印工费】
	 * @since 1.0, 2017年10月16日 上午11:49:45, think
	 */
	function _startprintfeeCommonValidate(name)
	{
		// 【普通|专色】开机费
		if (!validateFieldText($("#machine" + name + "StartFee"), "请填写开机费"))
		{
			return false;
		}
		if (!validateFieldIntegerText($("#machine" + name + "StartFee"), "请填写正确的开机费"))
		{
			return false;
		}
		// 【普通|专色】包含印次
		if (!validateFieldText($("#machine" + name + "StartSpeed"), "请填写包含印次"))
		{
			return false;
		}
		if (!validateFieldIntegerText($("#machine" + name + "StartSpeed"), "请填写正确的包含印次"))
		{
			return false;
		}
		// 【普通|专色】次以下
		if (!validateFieldText($("#machine" + name + "ThousandSpeedBelow"), "请填写印公费-次以下"))
		{
			return false;
		}
		if (!validateFieldIntegerText($("#machine" + name + "ThousandSpeedBelow"), "请填写正确的印公费-次以下"))
		{
			return false;
		}
		// 【普通|专色】次以上
		if (!validateFieldText($("#machine" + name + "ThousandSpeedAbove"), "请填写印公费-次以上"))
		{
			return false;
		}
		if (!validateFieldIntegerText($("#machine" + name + "ThousandSpeedAbove"), "请填写正确的印公费-次以上"))
		{
			return false;
		}
		// 【普通|专色】元/千次
		if (!validateFieldText($("#machine" + name + "ThousandSpeedBelowMoney"), "请填写印公费-元/千次以下"))
		{
			return false;
		}
		if (!validateFieldIntegerText($("#machine" + name + "ThousandSpeedBelowMoney"), "请填写正确的印公费-元/千次以下"))
		{
			return false;
		}
		// 【普通|专色】元/千次
		if (!validateFieldText($("#machine" + name + "ThousandSpeedAboveMoney"), "请填写印公费-元/千次以上"))
		{
			return false;
		}
		if (!validateFieldIntegerText($("#machine" + name + "ThousandSpeedAboveMoney"), "请填写正确的印公费-元/千次以上"))
		{
			return false;
		}

		return true;
	}

	// ================ 表单验证【自定义报价公式】 ================

	/**
	 * 
	 * @since 1.0, 2017年10月23日 上午11:49:45, think
	 */
	function customValidate()
	{
		// 1. 验证公共表单数据
		if (!commonValidate())
		{
			return false;
		}
		// 2.验证自定义公式数据
		if($("#custom_formula").val() == null || $("#custom_formula").val() == "")
		{
			return Helper.message.warn("请填写自定义公式");
		}
		return true;
	}

	// ================ 表单验证公共组件方法 ================

});
