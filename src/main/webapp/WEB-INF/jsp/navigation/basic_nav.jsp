<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>基础设置</title>
</head>
<style type="text/css">
a{color:black}
</style>
<body>
	<div class="page-container">
		<div class="page-border page-nav-wrap">
			<!--导航页面-->
			<div class="page-nav">
				<!--导航图标-->
				<div class="nav_icon_container" style="width: auto; height: 550px">
					<h4>基础设置流程图</h4>
					<div class="n_line"></div>
					<div class="basic_icons">
						<div class="basic_icon_item">
							<span>
								<img src="${ctxStatic }/layout/images/navigation/nav_icon_24.png">
								<p>基础资料</p>
							</span>
							<ul>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('客户信息','/basic/customer/list')">客户信息</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('产品信息','/basic/product/list')">产品信息</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('供应商信息','/basic/supplier/list')">供应商信息</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('材料信息','/basic/material/list')">材料信息</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('单位信息','/basic/unit/list')">单位信息</a>
								</li>
								<ul>
						</div>
						<div class="basic_icon_item">
							<span>
								<img src="${ctxStatic }/layout/images/navigation/nav_icon_25.png">
								<p>财务设置</p>
							</span>
							<ul>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('税收信息','/basic/taxRate/list')">税收信息</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('账户信息','/basic/account/list')">账户信息</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('付款方式','/basic/paymentClass/list')">付款方式</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('结算方式','/basic/settlementClass/list')">结算方式</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('送货方式','/basic/deliveryClass/list')">送货方式</a>
								</li>
								<ul>
						</div>
						<div class="basic_icon_item">
							<span>
								<img src="${ctxStatic }/layout/images/navigation/nav_icon_26.png">
								<p>基础分类</p>
							</span>
							<ul>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('客户分类','/basic/customerClass/list')">客户分类</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('产品分类','/basic/productClass/list')">产品分类</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('供应商分类','/basic/supplierClass/list')">供应商分类</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('材料分类','/basic/materialClass/list')">材料分类</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('工序分类','/basic/procedureClass/list')">工序分类</a>
								</li>
								<ul>
						</div>
						<div class="basic_icon_item">
							<span>
								<img src="${ctxStatic }/layout/images/navigation/nav_icon_27.png">
								<p>期初设置</p>
							</span>
							<ul>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('账户期初列表','/begin/account/list')">账户期初</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('账户期初列表','/begin/customer/list')">客户期初</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('供应商期初列表','/begin/supplier/list')">供应商期初</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('产品期初列表','/begin/product/list')">产品期初</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('材料期初列表','/begin/material/list')">材料期初</a>
								</li>
								<ul>
						</div>
						<div class="basic_icon_item">
							<span>
								<img src="${ctxStatic }/layout/images/navigation/nav_icon_23.png">
								<p>组织机构</p>
							</span>
							<ul>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('职位信息','/basic/position/list')">职位信息</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('部门信息','/basic/department/list')">部门信息</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('员工信息','/basic/employee/list')">员工信息</a>
								</li>
								<ul>
						</div>
						<div class="basic_icon_item">
							<span>
								<img src="${ctxStatic }/layout/images/navigation/nav_icon_22.png">
								<p>生产设置</p>
							</span>
							<ul>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('仓库信息','/basic/warehouse/list')">仓库信息</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('工序信息','/basic/procedure/list')">工序信息</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('机台信息','/basic/machine/list')">机台信息</a>
								</li>

								<ul>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
			$(".finance_icon_list_item a").mouseover(function(){
				var idx = $(this).index();
				var imgPath = $(this).children("span").children("img").attr("src");
				var imgName =  imgPath.substr(imgPath.lastIndexOf("/")+1);
				var nameArr = imgName.split("_");
				var newImg = nameArr[0]+"_"+nameArr[1]+"_h"+nameArr[2];
				$(this).children("span").children("img").attr("src","${ctxStatic }/layout/images/navigation/"+newImg);
				$(this).children("p").css("color","#FFB257");				
			})
			$(".finance_icon_list_item a").mouseout(function(){
			    var idx = $(this).index();
					var imgPath = $(this).children("span").children("img").attr("src");
					var imgName =  imgPath.substr(imgPath.lastIndexOf("/")+1);
					var nameArr = imgName.split("_");
					var newImg = nameArr[0]+"_"+nameArr[1]+"_"+nameArr[2].substr(nameArr[2].indexOf("h")+1);
					$(this).children("span").children("img").attr("src","${ctxStatic }/layout/images/navigation/"+newImg);
					$(this).children("p").css("color","#15a67f");
			})
	</script>
</body>
</html>
