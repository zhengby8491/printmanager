$(function()
{
	$("#jsonForm").validate({
		submitHandler : function()
		{// 必须写在验证前面，否则无法ajax提交
			Helper.request({
				url : Helper.basePath + "/basic/material/save",
				data : $("#jsonForm").formToJson(),// 将form序列化成JSON字符串
				async : false,
				success : function(data)
				{
					if (data.success)
					{
						parent.location.href = Helper.basePath + "/basic/material/list";
					} else
					{
						layer.alert('创建失败：' + data.message);
					}
				}
			});

		},
		rules : {
			code : {
				required : true,
			},
			name : {
				required : true,
			},
			minStockNum : {
				isMinStockNum : true,
			},
			lastPurchPrice : {
				isLastPurchPrice : true,
			}
		},
		onkeyup : false,
		focusCleanup : true
	});

	$(document).on('keypress', "#weight", function()
	{
		return (/\d/.test(String.fromCharCode(event.keyCode)));
	});

	// 最近采购价格式验证
	$.validator.addMethod("isLastPurchPrice", function(value, element)
	{
		var reg = /^(0|[1-9][0-9]{0,9})(\.[0-9]{1,4})?$/;

		if (value == "")
		{
			return true;
		}
		if (reg.test(value))
		{
			return true;
		} else
		{
			Helper.message.warn("最近采购价输入非法！");
			return false;
		}
	}, " ");

	// 最小库存格式验证
	$.validator.addMethod("isMinStockNum", function(value, element)
	{
		var reg = /^(0|[1-9][0-9]{0,9})(\.[0-9]{1,9})?$/;

		if (value == "")
		{
			return true;
		}
		if (reg.test(value))
		{
			return true;
		} else
		{
			Helper.message.warn("最小库存输入非法！");
			return false;
		}
	}, " ");

});
