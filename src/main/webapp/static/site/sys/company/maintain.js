$(function() {
	$("#form-company").validate({
		submitHandler : function(form) {
			// 必须写在验证前面，否则无法ajax提交
			Helper.Remote.fromSubmit(form, {// 验证新增是否成功
				type : "post",
				url:Helper.basePath+"/sys/company/maintenUpdate",
				dataType : "json",
				async:false,
				success : function(data) {
					if (data.success) {
							parent.location.href=Helper.basePath+"/sys/company/list";
							Helper.popup.close();
							return false;
					}else {
						layer.alert('更新失败：'+data.message);
					}
				}
			});
		},
		rules : {
		  contractCompanyName : {required : true},
		},
		messages : {
		  contractCompanyName: {required : "请输入公司名"},
		}/*,
		errorPlacement : function(error, element) {// 自定义提示错误位置
			layer.msg(error.html());
		}*/
	});
	
	$("#expireTime").val(new Date().add("y",1).format('yyyy-MM-dd'));
});
