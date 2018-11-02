<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/wx/public.jsp"%>
<title>印管家登录</title>
</head>

<body>
	<div class="page page-current bind_content">
		<div class="content content-padded list-block ">
			<div class="logo_div">
				<img src="${ctxStatic}/wx/img/logoV3.png">
			</div>
			<ul>
				<li>
					<div class="item-content">
						<div class="item-media">
							<i class="icon icon-form-name"></i>
						</div>
						<div class="item-inner">
							<div class="item-title label">
								<i class="icon iconfont icon-zhanghu"></i>
								账号
							</div>
							<div class="item-input">
								<input id="userName" type="text" placeholder="用户名/手机号码" />
							</div>
						</div>
					</div>
				</li>
				<li>
					<div class="item-content">
						<div class="item-media">
							<i class="icon icon-form-email"></i>
						</div>
						<div class="item-inner">
							<div class="item-title label">
								<i class="icon iconfont icon-mimaguanli"></i>
								密码
							</div>
							<div class="item-input">
								<input id="passWord" type="password" placeholder="密码" />
							</div>
						</div>
					</div>
				</li>
				<li style="margin-top: 2rem;">
					<div class="item-content">
						<input id="btn" class="button button-fill button-success" type="button" value="登录">
						<input id="wxUrl" type="hidden" value="${WXTurnUrl }">
					</div>
				</li>
				<li style="margin-top: 4rem;">
					<div class="item-content">
						<div class="item-inner">
							<div class="item-title">
								<i class="icon iconfont icon-dianhua1"></i>
								服务电话：400-800-8755
							</div>
						</div>
					</div>
				</li>
			</ul>
		</div>
	</div>
	<script type="text/javascript">
$(function(){

    $("#btn").click(function(){
    	var userName = $("#userName").val().trim();
    	var passWord = $("#passWord").val().trim();
    	if(userName==""||passWord==""){
    		$.alert('账号和密码不能为空！');
    		return ;
    	}
        $("#btn").attr({"disabled":"disabled"});
        HYWX.requestByObj({
        		url:'${ctx}/wx/menu/bind',
                data:{'userName':$("#userName").val(),'passWord':$("#passWord").val()},
                success:function(data){
                	if(data.success){
                	    window.location.href=$("#wxUrl").val();
                	}else{
                		$.alert(""+data.message);
                	    $("#btn").removeAttr("disabled");
                	}
                },
                error:function(data){
                	$.alert(""+data.message);
                    $("#btn").removeAttr("disabled");
                }
        	})
    });
})
</script>
</body>

</html>
