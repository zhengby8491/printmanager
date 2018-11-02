$(function() {
	$("#jsonForm").validate(
			{
				submitHandler : function(form) {// 必须写在验证前面，否则无法ajax提交
				  Helper.request({// 验证新增是否成功
						url : Helper.basePath + "/basic/account/update",
						data: $(form).formToJson(),
						async : false,
						success : function(data) {
							if (data.success) {
									parent.location.href = Helper.basePath + "/basic/account/list";
							} else {
								layer.alert('更新失败：' + data.message);
							}
						}
					});
				},
        rules : {
          branchName : {
            required : true,
          },
          bankNo : {
            required : true,isbankNo:true,
          },
          sort : {
            required : true,
          },
        },
        onkeyup : false,
        focusCleanup : true
			});
	
      $(document).on('keypress',"#bankNo",function(){
        return (/\d/.test(String.fromCharCode(event.keyCode)));
      });
      
      //验证银行卡账号格式
      $.validator.addMethod("isbankNo", function(value,element) {
        var backNo = /^\d*$/;
        if(backNo.test(value))
        {
          return true;
        }else if(Helper.isNotEmpty(value))
        {
          Helper.message.warn("请输入正确卡号"); 
          return false;
        }
    }," ");
});
