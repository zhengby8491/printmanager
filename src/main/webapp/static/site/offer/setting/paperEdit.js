/**
 * Author:       THINK
 * Create:       2017年10月31日 下午4:11:11
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 */
$(function()
{
	init();// 初始化表单提交功能

	/**
	 * 初始化表单提交功能
	 * @since 1.0, 2017年10月31日 上午11:25:14, THINK
	 */
	function init()
	{
		// 表单提交
		$("#jsonForm").validate({
			submitHandler : function(form)
			{
				// 必须写在验证前面，否则无法ajax提交
				Helper.request({
					url : Helper.basePath + "/offer/setting/updatePaper",
					data : $(form).formToJson(),
					success : function(data)
					{
						if (data.success)
						{
							parent.location.href = Helper.basePath + "/offer/setting/paper?type=" + $("#offerType").val();
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
				weight : {
					required : true,
				},
			},
			onkeyup : false,
			focusCleanup : true
		});
	}
})