$(function()
{
	$("#jsonForm").validate({
		submitHandler : function()
		{// 必须写在验证前面，否则无法ajax提交

			Helper.request({// 验证新增是否成功
				url : Helper.basePath + "/basic/settlementClass/update",
				data : $("#jsonForm").formToJson(),
				async : false,
				success : function(data)
				{
					if (data.success)
					{
						parent.location.href = Helper.basePath + "/basic/settlementClass/list";
					} else
					{
						layer.alert('更新失败：' + data.message);
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
