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
		url : Helper.basePath + "/basic/account/listAjax",
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
		height : 426, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
		cardView : false, // 是否显示详细视图
		detailView : false, // 是否显示父子表

		showExport : true,// 是否显示导出按钮
		// exportDataType: 'all',
		exportTypes : [ 'csv', 'txt', 'excel', 'doc' ],

		cookie : true,// 是否启用COOKIE
		cookiesEnabled : [ 'bs.table.columns' ],// 默认会记住很多属性，这里控制只记住列选择属性
		cookieIdTable : "print_basic_account",// 必须制定唯一的表格cookieID

		uniqueId : "id",// 定义列表唯一键
		columns : [ {
			field : 'index',
			title : '序号',
			width : 40,
			formatter : function(value, row, index)
			{
				// console.log($(this).pageSixze)
				return index + 1;
			}
		}, {
			field : 'bankTypeText',
			title : '银行卡类型',
			width : 80
		}, {
			field : 'branchName',
			title : '支行名称',
			width : 100
		}, {
			field : 'bankNo',
			title : '账户',
			width : 140
		}, {
			field : 'currencyTypeText',
			title : '币种',
			width : 80
		}, {
			field : 'accountTypeText',
			title : '账号类型',
			width : 60
		}, {
			field : 'isBeginText',
			title : '是否期初',
			width : 60
		}, {
			field : 'money',
			title : '余额',
			width : 60
		}, {
			field : 'createName',
			title : '创建人',
			width : 80
		}, {
			field : 'createTime',
			title : '创建时间',
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
			field : 'lastTransTime',
			title : '最近交易日期',
			width : 80,
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
			width : 200,
			'class' : 'memoView',
			visible : false
		}, {
			field : 'operator',
			title : '操作',
			width : 80,
			formatter : function(value, row, index)
			{
				var operator = "";
				if (Helper.basic.hasPermission("basic:account:edit"))
				{
					operator += '<a title="编辑" href="javascript:;" onclick="account_edit(' + row.id + ')" style="padding: 0 8px; color: green"><i class="fa fa-pencil"></i></a>';
				}

				if (Helper.basic.hasPermission("basic:account:del"))
				{
					operator += '<a title="删除" href="javascript:;" onclick="account_del(this,' + row.id + ')" style="padding: 0 8px; color: red"><i class="fa fa-trash-o"></i></a>';
				}
				return operator;
			}
		} ],
		onDblClickRow : function(row)
		{
			account_edit(row.id);
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
			if (!Helper.basic.hasPermission('basic:account:export'))
			{
				$(".export.btn-group").remove();
			}
			$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
		}
	});
});

/* 账户信息-增加 */
function account_create()
{
	Helper.popup.show('添加账户', Helper.basePath + '/basic/account/create', '700', '300');
}

/* 账户信息-编辑 */
function account_edit(id)
{
	Helper.popup.show('编辑账户', Helper.basePath + '/basic/account/edit/' + id, '700', '300');
}

/* 账户信息-删除 */
function account_del(obj, id)
{
	Helper.message.confirm('确认要删除吗？', function(index)
	{
		// 此处请求后台程序，下方是成功后的前台处理……
		Helper.post(Helper.basePath + '/basic/account/delete/' + id, null, function(data)
		{
			if (data.success && data.obj)
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

function responseHandler(res)
{
	return {
		rows : res.result,
		total : res.count
	};
}

function queryParams(params)
{
	params['bankNo'] = $("#bankNo").val();
	return params;
}