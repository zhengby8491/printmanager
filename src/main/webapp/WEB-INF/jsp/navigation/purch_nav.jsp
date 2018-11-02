<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>采购管理流程图</title>
</head>
<body>
	<div class="page-container">
		<div class="page-border page-nav-wrap">
			<!--导航页面-->
			<div class="page-nav">
				<!--导航图标-->
				<div class="nav_icon_container">
					<h4>采购管理流程图</h4>
					<div class="n_line"></div>
					<div class="nav_icons sale-nav">
						<!--图标-->
						<a class="nav_icon_1" href="javascript:redirect('采购订单','/purch/order/create')">
							<span>
								<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_1.png" />
							</span>
							<p>采购订单</p>
						</a>
						<a class="nav_icon_2" href="javascript:redirect('工单未采购','/purch/transmit/to_purch_order')">
							<span>
								<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_3.png" />
							</span>
							<p>工单转采购</p>
						</a>
						<%-- 							<a class="nav_icon_3" href="javascript:redirect('订单转入库','/purch/transmit/to_purch_stock')"><span><img alt=""  src="${ctxStatic }/layout/images/navigation/nav_icon_9.png"/></span><p>订单转入库</p></a> --%>
						<a class="nav_icon_4" href="javascript:redirect('采购未入库','/purch/transmit/to_purch_stock')">
							<span>
								<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_12.png" />
							</span>
							<p>采购入库</p>
						</a>
						<%-- 							<a class="nav_icon_5" href="javascript:redirect('入库转对账','/purch/transmit/to_purch_reconcil')"><span><img alt=""  src="${ctxStatic }/layout/images/navigation/nav_icon_5.png"/></span><p>快捷对账</p></a> --%>
						<a class="nav_icon_6" href="javascript:redirect('采购退货','/purch/refund/create')">
							<span>
								<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_8.png" />
							</span>
							<p>采购退货</p>
						</a>
						<a class="nav_icon_7" href="javascript:redirect('入库未对账','/purch/transmit/to_purch_reconcil')">
							<span>
								<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_6.png" />
							</span>
							<p>采购对账</p>
						</a>
						<a class="nav_icon_8" href="javascript:redirect('付款单','/finance/payment/create')">
							<span>
								<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_7.png" />
							</span>
							<p>付款单</p>
						</a>
						<!--箭头-->
						<span class="nav_arrow_1">
							<img alt="" src="${ctxStatic }/layout/images/navigation/line_3.png" />
						</span>
						<span class="nav_arrow_2">
							<img alt="" src="${ctxStatic }/layout/images/navigation/line_1.png" />
						</span>
						<%-- 							<span class="nav_arrow_3"><img alt=""  src="${ctxStatic }/layout/images/navigation/line_1.png"/></span> --%>
						<span class="nav_arrow_4">
							<img alt="" src="${ctxStatic }/layout/images/navigation/line_12.png" />
						</span>
						<span class="nav_arrow_5">
							<img alt="" src="${ctxStatic }/layout/images/navigation/line_13.png" />
						</span>
						<%-- 							<span class="nav_arrow_6"><img alt=""  src="${ctxStatic }/layout/images/navigation/line_2.png"/></span> --%>
						<span class="nav_arrow_7">
							<img alt="" src="${ctxStatic }/layout/images/navigation/line_1.png" />
						</span>
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
									<a href="javascript:redirect('供应商分类','/basic/supplierClass/list')">供应商分类</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('供应商信息','/basic/supplier/list')">供应商信息</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('单位信息','/basic/unit/list')">单位信息</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('付款方式','/basic/paymentClass/list')">付款方式</a>
								</li>
							</ul>
							<ul class="list_item">
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('材料分类','/basic/materialClass/list')">材料分类</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('材料信息','/basic/material/list')">材料信息</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('送货方式','/basic/deliveryClass/list')">送货方式</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('结算方式','/basic/settlementClass/list')">结算方式</a>
								</li>
							</ul>
						</div>
					</div>
					<div class="nav_list_item nav_list_2">
						<h4>明细报表</h4>
						<div class="n_line"></div>
						<div class="cl nav_list_content">
							<ul class="list_item">
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('采购订单明细','/purch/order/detailList')">采购订单明细表</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('采购退货明细','/purch/refund/detailList')">采购退货明细表</a>
								</li>
							</ul>
							<ul class="list_item">
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('采购入库明细','/purch/stock/detailList')">采购入库明细表</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('采购对账明细','/purch/reconcil/detailList')">采购对账明细表</a>
								</li>
							</ul>
						</div>
					</div>
					<div class="nav_list_item nav_list_3">
						<h4>采购转单</h4>
						<div class="n_line"></div>
						<div class="cl nav_list_content">
							<ul class="list_item">
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('工单未采购','/purch/transmit/to_purch_order')">工单未采购</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('入库未对账','/purch/transmit/to_purch_reconcil')">入库未对账</a>
								</li>
							</ul>
							<ul class="list_item">
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('采购未入库','/purch/transmit/to_purch_stock')">采购未入库</a>
								</li>
							</ul>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
			$(".nav_icons a").mouseover(function(){
				var idx = $(this).index();
				var imgPath = $(this).children("span").children("img").attr("src");
				var imgName =  imgPath.substr(imgPath.lastIndexOf("/")+1);
				var nameArr = imgName.split("_");
				var newImg = nameArr[0]+"_"+nameArr[1]+"_h"+nameArr[2];
				$(this).children("span").children("img").attr("src","${ctxStatic }/layout/images/navigation/"+newImg);
				$(this).children("p").css("color","#FFB257");
				if(idx == 0){
				    $(".nav_arrow_1 img").attr("src","${ctxStatic }/layout/images/navigation/line_h3.png")
				}else if(idx == 1){
				}else if(idx == 2){
				    $(".nav_arrow_2 img").attr("src","${ctxStatic }/layout/images/navigation/line_h1.png")
				}else if(idx == 3){
				    $(".nav_arrow_4 img").attr("src","${ctxStatic }/layout/images/navigation/line_h12.png")
				}else if(idx == 4){
				    $(".nav_arrow_5 img").attr("src","${ctxStatic }/layout/images/navigation/line_h13.png")
				}else if(idx == 5){
				    $(".nav_arrow_7 img").attr("src","${ctxStatic }/layout/images/navigation/line_h1.png")
				}
				
			})
			$(".nav_icons a").mouseout(function(){
			    var idx = $(this).index();
					var imgPath = $(this).children("span").children("img").attr("src");
					var imgName =  imgPath.substr(imgPath.lastIndexOf("/")+1);
					var nameArr = imgName.split("_");
					var newImg = nameArr[0]+"_"+nameArr[1]+"_"+nameArr[2].substr(nameArr[2].indexOf("h")+1);
					$(this).children("span").children("img").attr("src","${ctxStatic }/layout/images/navigation/"+newImg);
					$(this).children("p").css("color","#15a67f");
					if(idx == 0){
					    $(".nav_arrow_1 img").attr("src","${ctxStatic }/layout/images/navigation/line_3.png")
					}else if(idx == 1){

					}else if(idx == 2){
					    $(".nav_arrow_2 img").attr("src","${ctxStatic }/layout/images/navigation/line_1.png")
					}else if(idx == 3){
					    $(".nav_arrow_4 img").attr("src","${ctxStatic }/layout/images/navigation/line_12.png")
					}else if(idx == 4){
					    $(".nav_arrow_5 img").attr("src","${ctxStatic }/layout/images/navigation/line_13.png")
					}else if(idx == 5){
					    $(".nav_arrow_7 img").attr("src","${ctxStatic }/layout/images/navigation/line_1.png")
					}
			})
		</script>
</body>
</html>
