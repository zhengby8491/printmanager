<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript">
    function form_submit()
    {
        $("#btn_save").attr({"disabled":"disabled"});
        var request_json = $("#jsonForm").formToJson();
       $("#requestContent").html(JSON.stringify(request_json));
        Helper.request({
            url: "${pageContext.request.contextPath}/sms/send",
            data: request_json,//将form序列化成JSON字符串  
            success: function(data)
            {
                $("#sendResult").html("返回码："+data.code+"\r\n返回信息："+data.message);
                
                if(data.isSuccess==true) {
                    Helper.message.suc('发送成功!');
                    $("#sendResult").css("color","green");
                }else {
                	  Helper.message.warn(data.message);
                    $("#btn_save").removeAttr("disabled");
                    $("#sendResult").css("color","red");
                }
                
            },
            error: function(data)
            {
            	  Helper.message.warn(data.message);
            }
        });
    }
</script>
<title>测试短信接入商</title>
</head>
<body>
	<div class="layer_container">
		<!--表单部分START-->
		<form id="jsonForm">
			<div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							接入商编号：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" id="accountId" name="accountId">
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							摘要：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" id="digest" name="digest" />
							<span style="color: red">MD5(接入商编号+密钥+短信内容, "utf-8")</span>
						</div>
					</div>
				</div>
				<div class="cl">

					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							手机：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" id="mobile" name="mobile" />
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							内容：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<input type="text" class="input-txt input-txt_7" id="content" name="content" />
						</div>
					</div>

				</div>
			</div>
			<!--表单部分END-->
			<div style="margin-left: 850px; margin-top: 10px">
				<input class="nav_btn table_nav_btn" type="button" id="btn_save" onclick="form_submit();" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
			</div>
		</form>
		<div>请求：</div>
		<div>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;URL:
			<span style="color: blue">${pageContext.request.contextPath}/sms/send</span>
			</br>

			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;数据类型:
			<span style="color: blue">json</span>
			</br>

			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;内容示例:
			<span style="color: blue" id="requestContent"></span>

		</div>
		<div>
			结果：
			<div>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<span id="sendResult"></span>
			</div>
		</div>
	</div>
</body>
</html>
