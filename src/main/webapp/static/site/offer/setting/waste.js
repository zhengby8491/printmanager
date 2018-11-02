/**
 * Author:		   think
 * Create:	 	   2017年10月16日 上午11:50:58
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 */
$(function()
{

	init(); // 初始化

	/**
	 * 
	 * 初始化功能、按钮事件
	 * 
	 * @since 1.0, 2017年10月16日 上午11:40:02, think
	 */
	function init()
	{
		// 保存
		$("#btnSave").click(function()
		{
			formSubmit();
		});
		// 取消
		$("#btnCancel").click(function()
		{
			closeTabAndJump("")
		});
	}

	/**
	 * 
	 * 表单提交
	 * @since 1.0, 2017年10月16日 上午11:49:45, think
	 */
	function formSubmit()
	{
		var sw = $("#settingWasteForm").formToJson();
		if (sw && sw.swList)
		{
			// 验证必填
			for (var i = 0; i < sw.swList.length; i++)
			{
				var settingWaste = sw.swList[i];
				for ( var p in settingWaste)
				{
					if (Helper.isEmpty(settingWaste[p]))
					{
						Helper.message.warn("请输入必填数据!");
						return false;
					}
				}
			}
		}

		Helper.request({
			url : Helper.basePath + "/offer/setting/saveWaste",
			data : sw.swList,
			success : function(data)
			{
				if (data.success)
				{
					Helper.message.suc("已保存!");
				} else
				{
					Helper.message.warn('保存失败! ' + data.message);
				}
			},
			error : function(data)
			{
				Helper.message.warn('保存失败! ' + data.message);
			}
		});
	}

});
