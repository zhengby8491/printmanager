$(function() {
	$("#form-systemconfig").validate({
		submitHandler : function(form) {
			// 必须写在验证前面，否则无法ajax提交
			Helper.Remote.fromSubmit(form, {// 验证新增是否成功
				type : "post",
				dataType : "json",
				url:Helper.basePath+"/sys/systemconfig/update",
				async:false,
				success : function(data) {
					if (data.success) {
							parent.location.href=Helper.basePath+"/sys/systemconfig/list";
							Helper.popup.close();
					} else {
						layer.alert('更新失败：'+data.message);
					}
				}
			});
		},
		rules : {
			id : {required : true},
			value:{required:true}
		},
		messages : {
		  id : {required : "请输入参数标识"},
		  value: {required:"请输入参数值"}
		},
		onkeyup:false,
		focusCleanup:true
	});
});
