$(function()
{
	$("#jsonForm").validate({
		submitHandler : function(form)
		{// 必须写在验证前面，否则无法ajax提交
			Helper.request({
				url : Helper.basePath + "/basic/account/save",
				data : $(form).formToJson(),
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
							parent.location.href = Helper.basePath + "/basic/account/list";
						}
					} else
					{
						layer.alert('创建失败：' + data.message);
					}
				}
			});
		},
		rules : {
			branchName : {
				required : true,
			},
			bankNo : {
				required : true,
				isbankNo : true,
			},
			sort : {
				required : true,
			},
		},
		onkeyup : false,
		focusCleanup : true
	});

	$(document).on('keypress', "#bankNo", function()
	{
		return (/\d/.test(String.fromCharCode(event.keyCode)));
	});

	// 验证银行卡账号格式
	$.validator.addMethod("isbankNo", function(value, element)
	{
		var backNo = /^\d*$/;
		if (backNo.test(value))
		{
			return true;
		} else if (Helper.isNotEmpty(value))
		{
			Helper.message.warn("请输入正确卡号");
			return false;
		}
	}, " ");

});
