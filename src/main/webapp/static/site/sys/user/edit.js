$(function() {
	$("#form-user").validate({
		submitHandler : function(form) {
      if(Helper.isEmpty($("#roles:checked").val()))
      {
         Helper.message.warn("至少选择一个角色")
         return false;
      }
			// 必须写在验证前面，否则无法ajax提交
			Helper.Remote.fromSubmit(form, {// 验证新增是否成功
				type : "post",
        dataType : "json",
				url:Helper.basePath+"/sys/user/update",
				async:false,
				success : function(data) {
					if (data.success) {
							parent.location.href=Helper.basePath+"/sys/user/list";
							Helper.popup.close();
							return false;
					} else {
						layer.alert('更新失败：'+data.message);
					}
				}
			});
		},
		rules : {
		  "mobile": {
          isMobile: true,
          remote: {
              type: "POST",
              url: Helper.basePath + '/sys/user/exist/mobile',
              dataType: "json",
              data: {
                  mobile: function()
                  {
                      return $("#mobile").val()
                  },
                  userId:function(){
                      return $("#user_id").val()
                  }
              }
          }
      },
			email:{email:true}
		}, messages: {
        mobile: {
            isMobile: '<span class="m_error"><i>*</i>手机号码格式有误</span>',
            remote: '<span class="m_error"><i>*</i>手机号码已被使用</span>'
        }
    },
    onkeyup: false,
    onfocusout: false,
    onsubmit: true/*,
		errorPlacement : function(error, element) {// 自定义提示错误位置
			layer.msg(error.html());
		}*/
	});
	 loadColleague();
});
