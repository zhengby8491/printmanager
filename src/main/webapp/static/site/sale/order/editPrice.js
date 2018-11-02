$(function()
{
	$("#jsonForm").validate({
		submitHandler : function()
		{// 必须写在验证前面，否则无法ajax提交
			Helper.request({// 验证新增是否成功
				url : Helper.basePath + "/sale/order/changePrice",
				data : $("#jsonForm").formToJson(),
				async : false,
				success : function(data)
				{

					if (data.success)
					{
						parent.location.href = Helper.basePath + "/sale/order/view/" + $("#saleOrderDetail").val();
					} else
					{
						layer.alert('创建失败：' + data.message);
					}
				}
			});
		},
		rules : {
			name : {
				required : true,
			}
		},
		onkeyup : false,
		focusCleanup : true
	// 如果该属性设置为True,
	// 那么控件获得焦点时，移除出错的class定义，隐藏错误信息
	});

	$("#price").on("blur", function()
	{

		var price = Number($(this).val());
		var qty = $("#qty").val();
		var money = (price.mul(Number(qty))).toFixed(2);
		$("#money").val(money);
	});

	$("#money").on("blur", function()
	{
		var money = Number($(this).val()).tomoney();
		var qty = $("#qty").val();
		var price = (money.div(Number(qty))).toFixed(4);
		$("#price").val(price);
	});
});