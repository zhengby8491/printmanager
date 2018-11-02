$(function()
{
	$("#jsonForm").validate({
		submitHandler : function()
		{// 必须写在验证前面，否则无法ajax提交
			Helper.request({// 验证新增是否成功
				url : Helper.basePath + "/basic/paymentClass/update",
				data : $("#jsonForm").formToJson(),
				success : function(data)
				{
					if (data.success)
					{
						parent.location.href = Helper.basePath + "/basic/paymentClass/list";
					} else
					{
						layer.alert('更新失败：' + data.message);
					}
				}
			});
		},
		rules : {
			name : {
				required : true,
			},
			payType : {
				required : true,
			},
			dayNum : {
				required : true,
			},
			sort : {
				required : true,
			}
		},
		onkeyup : false,
		focusCleanup : true
	});
	$(document).on('keypress', "#dayNum", function()
	{
		return (/\d/.test(String.fromCharCode(event.keyCode)));
	});

});
