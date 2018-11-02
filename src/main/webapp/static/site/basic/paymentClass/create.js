$(function()
{
	$("#jsonForm").validate({
		submitHandler : function()
		{// 必须写在验证前面，否则无法ajax提交

			Helper.request({// 验证新增是否成功
				url : Helper.basePath + "/basic/paymentClass/save",
				data : $("#jsonForm").formToJson(),
				async : false,
				success : function(data)
				{
					if (data.success)
					{
						// 快捷方式添加资料后返回后方法
						if (parent.$this.type && data.obj != null)
						{
							parent.fillSelection(data.obj);
							Helper.popup.close();
						} else
						{
							// 正常方式添加资料后返回方法
							parent.location.href = Helper.basePath + "/basic/paymentClass/list";
						}
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
