$(function()
{
	$("#resetForm").validate({
		submitHandler : function(form)
		{
			form.submit();
		},
		rules : {
			"validCode" : {
				required : true,
				remote : {
					type : "POST",
					url : Helper.basePath + '/public/checkValidCode',
					dataType : "json",
					data : {
						mobile : function()
						{
							return $("#mobile").val().trim()
						},
						code : function()
						{
							return $("#validCode").val().trim()
						}
					}
				}
			}
		},
		messages : {
			validCode : {
				required : '<span class="m_error step2_error_yzm"><i>*</i> 请输入验证码</span>',
				remote : '<span class="m_error step2_error_yzm"><i>*</i> 请输入正确的验证码</span>'
			}
		},
		onkeyup : false,
		onfocusout : false,
		onsubmit : true
	});
});