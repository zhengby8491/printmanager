<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic }/site/transmit.js?v=${v }"></script>
<title>工单未采购</title>
<style type="text/css">
.fixed-table-container tbody .selected td {
	background: transparent;
}

#bootTable tbody tr.current td {
	background: #ccc;
}

.fixed-table-pagination .pagination-detail, .fixed-table-pagination div.pagination {
	margin-top: 20px;
	margin-bottom: 0px;
}

.fixed-table-pagination .pagination-detail, .fixed-table-pagination div.pagination {
	margin-top: 5px;
	margin-bottom: 0px;
}
</style>
<script type="text/javascript">
	$(function()
	{
		var selectData = {};
		var $selectRow;
		$("#detailTableDiv").css({
			"max-height" : ($("body").height() - 100) * 0.5 + "px",
			"overflow" : "auto"
		});
		/* 查询强制完工状态 */
		$("input[name='completeFlag']").change(function()
		{
			selectData = {};
			$("#detailTable tbody").empty();
			
			// 初始化表格拖动功能
			table_ColDrag($("#detailTable"));
		});
		/* 查询*/
		$("#btn_search").click(function()
		{
			selectData = {};
			$("#detailTable tbody").empty();
		});
		/* 转订单*/
		$("#btn_transmit").click(function()
		{
			var ids = [];
			for ( var s in selectData)
			{
				if (selectData[s].length != 0)
				{
					for (var i = 0, len = selectData[s].length; i < len; i++)
					{
						ids.push(selectData[s][i].id);
					}
				}
			}
			var rows = ids;
			if (Helper.isNotEmpty(rows))
			{
				var _paramStr = "";
				$(rows).each(function(index, item)
				{
					if (index == 0)
					{
						_paramStr = "ids=" + item;
					} else
					{
						_paramStr = _paramStr + "&ids=" + item;
					}
				});
				var url = Helper.basePath + '/purch/order/toPurch?' + _paramStr;
				var title = "采购订单";
				admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
			} else
			{
				Helper.message.warn("请选择工单");
			}
		});
		//强制完工
		$("#btn_complete").on("click", function()
		{
			var ids = [];
			for ( var s in selectData)
			{
				if (selectData[s].length != 0)
				{
					for (var i = 0, len = selectData[s].length; i < len; i++)
					{
						ids.push(selectData[s][i].id);
					}
				}
			}
			if (Helper.isNotEmpty(ids))
			{
				Helper.post(Helper.basePath + '/purch/order/forceCompleteYes', {
					"ids[]" : ids
				}, function(data)
				{
					if (data.success)
					{
						Helper.message.suc(data.message);
					} else
					{
						Helper.message.warn(data.message);
					}
					$("#bootTable").bootstrapTable("refreshOptions", {
						pageNumber : 1
					});
					$("#detailTable tbody").empty();
					selectData = {};
				});
			} else
			{
				Helper.message.warn("请选择工单");
			}
			
			// 初始化表格拖动功能
			table_ColDrag($("#detailTable"));
		});
		//取消强制完工
		$("#btn_complete_cancel").on("click", function()
		{
			var ids = [];
			for ( var s in selectData)
			{
				if (selectData[s].length != 0)
				{
					for (var i = 0, len = selectData[s].length; i < len; i++)
					{
						ids.push(selectData[s][i].id);
					}
				}
			}
			if (Helper.isNotEmpty(ids))
			{
				Helper.post(Helper.basePath + '/purch/order/forceCompleteNo', {
					"ids[]" : ids
				}, function(data)
				{
					if (data.success)
					{
						Helper.message.suc(data.message);
					} else
					{
						Helper.message.warn(data.message);
					}
					$("#bootTable").bootstrapTable("refreshOptions", {
						pageNumber : 1
					});
					$("#detailTable tbody").empty();
					selectData = {};
				});
			} else
			{
				Helper.message.warn("请选择工单");
			}
			
			// 初始化表格拖动功能
			table_ColDrag($("#detailTable"));
		});

		//订单详情table
		$("#bootTable").bootstrapTable({
			url : Helper.basePath + "/purch/transmit/to_purch_order_list",
			method : "post",
			contentType : 'application/json', //设置请求头信息  
			dataType : "json",
			pagination : true, // 是否显示分页（*）
			sidePagination : 'server',//设置为服务器端分页
			pageList : Helper.bootPageList,
			queryParamsType : "",
			pageSize : 10,
			pageNumber : 1,
			queryParams : queryParams,//参数
			responseHandler : responseHandler,

			//resizable : true, //是否启用列拖动
			showColumns : true, //是否显示所有的列
			minimumCountColumns : 2, //最少允许的列数
			striped : true, // 是否显示行间隔色
			cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			sortable : false, // 是否启用排序
			clickToSelect : false, // 是否启用点击选中行
			height : ($("body").height() - 100) * 0.5, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			cardView : false, // 是否显示详细视图
			detailView : false, // 是否显示父子表

			showExport : true,//是否显示导出按钮
			//exportDataType: 'all',
			exportTypes : [ 'csv', 'txt', 'excel', 'doc' ],

			cookie : true,//是否启用COOKIE
			cookiesEnabled : [ 'bs.table.columns' ],//默认会记住很多属性，这里控制只记住列选择属性
			cookieIdTable : "print_workmaterial_transmit_purch",//必须制定唯一的表格cookieID

			uniqueId : "id",//定义列表唯一键
			columns : [ {
				field : 'index',
				title : '序号',
				width : 40,
				formatter : function(value, row, index)
				{
					if (row.code == null)
					{
						return;
					}
					return index + 1;
				}
			}, {
				field : 'state',
				title : '选中',
				formatter : function(value, row, index)
				{
					return '<input type="checkbox" class="selectMaterial"/>'
				},
				visible : true,
				width : 60
			}, {
				field : 'materialClassName',
				title : '材料分类',
				width : 80
			}, {
				field : 'code',
				title : '材料编号',
				width : 100
			}, {
				field : 'materialName',
				title : '材料名称',
				width : 120,
				class : 'materialName',
				formatter : function(value, row, index)
				{
					if (value == null)
					{
						return;
					}
					return value + '<input type="hidden" class="materialId" value="'+row.materialId+'"/>';
				}
			}, {
				field : 'style',
				title : '材料规格',
				width : 120,
				class : "style"
			}, {
				field : 'weight',
				title : '克重',
				width : 80
			}, {
				field : 'unitName',
				title : '单位',
				width : 80
			}, {
				field : 'stockQty',
				title : '库存数量',
				width : 60
			}, {
				field : 'workQty',
				title : '工单占用量',
				width : 60
			}, {
				field : 'purchQty',
				title : '在途采购量',
				width : 80,
				formatter : function(value, row, index)
				{
					if (Number(value) < 0)
					{
						return "0";
					} else
					{
						return value;
					}
				}
			}, {
				field : 'qty',
				title : '需采购数量',
				width : 80

			} ],
			onLoadSuccess : function(data)
			{
				$("#bootTable tbody").find("tr:last").find("td:first").text("合计");
				$("#bootTable tbody").find("tr:last").find("td:first").next().children("input[type='checkbox']").remove();
				var _tds = $("#bootTable tbody").find("tr:last").find("td");
				for (var index = 0; index < _tds.length; index++)
				{
					if (_tds.eq(index).text() == "-")
					{
						_tds.eq(index).text('');
					}
				}
				//$("#bootTable").find("thead tr").find("th").eq(0).attr("style", "width:;");
			},
			onColumnSwitch : function(field, checked)
			{	
				// 在使用筛选增加或减少列时重置表格列拖动效果
				bootstrapTable_ColDrag($("#bootTable"));
			}
		});
		$("#bootTable").on('load-success.bs.table column-switch.bs.table page-change.bs.table search.bs.table', function(event, data)
		{
			/* 表格工具栏 */
			if ($(".glyphicon-th").next().html() == '')
			{
				$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
				if (!Helper.basic.hasPermission('transmit:to_purch_order:export'))
				{
					$(".export.btn-group").remove();
				}
				$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
			}
			//每次翻页检查是否有数据选中
			$("#bootTable tbody").find("tr").each(function()
			{
				var materialId = $(this).find(".materialId").val();
				var style = $(this).find(".style").text();
				var key = materialId + "-" + style;
				if (selectData[key] && selectData[key].length > 0)
				{
					$(this).find("input[type=checkbox]").prop("checked", true);
				}
			})
			if (data.total > 0)
			{
				//默认选中第一条
				$(this).find("tbody tr:first-child").addClass("current");
				$selectRow = $(this).find("tbody tr:first-child");
				var materialName = $selectRow.find(".materialName").text();
				var style = $selectRow.find(".style").text();
				getWorkByMaterial(materialName, style);
			}

		});
		//主表选中或取消选中
		$(document).on("change", "#bootTable input.selectMaterial", function()
		{
			$("input.displaySelect").removeClass("active").val("显示已选");
			$selectRow = $(this).parents("tr");
			$(this).parents("tr").addClass("current").siblings().removeClass("current");
			if ($(this).prop("checked") == true)
			{
				var $tr = $(this).parent().parent();
				var materialName = $tr.find(".materialName").text();
				var style = $tr.find(".style").text();
				getWorkByMaterialAndCheck(materialName, style, true);
			} else
			{
				var $tr = $(this).parent().parent();
				var materialName = $tr.find(".materialName").text();
				var style = $tr.find(".style").text();
				getWorkByMaterialAndCheck(materialName, style, false);
			}

		})
		//主表点击行，添加背景色并请求从表
		$(document).on("click", "#bootTable tbody tr:not(.no-records-found)", function()
		{
			$("input.displaySelect").removeClass("active").val("显示已选");
			$selectRow = $(this);
			// 避开选择合计行
			if (Helper.isNotEmpty($(this).find(".materialId").val()))
			{
				$(this).addClass("current").siblings().removeClass("current");
				var materialName = $(this).find(".materialName").text();
				var style = $(this).find(".style").text();
				getWorkByMaterial(materialName, style);
			}
		})
		//从表批量选中
		$(".batchSelectDetail").on("change", function()
		{
			$("#detailTable tbody").find("tr").each(function()
			{
				if ($(".batchSelectDetail").prop("checked") == true)
				{
					$(this).find('input[type=checkbox].check').prop("checked", true);
				} else
				{
					$(this).find('input[type=checkbox].check').prop("checked", false);
				}
				$(this).find('input[type=checkbox].check').trigger("change");
			})
		})
		//从表点击行
		$("#detailTable").on("click", 'tr', function(e)
		{
			//判断是否点击checkbox 选中按钮，不是则执行
			if (!$(e.target).is("input[type='checkbox'].check"))
			{
				if ($(this).find("input[type=checkbox].check").prop("checked") == true)
				{
					$(this).find("input[type=checkbox].check").prop("checked", false);
				} else
				{
					$(this).find("input[type=checkbox].check").prop("checked", true);
				}
				$(this).find("input[type=checkbox].check").trigger("change");
			}
		})
		//从表单条选中或取消选中
		$("#detailTable").on("change", 'input[type=checkbox].check', function(e)
		{
			e.stopPropagation();
			var $tr = $(this).parent().parent();
			var workId = $tr.find(".id").text();
			var materialId = $tr.find(".materialName").data("materialid");
			var style = $tr.find(".style").text();
			var key = materialId + '-' + style;
			if ($(this).prop("checked") == true)
			{
				var work = {};
				work.id = $tr.find(".id").text();
				work.billTypeText = $tr.find(".billTypeText").text();
				work.workBillNo = $tr.find(".workBillNo").text();
				work.createTime = $tr.find(".createTime").text();
				work.productNames = $tr.find(".productNames").text();
				work.materialName = $tr.find(".materialName").text();
				work.materialId = $tr.find(".materialName").data("materialid");
				work.style = $tr.find(".style").text();
				work.weight = $tr.find(".weight").text();
				work.stockUnitName = $tr.find(".stockUnitName").text();
				work.qty = $tr.find(".qty").text();
				work.purchQty = $tr.find(".purchQty").text();
				work.notPurchQty = $tr.find(".notPurchQty").text();
				work.memo = $tr.find(".workMemo").text();
				if (selectData[key] == undefined)
				{
					selectData[key] = [];
				}
				selectData[key].push(work);
				//检查主表，从表有选中则check
				$("#bootTable tbody").find("tr").each(function()
				{
					var boot_materialId = $(this).find(".materialId").val();
					var boot_style = $(this).find(".style").text();
					var boot_key = boot_materialId + '-' + boot_style;
					if (key == boot_key && selectData[boot_key].length > 0)
					{
						$(this).find("input.selectMaterial").prop("checked", true);
					}
				})
			} else
			{
				for (var i = 0, len = selectData[key].length; i < len; i++)
				{
					if (selectData[key][i].id == workId)
					{
						selectData[key].splice(i, 1);
						break;
					}
				}
				//检查主表，从表选中数等于0则不 check
				if (selectData[key].length == 0)
				{
					$("#bootTable tbody").find("tr").each(function()
					{
						var boot_materialId = $(this).find(".materialId").val();
						var boot_style = $(this).find(".style").text();
						var boot_key = boot_materialId + '-' + boot_style;
						if (boot_key == key)
						{
							$(this).find("input.selectMaterial").prop("checked", false);
						}
					})
				}
			}
			checkSelectAll();

		})
		//显示已选中数据
		$("input.displaySelect").on("click", function()
		{
			if (!$(this).hasClass("active"))
			{
				$(this).addClass("active");
				$(this).val("显示明细");
				$("#detailTable tbody").empty();
				$("#bootTable tbody").find("tr.current").removeClass("current");
				for ( var s in selectData)
				{
					if (selectData[s].length != 0)
					{
						renderDetail(selectData[s]);
					}
				}
			} else
			{
				$(this).removeClass("active");
				$(this).val("显示已选");
				$selectRow.addClass("current");
				var materialName = $selectRow.find(".materialName").text();
				var style = $selectRow.find(".style").text();
				getWorkByMaterial(materialName, style);
			}
		})
		//根据材料和规格渲染从表
		function getWorkByMaterial(materialName, style)
		{
			Helper.request({
				data : {
					"materialName" : materialName,
					"specifications" : style,
					"billNo" : $("input[name='billNo']").val().trim(),
					"completeFlag" : $("input[name='completeFlag']:checked").val()
				},
				url : Helper.basePath + "/purch/transmit/getWorkToPurchByMaterial",
				success : function(data)
				{
					$("#detailTable tbody").html("");
					renderDetail(data);
				}
			})
		}
		//根据材料和规格渲染从表,全部选中或全部取消选中
		function getWorkByMaterialAndCheck(materialName, style, checkFlag)
		{
			Helper.request({
				data : {
					"materialName" : materialName,
					"specifications" : style,
					"billNo" : $("input[name='billNo']").val().trim(),
					"completeFlag" : $("input[name='completeFlag']:checked").val()
				},
				url : Helper.basePath + "/purch/transmit/getWorkToPurchByMaterial",
				success : function(data)
				{
					$("#detailTable tbody").html("");
					renderDetailAndCheck(data, checkFlag);
					$("#detailTable tbody").find("tr").each(function()
					{
						$(this).find('input[type=checkbox].check').trigger("change");
					})
				}
			})
		}
		//渲染从表
		function renderDetail(data)
		{
			var html = "";
			for (var i = 0, len1 = data.length; i < len1; i++)
			{
				if (data[i].work)
				{
					var checkFlag = false;
					for ( var s in selectData)
					{
						if (selectData[s].length != 0)
						{
							for (var j = 0, len2 = selectData[s].length; j < len2; j++)
							{
								if (selectData[s][j].id == data[i].id)
								{
									checkFlag = true;
									break;
								}
							}
						}
					}
					if (checkFlag)
					{
						html += '<tr><td>' + '<input class="check" type="checkbox" checked="checked"/>' + '</td>';
					} else
					{
						html += '<tr><td>' + '<input class="check" type="checkbox"/>' + '</td>';
					}
					html += '<td class="id" style="display:none">' + data[i].id + '</td>';
					html += '<td class="billTypeText">' + data[i].work.billTypeText + '</td>';
					html += '<td class="workBillNo">' + billNoTransToUrl(data[i].workBillNo) + '</td>';
					html += '<td class="createTime">' + new Date(data[i].work.createTime).format('yyyy-MM-dd') + '</td>';
					html += '<td class="productNames">' + data[i].productNames + '</td>';
					html += '<td class="materialName" data-materialId="'+data[i].materialId+'">' + data[i].materialName + '</td>';
					html += '<td class="style">' + data[i].style + '</td>';
					html += '<td class="weight">' + data[i].weight + '</td>';
					html += '<td class="stockUnitName">' + data[i].stockUnitName + '</td>';
					html += '<td class="qty">' + data[i].qty + '</td>';
					html += '<td class="purchQty">' + data[i].purchQty + '</td>';
					if (data[i].stockUnitName == "令")
					{
						var accuracy = Helper.basic.info('UNIT', data[i].stockUnitId).accuracy;
						html += '<td class="notPurchQty">' + (data[i].qty - data[i].purchQty).toFixed(accuracy) + '</td>';			
					} else
					{
						html += '<td class="notPurchQty">' + (data[i].qty - data[i].purchQty) + '</td>';
					}
					html += '<td class="workMemo">' + data[i].work.memo + '</td></tr>';
				} else
				{
					html += '<tr><td>' + '<input class="check" type="checkbox" checked="checked"/>' + '</td>';
					html += '<td class="id" style="display:none">' + data[i].id + '</td>';
					html += '<td class="billTypeText">' + data[i].billTypeText + '</td>';
					html += '<td class="workBillNo">' + billNoTransToUrl(data[i].workBillNo) + '</td>';
					html += '<td class="createTime">' + data[i].createTime + '</td>';
					html += '<td class="productNames">' + data[i].productNames + '</td>';
					html += '<td class="materialName" data-materialId="'+data[i].materialId+'">' + data[i].materialName + '</td>';
					html += '<td class="style">' + data[i].style + '</td>';
					html += '<td class="weight">' + data[i].weight + '</td>';
					html += '<td class="stockUnitName">' + data[i].stockUnitName + '</td>';
					html += '<td class="qty">' + data[i].qty + '</td>';
					html += '<td class="purchQty">' + data[i].purchQty + '</td>';
					html += '<td class="notPurchQty">' + data[i].notPurchQty + '</td>';
					html += '<td class="workMemo">' + data[i].memo + '</td></tr>';
				}
			}
			$("#detailTable tbody").append(html);
			//判断全选按钮是否选中
			checkSelectAll();
			
			// 初始化表格拖动功能
			table_ColDrag($("#detailTable"));
		}
		//渲染从表并check
		function renderDetailAndCheck(data, checkFlag)
		{
			var html = "";console.log("data"+data);
			for (var i = 0, len1 = data.length; i < len1; i++)
			{
				if (checkFlag)
				{
					html += '<tr><td>' + '<input class="check" type="checkbox" checked="checked"/>' + '</td>';
				} else
				{
					html += '<tr><td>' + '<input class="check" type="checkbox"/>' + '</td>';
				}
				html += '<td class="id" style="display:none">' + data[i].id + '</td>';
				html += '<td class="billTypeText">' + data[i].work.billTypeText + '</td>';
				html += '<td class="workBillNo">' + billNoTransToUrl(data[i].workBillNo) + '</td>';
				html += '<td class="createTime">' + new Date(data[i].work.createTime).format('yyyy-MM-dd') + '</td>';
				html += '<td class="productNames">' + data[i].productNames + '</td>';
				html += '<td class="materialName" data-materialId="'+data[i].materialId+'">' + data[i].materialName + '</td>';
				html += '<td class="style">' + data[i].style + '</td>';
				html += '<td class="weight">' + data[i].weight + '</td>';
				html += '<td class="stockUnitName">' + data[i].stockUnitName + '</td>';
				html += '<td class="qty">' + data[i].qty + '</td>';
				html += '<td class="purchQty">' + data[i].purchQty + '</td>';
				if (data[i].stockUnitName == "令")
				{
					var accuracy = Helper.basic.info('UNIT', data[i].stockUnitId).accuracy;
					html += '<td class="notPurchQty">' + (data[i].qty - data[i].purchQty).toFixed(accuracy) + '</td>';			
				} else
				{
					html += '<td class="notPurchQty">' + (data[i].qty - data[i].purchQty) + '</td>';
				}
				html += '<td class="workMemo">' + data[i].work.memo + '</td></tr>';
			}
			// 合计行
			$("#detailTable tbody").append(html);
		}
		//判断全选按钮是否选中
		function checkSelectAll()
		{
			if ($("#detailTable tbody").find("input[type=checkbox]:checked").length != $("#detailTable tbody").find("tr").length || $("#detailTable tbody").find("tr").length == 0)
			{
				$("#detailTable thead").find("input[type=checkbox]").prop("checked", false);
			} else
			{
				$("#detailTable thead").find("input[type=checkbox]").prop("checked", true);
			}
		}
		function queryParams(params)
		{
			params['billNo'] = $("#billNo").val().trim();
			params['materialName'] = $("#materialName").val().trim();
			params['completeFlag'] = $("input[name='completeFlag']:checked").val().trim();
			params['materialClassId'] = $("#materialClassId").val();
			params['code'] = $("#code").val().trim();

			params['productStyle'] = $("#style").val().trim();

			return params;
		}
	});
</script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--头部START-->
			<div class="cl">
				<!-- 				<div class="iframe-top"> -->
				<%-- 					<sys:nav struct="采购管理-转单功能-工单未采购"></sys:nav> --%>
				<!-- 				</div> -->
				<div class="top_nav">
					<shiro:hasPermission name="purch:order:create">
						<button class="nav_btn table_nav_btn" id="btn_transmit">生成采购单</button>
					</shiro:hasPermission>
					<shiro:hasPermission name="produce:work:complete">
						<button class="nav_btn table_nav_btn" id="btn_complete">强制完工</button>
					</shiro:hasPermission>

					<shiro:hasPermission name="produce:work:complete_cancel">
						<button class="nav_btn table_nav_btn" id="btn_complete_cancel" style="display: none;">取消强制完工</button>
					</shiro:hasPermission>

				</div>
			</div>
			<!--头部END-->
			<!--查询表单START-->
			<div class="cl">
				<div class="cl order-info">
					<dl class="cl row-dl">
						<dd class="row-dd">
							<label class="form-label label_ui label_1">材料分类：</label>
							<span class="ui-combo-wrap wrap-width">
								<phtml:list items="${fns:basicList('MATERIALCLASS')}" valueProperty="id" textProperty="name" name="materialClassId" defaultOption="请选择" defaultValue="" cssClass="input-txt input-txt_1 hy_select2" />
							</span>
						</dd>

						<dd class="row-dd">
							<label class="form-label label_ui label_1">材料编号：</label>
							<span class="ui-combo-wrap wrap-width">
								<input id="code" type="text" class="input-txt input-txt_8" name="code" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">材料名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input id="materialName" type="text" class="input-txt input-txt_9" name="materialName" />
								<div class="select-btn" id="material_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">材料规格：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_8" id="style" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">生产单号：</label>
							<span class="ui-combo-wrap form_text">
								<input id="billNo" name="billNo" type="text" class="input-txt input-txt_9" />
							</span>
						</dd>
						<dd class="row-dd">
							<button type="button" class="nav_btn table_nav_btn search_btn" id="btn_search">
								<i class="fa fa-search"></i>
								查询
							</button>
						</dd>
					</dl>
				</div>
				<div>
					<div class="radio-box">
						<label>
							<input type="radio" value="YES" name="completeFlag" />
							已强制完工
						</label>
						<label>
							<input type="radio" value="NO" name="completeFlag" checked="checked" />
							未强制完工
						</label>
					</div>
				</div>
			</div>
			<!--查询表单End-->
			<!--主表表格Start-->
			<div class="boot-mar">
				<table class="border-table resizable" id="bootTable">
				</table>
			</div>
			<!--主表表格End-->
			<div class="cl">
				<input type="button" class="r nav_btn table_nav_btn displaySelect" style="margin-left: 5px; margin-top: 2px" value="显示已选" />
			</div>
			<!--从表表格Start-->
			<div id="detailTableDiv" style="margin-top: 2px;">
				<table class="border-table resizable" id="detailTable" style="overflow: auto">
					<thead>
						<th>
							<input type="checkbox" class="batchSelectDetail" />
						</th>
						<th class="billId" style="display: none">id</th>
						<th>工单类型</th>
						<th>生产单号</th>
						<th>制单日期</th>
						<th>成品名称</th>
						<th>材料名称</th>
						<th>材料规格</th>
						<th>克重</th>
						<th>单位</th>
						<th>需求数量</th>
						<th>已采购数量</th>
						<th>未采购数量</th>
						<th>备注</th>
					</thead>
					<tbody></tbody>
				</table>
			</div>
			<!--从表表格End-->
		</div>
	</div>
</body>
</html>