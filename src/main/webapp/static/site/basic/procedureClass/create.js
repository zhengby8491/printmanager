$(function()
{
	$("#jsonForm").validate({
		submitHandler : function()
		{// 必须写在验证前面，否则无法ajax提交

			Helper.request({// 验证新增是否成功
				url : Helper.basePath + "/basic/procedureClass/save",
				data : $("#jsonForm").formToJson(),
				async : false,
				success : function(data)
				{
					if (data.success)
					{
						parent.location.href = Helper.basePath + "/basic/procedureClass/list";
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
});
