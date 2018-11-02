$(function()
{
	$("#jsonForm").validate({
		submitHandler : function()
		{// 必须写在验证前面，否则无法ajax提交
			var msg = "变更产品名称将会把所有历史单据的产品名称全部变更，请问是否继续变更？"
			Helper.message.confirm(msg, function(index)
			{
				Helper.request({
					url : Helper.basePath + "/basic/product/updateName",
					data : $("#jsonForm").formToJson(),// 将form序列化成JSON字符串
					async : false,
					success : function(data)
					{
						if (data.success)
						{
							Helper.message.suc("变更成功");
							setTimeout(function(){
								Helper.popup.close();
								$(parent.document.getElementById("btn_search")).trigger("click");
							},2000);
						} else
						{
							Helper.message.warn(data.message);
						}
					}
				});
			})

		},
		rules : {
			name : {
				required : true,
			}
		},
		onkeyup : false,
		focusCleanup : true
	});
})