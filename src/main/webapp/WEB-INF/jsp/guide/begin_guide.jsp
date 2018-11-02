<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>新手指南</title>
<script type="text/javascript" src="${ctxHYUI}/plugins/ueditor/ueditor.config.js"></script>
<script type="text/javascript" src="${ctxHYUI}/plugins/ueditor/ueditor.all.min.js"></script>
</head>
<link rel="stylesheet" type="text/css" href="${ctxHYUI}/src/main.css?v=${v }" />
<link rel="stylesheet" type="text/css" href="${ctxHYUI}/css/hyui.main.css?v=${v }" />
<script type="text/javascript">
		$(function(){
		    var $item = $('ul.guide_l>li');
	        $item.hover(function() {
	            $item.removeClass('selected');
	            $(this).addClass('selected');
				$(this).children(".sub_menu").fadeIn(100);
				$(this).siblings().children(".sub_menu").fadeOut(100);
	        },function(){
	            $item.removeClass('selected');
	            $(this).children(".sub_menu").fadeOut(100);
	        });
	        var $sub_item = $(".sub_menu>ul>li");
	        $sub_item.click(function(){
	            $sub_item.removeClass("selected");
// 	            $(".sub_menu").hide();
	            $(this).addClass('selected');
	        })
		})      
    </script>
<body style="height: auto">
	<div class="guide_content">
		<ul class="guide_l">
			<li class="selected">
				<div class="menu_item">
					<img src="${ctxStatic }/layout/images/menu/销售管理_h.png" />
					<label>销售管理</label>
				</div>
				<div class="sub_menu" style="top: 0">
					<ul class="sale">
						<li>
							<a href="javascript:;">销售订单</a>
						</li>
						<li>
							<a href="javascript:;">销售未送货</a>
						</li>
						<li>
							<a href="javascript:;">销售送货</a>
						</li>
						<li>
							<a href="javascript:;">销售退货</a>
						</li>
						<li>
							<a href="javascript:;">送货未对账</a>
						</li>
						<li>
							<a href="javascript:;">销售对账</a>
						</li>
					</ul>
				</div>
			</li>
			<li class="">
				<div class="menu_item">
					<img src="${ctxStatic }/layout/images/menu/生产管理_h.png" />
					<label>生产管理</label>
				</div>
				<div class="sub_menu" style="top: 0">
					<ul class="produce">
						<li>
							<a href="javascript:;">包装类工单</a>
						</li>
						<li>
							<a href="javascript:;">书刊类工单</a>
						</li>
						<li>
							<a href="javascript:;">包装类合版工单</a>
						</li>
					</ul>
				</div>
			</li>
			<li class="">
				<div class="menu_item">
					<img src="${ctxStatic }/layout/images/menu/采购管理_h.png" />
					<label>采购管理</label>
				</div>
				<div class="sub_menu" style="top: 0">
					<ul class="purch">
						<li>
							<a href="javascript:;">工单未采购</a>
						</li>
						<li>
							<a href="javascript:;">采购订单</a>
						</li>
						<li>
							<a href="javascript:;">采购未入库</a>
						</li>
						<li>
							<a href="javascript:;">采购入库</a>
						</li>
						<li>
							<a href="javascript:;">采购退货</a>
						</li>
						<li>
							<a href="javascript:;">入库未对账</a>
						</li>
						<li>
							<a href="javascript:;">采购对账</a>
						</li>
					</ul>
				</div>
			</li>
			<li class="">
				<div class="menu_item">
					<img src="${ctxStatic }/layout/images/menu/发外管理_h.png" />
					<label>发外管理</label>
				</div>
				<div class="sub_menu" style="top: -100px">
					<ul class="outsource">
						<li>
							<a href="javascript:;">工序未发外</a>
						</li>
						<li>
							<a href="javascript:;">整单未发外</a>
						</li>
						<li>
							<a href="javascript:;">发外加工</a>
						</li>
						<li>
							<a href="javascript:;">发外未到货</a>
						</li>
						<li>
							<a href="javascript:;">发外到货</a>
						</li>
						<li>
							<a href="javascript:;">发外退货</a>
						</li>
						<li>
							<a href="javascript:;">发外未对账</a>
						</li>
						<li>
							<a href="javascript:;">发外对账</a>
						</li>
					</ul>
				</div>
			</li>
			<li class="">
				<div class="menu_item">
					<img src="${ctxStatic }/layout/images/menu/库存管理_h.png" />
					<label>材料库存管理</label>
				</div>
				<div class="sub_menu" style="top: -107px;">
					<ul class="stock_material">
						<li>
							<a href="javascript:;">工单转领料</a>
						</li>
						<li>
							<a href="javascript:;">生产领料</a>
						</li>
						<li>
							<a href="javascript:;">生产补料</a>
						</li>
						<li>
							<a href="javascript:;">生产退料</a>
						</li>
						<li>
							<a href="javascript:;">材料其他入库</a>
						</li>
						<li>
							<a href="javascript:;">材料其他出库</a>
						</li>
						<li>
							<a href="javascript:;">材料库存调整</a>
						</li>
						<li>
							<a href="javascript:;">材料库存调拨</a>
						</li>
						<li>
							<a href="javascript:;">材料库存盘点</a>
						</li>
					</ul>
				</div>
			</li>
			<li class="">
				<div class="menu_item">
					<img src="${ctxStatic }/layout/images/menu/库存管理_h.png" />
					<label>成品库存管理</label>
				</div>
				<div class="sub_menu" style="top: -80px;">
					<ul class="stock_product">
						<li>
							<a href="javascript:;">工单转入库</a>
						</li>
						<li>
							<a href="javascript:;">成品入库</a>
						</li>
						<li>
							<a href="javascript:;">成品其他入库</a>
						</li>
						<li>
							<a href="javascript:;">成品其他出库</a>
						</li>
						<li>
							<a href="javascript:;">成品库存调整</a>
						</li>
						<li>
							<a href="javascript:;">成品库存调拨</a>
						</li>
						<li>
							<a href="javascript:;">成品库存盘点</a>
						</li>
					</ul>
				</div>
			</li>
			<li class="">
				<div class="menu_item">
					<img src="${ctxStatic }/layout/images/menu/财务管理_h.png" />
					<label>财务管理</label>
				</div>
				<div class="sub_menu" style="bottom: -118px">
					<ul class="finance">
						<li>
							<a href="javascript:;">付款单</a>
						</li>
						<li>
							<a href="javascript:;">收款单</a>
						</li>
						<li>
							<a href="javascript:;">付款核销单</a>
						</li>
						<li>
							<a href="javascript:;">收款核销单</a>
						</li>
					</ul>
				</div>
			</li>
			<li class="">
				<div class="menu_item">
					<img src="${ctxStatic }/layout/images/menu/基础设置_h.png" />
					<label>基础设置</label>
				</div>
				<div class="sub_menu basic_set" style="bottom: -59px">
					<ul class="basic">
						<!--             			<li><a href="javascript:;">部门信息</a></li> -->
						<!--             			<li><a href="javascript:;">职位信息</a></li> -->
						<li>
							<a href="javascript:;">添加员工信息</a>
						</li>
						<!--             			<li><a href="javascript:;">财务信息</a></li> -->
						<!--             			<li><a href="javascript:;">税收信息</a></li> -->
						<!--             			<li><a href="javascript:;">付款方式</a></li> -->
						<!--             			<li><a href="javascript:;">结算方式</a></li> -->
						<li>
							<a href="javascript:;">添加客户信息</a>
						</li>
						<li>
							<a href="javascript:;">添加产品信息</a>
						</li>
						<li>
							<a href="javascript:;">添加供应商信息</a>
						</li>
						<li>
							<a href="javascript:;">添加材料</a>
						</li>
						<li>
							<a href="javascript:;">添加账户</a>
						</li>
						<!--             			<li><a href="javascript:;">材料分类</a></li> -->
						<!--             			<li><a href="javascript:;">产品信息</a></li> -->
						<!--             			<li><a href="javascript:;">单位信息</a></li> -->
						<!--             			<li><a href="javascript:;">送货方式</a></li> -->
						<!--             			<li><a href="javascript:;">工序方式</a></li> -->
						<!--             			<li><a href="javascript:;">仓库信息</a></li> -->
						<li>
							<a href="javascript:;">客户期初</a>
						</li>
						<li>
							<a href="javascript:;">供应商期初</a>
						</li>
						<li>
							<a href="javascript:;">产品期初</a>
						</li>
						<li>
							<a href="javascript:;">材料期初</a>
						</li>
						<li>
							<a href="javascript:;">账户期初</a>
						</li>
					</ul>
				</div>
			</li>
			<li class="">
				<div class="menu_item">
					<img src="${ctxStatic }/layout/images/menu/系统管理_h.png" />
					<label>系统管理</label>
				</div>
				<div class="sub_menu" style="bottom: 0;">
					<ul class="system">
						<li>
							<a href="javascript:;">公司管理</a>
						</li>
						<li>
							<a href="javascript:;">添加用户</a>
						</li>
						<li>
							<a href="javascript:;">添加角色</a>
						</li>
					</ul>
				</div>
			</li>
		</ul>
		<div class="guide_r">
			<div class="img_content">
				<span class="btn_pre">
					<i class="fa fa-chevron-left"></i>
				</span>
				<span class="btn_next">
					<i class="fa fa-chevron-right"></i>
				</span>
				<div style="width: 100%; height: 100%" class="img_list">
					<!-- 			<div class="des_content"></div>       		 -->
				</div>
			</div>
		</div>

		<script type="text/javascript">
    			

		var guideData = {

		"sale":[
			{
				"src":"${ctxStatic }/layout/images/guide/sale_order.png",
				"text":"销售管理 — 销售订单"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/sale_toDeliver.png",
				"text":"销售管理 — 销售未送货"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/sale_deliver.png",
				"text":"销售管理 — 销售送货"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/sale_return.png",
				"text":"销售管理 — 销售退货"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/sale_toReconcil.png",
				"text":"销售管理 — 销售未对账"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/sale_reconcil.png",
				"text":"销售管理 — 销售对账"
			}
		],
		"produce":[
			{
				"src":"${ctxStatic }/layout/images/guide/produce_01.png",
				"text":"生产管理  — 包装类工单"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/produce_02.png",
				"text":"生产管理  — 印刷类工单"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/produce_03.png",
				"text":"生产管理  — 包装类合版工单"
			}
		],
		"purch":[
			{
				"src":"${ctxStatic }/layout/images/guide/purch_to_purch.png",
				"text":"采购管理  — 工单未采购"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/purch_order.png",
				"text":"采购管理  — 采购订单"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/purch_to_stock.png",
				"text":"采购管理  — 采购未入库"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/purch_stock.png",
				"text":"采购管理  — 采购入库"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/purch_return.png",
				"text":"采购管理  — 采购退货"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/purch_to_reconcil.png",
				"text":"采购管理  — 入库未对账"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/purch_reconcil.png",
				"text":"采购管理  — 采购对账"
			}
		],
		"outsource":[
			{
				"src":"${ctxStatic }/layout/images/guide/outsource_to_process_procedure.png",
				"text":"外发管理  — 工序未发外"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/outsource_to_process_product.png",
				"text":"外发管理  — 整单未发外"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/outsource_to_process.png",
				"text":"外发管理  — 发外加工"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/outsource_to_arrive.png",
				"text":"外发管理  — 发外未到货"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/outsource_arrive.png",
				"text":"外发管理  — 发外到货"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/outsource_return.png",
				"text":"外发管理  — 发外退货"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/outsource_to_reconcil.png",
				"text":"外发管理  — 发外未对账"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/outsource_reconcil.png",
				"text":"外发管理  — 发外对账"
			}
		],
		"stock_material":[
			{
				"src":"${ctxStatic }/layout/images/guide/material_work_to_take.png",
				"text":"材料库存管理 — 工单转领料"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/material_take.png",
				"text":"材料库存管理 — 生产领料"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/material_supple.png",
				"text":"材料库存管理 — 生产补料"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/material_return.png",
				"text":"材料库存管理 — 生产退料"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/material_otherIn.png",
				"text":"材料库存管理 — 材料其他入库"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/material_otherOut.png",
				"text":"材料库存管理 — 材料其他出库"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/material_adjust.png",
				"text":"材料库存管理 — 材料库存调整"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/material_transfer.png",
				"text":"材料库存管理 — 材料库存调拨"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/material_inventory.png",
				"text":"材料库存管理 — 材料库存盘点"
			}
		],
		"stock_product":[
			{
				"src":"${ctxStatic }/layout/images/guide/product_work_to_take.png",
				"text":"成品库存管理 — 工单转入库"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/product_stock.png",
				"text":"成品库存管理 — 成品入库"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/product_otherIn.png",
				"text":"成品库存管理 — 成品其他入库"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/product_otherOut.png",
				"text":"成品库存管理 — 成品其他出库"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/product_adjust.png",
				"text":"成品库存管理 — 成品库存调整"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/product_transfer.png",
				"text":"成品库存管理 — 成品库存调拨"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/product_inventory.png",
				"text":"成品库存管理 — 成品库存盘点"
			}
		],
		"system":[
			{
				"src":"${ctxStatic }/layout/images/guide/sys_company.png",
				"text":"系统管理 — 公司管理"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/sys_addRole.png",
				"text":"系统管理 — 添加角色"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/sys_addUser.png",
				"text":"系统管理 — 添加用户"
			}
		],
		"basic":[
			{
				"src":"${ctxStatic }/layout/images/guide/basic_addEmployee.png",
				"text":"添加员工信息"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/basic_addCustomer.png",
				"text":"添加客户信息"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/basic_addProduct.png",
				"text":"基础设置 — 添加产品信息"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/basic_addSupplier.png",
				"text":"基础设置 — 添加供应商信息"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/basic_addMaterial.png",
				"text":"基础设置 — 添加材料"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/basic_addAccount.png",
				"text":"基础设置 — 添加账户"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/basic_customer_begin.png",
				"text":"基础设置 — 客户期初"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/basic_asupplier_begin.png",
				"text":"基础设置 — 供应商期初"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/basic_product_begin.png",
				"text":"基础设置 — 产品期初"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/basic_material_begin.png",
				"text":"基础设置 — 材料期初"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/basic_account_begin.png",
				"text":"基础设置 — 账号期初"
			}
		],
		"finance":[
			{
				"src":"${ctxStatic }/layout/images/guide/finance_pay.png",
				"text":"财务管理 — 付款单"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/finance_receive.png",
				"text":"财务管理 — 收款单"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/finance_pay_writeofff.png",
				"text":"财务管理 — 付款核销单"
			},
			{
				"src":"${ctxStatic }/layout/images/guide/finance_receive_writeoff.png",
				"text":"财务管理 — 收款核销单"
			}
		]	
	}
	
		
              		
			$(function(){
				var index = 0;
				var module = "sale";
// 				$.ajax({
// 						type:"get",
// 						url:"guide.json",
// 						async:true,
// 						dataType:"json",
// 						success:function(data){
// 							guideData = data;
// 							alert(JSON.stringify(data))
// 						}
// 				});
				function loadData(module,index){
					    $('.btn_next').show();
				    	$('.btn_pre').show();
				    	if(index == 0) {
				    	    $('.btn_pre').hide();
				    	}
				    	if(index == guideData[module].length-1){
				    	    $('.btn_next').hide();
				    	}
				    	
						$(".img_list").html('');
						var str = "";
						str = '<div class="g_img"><img alt="" src="'+guideData[module][index].src+'"/></div>'
					   +'<div class="g_description"><p class="g_desc">'+guideData[module][index].text+'</p></div>';
						$(".img_list").append(str);
						$(".sub_menu>ul>li").removeClass("selected");
			        	$("."+module).children().eq(index).addClass('selected');
				}
				loadData("sale",index);
				$('.sub_menu>ul>li').click(function(){
					index = $(this).index();
					module = $(this).parent().attr("class");
					loadData(module,index);				
				})
				$('.btn_next').click(function(){
					if(index+1 <= guideData[module].length-1) {
						index = index+1;
					}else{
						index = guideData[module].length-1;
					}					
					loadData(module,index);
				})
				$('.btn_pre').click(function(){
					if(index-1>=0){
						index = index-1;
					}else{
						index = 0;					
					}
					loadData(module,index);	
									
				})
			})
		</script>
</body>
</html>
