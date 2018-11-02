<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<meta charset="UTF-8">
<title>财务调整</title>
<script type="text/javascript">
	$(function()
	{
		$("#detailList").bootstrapTable({
			data:${fns:toJson(order.detailList)},
			showColumns : true, //是否显示所有的列
			minimumCountColumns : 2, //最少允许的列数
			striped : true, // 是否显示行间隔色
			cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination : false, // 是否显示分页（*）
			sortable : false, // 是否启用排序
			clickToSelect : true, // 是否启用点击选中行
			cardView : false, // 是否显示详细视图
			detailView : false, // 是否显示父子表	
			toolbar : "#toolbar", //设置工具栏的Id或者class
			//resizable : true, //是否启用列拖动
			cookie : true,//是否启用COOKIE
			cookieIdTable : "print_finance_adjust_master_view",//必须制定唯一的表格cookieID
			columns : [ {
				field : 'id',
				title : '序号',
				width : 40,
				formatter : function(value, row, index)
				{
					return index + 1;
				}
			}, {
				field : 'adjustType',
				title : '调整类型',
				width : 140,
				formatter : function (value, row, index)
				{
					var adjustTypeText = "${order.adjustTypeTarget }";
					return adjustTypeText;
				}
			}, {
				field : 'businessCode',
				title : '对象编号',
				width : 140
			}, {
				field : 'businessName',
				title : '调整对象',
				width : 160
			}, {
				field : 'adjustMoney',
				title : '调整金额',
				width : 100
			}, {
				field : 'reason',
				title : '调整事由',
				'class' : 'memoView',
				width : 200
			}, {
				field : 'memo',
				title : '备注',
				'class' : 'memoView',
				width : 200
			} ],
			onLoadSuccess : function()
			{

			},
			onLoadError : function()
			{
				// alert("数据加载异常");
			},
			onColumnSwitch : function(field, checked)
			{	
				// 在使用筛选增加或减少列时重置表格列拖动效果
				bootstrapTable_ColDrag($("#detailList"));
			}
		});

		/* 表格工具栏 */
		$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
		// 控制筛选菜单金额选择,删除已隐藏字段的选项
		$("#btn_edit").click(function()
		{
			window.location.href = Helper.basePath + '/finance/adjust/edit/' + $(this).val();
		});
		$("#btn_back").click(function()
		{
			var url = Helper.basePath + '/finance/adjust/list';
			var title = "财务调整单列表";
			admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
		});
		$("#btn_audit").click(function()
		{
			var order_id = $(this).val();
			Helper.post(Helper.basePath + '/finance/adjust/audit/' + order_id, function(data)
			{
				if (data.success)
				{
					Helper.message.suc(data.message);
					location.href = Helper.basePath + "/finance/adjust/view/" + order_id;
				} else
				{
					Helper.message.warn(data.message);
				}
			});
		});
		$("#btn_del").click(function()
		{
			var this_ = $(this);
			Helper.message.confirm('确认要删除吗？', function(index)
			{
				var order_id = this_.val();
				Helper.post(Helper.basePath + '/finance/adjust/delete/' + order_id, function(data)
				{
					if (data.success)
					{
						closeTabAndJump("财务调整单列表");
					} else
					{
						Helper.message.warn(data.message);
					}
				});
			});
		});
		
		// 表格列拖动效果
		bootstrapTable_ColDrag($("#detailList"));
	})
</script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--头部START-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="财务管理-财务调整-查看"></sys:nav>
				</div>
				<div class="top_nav">
					<button class="nav_btn table_nav_btn" id="btn_back">返回</button>
					<c:if test="${order.isCheck eq 'NO'}">
						<shiro:hasPermission name="finance:adjust:edit">
							<button class="nav_btn table_nav_btn" id="btn_edit" value="${order.id }">修改</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="finance:adjust:audit">
							<button class="nav_btn table_nav_btn" id="btn_audit" value="${order.id }">审核</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="finance:adjust:del">
							<button class="nav_btn table_nav_btn" id="btn_del" value="${order.id }">删除</button>
						</shiro:hasPermission>
					</c:if>
				</div>
			</div>
			<!--头部END-->
			<!--查询表单START-->
			<div class="form-container">
				<div class="form-wrap">
					<div class="cl form_content">
						<dl class="cl row-dl">
							<dd class="row-dd">
								<label class="form-label label_ui label_1">调整类型：</label>
							 		<input type="text" class="input-txt input-txt_1" readonly="readonly" id="adjustType" value="${order.adjustTypeText }" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">调整单号：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_3s" readonly="readonly" value="${order.billNo }" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">调整日期：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_1" readonly="readonly" value="<fmt:formatDate pattern="yyyy-MM-dd"  value='${order.adjustTime}' type='date' />" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">调 整 人：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_1" readonly="readonly" value="${fns:basicInfo('EMPLOYEE',order.employeeId).name}" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">制 单 人：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_1" readonly="readonly" value="${order.createName }" />
								</span>
							</dd>

							<dd class="row-dd">
								<label class="form-label label_ui label_1">制单日期：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_3s" readonly="readonly" value="<fmt:formatDate pattern="yyyy-MM-dd"  value='${order.createTime}' type='date' />" />
								</span>
							</dd>
						</dl>
						<dl class="cl row-dl">
							<dd class="row-dd">
								<label class="form-label label_ui label_1">备注：</label>
								<span class="form_textarea">
									<textarea class="noborder" name="memo" readonly="readonly" style="width: 996px">${order.memo }</textarea>
								</span>
							</dd>
						</dl>
					</div>
				</div>
			</div>

			<!--查询表单END-->
			<!--表格部分Start-->
			<div class="table-view">
				<table class="border-table resizable" id="detailList">
				</table>
			</div>
			<!--表格部分End-->
			<!--审核标签-->
			<c:if test="${order.isCheck.value==true }">
				<div class="review">
					<span class="review_font">已审核</span>
				</div>
			</c:if>

		</div>
	</div>
</body>
</html>