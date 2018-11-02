/**
 * Author:       THINK
 * Create:       2017年10月19日 下午4:04:22
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 */
$(function()
{
	init(); // 初始化

	/**
	 * 
	 * @since 1.0, 2017年10月30日 上午9:42:15, THINK
	 */
	function init()
	{
		initBtn(); // 初始化初始化功能、按钮事件
		initListener(); // 初始化监听事件
	}

	/**
	 * 
	 * @since 1.0, 2017年10月30日 上午9:45:15, THINK
	 */
	function initBtn()
	{
		// 保存
		$("#btn_save").on("click", function()
		{
			// 验证必填数据
			if (!validate())
			{
				return false;
			}
			// 提交
			formSubmit();
		});
		// 取消
		$("#btn_cancel").click(function()
		{
			closeTabAndJump("");
		});
	}

	/**
	 * 表单提交
	 * @since 1.0, 2017年10月19日 下午4:16:46, THINK
	 */
	function formSubmit()
	{
		var jsonData = $("#prePrint_form").formToJson();

		if (jsonData.packingChk == "on")
		{
			jsonData.packingChk = "YES";
		} else
		{
			jsonData.packingChk = "NO";
			delete jsonData.packing;
			delete jsonData.packingPer;
		}
		if (jsonData.freightChk == "on")
		{
			jsonData.freightChk = "YES";
		} else
		{
			jsonData.freightChk = "NO";
			delete jsonData.freight;
		}
		Helper.request({
			url : Helper.basePath + "/offer/setting/savePrePrint",
			data : jsonData,
			success : function(data)
			{
				if (data.success)
				{
					$("#id").val(data.obj.id);
					$("#offerType").val(data.obj.offerType);
					Helper.message.suc("已保存!");
					setTimeout(function(){
						window.location.reload();
					},1500);
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

	/**
	 *  初始化所有监听事件
	 * @since 1.0, 2017年10月19日 下午4:16:46, THINK
	 */
	function initListener()
	{
		// 包装费checkbox监听事件
		$("#packingChk").on("click", function()
		{
			if (!$(this).is(":checked"))
			{
				$("input[name='packing'").attr("readOnly", "readOnly").removeClass("whiteBg");
				$("input[name='packingPer'").attr("readOnly", "readOnly").removeClass("whiteBg");
			} else
			{
				$("input[name='packing'").removeAttr("readOnly").addClass("whiteBg");
				$("input[name='packingPer'").removeAttr("readOnly").addClass("whiteBg");
			}
		})
		// 运费checkbox监听事件
		$("#freightChk").on("click", function()
		{
			if (!$(this).is(":checked"))
			{
				$("input[name='freight'").attr("readOnly", "readOnly");
				$("input[name='freight'").removeClass("whiteBg");
			} else
			{
				$("input[name='freight'").removeAttr("readOnly");
				$("input[name='freight'").addClass("whiteBg");
			}
		})
	}
})

/**
 * 验证必填字段
 * @since 1.0, 2017年11月20日 下午5:37:41, zhengby
 */
function validate()
{
	try
	{
		// 出货日期
		var $deliveryTimeMin = $("#deliveryTimeMin");
		if (!Helper.validfield.validateFieldText($deliveryTimeMin, "请填写出货日期"))
		{
			throw "";
		}
		var $deliveryTimeMax = $("#deliveryTimeMax");
		if (!Helper.validfield.validateFieldText($deliveryTimeMax, "请填写出货日期"))
		{
			throw "";
		}
		// 包装费/元
		var $packing = $("#packingChk:checked").next().children("input[name='packing']");
		if ($packing.length > 0 && !Helper.validfield.validateFieldText($packing, "请填写包装费/元"))
		{
			throw "";
		}
		if ($packing.length > 0 && !Helper.validfield.validateFieldEqText($packing, 0, "请填写包装费/元"))
		{
			throw "";
		}
		// 包装费/个
		var $packingPer = $("#packingChk:checked").next().children("input[name='packingPer']");
		if ($packingPer.length > 0 && !Helper.validfield.validateFieldText($packingPer, "请填写包装费/个"))
		{
			throw "";
		}
		if ($packingPer.length > 0 && !Helper.validfield.validateFieldEqText($packingPer, 0, "请填写包装费/个"))
		{
			throw "";
		}
		// 运费
		var $freight = $("#freightChk:checked").next().children("input[name='freight']");
		if ($freight.length > 0 && !Helper.validfield.validateFieldText($freight, "请填写运费"))
		{
			throw "";
		}
		if ($freight.length > 0 && !Helper.validfield.validateFieldEqText($freight, 0, "请填写运费"))
		{
			throw "";
		}

	} catch (e)
	{
		console.log(e);
		return false;
	}
	return true;
}
