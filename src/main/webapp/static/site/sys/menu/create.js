$(function() {
	$("#form-menu").validate({
		submitHandler : function(form) {// 必须写在验证前面，否则无法ajax提交
			Helper.Remote.fromSubmit(form, {// 验证新增是否成功
				type : "post",
				dataType : "json",
				url:Helper.basePath+"/sys/menu/save",
				async:false,
				success : function(data) {
					if (data.success) {
							//parent.location.href=Helper.basePath+"/sys/menu/list";
							Helper.popup.close();
							return false;
					} else {
						layer.alert('添加失败：'+data.message);
					}
				}
			});
		},
		onkeyup:true,
		focusCleanup:true//如果该属性设置为True, 那么控件获得焦点时，移除出错的class定义，隐藏错误信息
		/*errorPlacement : function(error, element) {// 自定义提示错误位置
			layer.msg(error.html());
		}*/
	});

});
