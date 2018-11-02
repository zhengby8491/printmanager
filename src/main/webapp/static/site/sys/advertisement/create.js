$(function()
{
	$("#jsonForm").validate({

		submitHandler : function()
		{// 必须写在验证前面，否则无法ajax提交
			$(jsonForm).ajaxSubmit({
				url : Helper.basePath + "/sys/advertisement/save",
				type : 'POST',
				dataType : 'json',
				success : function(data)
				{
					if (data.success)
					{
						parent.location.href = Helper.basePath + "/sys/advertisement/list";
					} else
					{
						layer.alert('创建失败：' + data.message);
					}
				}
			});
		},
		rules : {
			pic : {
				required : true
			},
		},
		messages : {
			pic : "请选择图片"
		},
		onkeyup : false
	});
});