$(function()
{
	$("#jsonForm").validate({
		submitHandler : function()
		{// 必须写在验证前面，否则无法ajax提交

			Helper.request({// 验证新增是否成功
				url : Helper.basePath + "/basic/taxRate/update",
				data : $("#jsonForm").formToJson(),// 将form序列化成JSON字符串
				async : false,
				success : function(data)
				{
					if (data.success)
					{
						parent.location.href = Helper.basePath + "/basic/taxRate/list";
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
			revenueType : {
				required : true,
			},
			percent : {
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
