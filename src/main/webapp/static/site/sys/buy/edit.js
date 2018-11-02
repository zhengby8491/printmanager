$(function(){
	$("#jsonForm").validate(
			{
				submitHandler : function() {
				
				  Helper.request({// 验证新增是否成功
						url : Helper.basePath + "/sys/buy/update",
						data:$("#jsonForm").formToJson(),
						async : false,
						success : function(data) {
							if (data.success) {
							    Helper.popup.close();
							    parent.triggerClick();    //location.href = Helper.basePath + "/sys/notice/list";
							} else {
								layer.alert('更新失败：' + data.message);
							}
						}
					});
				},
				rules : {
				  title : {
						required : true,
					}
				},
				onkeyup : false,
				focusCleanup : true
			});
});
