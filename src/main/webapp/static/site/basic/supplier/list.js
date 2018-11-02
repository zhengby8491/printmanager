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
		url : Helper.basePath + "/basic/supplier/listAjax",
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
		cookieIdTable : "print_basic_supplier",// 必须制定唯一的表格cookieID

		uniqueId : "id",// 定义列表唯一键
		columns : [ {
			field : 'state',
			checkbox : true,
			visible : true,
			width : 40
		}, {
			field : 'supplierClassName',
			title : '供应商分类',
			width : 100
		}, {
			field : 'code',
			title : '供应商编号',
			width : 100
		}, {
			field : 'name',
			title : '供应商名称',
			width : 220
		}, {
			field : 'employeeName',
			title : '采购员',
			width : 70
		}, {
			field : 'deliveryClassName',
			title : '送货方式',
			width : 100
		}, {
			field : 'paymentClassName',
			title : '付款方式',
			width : 100
		}, {
			field : 'settlementClassName',
			title : '结算方式',
			width : 80
		}, {
			field : 'taxRateName',
			title : '税收',
			width : 100
		}, {
			field : 'advanceMoney',
			title : '预付款',
			width : 80
		}, {
			field : 'isBeginText',
			title : '是否期初',
			width : 60
		}, {
			field : 'isValidText',
			title : '是否有效',
			width : 60
		}, {
			field : 'originCompanyId',
			title : '代工平台',
			width : 100,
			formatter : function(value, row, index)
			{
				if (value == "" || value == null)
				{
					return '否';
				} else
				{
					return '是';
				}
			},
			cellStyle : function(value, row, index, field)
			{
				if (value == "是")
				{
					return {
						css : {
							"color" : "#f00"
						}
					};
				} else
				{
					return {
						css : {
							"color" : "#000"
						}
					};
				}
			},
			visible : false
		}, {
			field : 'createName',
			title : '创建人',
			width : 80,
			visible : false
		}, {
			field : 'createTime',
			title : '创建时间',
			width : 80,
			visible : false,
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
			width : 60,
			visible : false
		}, {
			field : 'updateTime',
			title : '修改日期',
			width : 80,
			visible : false,
			formatter : function(value, row, index)
			{
				if (Helper.isNotEmpty(value))
				{
					return new Date(value).format("yyyy-MM-dd");
				}
			}
		}, {
			field : 'memo',
			'class' : 'memoView',
			title : '备注',
			width : 150
		}, {
			field : 'operator',
			title : '操作',
			width : 100,
			formatter : function(value, row, index)
			{
				var operator = "";
				if (Helper.basic.hasPermission("basic:supplier:edit"))
				{
					operator += '<a title="编辑" href="javascript:;" onclick="supplier_edit(' + row.id + ')" style="margin-right: 20px"> <i class="fa fa-pencil"></i></a>';
				}

				if (Helper.basic.hasPermission("basic:supplier:del"))
				{
					operator += '<a title="删除" href="javascript:;"onclick="supplier_del(this,' + row.id + ')"><i class="fa fa-trash-o"></i></a>';
				}

				if (Helper.basic.hasPermission("basic:supplier:copyCreate"))
				{
					operator += '<a title="复制" href="javascript:;" onclick="supplier_copyCreate(' + row.id + ')" style="margin-left: 20px"><i class="fa fa-copy"></i></a>';
				}

				return operator;
			}
		} ],
		onDblClickRow : function(row)
		{
			supplier_edit(row.id);
		},
		onClickRow : function(row, $element)
		{
			$element.addClass("tr_active").siblings().removeClass("tr_active");
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
			if (!Helper.basic.hasPermission('basic:supplier:export'))
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
				Helper.post(Helper.basePath + '/basic/supplier/batchDelete', {
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
	Helper.popup.show('导入信息', Helper.basePath + '/import/company_supplier', '540', '150');
}

/* 供应商信息-增加 */
function supplier_add()
{
	Helper.popup.show('添加供应商', Helper.basePath + '/basic/supplier/create', '1016', '520');
}

/* 供应商信息-复制添加 */
function supplier_copyCreate(id)
{
	Helper.popup.show('添加供应商', Helper.basePath + '/basic/supplier/copyCreate/' + id, '1016', '520');
}

/* 供应商信息-编辑 */
function supplier_edit(id)
{
	Helper.popup.show('编辑供应商', Helper.basePath + '/basic/supplier/edit/' + id, '1016', '520');
}
/* 供应商信息-删除 */
function supplier_del(obj, id)
{
	Helper.message.confirm('确认要删除吗？', function(index)
	{
		// 此处请求后台程序，下方是成功后的前台处理……
		$.post(Helper.basePath + '/basic/supplier/delete/' + id, function(result)
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
/* 供应商名称-变更 */
function supplier_updateName()
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
	Helper.popup.show('变更供应商名称', Helper.basePath + '/basic/supplier/updateName/' + id, '400', '200');
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
	params['supplierName'] = $("#supplierName").val();
	params['code'] = $("#code").val();
	params['isOem'] = $("#isOem").is(":checked")?"YES":"NO";
	return params;
}

function getSelectedRows()
{
	return $("#bootTable").bootstrapTable('getAllSelections');
}

// 获取返回信息
function getCallInfo(content)
{
	console.log(content);
	// alert("获取返回信息：选择供应商信息为："+content);
}