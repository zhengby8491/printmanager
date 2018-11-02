$(function()
{
	$("#jsonForm").validate({
		submitHandler : function(form)
		{
			Helper.Remote.fromSubmit(form, {
				type : "post",
				dataType : "json",
				url : Helper.basePath + "/basic/employee/save",
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
							parent.location.href = Helper.basePath + "/basic/employee/list";
							Helper.popup.close();
							return false;
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
	// 如果该属性设置为True,
	// 那么控件获得焦点时，移除出错的class定义，隐藏错误信息
	});
});
