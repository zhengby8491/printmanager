/**
 * Author:       think
 * Create:       2017年10月20日 下午2:56:48
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 */
$(function()
{

	init(); // 初始化

	/**
	 * 
	 * 初始化功能、按钮事件
	 * 
	 * @since 1.0, 2017年10月26日 上午11:40:02, think
	 */
	function init()
	{
		initBtn(); // 初始化初始化功能、按钮事件
		initProfit(); // 初始化固定金额与利润百分比二选一
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
	 * 初始化固定金额与利润百分比二选一
	 * @since 1.0, 2017年10月26日 上午11:43:43, think
	 */
	function initProfit()
	{
		// 固定金额与利润百分比切换修改title
		$(".offerProftType").on("change", function()
		{
			if ($(this).val() == "MONEY")
			{
				$("#row_name").children("span").remove();
				$("#row_name").append("<span>金额范围</span>");
			} else
			{
				$("#row_name").children("span").remove();
				$("#row_name").append("<span>数量范围</span>");
			}
		});
		// 监听动态改变范围
		$("#profit_table").on("keyup", "input[name='profit.rangeEnd']", function()
		{
			_updateRange($(this));
		})
		// 监听数据填写是否符合要求
		$("#profit_table").on("blur", "input[name='profit.rangeEnd']", function()
		{
			_valiteEndVal($(this));
		})
		// 监听固定金额填写事件
		$("#profit_table").on("keyup", "input.valid_profit_fixed", function()
		{
			if ($(this).val() != 0)
			{
				$(this).parents("tr").find("input.valid_profit_percent").attr("readOnly", "readonly").removeClass("whiteBg constraint_negative").val("");
			} else if ($(this).val() == 0 || $(this).val() == "")
			{
				$(this).parents("tr").find("input.valid_profit_percent").removeAttr("readOnly").addClass("whiteBg constraint_negative");
			}
		})
		// 监听利润百分比填写事件
		$("#profit_table").on("keyup", "input.valid_profit_percent", function()
		{
			if ($(this).val() != 0)
			{
				$(this).parents("tr").find("input.valid_profit_fixed").attr("readOnly", "readonly").removeClass("whiteBg constraint_negative").val("");
			} else if ($(this).val() == 0 || $(this).val() == "")
			{
				$(this).parents("tr").find("input.valid_profit_fixed").removeAttr("readOnly").addClass("whiteBg constraint_negative");
			}
		})
	}

	/**
	 * 
	 * 表单提交
	 * @since 1.0, 2017年10月26日 上午11:49:45, think
	 */
	function formSubmit()
	{
		// 验证范围数据
		if (!_validateRangeData())
		{
			return false
		}

		var sw = $("#profit_form").formToJson();
		if (sw && sw.profit)
		{
			// 验证必填
			for (var i = 0; i < sw.profit.length; i++)
			{
				var settingProfit = sw.profit[i];
				settingProfit.offerType = $("#offerType").val();
				settingProfit.offerProfitType = $(".offerProftType").val();
				settingProfit.sort = i;
				settingProfit.rangeEnd = (settingProfit.rangeEnd == "无限大" ? null : settingProfit.rangeEnd);

				if (Helper.isNotEmpty(settingProfit["percent"]) || Helper.isNotEmpty(settingProfit["money"]))
				{
					continue;
				} else
				{
					Helper.message.warn("请输入利润百分比或固定金额");
					return false;
				}
			}
		}

		Helper.request({
			url : Helper.basePath + "/offer/setting/saveProfit",
			data : sw.profit,
			success : function(data)
			{
				if (data.success)
				{
					Helper.message.suc("已保存!");
				} else
				{
					Helper.message.warn('保存失败! ' + data.message);
				}
			},
			error : function(data)
			{
				Helper.message.warn('保存失败! ' + data.message);
			}
		});
	}
});

/**
 * 增加行
 * @param obj
 * @since 1.0, 2017年10月26日 上午11:45:42, think
 */
function add(obj)
{
	var TR = "<tr>";
	TR = TR + "<td><input class='constraint_negative input-txt input-txt_26' type='text' name='profit.rangeStart' value=''></td>" + "<td><input class='constraint_negative input-txt input-txt_26 whiteBg' type='text' name='profit.rangeEnd' value=''></td>" + "<td><input class='constraint_negative input-txt input-txt_25 whiteBg valid_profit_percent' type='text' name='profit.percent' value=''>&nbsp;%</td>"
			+ "<td><input class='constraint_negative input-txt input-txt_26 whiteBg valid_profit_fixed' type='text' name='profit.money' value=''></td>" + "<td><a class='nav_btn table_nav_btn' onclick='add($(this))'><i class='fa fa-plus-square'></i></a>" + "<a class='nav_btn table_nav_btn' onclick='del($(this))'><i class='fa fa-minus-square'></i></a></td>";
	TR = TR + "</tr>";
	obj.parents("tr").after(TR);
	var currentVal = obj.parents("tr").find("input[name='profit.rangeEnd']").val();
	obj.parents("tr").next("tr").find("input[name='profit.rangeStart']").val(currentVal).attr("readonly","readonly");
	obj.parents("tr").next("tr").find("input[name='profit.rangeEnd']").val(0);
	// _resetRange(obj);
}

/**
 * 删除行
 * @param obj
 * @since 1.0, 2017年10月26日 上午11:45:54, think
 */
function del(obj)
{
	// 不能删除第一条
	if (obj.parents("tr").data("index") == 0)
	{
		return false;
	}

	// 至少保留2个TR
	if (!_moreTr())
	{
		return false;
	}
	// 删除时自动把该行的rangeStart的值设给下一行的rangeStart
	var rangeStart = obj.parents("tr").find("input[name='profit.rangeStart']").val();
	obj.parents("tr").next().find("input[name='profit.rangeStart']").val(rangeStart);
	// 删除当前行
	obj.parents("tr").remove();
	
	// _resetRange(obj);
}

/**
 * 至少保留2个TR
 * @since 1.0, 2017年10月26日 下午4:22:14, think
 */
function _moreTr()
{
	if ($("#profit_form tbody tr").length <= 2)
	{
		return false;
	}

	return true;
}

/**
 * 重新计算范围
 * @since 1.0, 2017年10月26日 下午3:32:41, think
 */
function _resetRange(obj)
{
	/**
	 * 1. 获取当前节点的数据
	 * 2. 遍历所有节点并设置每个tr的排序
	 */
	// 第一条tr的范围最大值作为基数累加
	var $_rangeStart = 0;
	var $_rangeEnd = $("#profit_form tbody tr").first().find("input[name='profit.rangeEnd']").val();
	var $_rangeBase = parseInt($_rangeEnd);
	var startIdex = $(obj).parents("tr").data("index");

	// 当前tr
	var $currentTr = obj.parent().parent();
	// 当前tr的index
	var $currentIndex = $currentTr.data("index");
	// 当前tr的范围最大值
	var $currentRangeEnd = $currentTr.find("input[name='profit.rangeEnd']");

	// 1. 遍历所有节点
	$("#profit_form tbody tr").each(function(index, item)
	{
		var $this = $(this);
		$this.data("index", index);
		// 重新计算Range
		if (index > 0)
		{
			$_rangeStart = index * $_rangeBase;
			$_rangeEnd = index * $_rangeBase + $_rangeBase;
		}
		// 跳过当前按钮之前的计算
		if (index > 0)
		{
			$this.find("input[name='profit.rangeStart']").val($_rangeStart);
			// 除最后一个rangeEnd之外，全部设置样式whiteBg
			$this.find("input[name='profit.rangeEnd']").val($_rangeEnd).removeClass("whiteBg").addClass("whiteBg");
		}
	});
	// 最后一条为无限大
	var $lastRangeEnd = $("#profit_form tbody tr").last().find("input[name='profit.rangeEnd']");
	$lastRangeEnd.val("无限大");
	$lastRangeEnd.attr("readOnly", "readonly").removeClass("whiteBg");
}

/**
 * 数据范围更新
 * @since 1.0, 2017年11月28日 下午2:16:28, zhengby
 */
function _updateRange(obj)
{
	var $endVal = obj;
	obj.parents("tr").next("tr").find("input[name='profit.rangeStart']").val($endVal.val());
}

/**
 * 验证范围开始数值与范围结束数值
 * @since 1.0, 2017年11月28日 下午4:14:00, zhengby
 */
function _valiteEndVal(obj)
{
	var $endVal = obj;
	var $startVal = obj.parents("tr").find("input[name='profit.rangeStart']");
	return _validateFieldEq($startVal, $endVal);
}

/**
 * 验证范围开始数值与范围结束数值
 * @since 1.0, 2017年11月28日 下午4:10:44, zhengby
 */
function _validateFieldEq($fieldStart, $fieldEnd)
{
	if (parseInt($fieldStart.val()) >= parseInt($fieldEnd.val()))
	{
		var Str = "请填写大于" + $fieldStart.val() + "的数值";
		Helper.message.tips(Str, $fieldEnd);
		// $fieldEnd.focus();
		return false;
	}
	return true;
}

/**
 * 提交前验证数据范围数据
 * @since 1.0, 2017年11月28日 下午4:25:20, zhengby
 */
function _validateRangeData()
{
	try
	{
		// 获取rangEnd的域
		$("#profit_form tr td input[name='profit.rangeEnd']").each(function(index, obj)
		{
			var $rangeEnd = $(obj);
			var $rangeStart = $(obj).parents("tr").find("input[name='profit.rangeStart']");

			if (!_validateFieldEq($rangeStart, $rangeEnd))
			{
				throw "";
			}
		})
	} catch (e)
	{
		return false;
	}
	return true;
}