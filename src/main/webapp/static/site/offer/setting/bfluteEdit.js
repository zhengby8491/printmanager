/**
 * Author:       THINK
 * Create:       2017年11月1日 下午2:36:54
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 */
$(function()
{
	init();// 初始化表单提交功能

	/**
	 * 初始化表单提交功能
	 * @since 1.0, 2017年11月1日 上午11:45:15, THINK
	 */
	function init()
	{
		// 表单提交
		$("#jsonForm").validate({
			submitHandler : function(form)
			{
				// 必须写在验证前面，否则无法ajax提交
				Helper.request({
					url : Helper.basePath + "/offer/setting/updateBflute",
					data : $(form).formToJson(),
					success : function(data)
					{
						if (data.success)
						{
							parent.location.href = Helper.basePath + "/offer/setting/bflute?type=" + $("#offerType").val();
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