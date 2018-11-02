$(function()
{
	$("#jsonForm").validate({
		submitHandler : function()
		{// 必须写在验证前面，否则无法ajax提交
			Helper.request({
				url : Helper.basePath + "/basic/machine/save",
				data : $("#jsonForm").formToJson(),// 将form序列化成JSON字符串
				async : false,
				success : function(data)
				{
					if (data.success)
					{ 
						if (parent.$this.type == 'WORK')
						{
							parent.fillSelection();
							Helper.popup.close();
						} else 
						{
							parent.location.href = Helper.basePath + "/basic/machine/list";
						}
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
			}
		},
		onkeyup : false,
		focusCleanup : true
	});
	// 规格键盘输入控制
	$(document).on('keypress', "#maxStyle,#minStyle", function()
	{
		return (/\d|\*/.test(String.fromCharCode(event.keyCode)));
	});
	// 规格失去焦点判断
	$(document).on('blur', "#maxStyle,#minStyle", function()
	{
		if (Helper.validata.isNotEmpty($(this).val()))
		{
			if (!/^[1-9][0-9]{0,5}\*[1-9][0-9]{0,5}$/.test($(this).val()))
			{
				Helper.message.tips("请录入正确的规格格式 xxxx*xxxx", this);
				$(this).val("");
			}
		}
	});
});
