<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>

<meta charset="UTF-8">
<title>设置常用功能</title>
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<link rel="stylesheet" type="text/css" href="${ctxHYUI}/src/welcome.css?v=${v }" />
<link rel="stylesheet" type="text/css" href="${ctxHYUI}/css/hyui.welcome.css?v=${v }" />


<script type="text/javascript">
		$(function(){
			//初始化全部菜单
			initHiddenMenu();
			//点击选择菜单
			$(document).on("change",".menu_check",function(){
				if($(this).prop("checked")==true){
					if($(".sel_menu_ul").children().length<=7){
						var html = '<li data-title="'+$(this).data("title")+'"><input type="hidden" name="oftenList.url" value="'+$(this).data("url")+'"/><input type="hidden" name="oftenList.name" value="'+$(this).data("title")+'"/><span class="sel_menu_title">'+$(this).parent().text()+'</span><i class="fa fa-close"></i></li>';
						$(".sel_menu_ul").append(html);
					}else{
						$(this).prop("checked",false);
					}
				}else{
					$(".sel_menu_ul").children('li[data-title="'+$(this).data("title")+'"]').remove();
				}
			})
			//删除已有菜单
			$(document).on("click",".sel_menu_ul li",function(){
				$(this).remove();
				$(".offen_menu_ul").find('.menu_check[data-title="'+$(this).data("title")+'"]').prop("checked",false);
			})
			
			$("#btn_confirm").click(function(){
			    Helper.request({ 
				    url: Helper.basePath+"/workbench/often/save",  
				    data: $("#form_order").formToJson(),
	                error: function(request) {
	                    Helper.message.warn("服务器繁忙");
	                },
	                success: function(data) {
					   if(data.success){
						   var data = {};
						   $(".sel_menu_ul").children("li").each(function(){
							   data[$(this).data("title")] = $(this).find('input[name="oftenList.url"]').val();
						   })
					       parent.save_success(data);
					       Helper.popup.close();
					   }else{
					  	 Helper.message.warn("服务器繁忙");
					   }
	                   
	                }
	            });
			})
			
			$("#btn_cancel").click(function(){
			    Helper.popup.close();
			})
			
		})
		//初始菜单
		function initHiddenMenu(){
				var hidden_len = $(".hidden_menu").children(".hidden_menu_item").size();
				var menu_len = $(".menu_list").children(".menu_list_item").size();
				var $arr=[];
				for(var i=0;i<hidden_len;i++){
					$arr.splice(0,$arr.length);
					for(var j=0;j<menu_len;j++){
						$arr.push($(".menu_list").children(".menu_list_item").eq(j));
					}
					var html = $(".hidden_menu").children(".hidden_menu_item").eq(i).html();
					minDiv($arr).children("div").append(html);
				}
				$(".sel_menu_ul li").each(function(){
				    $(".offen_menu_ul").find('.menu_check[data-title="'+$(this).data("title")+'"]').prop("checked",true);
				})
			}
		//算最小高度的div
		function minDiv($arr){
			var arr_len = $arr.length;
			var $min = $arr[0];
			for(var i=1;i<arr_len;i++){
				$min=$min.children().height()<=$arr[i].children().height()?$min:$arr[i];
			}
			return $min;
		}
	</script>
</head>
<body style="overflow: hidden;">
<div class="hy_workdesk" style="padding: 0px">
	<div class="offen_menu_con">
		<div class="sel_menu_con">
			<form action="${ctx}/workbench/often/save" id="form_order" method="post">
				<ul class="sel_menu_ul">
					<c:forEach items="${oftenList }" var="often">
						<li data-title="${often.name }">
							<input type="hidden" name="oftenList.id" value="${often.id }" />
							<input type="hidden" name="oftenList.url" value="${often.url }" />
							<input type="hidden" name="oftenList.name" value="${often.name }" />
							<span class="sel_menu_title">${often.name }</span>
							<i class="fa fa-close"></i>
						</li>
					</c:forEach>
				</ul>
				<div class="btn_group">
					<button type="button" class="nav_btn table_nav_btn" id="btn_confirm">确定</button>
					<button type="button" class="nav_btn table_nav_btn" id="btn_cancel">取消</button>
				</div>
			</form>
		</div>
		<div class="menu_list cl">
			<div class="menu_list_item">
				<div></div>
			</div>
			<div class="menu_list_item">
				<div></div>
			</div>
			<div class="menu_list_item">
				<div></div>
			</div>
			<div class="menu_list_item">
				<div style="border-right: 0"></div>
			</div>
		</div>
		<div class="hidden_menu" style="display: none">
			<c:forEach items="${fns:getNavigationMenu()}" var="m1">
				<div class="hidden_menu_item">
					<h1 class="offen_menu_title">${m1.name}</h1>
					<ul class="offen_menu_ul">
						<c:forEach items="${m1.childrens }" var="m2">
							<c:if test="${!empty m2.childrens }">
								<c:forEach items="${m2.childrens }" var="m3">
									<li>
										<label>
											<input type="checkbox" class="menu_check" data-url="${m3.url}" data-title="${m3.name }" refresh="${m3.refresh.value }" value="" />
											${m3.name } 
									</li>
									</dd>
								</c:forEach>
							</c:if>
						</c:forEach>
					</ul>
				</div>
			</c:forEach>
		</div>
	</div>
</div>
</body>
</html>