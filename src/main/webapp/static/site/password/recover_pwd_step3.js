$(function()
{
	$("#resetForm").validate({
		submitHandler : function(form)
		{
			Helper.post(Helper.basePath + "/password/recover/reset", {
				"password" : $("#password").val()
			},// 将form序列化成JSON字符串
			function(data)
			{
				if (data.isSuccess)
				{
					Helper.message.suc('密码重置成功!');
					location.href = Helper.basePath;
				} else
				{
					Helper.message.warn(data.message);
				}
			}, function(data)
			{
				Helper.message.suc('操作异常');
			});
		},
		rules : {
			"password" : {
				required : true,
				minlength : 6,
			},
			"confirm_password" : {
				required : true,
				minlength : 6,
				equalTo : "#password"
			}
		},
		messages : {
			password : {
				required : '<span class="m_error step3_error"><i>*</i> 请填写新密码</span>',
				minlength : '<span class="m_error step3_error"><i>*</i> 至少输入6个字符</span>'
			},
			confirm_password : {
				required : '<span class="m_error step3_error"><i>*</i> 请填写新密码</span>',
				minlength : '<span class="m_error step3_error"><i>*</i> 至少输入6个字符</span>',
				equalTo : '<span class="m_error step3_error"><i>*</i> 两次输入密码不一致</span>'
			}
		},
		onkeyup : false,
		onfocusout : false,
		onsubmit : true
	});
});