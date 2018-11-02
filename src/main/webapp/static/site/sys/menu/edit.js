$(function() {

	$("#form-menu").validate({
		submitHandler : function(form) {// 必须写在验证前面，否则无法ajax提交
			Helper.Remote.fromSubmit(form, {// 验证新增是否成功
				type : "post",
				url:Helper.basePath+"/sys/menu/update",
				dataType : "json",//ajaxSubmi带有文件上传的。不需要设置json
				async:false,
				success : function(data) {
					if (data.success) {
							//parent.location.href=Helper.basePath+"/sys/menu/list";
							Helper.popup.close();
							return false;
					} else {
						layer.alert('更新失败：'+data.message);
					}
				}
			});
		}/*,
		errorPlacement : function(error, element) {// 自定义提示错误位置
			layer.msg(error.html());
		}*/
	});
});
