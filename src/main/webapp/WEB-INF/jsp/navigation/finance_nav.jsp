<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>财务管理</title>
</head>
<body>
	<div class="page-container">
		<div class="page-border page-nav-wrap">
			<!--导航页面-->
			<div class="page-nav">
				<!--导航图标-->
				<div class="nav_icon_container">
					<h4>财务管理流程图</h4>
					<div class="n_line"></div>
					<div class="finance_icons">
						<div class="finance_icon_list_1 finance_icon_list_item">
							<!--图标-->
							<a class="finance_icon_1" href="javascript:redirect('送货未对账','/sale/transmit/to_reconcil')">
								<span>
									<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_1.png" />
								</span>
								<p>销售对账</p>
							</a>
							<a class="finance_icon_3" href="javascript:redirect('收款单','/finance/receive/create')">
								<span>
									<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_5.png" />
								</span>
								<p>销售收款单</p>
							</a>
							<a class="finance_icon_4" href="javascript:redirect('应收账款明细','/finance/unfinished/saleReceive')">
								<span>
									<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_7.png" />
								</span>
								<p>应收明细</p>
							</a>
							<!--箭头-->
							<span class="finance_arrow_1">
								<img alt="" src="${ctxStatic }/layout/images/navigation/line_7.png" />
							</span>
							<span class="finance_arrow_3">
								<img alt="" src="${ctxStatic }/layout/images/navigation/line_7.png" />
							</span>
						</div>
						<div class="finance_icon_list_2 finance_icon_list_item">
							<!--图标-->
							<a class="finance_icon_1" href="javascript:redirect('入库未对账','/purch/transmit/to_purch_reconcil')">
								<span>
									<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_1.png" />
								</span>
								<p>采购对账</p>
							</a>
							<a class="finance_icon_3" href="javascript:redirect('付款单','/finance/payment/create')">
								<span>
									<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_5.png" />
								</span>
								<p>采购付款单</p>
							</a>
							<a class="finance_icon_4" href="javascript:redirect('应付账款明细','/finance/unfinished/purchPayment')">
								<span>
									<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_7.png" />
								</span>
								<p>应付明细</p>
							</a>
							<!--箭头-->
							<span class="finance_arrow_1">
								<img alt="" src="${ctxStatic }/layout/images/navigation/line_7.png" />
							</span>
							<span class="finance_arrow_3">
								<img alt="" src="${ctxStatic }/layout/images/navigation/line_7.png" />
							</span>
						</div>
						<div class="finance_icon_list_3 finance_icon_list_item">
							<!--图标-->
							<a class="finance_icon_1" href="javascript:redirect('到货未对账','/outsource/transmit/to_reconcil')">
								<span>
									<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_1.png" />
								</span>
								<p>发外对账</p>
							</a>
							<a class="finance_icon_3" href="javascript:redirect('付款单','/finance/payment/create')">
								<span>
									<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_5.png" />
								</span>
								<p>发外付款单</p>
							</a>
							<a class="finance_icon_4" href="javascript:redirect('应付账款明细','/finance/unfinished/outSourcePayment')">
								<span>
									<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_7.png" />
								</span>
								<p>应付明细</p>
							</a>
							<!--箭头-->
							<span class="finance_arrow_1">
								<img alt="" src="${ctxStatic }/layout/images/navigation/line_7.png" />
							</span>
							<span class="finance_arrow_3">
								<img alt="" src="${ctxStatic }/layout/images/navigation/line_7.png" />
							</span>
						</div>
					</div>
				</div>
				<!--导航列表-->
				<div class="nav_list_container">
					<div class="nav_list_item nav_list_1">
						<h4>基础信息</h4>
						<div class="n_line"></div>
						<div class="cl nav_list_content">
							<ul class="list_item">
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
									<a href="javascript:redirect('职位信息','/basic/position/list')">职位信息</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('员工信息','/basic/employee/list')">员工信息</a>
								</li>
							</ul>
							<ul class="list_item">
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('税收信息','/basic/taxRate/list')">税收信息</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('结算方式','/basic/settlementClass/list')">结算方式</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('部门信息','/basic/department/list')">部门信息</a>
								</li>
							</ul>
						</div>
					</div>
					<div class="nav_list_item nav_list_2">
						<h4>期初设置</h4>
						<div class="n_line"></div>
						<div class="cl nav_list_content">
							<ul class="list_item">
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('客户期初列表','/begin/customer/list')">客户期初</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('产品期初列表','/begin/product/list')">产品期初</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('账户期初列表','/begin/account/list')">账户期初</a>
								</li>
							</ul>
							<ul class="list_item">
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('供应商期初列表','/begin/supplier/list')">供应商期初</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('材料期初列表','/begin/material/list')">材料期初</a>
								</li>
							</ul>
						</div>
					</div>
					<div class="nav_list_item nav_list_3">
						<h4>明细报表</h4>
						<div class="n_line"></div>
						<div class="cl nav_list_content">
							<ul class="list_item">
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('收款单明细','/finance/receive/detailList')">销售收款明细表</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('付款单明细','/finance/payment/detailList')">付款明细表</a>
								</li>
							</ul>
							<ul class="list_item">
								<!-- 									<li><i class="n_circle"></i><a href="javascript:redirect('付款单明细','/finance/payment/detailList')">采购付款明细表</a></li> -->
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('账户流水明细表','/finance/log/accountLog')">账户流水明细表</a>
								</li>
							</ul>
						</div>
					</div>
					<!-- 						<div class="nav_list_item nav_list_3"> -->
					<!-- 							<h4>分析报表</h4> -->
					<!-- 							<div class="n_line"></div> -->
					<!-- 							<div class="cl nav_list_content"> -->
					<!-- 								<ul class="list_item"> -->
					<!-- 									<li><i class="n_circle"></i><a href="javascript:redirect('生产工单列表','/produce/work/list')">往来单位欠款表</a></li> -->
					<!-- 									<li><i class="n_circle"></i><a href="javascript:redirect('生产工单列表','/produce/work/list')">应付账款汇总表</a></li> -->
					<!-- 									<li><i class="n_circle"></i><a href="javascript:redirect('生产工单列表','/produce/work/list')">采购应付账款明细</a></li> -->
					<!-- 								</ul> -->
					<!-- 								<ul class="list_item"> -->
					<!-- 									<li><i class="n_circle"></i><a href="javascript:redirect('生产工单列表','/produce/work/list')">应收账款汇总表</a></li> -->
					<!-- 									<li><i class="n_circle"></i><a href="javascript:redirect('生产工单列表','/produce/work/list')">销售应收账款明细</a></li> -->
					<!-- 									<li><i class="n_circle"></i><a href="javascript:redirect('生产工单列表','/produce/work/list')">发外应付款明细</a></li> -->
					<!-- 								</ul> -->
					<!-- 							</div> -->
					<!-- 						</div> -->
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
				if(idx == 0){
				}else if(idx == 1){
				    $(this).parent().children(".finance_arrow_1").children("img").attr("src","${ctxStatic }/layout/images/navigation/line_h7.png")
				}else if(idx == 2){
				    $(this).parent().children(".finance_arrow_3").children("img").attr("src","${ctxStatic }/layout/images/navigation/line_h7.png")
				}
			})
			$(".finance_icon_list_item a").mouseout(function(){
			    var idx = $(this).index();
					var imgPath = $(this).children("span").children("img").attr("src");
					var imgName =  imgPath.substr(imgPath.lastIndexOf("/")+1);
					var nameArr = imgName.split("_");
					var newImg = nameArr[0]+"_"+nameArr[1]+"_"+nameArr[2].substr(nameArr[2].indexOf("h")+1);
					$(this).children("span").children("img").attr("src","${ctxStatic }/layout/images/navigation/"+newImg);
					$(this).children("p").css("color","#15a67f");
					if(idx == 0){
					}else if(idx == 1){
					    $(this).parent().children(".finance_arrow_1").children("img").attr("src","${ctxStatic }/layout/images/navigation/line_7.png")
					}else if(idx == 2){
					    $(this).parent().children(".finance_arrow_3").children("img").attr("src","${ctxStatic }/layout/images/navigation/line_7.png")
					}
			})
	</script>
</body>
</html>
