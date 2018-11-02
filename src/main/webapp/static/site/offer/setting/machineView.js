/**
 * Author:		   think
 * Create:	 	   2017年10月23日 下午1:43:18
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
		initBtn() // 初始化初始化功能、按钮事件
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
		// 返回
		$("#btnCancel").click(function()
		{
			var $type = $("#machineOfferType").val();
			var url = Helper.basePath + '/offer/setting/machine?type=' + $type;
			// 这里类型关联菜单名称（如果菜单名称修改了，这里也对应修改）
			var $titleMap = {
				"SINGLE": "单张设置",
				"ALBUMBOOK": "书刊设置",
				"CARTONBOX": "彩盒设置",
				"NOTESLETTERFORM": "便签信纸设置",
				"TAGCARD": "吊牌卡片设置",
				"ENVELOPETYPE": "封套设置",
				"PRESSURESENSITIVEADHSIVE": "不干胶设置",
				"ASSOCIATEDSINGLECLASS": "联单设置",
				"MAILERTYPE": "信封设置",
				"CUP": "纸杯设置",
			};
			var title = $titleMap[$type];
			admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"))
		});
		// 修改
		$("#btnEdit").click(function()
		{
			location.href = Helper.basePath + '/offer/setting/machineEdit/' + $("#machineId").val() + '?type=' + $("#machineOfferType").val();
		});
		// 删除
		$("#btnDel").click(function()
		{
			Helper.message.confirm('确认要删除吗？', function(index)
			{
				Helper.post(Helper.basePath + '/offer/setting/deleteMachine/' + $("#machineId").val() + '?type=' + $("#machineOfferType").val(), function(data)
				{
					if (data.success)
					{
						Helper.message.suc(data.message);
						closeTabAndJump("机台列表");
					} else
					{
						Helper.message.warn(data.message);
					}
				});
			});
		});
	}

	/**
	* 
	* 初始化开机+印工费
	* @since 1.0, 2017年10月17日 下午6:53:43, think
	*/
	function initStartPrintFee()
	{
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
	}

	// ================ 业务功能 ================

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

	// ================ 表单提交 ================
	// ================ 表单验证公共组件方法 ================
});