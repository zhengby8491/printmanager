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
				url:Helper.basePath+"/sys/user/save",
				async:false,
				success : function(data) {
					if (data.success) {
							parent.location.href=Helper.basePath+"/sys/user/list";
							Helper.popup.close();
					} else {
						layer.alert('添加失败：'+data.message);
					}
				}
			});
		},
		rules : {
			userName : {
				required : true,
        stringCheck:true,
				maxlength:16,
				remote : {
					type : "POST",
					url : Helper.basePath+'/sys/user/exist/userName',
					dataType : "json",
					data : {userName : function() {return $("#userName").val();}
					}
				}
			}, 
			"mobile": {
          isMobile: true,
          remote: {
              type: "POST",
              url: Helper.basePath + '/sys/user/exist/mobile',
              dataType: "json",
              data: {
                  mobile: function()
                  {
                      return $("#mobile").val();
                  }
              }
          }
      },
			password:{
				minlength: 6,
				required:true
				},
			email:{email:true}
		},
		messages : {
		  mobile: {
            isMobile: '<span class="m_error"><i>*</i>手机号码格式有误</span>',
            remote: '<span class="m_error"><i>*</i>手机号码已被使用</span>'
        },
			userName: {required : "请输入用户名",remote : "用户名已经存在"},
			password: {required:"请输入密码"}
		},
		onkeyup:false,
		focusCleanup:false//如果该属性设置为True, 那么控件获得焦点时，移除出错的class定义，隐藏错误信息
		/*errorPlacement : function(error, element) {// 自定义提示错误位置
			layer.msg(error.html());
		}*/
	});
	loadColleague();
});
