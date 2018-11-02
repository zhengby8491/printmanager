$(function()
{
	$("#jsonForm").validate({
		submitHandler : function()
		{// 必须写在验证前面，否则无法ajax提交

			Helper.request({// 验证新增是否成功
				url : Helper.basePath + "/basic/deliveryClass/save",
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
							parent.location.href = Helper.basePath + "/basic/deliveryClass/list";
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
			},
			sort : {
				required : true,
			}
		},
		onkeyup : false,
		focusCleanup : true
	});
});
