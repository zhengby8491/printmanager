$(function()
{
	// 查询，刷新table
	$("#btn_search").click(function()
	{
		$("#bootTable").bootstrapTable("refreshOptions", {
			pageNumber : 1
		});
	});

	// 订单详情table
	$("#bootTable").bootstrapTable({
		url : Helper.basePath + "/basic/material/listAjax",
		method : "post",
		contentType : 'application/json', // 设置请求头信息
		dataType : "json",
		pagination : true, // 是否显示分页（*）
		sidePagination : 'server',// 设置为服务器端分页
		pageList : Helper.bootPageList,
		queryParamsType : "",
		pageSize : 10,
		pageNumber : 1,
		queryParams : queryParams,// 参数
		responseHandler : responseHandler,

		// resizable : true, //是否启用列拖动
		showColumns : true, // 是否显示所有的列
		minimumCountColumns : 2, // 最少允许的列数
		striped : true, // 是否显示行间隔色
		cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
		sortable : false, // 是否启用排序
		clickToSelect : true, // 是否启用点击选中行
		height : 430, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
		cardView : false, // 是否显示详细视图
		detailView : false, // 是否显示父子表

		showExport : true,// 是否显示导出按钮
		// exportDataType: 'all',
		exportTypes : [ 'csv', 'txt', 'excel', 'doc' ],

		cookie : true,// 是否启用COOKIE
		cookiesEnabled : [ 'bs.table.columns' ],// 默认会记住很多属性，这里控制只记住列选择属性
		cookieIdTable : "print_basic_material",// 必须制定唯一的表格cookieID

		uniqueId : "id",// 定义列表唯一键
		columns : [ {
			field : 'state',
			checkbox : true,
			visible : true,
			width : 40
		}, {
			field : 'materialTypeText',
			title : '材料类别',
			width : 80
		}, {
			field : 'materialClassName',
			title : '材料分类',
			width : 80
		}, {
			field : 'code',
			title : '材料编号',
			width : 80
		}, {
			field : 'name',
			title : '材料名称',
			width : 80
		}, {
			field : 'weight',
			title : '克重',
			width : 60
		}, {
			field : 'valuationUnitName',
			title : '计价单位',
			width : 60
		}, {
			field : 'stockUnitName',
			title : '库存单位',
			width : 80
		}, {
			field : 'lastPurchPrice',
			title : '最近采购价',
			width : 80
		}, {
			field : 'minStockNum',
			title : '最低库存',
			width : 80
		}, {
			field : 'isValidText',
			title : '是否有效',
			width : 80
		}, {
			field : 'createName',
			title : '创建人',
			visible : false,
			width : 80
		}, {
			field : 'createTime',
			title : '创建时间',
			visible : false,
			width : 80,
			formatter : function(value, row, index)
			{
				if (Helper.isNotEmpty(value))
				{
					return new Date(value).format("yyyy-MM-dd");
				}
			}
		}, {
			field : 'updateName',
			title : '修改人',
			visible : false,
			width : 60
		}, {
			field : 'updateTime',
			title : '修改时间',
			visible : false,
			width : 80,
			formatter : function(value, row, index)
			{
				if (Helper.isNotEmpty(value))
				{
					return new Date(value).format("yyyy-MM-dd");
				}
			}
		}, {
			field : 'operator',
			title : '操作',
			width : 80,
			formatter : function(value, row, index)
			{
				var operator = "";
				if (Helper.basic.hasPermission("basic:material:edit"))
				{
					operator += '<a title="编辑" href="javascript:;"onclick="material_edit(' + row.id + ')" style="margin-right: 20px"><i class="fa fa-pencil"></i></a>';
				}

				if (Helper.basic.hasPermission("basic:material:del"))
				{
					operator += '<a title="删除" href="javascript:;" onclick="material_del(this,' + row.id + ')"><i class="fa fa-trash-o"></i></a>';
				}

				if (Helper.basic.hasPermission("basic:material:copyCreate"))
				{
					operator += '<a title="复制" href="javascript:;"onclick="material_copyCreate(' + row.id + ')" style="margin-left: 20px"><i class="fa fa-copy"></i></a>';
				}
				return operator;
			}
		} ],
		onDblClickRow : function(row)
		{
			material_edit(row.id);
		},
		onClickRow : function(row, $element)
		{
			$element.addClass("tr_active").siblings().removeClass("tr_active");
		}
	});
	$("#bootTable").on('load-success.bs.table column-switch.bs.table page-change.bs.table search.bs.table', function()
	{
		/* 表格工具栏 */
		if ($(".glyphicon-th").next().html() == '')
		{
			$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
			if (!Helper.basic.hasPermission('basic:material:export'))
			{
				$(".export.btn-group").remove();
			}
			$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
		}
	});

	/* 批量删除 */
	$("#batch_delete").on("click", function()
	{
		Helper.message.confirm('确认要删除吗？', function(index)
		{
			var rows = getSelectedRows();
			var ids = $(rows).map(function()
			{
				return this.id;
			}).get();
			if (Helper.isNotEmpty(rows))
			{
				Helper.post(Helper.basePath + '/basic/material/batchDelete', {
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
				});
			} else
			{
				Helper.message.warn("至少选择1项");
			}
		});
	});

});

/* 导入 */
function import_info()
{
	Helper.popup.show('导入信息', Helper.basePath + '/import/company_material', '540', '150');
}
/* 材料信息-增加 */
function material_create()
{
	Helper.popup.show('添加材料', Helper.basePath + '/basic/material/create', '700', '325');
}
/* 材料信息-编辑 */
function material_edit(id)
{
	Helper.popup.show('编辑材料', Helper.basePath + '/basic/material/edit/' + id, '700', '325');
}
/* 材料信息-复制添加 */
function material_copyCreate(id)
{
	Helper.popup.show('添加材料', Helper.basePath + '/basic/material/copyCreate/' + id, '700', '325');
}
/* 材料信息-删除 */
function material_del(obj, id)
{
	Helper.message.confirm('确认要删除吗？', function(index)
	{

		// 此处请求后台程序，下方是成功后的前台处理……
		$.post(Helper.basePath + '/basic/material/delete/' + id, function(result)
		{
			if (result.toBoolean())
			{
				$(obj).parents("tr").remove();
				Helper.message.suc("已删除！")
			} else
			{
				Helper.message.warn("删除失败，已被引用")
			}
		});
	});
}

/* 材料名称-变更 */
function material_updateName()
{
	var row = getSelectedRows();
	if (row.length > 1)
	{
		return Helper.message.warn("至多选择1项");
	} else if (row.length == 0)
	{
		return Helper.message.warn("至少选择1项");
	}
	var id = row[0].id;
	Helper.popup.show('变更材料名称', Helper.basePath + '/basic/material/updateName/' + id, '400', '200');
}
/* 材料信息-来源报价系统 */
function materialFromOffer()
{
	Helper.popup.show('添加材料', Helper.basePath + '/quick/offer_material_select?multiple=true', '900', '490');
}

function responseHandler(res)
{
	return {
		rows : res.result,
		total : res.count
	};
}
function queryParams(params)
{
	params['materialName'] = $("#materialName").val();
	params['code'] = $("#code").val();
	return params;
}

function getSelectedRows()
{
	return $("#bootTable").bootstrapTable('getAllSelections');
}

/**
 * 来源报价系统 - 新增
 * @param rows
 * @since 1.0, 2018年2月12日 下午1:49:00, think
 */
function getCallInfo_materialArray(rows)
{
	var _materialList = [];
	for (var i = 0; i < rows.length; i++)
	{
		var obj = rows[i];
		_materialList.push({
			offerMaterialType : obj.type,
			offerClassName : obj.className,
			name : obj.name,
			lastPurchPrice : obj.price,
			weight : obj.weight
		});
	}
	// 快速添加
	Helper.request({
		url : Helper.basePath + "/basic/material/saveQuick",
		data : _materialList,
		async : false,
		success : function(data)
		{
			if (data.success)
			{
				location.reload();
			} else
			{
				layer.alert('创建失败：' + data.message);
			}
		}
	});
}