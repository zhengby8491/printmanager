<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<meta charset="UTF-8">
<title>选择材料</title>
<style type="text/css">
.layer_content, .tree {
	height: 90%;
}

.layer_table_container {
	width: auto;
	height: 480px;
	right: 0;
}

.layer_container .fixed-table-container {
	border: 1px solid #ddd;
}

.fixed-table-container tbody .selected td {
	background: transparent;
}

#bootTable tbody tr.current td {
	background: #ddd;
}

.layer_table thead th {
	border: 1px solid #ccc;
	padding: 2px 0;
}
</style>
<script type="text/javascript">
	$(function()
	{
		var selectData = [];
		var isEmpty = false;
		$(".tree").on("click", "a[name='materialClassId']", function()
		{
			$(".tree a[name='materialClassId']").css("color", "");
			$(this).css("color", "red");
			var id = $(this).attr("_id");
			$("#materialClassId").val(id);
			$("#bootTable").bootstrapTable("refreshOptions", {
				pageNumber : 1
			});
		});

		//查询
		$("#btn_search").click(function()
		{
			$("#bootTable").bootstrapTable("refreshOptions", {
				pageNumber : 1
			});
		});

		//选择确认
		$("#btn_ok").click(function()
		{
			//console.log(JSON.stringify(selectData))
			if ($("#multiple").val().toBoolean())
			{//多选,则双击事件
				parent.getCallInfo_materialArray(selectData);
			}
			Helper.popup.close();
		});

		$("#btn_cancel").click(function()
		{
			Helper.popup.close();
		});
		//订单详情table
		$("#bootTable").bootstrapTable({
			url : Helper.basePath + "/quick/material_list",
			method : "post",
			contentType : 'application/json', //设置请求头信息  
			dataType : "json",
			pagination : true, // 是否显示分页（*）
			sidePagination : 'server',//设置为服务器端分页
			pageList : [ 10, 20, 50 ],
			queryParamsType : "",
			pageSize : 10,
			pageNumber : 1,
			queryParams : queryParams,//参数
			responseHandler : responseHandler,

			//showColumns : true, //是否显示所有的列
			//minimumCountColumns : 2, //最少允许的列数
			striped : true, // 是否显示行间隔色
			cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			sortable : false, // 是否启用排序
			clickToSelect : true, // 是否启用点击选中行
			height : 360, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			cardView : false, // 是否显示详细视图
			detailView : false, // 是否显示父子表
			checkbox : $("#multiple").val().toBoolean(),//开启多选
			uniqueId : "id",//定义列表唯一键
			//resizable : true, //是否启用列拖动
			columns : [ {
				field : 'state',
				title : '选中',
				formatter : function(value, row, index)
				{
					return '<input type="checkbox" class="selectMaterial"/>'
				},
				visible : true,
				width : 40
			}, {
				field : 'materialClassName',
				title : '分类',
				width : 80
			}, {
				field : 'code',
				title : '材料编号',
				width : 120,
				class : 'code'
			}, {
				field : 'name',
				title : '材料名称',
				width : 120,
				class : 'materialName',
				formatter : function(value, row, index)
				{
					return value + '<input type="hidden" class="materialId" value="'+row.id+'"/>';
				},
			}, {
				field : 'weight',
				title : '克重',
				width : 50,
				class : 'weight'
			}, {
				field : 'stockUnitName',
				title : '单位',
				width : 50,
				class : 'stockUnitName',
				formatter : function(value, row, index)
				{
					return value + '<input type="hidden" class="stockUnitId" value="'+row.stockUnitId+'"/>' + '<input type="hidden" class="valuationUnitId" value="'+row.valuationUnitId+'"/>';
				},
			} ],
			// 							onDblClickRow : function(row) {
			// 							  	selectRow(row);
			// 							},
			onPageChange : function(number, size)
			{
				if (size == 20)
				{
					setTimeout(function()
					{
						if ($('#bootTable tbody').find('tr').length > 10)
						{
							$('.fixed-table-header').css('margin-right', '17px');
						}
					}, 1000);

				} else if (size == 50)
				{
					setTimeout(function()
					{
						if ($('#bootTable tbody').find('tr').length > 10)
						{
							$('.fixed-table-header').css('margin-right', '17px');
						}
					}, 2000);

				}
			}

		});
		$("#bootTable").on('load-success.bs.table column-switch.bs.table page-change.bs.table search.bs.table', function(event, data)
		{
			//每次翻页检查是否有数据选中
			$("#bootTable tbody").find("tr").each(function()
			{
				var materialId = $(this).find(".materialId").val();
				for (var i = 0, len = selectData.length; i < len; i++)
				{
					if (selectData[i].materialId == materialId)
					{
						$(this).find("input[type=checkbox]").prop("checked", true);
						break;
					}
				}
			})
			if (data.total > 0)
			{
				//默认选中第一条
				var materialId = $(this).find(".materialId").val();
				$(this).find("tbody tr:first-child").addClass("current");
				getMaterialById(materialId);
			}

		});
		//主表选中或取消选中
		$(document).on("change", "#bootTable input.selectMaterial", function()
		{
			var $boot_tr = $(this).parents("tr");
			$boot_tr.addClass("current").siblings().removeClass("current");
			var materialId = $(this).parent().parent().find(".materialId").val();
			if ($(this).prop("checked") == true)
			{
				getMaterialById(materialId, true);
			} else
			{
				getMaterialById(materialId, false);
			}
		})
		//主表点击行，添加背景色并请求从表
		$(document).on("click", "#bootTable tbody tr:not(.no-records-found)", function(e)
		{
			$(this).addClass("current").siblings().removeClass("current");
			var materialId = $(this).find(".materialId").val();
			if (!$(e.target).is("input[type='checkbox'].selectMaterial"))
			{
				getMaterialById(materialId);
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
			var $tr = $(this).parent().parent();
			var $boot_tr = $("#bootTable").find("tr.current");
			var material = {};
			material.id = $boot_tr.find(".materialId").val();
			material.code = $boot_tr.find(".code").text();
			material.name = $boot_tr.find(".materialName").text();
			material.weight = $boot_tr.find(".weight").text();
			material.stockUnitName = $boot_tr.find(".stockUnitName").text();
			material.stockUnitId = $boot_tr.find(".stockUnitId").val();
			material.valuationUnitId = $boot_tr.find(".valuationUnitId").val();
			material.warehouseId = $tr.find(".warehouseId").val();
			material.style = $tr.find(".style").text();
			if ($(this).prop("checked") == true)
			{
				var flag = false;
				for (var i = 0, len = selectData.length; i < len; i++)
				{
					if (selectData[i].warehouseId == material.warehouseId && selectData[i].id == material.id && selectData[i].style == material.style)
					{
						flag = true;
						break;
					}
				}
				if (flag == false)
				{
					selectData.push(material)
				}
			} else
			{
				for (var i = 0, len = selectData.length; i < len; i++)
				{
					if (selectData[i].warehouseId == material.warehouseId && selectData[i].id == material.id && selectData[i].style == material.style)
					{
						selectData.splice(i, 1);
						break;
					}
				}
			}
			console.log(selectData);
			checkSelectAll();
			//检查主表是否选中
			for (var j = 0, len1 = $("#bootTable tbody").find("tr").length; j < len1; j++)
			{
				var checkFlag = false;
				var id = $("#bootTable tbody").find("tr").eq(j).find(".materialId").val();
				for (var i = 0, len2 = selectData.length; i < len2; i++)
				{
					var warehouseId = selectData[i].warehouseId;
					if (selectData[i].id == id && warehouseId != null)
					{
						checkFlag = true;
						break;
					}
				}
				if (checkFlag)
				{
					$("#bootTable tbody").find("tr").eq(j).find("input[type=checkbox].selectMaterial").prop("checked", true);
				} else
				{
					if ($("#detailTable").find("input[type=checkbox].check:checked").length == 0 && $("#bootTable tbody").find("tr").eq(j).find("input[type=checkbox].selectMaterial").prop("checked"))
					{
						//	$("#bootTable tbody").find("tr").eq(j).find("input[type=checkbox].selectMaterial").prop("checked", false);
						// 当取消全选时，判断从表已没有勾选中并判断主表是否已选中当前材料，增加空规格材料 
						var material = {};
						material.id = $boot_tr.find(".materialId").val();
						material.code = $boot_tr.find(".code").text();
						material.name = $boot_tr.find(".materialName").text();
						material.weight = $boot_tr.find(".weight").text();
						material.stockUnitName = $boot_tr.find(".stockUnitName").text();
						material.stockUnitId = $boot_tr.find(".stockUnitId").val();
						material.valuationUnitId = $boot_tr.find(".valuationUnitId").val();
						material.warehouseId = null;
						material.style = "";
						var flag = false;
						for (var i = 0, len = selectData.length; i < len; i++)
						{
							if (selectData[i].warehouseId == material.warehouseId && selectData[i].id == material.id && selectData[i].style == material.style)
							{
								flag = true;
								break;
							}
						}
						if (flag == false)
						{
							selectData.push(material)
						}
					}
				}
			}
		})
		//从表双击行
		$("#detailTable").on("dblclick", 'tr', function(e)
		{
			var $boot_tr = $("#bootTable").find("tr.current");
			var material = {};
			material.id = $boot_tr.find(".materialId").val();
			material.code = $boot_tr.find(".code").text();
			material.name = $boot_tr.find(".materialName").text();
			material.weight = $boot_tr.find(".weight").text();
			material.stockUnitName = $boot_tr.find(".stockUnitName").text();
			material.stockUnitId = $boot_tr.find(".stockUnitId").val();
			material.valuationUnitId = $boot_tr.find(".valuationUnitId").val();
			material.warehouseId = $(this).find(".warehouseId").val();
			material.style = $(this).find(".style").text();
			selectRow(material)
			Helper.popup.close();
		})
		//根据材料和规格渲染从表
		function getMaterialById(materialId, checkAllFlag)
		{
			Helper.request({
				async : false,
				url : Helper.basePath + "/quick/findStockByMaterialId?materialId=" + materialId,
				success : function(data)
				{
					$("#detailTable tbody").html("");
					renderDetail(data, checkAllFlag);
				}
			})
		}

		//渲染从表
		function renderDetail(data, checkAllFlag)
		{
			var html = "";
			for (var i = 0, len1 = data.length; i < len1; i++)
			{
				if (checkAllFlag == undefined)
				{
					var checkFlag = false;
					for (var j = 0, len2 = selectData.length; j < len2; j++)
					{
						if (data[i].warehouseId == selectData[j].warehouseId && data[i].materialId == selectData[j].id && data[i].style == selectData[j].style)
						{
							checkFlag = true;
							break;
						}
					}
					if (checkFlag)
					{
						html += '<tr><td>' + '<input class="check" type="checkbox" checked="checked"/>' + '</td>';
					} else
					{
						html += '<tr><td>' + '<input class="check" type="checkbox"/>' + '</td>';
					}
				} else if (checkAllFlag == true)
				{
					html += '<tr><td>' + '<input class="check" type="checkbox" checked="checked"/>' + '</td>';
				} else if (checkAllFlag == false)
				{
					html += '<tr><td>' + '<input class="check" type="checkbox"/>' + '</td>';
				}
				html += '<td class="warehouseName"><input type="hidden" class="warehouseId" value="'+data[i].warehouseId+'">' + data[i].warehouseName + '</td>';
				html += '<td class="style">' + data[i].style + '</td>';
				html += '<td class="qty">' + (data[i].qty < 0 ? 0 : data[i].qty) + '</td>';
				html += '<td class="workEmployQty">' + (data[i].workEmployQty < 0 ? 0 : data[i].workEmployQty) + '</td>';
				html += '<td class="purchQty">' + (data[i].purchQty < 0 ? 0 : data[i].purchQty) + '</td>';
			}
			$("#detailTable tbody").append(html);
			$("#detailTable tbody").find("input[type=checkbox].check").trigger("change");
			if (data.length == 0)
			{
				var $boot_tr = $("#bootTable").find("tr.current");
				var material = {};
				material.id = $boot_tr.find(".materialId").val();
				material.code = $boot_tr.find(".code").text();
				material.name = $boot_tr.find(".materialName").text();
				material.weight = $boot_tr.find(".weight").text();
				material.stockUnitName = $boot_tr.find(".stockUnitName").text();
				material.stockUnitId = $boot_tr.find(".stockUnitId").val();
				material.valuationUnitId = $boot_tr.find(".valuationUnitId").val();
				material.style = "";
				if (checkAllFlag == true)
				{
					var flag = false;
					for (var i = 0, len = selectData.length; i < len; i++)
					{
						if (selectData[i].id == material.id)
						{
							flag = true;
							break;
						}
					}
					if (flag == false)
					{
						selectData.push(material);
					}
				} else if (checkAllFlag == false)
				{
					for (var i = 0, len = selectData.length; i < len; i++)
					{
						if (selectData[i].id == material.id)
						{
							selectData.splice(i, 1);
							break;
						}
					}
				}
			}
			//判断全选按钮是否选中
			checkSelectAll();
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
				// 当取消全选时，并判断左侧已没有勾选中的话，增加空规格材料 
				if ($("#detailTable").find("input[type=checkbox].check:checked").length == 0)
				{
					var $boot_tr = $("#bootTable").find("tr.current");
					var material = {};
					material.id = $boot_tr.find(".materialId").val();
					material.code = $boot_tr.find(".code").text();
					material.name = $boot_tr.find(".materialName").text();
					material.weight = $boot_tr.find(".weight").text();
					material.stockUnitName = $boot_tr.find(".stockUnitName").text();
					material.stockUnitId = $boot_tr.find(".stockUnitId").val();
					material.valuationUnitId = $boot_tr.find(".valuationUnitId").val();
					material.warehouseId = null;
					material.style = "";
					var flag = false;
					for (var i = 0, len = selectData.length; i < len; i++)
					{
						if (selectData[i].warehouseId == material.warehouseId && selectData[i].id == material.id && selectData[i].style == material.style)
						{
							flag = true;
							break;
						}
					}
					if (flag == false)
					{
						selectData.push(material)
					}
				}
				// 当选中主表勾选时，删除空白规格的材料
				for (var i = 0, len = selectData.length; i < len; i++)
				{
					var $boot_tr = $("#bootTable").find("tr.current");
					var materialId = $boot_tr.find(".materialId").val();
					var warehouseId = selectData[i].warehouseId;
					if (warehouseId == null && selectData[i].id == materialId && selectData[i].style == "")
					{
						selectData.splice(i, 1);
						break;
					}
				}
			}
		}

	});
	//请求参数
	function queryParams(params)
	{
		params['materialClassId'] = $("#materialClassId").val();
		params['materialName'] = $("#materialName").val();
		return params;
	}
	//ajax结果
	function responseHandler(res)
	{
		return {
			rows : res.result,
			total : res.count
		};
	}
	function selectRow(row)
	{
		if ($("#multiple").val().toBoolean())
		{//多选,则双击事件无效
			parent.getCallInfo_materialArray([ row ]);
		} else
		{//单选，双击事件立即返回,处理返回index事件
			parent.getCallInfo_material(row);
			Helper.popup.close();
		}

	}
	//findStockByMaterialId    参数：materialId
</script>
</head>
<body>
	<input type="hidden" id="materialClassId" value="" />
	<input type="hidden" id="multiple" value="${multiple }" />
	<div class="layer_container">
		<div class="cl layer_content">
			<!--分类选择-->
			<div class="tree">
				<ul>
					<a href="javascript:void(0);" _id="" name="materialClassId">全部</a>
					<c:forEach items="${fns:basicList('MATERIALCLASS') }" var="item">
						<a href="javascript:void(0);" _id="${item.id }" name="materialClassId">${item.name }</a>
					</c:forEach>
				</ul>
			</div>
			<!--表格容器左START-->
			<div class="layer_table_container wrap_width_1">
				<div class="cl layer_top">
					<div class="row-dd top_bar">
						<label class="form-label label_ui"></label>
						<span class="ui-combo-wrap" class="form_text">
							<input type="text" class="input-txt input-txt_9" id="materialName" name="materialName" placeholder="材料名称" />
						</span>
						<div class="layer_btns">
							<button class="nav_btn table_nav_btn search_btn" id="btn_search">
								<i class="fa fa-search"></i>
								查找
							</button>
							<button type="button" class="nav_btn table_nav_btn" id="btn_ok" />
							确认
							</button>
							<button type="button" class="nav_btn table_nav_btn" id="btn_cancel">取消</button>
						</div>
					</div>
				</div>
				<!-- 表格 -->
				<div class="table-wrap" style="position: absolute; top: 35px; left: 0; width: 60%; height: 430px;">
					<table class="layer_table " id="bootTable">

					</table>
				</div>
				<div class="r" style="width: 39.9%; height: 390px">
					<table class="layer_table r" id="detailTable">
						<thead>
							<th>
								<input type="checkbox" class="batchSelectDetail" />
							</th>
							<th>仓库</th>
							<th>规格</th>
							<th>数量</th>
							<th>工单占用量</th>
							<th>在途采购量</th>
						</thead>
						<tbody></tbody>
					</table>
				</div>
			</div>
			<!--表格容器左END-->
		</div>
	</div>
</body>
</html>
