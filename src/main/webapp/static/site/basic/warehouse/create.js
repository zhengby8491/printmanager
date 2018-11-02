$(function()
{
	$("#jsonForm").validate({
		submitHandler : function()
		{// 必须写在验证前面，否则无法ajax提交

			Helper.request({// 验证新增是否成功
				url : Helper.basePath + "/basic/warehouse/save",
				data : $("#jsonForm").formToJson(),// 将form序列化成JSON字符串
				async : false,
				success : function(data)
				{
					if (data.success)
					{
						// 快捷方式添加资料后返回后方法
						if (parent.$this.type && data.obj != null)
						{
							// 仓库分类型
							if(data.obj.warehouseType == parent.$this.wareType)
							{
								parent.fillSelection(data.obj);
							}
							Helper.popup.close();
						}else
						{
							// 正常方式添加资料后返回方法
							parent.location.href = Helper.basePath + "/basic/warehouse/list";
						}
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
			warehouseType : {
				required : true,
			},
			isBad : {
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
