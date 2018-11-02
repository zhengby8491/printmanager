<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html class="root61">
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>账号绑定</title>
</head>
<body class="bind">
	<div class="header">
		<div class="logo-con w cl">
			<a href="" class="logo"></a>
			<div class="logo-title">${userShareTypeText }</div>
		</div>
	</div>
	<div class="container w">
		<div class="main cl">
			<div class="register-tabNav clearfix">
				<div class="r-tab r-tab-cur">
					<span class="icon i-bind"></span>
					<span>已有应管家账号，请绑定</span>
				</div>
				<div class="r-tab">
					<span class="icon i-reg"></span>
					<span>没有印管家账号，请完善资料</span>
				</div>
			</div>
			<div class="reg-tab-con">
				<sys:message content="${param.error_message}" />
				<div class="r-tabCon bind-login-content hy_count">
					<div class="account-login-panle">
						<div class="wellcome-tip">
							<img src="${figureurl_qq_2 }" width="28" height="28">
							<p>Hi, ${nickname }! 欢迎来印管家，完成绑定后可以${userShareTypeText }账号一键登录哦~</p>
						</div>
						<form id="login-form" method="post" onsubmit="return false;" novalidate="novalidate" action="${ctx}/public/user_center_bind">
							<div class="login-error-container">
								<div id="login-error" class="login-error">
									<span id="login-server-error" class="error"></span>
								</div>
							</div>
							<div class="m_item" id="form-item-account">
								<label class="m_label">印管家账号</label>
								<input type="text" id="userName" name="userName" class="m_input" autocomplete="off" placeholder="邮箱/用户名/已验证手机">
							</div>
							<div class="m_item">
								<label class="m_label">输 入 密 码</label>
								<input autocomplete="off" type="password" name="passWord" id="passWord" class="m_input" placeholder="密码">
							</div>
							<button type="submit" class="btn-register" id="form-bind" style="margin-top: 50px">立即绑定</button>
						</form>
					</div>
				</div>
				<div class="r-tabCon reg-form hy_count" style="display: none">
					<div class="wellcome-tip">
						<img src="${figureurl_qq_2 }" width="28" height="28">
						<p>Hi, ${nickname }! 欢迎来印管家，完成绑定后可以${userShareTypeText }账号一键登录哦~</p>
					</div>
					<!-- 注册账号Start -->
					<div class="l main_left">
						<form id="registerForm" method="post" action="${ctx }/register_submit">
							<h3>注&nbsp;册&nbsp;账&nbsp;号</h3>
							<div>
								<div class="m_item">
									<label class="m_label">用户名：</label>
									<input class="m_input" id="userName" name="userName" placeholder="请输入用户名" type="text" />
								</div>
								<div class="m_item">
									<label class="m_label">手机号码：</label>
									<input class=" m_input" placeholder="请输入要注册的手机号码" id="mobile" name="mobile" type="text" />
								</div>
								<div class="m_item register_pw">
									<label class="m_label">密码：</label>
									<input class="m_input" name="password" type="password" placeholder="请输入密码，至少六个字符" id="password" />
								</div>
								<div class="m_item">
									<label class="m_label">确认密码：</label>
									<input class="m_input" type="password" placeholder="请确认密码" name="confirm_password" id="confirm_password" />
								</div>
								<div class="m_item register_yzm" id="third_yzm">
									<label class="m_label" style="float: left;">验证码：</label>
									<sys:captcha name="captcha" />
								</div>
								<div class="m_item register_yzm">
									<label class="m_label">验证码：</label>
									<sys:smsValidCode type="REGEDIT_VALIDCODE" code_css="m_code" btn_css="m_send m_active" />
								</div>
								<div class="m_item">
									<label class="m_label"></label>
									<input class="m_submit m_active" type="submit" value="注 册" />
								</div>
							</div>
						</form>
					</div>
					<!-- 	注册账号End -->
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
$(function(){
    $(".r-tab").click(function(){
    	$(this).addClass("r-tab-cur").siblings().removeClass("r-tab-cur");
    	$(".reg-tab-con").children().eq($(this).index()).show().siblings().hide();
    })
     $("#login-form").validate({
            submitHandler: function(form)
            {
                form.submit();
            },
            rules: {
                "userName": {
                    required: true,
                    maxlength:30
                },
                "passWord": {
                    required: true,
                }
            },
            messages: {
                userName: {
                    required: '<span class="m_error"><i>*</i>请输入用户名</span>',
                    maxlength: '<span class="m_error"><i>*</i>最多可以输入 {0}个字符</span>'
                },
                passWord: {
                    required: '<span class="m_error"><i>*</i>请输入密码</span>',
                }
            },
            onkeyup: false,
            onfocusout: false,
            onsubmit: true
        });
    $("#registerForm").validate({
            submitHandler: function(form)
            {
                form.submit();
            },
            rules: {
                "userName": {
                    required: true,
                    maxlength:30,
                    stringCheck:true,
                    remote: {
                        type: "POST",
                        url: Helper.basePath + '/register/exist/userName',
                        dataType: "json",
                        data: {
                            userName: function()
                            {
                                return $("#registerForm #userName").val()
                            }
                        }
                    }
                },
                "mobile": {
                    required: true,
                    isMobile: true,
                    remote: {
                        type: "POST",
                        url: Helper.basePath + '/register/exist/mobile',
                        dataType: "json",
                        data: {
                            mobile: function()
                            {
                                return $("#registerForm #mobile").val()
                            }
                        }
                    }
                },
                "password": {
                    minlength: 6,
                    required: true,
                    isPwd:false
                },
                "confirm_password": {
                    required: true,
                    minlength: 6,
                    equalTo: "#password"
                },
                "validCode": {
                    required: true,
                    remote: {
                        type: "POST",
                        url: Helper.basePath + '/public/checkValidCode',
                        dataType: "json",
                        data: {
                            mobile: function(){ return $("#mobile").val().trim()},
                            code: function(){ return $("#validCode").val().trim()}
                        }
                    }
                }
            },
            messages: {
                userName: {
                    required: '<span class="m_error"><i>*</i>请输入用户名</span>',
                    maxlength: '<span class="m_error"><i>*</i>最多可以输入 {0}个字符</span>',
                    stringCheck:'<span class="m_error"><i>*</i>只能包含中文、英文、数字、下划线等字符</span>',
                    remote: '<span class="m_error"><i>*</i>用户名已被注册</span>'
                },
                mobile: {
                    required: '<span class="m_error"><i>*</i>请输入手机号码</span>',
                    isMobile: '<span class="m_error"><i>*</i>手机号码格式有误</span>',
                    remote: '<span class="m_error"><i>*</i>手机号码已被注册</span>'
                },
                password: {
                    required: '<span class="m_error"><i>*</i>请输入密码</span>',
                    isPwd:'<span class="m_error"><i>*</i>以字母开头，长度在6-12之间，只能包含字符、数字和下划线。</span>',
                },
                confirm_password: {
                    required: '<span class="m_error"><i>*</i>请输入密码</span>',
                    minlength: '<span class="m_error"><i>*</i>最少要输入 6 个字符</span>',
                    equalTo: '<span class="m_error"><i>*</i>密码和确认密码不一致</span>'
                },
                validCode: {
                    required: '<span class="m_error"><i>*</i> 请输入验证码</span>',
                    remote: '<span class="m_error"><i>*</i> 请输入正确的验证码</span>'
                }
            },
            onkeyup: false,
            onfocusout: false,
            onsubmit: true
        });
})
</script>

</body>
</html>