<%@ tag language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<%@ attribute name="type" type="java.lang.String" required="true" description="手机验证码类别"%>
<%@ attribute name="code_css" type="java.lang.String" description="手机验证码输入框样式"%>
<%@ attribute name="btn_css" type="java.lang.String" description="手机验证码按钮样式"%>

<input class="${code_css }" type="text" placeholder="请输入短信验证码" required="true" id="validCode" name="validCode" />
<span class="${btn_css}">
	<input type="button" value="获取验证码" id="sendValidCode" />
</span>


<script type="text/javascript">
    $(function()
    {
        $("#sendValidCode").click(function()
        {
            if(Helper.isEmpty($("#mobile").val())) {
                Helper.message.warn("请输入手机号码");
            }else if(!Helper.validata.isMobile($("#mobile").val())) {
                Helper.message.warn("请输入正确手机号码");
            }else if(Helper.isEmpty($("#captcha").val())) {
                Helper.message.warn("请输入图片验证码");
            }else {
                $("#sendValidCode").attr("disabled", "disabled");
                $("#sendValidCode").val("发送中...");
              
                Helper.Remote.post(Helper.basePath + "/public/sendValidCode", {
                    "mobile": $("#mobile").val(),
                    "type": "${type}",
                    "captcha":$("#captcha").val()
                },function(result){
                    if(result.isSuccess) {
                        Helper.message.suc("发送成功！");
                        time();
                    }else {
                        Helper.message.warn(result.message);
                        $("#sendValidCode").removeAttr("disabled");
                        $("#sendValidCode").val("获取验证码");
                    }
                },function(){
                    Helper.message.warn("发送失败！");
                    $("#sendValidCode").removeAttr("disabled");
                    $("#sendValidCode").val("获取验证码");
                },true);
            }
        })
    });
    var wait = 60;
    function time()
    {
        var o = $("#sendValidCode");
        if(wait == 0) {
            o.removeAttr("disabled");
            o.val("获取验证码");
            wait = 60;
        }else {
            o.attr("disabled", "disabled");
            o.val("(" + wait + ")后重新发送");
            wait--;
            setTimeout(function()
            {
                time()
            }, 1000)
        }
    }
</script>