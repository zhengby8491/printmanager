$(function()
{
	$("#resetForm").validate({
		messages : {
			searchContent : {
				required : '<span class="m_error m_error_id"><i>*</i>用户名或手机不能为空</span>'
			},
			captcha : {
				required : '<span class="m_error step1_error_yzm"><i>*</i>验证码不能为空</span>'
			}
		}
	})
});