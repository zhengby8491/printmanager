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
		url : Helper.basePath + "/basic/product/listAjax",
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
		cookieIdTable : "print_basic_product",// 必须制定唯一的表格cookieID

		uniqueId : "id",// 定义列表唯一键
		columns : [ {
			field : 'state',
			checkbox : true,
			visible : true,
			width : 40
		}, {
			field : 'productClassName',
			title : '产品分类',
			width : 80
		}, {
			field : 'customerMaterialCode',
			title : '客户料号',
			width : 80
		}, {
			field : 'code',
			title : '产品编号',
			width : 100
		}, {
			field : 'name',
			title : '成品名称',
			width : 120
		}, {
			field : 'specifications',
			title : '产品规格',
			width : 80
		}, {
			field : 'unitName',
			title : '单位',
			width : 50
		}, {
			field : 'salePrice',
			title : '单价',
			width : 50
		}, {
			field : 'isPublicText',
			title : '是否公共产品',
			width : 90
		}, {
			field : 'isValidText',
			title : '是否有效',
			width : 70
		}, {
			field : 'createName',
			title : '创建人',
			visible : false,
			width : 70
		}, {
			field : 'updateName',
			title : '修改人',
			visible : false,
			width : 70
		}, {
			field : 'createTime',
			title : '创建时间',
			visible : false,
			width : 75,
			formatter : function(value, row, index)
			{
				if (Helper.isNotEmpty(value))
				{
					return new Date(value).format("yyyy-MM-dd");
				}
			}
		}, {
			field : 'updateTime',
			title : '修改时间',
			visible : false,
			width : 75,
			formatter : function(value, row, index)
			{
				if (Helper.isNotEmpty(value))
				{
					return new Date(value).format("yyyy-MM-dd");
				}
			}
		}, {
			field : 'memo',
			title : '备注',
			'class' : 'memoView',
			width : 200
		}, {
			field : 'operator',
			title : '操作',
			width : 120,
			formatter : function(value, row, index)
			{
				var operator = "";
				if (Helper.basic.hasPermission("basic:product:edit"))
				{
					operator += '<a title="编辑" href="javascript:;" onclick="product_edit(' + row.id + ')" style="margin-right:20px"><i class="fa fa-pencil"></i></a>';
				}

				if (Helper.basic.hasPermission("basic:product:del"))
				{
					operator += '<a title="删除" href="javascript:;" onclick="product_del(this,' + row.id + ')"><i class="fa fa-trash-o"></i></a>';
				}

				if (Helper.basic.hasPermission("basic:product:copyCreate"))
				{
					operator += '<a title="复制" href="javascript:;" onclick="product_copyCreate(' + row.id + ')" style="margin-left:20px"><i class="fa fa-copy"></i></a>';
				}
				return operator;
			}
		}, {
			field : 'imgUrl',
			title : '产品图片',
			width : 60,
			formatter : function(value, row, index)
			{
				if (row.imgUrl != "" && row.imgUrl != null)
				{
					return '<img class="pimg" src="' + row.imgUrl + '"/>';
				} else
				{
					return "";
				}
			}
		} ],
		onDblClickRow : function(row)
		{
			product_edit(row.id);
		},
		onColumnSwitch : function(field, checked)
		{	
			// 在使用筛选增加或减少列时重置表格列拖动效果
			bootstrapTable_ColDrag($("#bootTable"));
		}
	});
	$("#bootTable").on('load-success.bs.table column-switch.bs.table page-change.bs.table search.bs.table', function()
	{
		/* 表格工具栏 */
		if ($(".glyphicon-th").next().html() == '')
		{
			$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
			if (!Helper.basic.hasPermission('basic:product:export'))
			{
				$(".export.btn-group").remove();
			}
			$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
		}
	});

	/* 批量删除 */
	$("#batch_delete").on("click", function()
	{
		var rows = getSelectedRows();
		var ids = $(rows).map(function()
		{
			return this.id;
		}).get();
		if (Helper.isNotEmpty(rows))
		{
			Helper.message.confirm('确认要删除吗？', function(index)
			{
				Helper.post(Helper.basePath + '/basic/product/batchDelete', {
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

			});
		} else
		{
			Helper.message.warn("至少选择1项");
		}
	});
});
/* 导入 */
function import_info()
{
	Helper.popup.show('导入信息', Helper.basePath + '/import/company_product', '540', '150');
}
/* 产品信息-增加 */
function product_create()
{
	Helper.popup.show('添加产品', Helper.basePath + '/basic/product/create', '1016', '520');
}
/* 产品信息-编辑 */
function product_edit(id)
{
	Helper.popup.show('编辑产品', Helper.basePath + '/basic/product/edit/' + id, '1016', '520');
}
/* 产品信息-复制添加 */
function product_copyCreate(id)
{
	Helper.popup.show('添加产品', Helper.basePath + '/basic/product/copyCreate/' + id, '1016', '520');
}
/* 产品信息-删除 */
function product_del(obj, id)
{
	Helper.message.confirm('确认要删除吗？', function(index)
	{

		// 此处请求后台程序，下方是成功后的前台处理……
		$.post(Helper.basePath + '/basic/product/delete/' + id, function(result)
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

/* 产品名称-变更 */
function product_updateName()
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
	Helper.popup.show('变更产品名称', Helper.basePath + '/basic/product/updateName/' + id, '400', '200');
}

function responseHandler(res)
{
	return {
		rows : res.result,
		total : res.count
	};
}

// 获取选择项
function getSelectedRows()
{
	return $("#bootTable").bootstrapTable('getAllSelections');
}

function queryParams(params)
{
	params['customerName'] = $("#customerName").val().trim();
	params['productName'] = $("#productName").val().trim();
	params['productCode'] = $("#productCode").val().trim();
	params['productStyle'] = $("#productStyle").val().trim();
	params['customerMaterialCode'] = $("#customerMaterialCode").val().trim();
	return params;
}