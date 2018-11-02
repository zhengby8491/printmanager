$(function()
{
	$("#jsonForm").validate({
		submitHandler : function(form)
		{
			Helper.Remote.fromSubmit(form, {
				type : "post",
				dataType : "json",
				url : Helper.basePath + "/basic/employee/update",
				async : false,
				success : function(data)
				{
					if (data.success)
					{
						parent.location.href = Helper.basePath + "/basic/employee/list";
						Helper.popup.close();
						return false;
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
			}
		},
		onkeyup : false,
		focusCleanup : true
	});
});
