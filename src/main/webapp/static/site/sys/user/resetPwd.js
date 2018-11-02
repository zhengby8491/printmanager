$(function() {
  $("#form-user").validate({
    submitHandler : function(form) {
      // 必须写在验证前面，否则无法ajax提交
      Helper.Remote.fromSubmit(form, {// 验证新增是否成功
        type : "post",
        dataType : "json",
        url:Helper.basePath+"/sys/user/resetPwd",
        async:false,
        success : function(data) {
          if(data.success){
              parent.location.href=Helper.basePath+"/sys/user/list";
              Helper.popup.close();
          }else {
            layer.alert('更新失败：'+data.message);
          }
        }
      });
    },
    rules : {
      password:{
    	  	required : true,	
			minlength:6,
			maxlength:16
    	  }
    },
    messages : {
      password: {required:"密码不能为空"}
    },
  });
});