<%@ tag language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<%@ attribute name="name" type="java.lang.String" required="true" description="验证码输入框名称"%>
<%@ attribute name="inputCssStyle" type="java.lang.String" required="false" description="验证框样式"%>
<%@ attribute name="imageCssStyle" type="java.lang.String" required="false" description="验证码图片样式"%>
<%@ attribute name="buttonCssStyle" type="java.lang.String" required="false" description="看不清按钮样式"%>
<input type="text" id="${name}" placeholder="验证码" name="${name}" maxlength="5" class="txt required login_input login_input_captcha" style="${inputCssStyle}" />
<img class="login_captcha captcha" alt="验证码" src="${path}/servlet/captcha" onclick="$('.${name}Refresh').click();" class="mid ${name}" style="${imageCssStyle}" />
<a class="change_captcha" href="javascript:" onclick="$('.${name}').attr('src','${path}/servlet/captcha?'+new Date().getTime());" class="mid ${name}Refresh" style="${buttonCssStyle}">换一张</a>