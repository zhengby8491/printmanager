<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>生产管理流程图</title>
</head>
<body>
	<div class="page-container">
		<div class="page-border page-nav-wrap">
			<!--导航页面-->
			<div class="page-nav">
				<!--导航图标-->
				<div class="nav_icon_container">
					<h4>生产管理流程图</h4>
					<div class="n_line"></div>
					<div class="nav_icons">
						<!--图标-->
						<a class="nav_icon_a1" href="javascript:redirect('销售订单','/sale/order/create')">
							<span>
								<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_11.png" />
							</span>
							<p>销售订单</p>
						</a>
						<%-- 						<a class="nav_icon_a2" href="javascript:"><span><img alt=""  src="${ctxStatic }/layout/images/navigation/nav_icon_3.png"/></span><p>工艺卡模板</p></a> --%>
						<a class="nav_icon_a3" href="javascript:redirect('生产工单','/produce/work/create')">
							<span>
								<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_9.png" />
							</span>
							<p>生产施工单</p>
						</a>
						<a class="nav_icon_a4" href="javascript:redirect('工序转发外','/outsource/transmit/to_process_procedure')">
							<span>
								<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_13.png" />
							</span>
							<p>工序发外</p>
						</a>
						<a class="nav_icon_a5" href="javascript:redirect('工单未采购','/purch/transmit/to_purch_order')">
							<span>
								<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_5.png" />
							</span>
							<p>工单未采购</p>
						</a>
						<a class="nav_icon_a6" href="javascript:redirect('工单转领料','/produce/transmit/to_take')">
							<span>
								<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_12.png" />
							</span>
							<p>生产领料</p>
						</a>
						<a class="nav_icon_a7" href="javascript:redirect('工单转入库','/produce/transmit/to_product_in')">
							<span>
								<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_14.png" />
							</span>
							<p>成品入库</p>
						</a>
						<a class="nav_icon_a8" href="javascript:redirect('整单转发外','/outsource/transmit/to_process_product')">
							<span>
								<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_10.png" />
							</span>
							<p>整单发外</p>
						</a>

						<!--箭头-->
						<span class="nav_arrow_a1">
							<img alt="" src="${ctxStatic }/layout/images/navigation/line_7.png" />
						</span>
						<span class="nav_arrow_a2">
							<img alt="" src="${ctxStatic }/layout/images/navigation/line_3.png" />
						</span>
						<span class="nav_arrow_a3">
							<img alt="" src="${ctxStatic }/layout/images/navigation/line_6.png" />
						</span>
						<span class="nav_arrow_a4">
							<img alt="" src="${ctxStatic }/layout/images/navigation/line_7.png" />
						</span>
						<span class="nav_arrow_a5">
							<img alt="" src="${ctxStatic }/layout/images/navigation/line_7.png" />
						</span>
						<span class="nav_arrow_a6">
							<img alt="" src="${ctxStatic }/layout/images/navigation/line_3.png" />
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
									<a href="javascript:redirect('工序分类','/basic/procedureClass/list')">工序分类</a>
								</li>
								<!-- 									<li><i class="n_circle"></i><a href="javascript:redirect('仓库信息','/basic/warehouse/list')">仓库信息</a></li> -->
								<!-- 									<li><i class="n_circle"></i><a href="javascript:">常用规格</a></li> -->
								<!-- 									<li><i class="n_circle"></i><a href="javascript:">工艺模板</a></li> -->
								<!-- 									<li><i class="n_circle"></i><a href="javascript:">机台信息</a></li> -->
							</ul>
							<ul class="list_item">
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('工序信息','/basic/procedure/list')">工序信息</a>
								</li>
								<!-- 									<li><i class="n_circle"></i><a href="javascript:redirect('销售订单列表','/sale/order/list')">包装方式</a></li> -->
								<!-- 									<li><i class="n_circle"></i><a href="javascript:redirect('销售订单列表','/sale/order/list')">班组信息</a></li> -->
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
									<a href="javascript:redirect('生产工单明细','/produce/work/detailList')">生产工单明细表</a>
								</li>
								<!-- 									<li><i class="n_circle"></i><a href="javascript:">生产补单明细表</a></li> -->
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('生产补料明细','/stockmaterial/supplement/detailList')">生产补料明细表</a>
								</li>
							</ul>
							<ul class="list_item">
								<!-- 									<li><i class="n_circle"></i><a href="javascript:">生产翻单明细表</a></li> -->
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('生产领料明细','/stockmaterial/take/detailList')">生产领料明细表</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('生产退料明细','/stockmaterial/return/detailList')">生产退料明细表</a>
								</li>
							</ul>
						</div>
					</div>
					<div class="nav_list_item nav_list_3">
						<h4>生产转单</h4>
						<div class="n_line"></div>
						<div class="cl nav_list_content">
							<ul class="list_item">
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('工单未领料','/produce/transmit/to_take')">工单未领料</a>
								</li>
							</ul>
							<ul class="list_item">
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('工单未入库','/produce/transmit/to_product_in')">工单未入库</a>
								</li>
							</ul>
							<ul class="list_item">
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('生产日报表','/produce/transmit/to_product_daily_report')">生产日报表</a>
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
				    
				}else if(idx == 1){
				    $(".nav_arrow_a1 img").attr("src","${ctxStatic }/layout/images/navigation/line_h7.png")
				}else if(idx == 2){
				    $(".nav_arrow_a2 img").attr("src","${ctxStatic }/layout/images/navigation/line_h3.png")
				}else if(idx == 3){
				    $(".nav_arrow_a3 img").attr("src","${ctxStatic }/layout/images/navigation/line_h6.png")
				}else if(idx == 4){
				    $(".nav_arrow_a4 img").attr("src","${ctxStatic }/layout/images/navigation/line_h7.png")
				}else if(idx == 5){
				    $(".nav_arrow_a5 img").attr("src","${ctxStatic }/layout/images/navigation/line_h7.png")
				}else if(idx == 6){
				    $(".nav_arrow_a6 img").attr("src","${ctxStatic }/layout/images/navigation/line_h3.png")
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
					   
					}else if(idx == 1){
					    $(".nav_arrow_a1 img").attr("src","${ctxStatic }/layout/images/navigation/line_7.png")
					}else if(idx == 2){
					    $(".nav_arrow_a2 img").attr("src","${ctxStatic }/layout/images/navigation/line_3.png")
					}else if(idx == 3){
					    $(".nav_arrow_a3 img").attr("src","${ctxStatic }/layout/images/navigation/line_6.png")
					}else if(idx == 4){
					    $(".nav_arrow_a4 img").attr("src","${ctxStatic }/layout/images/navigation/line_7.png")
					}else if(idx == 5){
					    $(".nav_arrow_a5 img").attr("src","${ctxStatic }/layout/images/navigation/line_7.png")
					}else if(idx == 6){
					    $(".nav_arrow_a6 img").attr("src","${ctxStatic }/layout/images/navigation/line_3.png")
					}
			})
		</script>
</body>
</html>
