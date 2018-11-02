<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/wx/public.jsp"%>
<title>个人中心</title>
</head>

<body>
	<div class="page page-current">
		<div class="content sum_content index_content center_content">
			<div class="center_top">
				<div class="head_img">
					<img src="${ctxStatic }/wx/img/head.jpg" />
				</div>
				<p style="margin-top: 0.7rem;">
					<span class="company_name">${userVo.companyName } </span>
				</p>
				<p style="margin-top: 0.3rem; color: #332f2f;">
					<c:if test="${!empty userVo.departmentName}">${userVo.departmentName } ：</c:if>${userVo.userName }</p>

			</div>
			<ul id="itemlist" class="list-container list-block" style="margin-top: 1rem;">
				<li class="sum_item">
					<i class="icon iconfont icon-dianhua1"></i>
					<span class="item_title">公司电话 ：${userVo.mobile }</span>
				</li>
				<li class="sum_item">
					<i class="icon iconfont icon-youxiang"></i>
					<span class="item_title">公司邮箱 ：${userVo.email }</span>
				</li>
				<li class="sum_item">
					<i style="color: #1F9A1F" class="iconfont">&#xe634;</i>
					<span class="item_title">联系人 ：${userVo.linkName }</span>
				</li>
				<a href="${ctx}/wx/pay/step5_shear1?linkName=${userVo.linkName }">
					<li class="sum_item">
						<i style="color: #1F9A1F" class="iconfont">&#xe609;</i>
						<span class="item_title">分享印管家</span>
						<span class="icon icon-right cl"></span>
					</li>
				</a>
				<a href="${ctx}/wx/menu/view/about"}>
					<li class="sum_item">
						<i class="icon iconfont icon-guanyu"></i>
						<span class="item_title">关于印管家</span>
						<span class="icon icon-right cl"></span>
					</li>
				</a>
			</ul>
			<div class="item-content">
				<c:if test="${type!='experience'}">
					<input id="btn" class="button button-fill button-warning cancel_bind_btn" type="button" value="解除绑定">
				</c:if>
				<c:if test="${type=='experience'}">
					<input id="btn" class="button button-fill button-warning cancel_bind_btn" type="button" value="转正式用户">
				</c:if>

			</div>
		</div>
		<jsp:include page="/WEB-INF/jsp/wx/foot.jsp">
			<jsp:param value="center" name="module" />
		</jsp:include>
	</div>
	<script type="text/javascript">
$(function(){

    $("#btn").click(function(){
    	$.confirm("确定解绑当前账号？",function(){
    		HYWX.request({
        		url:'${ctx}/wx/menu/ajaxDelBind',
                data:{},
                success:function(data){
                	if(data.success){
                	    window.location.href=HYWX.basePath+"/wx/homepage/center";
                	}else{
                	    $.toast(""+data.message,2000,"mytoast");
                	}
                },
                error:function(data){
                	$.toast(""+data.message,2000,"mytoast");
                }
       		})
    	})        
    });

    // sessionStorage.setItem('linkName','${userVo.linkName }');

})
</script>
</body>

</html>
