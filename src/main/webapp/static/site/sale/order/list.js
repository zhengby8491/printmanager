	$(function()
	{
		// 选择客户
		$("#customer_quick_select").click(function()
		{
			Helper.popup.show('选择客户', Helper.basePath + '/quick/customer_select?multiple=false', '900', '490');
		});
		// 查询审核状态
		$("input[name='auditFlag']").change(function()
		{
			$("#bootTable").bootstrapTable("refreshOptions", {
				pageNumber : 1
			});
		});
		// 查询，刷新table
		$("#btn_search").click(function()
		{
			$("#bootTable").bootstrapTable("refreshOptions", {
				pageNumber : 1
			});
		});
		//判断当前用户是否有权限查看金额
		var hasPermisson = Helper.basic.hasPermission('sale:order:money');
		// 订单详情table
		$("#bootTable").bootstrapTable({
			url : Helper.basePath + "/sale/order/ajaxList",
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
			clickToSelect : true, // 是否启用点击选中行
			height : 430, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			cardView : false, // 是否显示详细视图
			detailView : false, // 是否显示父子表
			showExport : true,//是否显示导出按钮
			//exportDataType: 'all',
			exportTypes : [ 'csv', 'txt', 'excel', 'doc' ],
			cookie : true,//是否启用COOKIE
			cookiesEnabled : [ 'bs.table.columns' ],//默认会记住很多属性，这里控制只记住列选择属性
			cookieIdTable : "print_sale_order_master",//必须制定唯一的表格cookieID

			uniqueId : "id",//定义列表唯一键
			columns : [ {
				field : 'index',
				title : '序号',
				width : 40,
				formatter : function(value, row, index)
				{
					return index + 1;
				}
			}, {
				field : 'isCheck',
				title : '单据状态',
				width : 70,
				formatter : function(value, row, index)
				{
					if (value == "NO")
					{
						return '未审核';
					}
					if (value == "YES")
					{
						return '已审核';
					}
				},
				cellStyle : function(value, row, index, field)
				{
					if (value == '未审核')
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
								"color" : "#080"
							}
						};
					}
				}
			}, {
				field : 'billTypeText',
				title : '单据类型',
				width : 80
			}, {
				field : 'billNo',
				title : '销售单号',
				width : 140,
				formatter : function(value, row, index)
				{
					return billNoTransToUrl(value);
				}
			}, {
				field : 'customerBillNo',
				title : '客户单号',
				width : 120
			}, {
				field : 'customerName',
				title : '客户名称',
				width : 160
			}, {
				field : 'employeeName',
				title : '销售员',
				width : 80
			}, {
				field : 'linkName',
				title : '联系人',
				width : 80
			}, {
				field : 'mobile',
				title : '联系电话',
				width : 100
			}, {
				field : 'totalMoney',
				title : '金额',
				visible : hasPermisson,
				width : 80
			}, {
				field : 'totalTax',
				title : '税额',
				visible : hasPermisson,
				width : 80
			}, {
				field : 'noTaxTotalMoney',
				title : '不含税金额',
				visible : hasPermisson,
				width : 120
			}, {
				field : 'createName',
				title : '制单人',
				width : 80
			}, {
				field : 'createTime',
				title : '制单日期',
				width : 80,
				formatter : function(value, row, index)
				{
					if (value)
					{
						return new Date(value).format("yyyy-MM-dd");
					}
				}
			}, {
				field : 'checkUserName',
				title : '审核人',
				width : 80
			}, {
				field : 'checkTime',
				title : '审核日期',
				width : 80,
				formatter : function(value, row, index)
				{
					if (value)
					{
						return new Date(value).format("yyyy-MM-dd");
					}
				}
			}, {
				field : 'memo',
				title : '备注',
				width : 120,
				'class' : 'memoView',
				visible : false
			}, {
				field : 'operator',
				title : '操作',
				width : 140,
				formatter : function(value, row, index)
				{
					if (!Helper.isNotEmpty(row.id))
					{
						return;
					}
					var operator = '<span class="table_operator">';
					if (Helper.basic.hasPermission('sale:order:list'))
					{
						operator += '<a title="查看"href="javascript:;" onclick="order_view(' + row.id + ')" style="padding: 0 8px; color: green"><i class="fa fa-search"></i></a>';
					}

					if (Helper.isNotEmpty(row.isCheck) && row.isCheck == 'NO')
					{
						if (Helper.basic.hasPermission('sale:order:edit'))
						{
							operator += '<a title="编辑" href="javascript:;" onclick="order_edit(' + row.id + ')" style="padding: 0 8px; color: green"><i class="fa fa-pencil"></i></a>';
						}
						if (Helper.basic.hasPermission('sale:order:del'))
						{
							operator += '<a title="删除" href="javascript:;" onclick="order_del(this,' + row.id + ')" style="padding: 0 8px; color: red"><i class="fa fa-trash-o"></i></a>';
						}
						if (Helper.basic.hasPermission('sale:order:copy'))
						{
							operator += '<a title="复制" href="javascript:;" onclick="order_copy(' + row.id + ')" style="padding: 0 8px; color: red"><i class="fa fa-copy"></i></a>';
						}
					}
					operator += '</span>';
					return operator;
				}
			} ],
			onDblClickRow : function(row)
			{
				//双击选中事件
				order_view(row.id);
			},
			onClickRow : function(row, $element)
			{
				$element.addClass("tr_active").siblings().removeClass("tr_active");
			},
			onLoadSuccess : function(data)
			{
				$("#bootTable tbody").find("tr:last").find("td:first").text("合计");
				var _tds = $("#bootTable tbody").find("tr:last").find("td");
				for (var index = 0; index < _tds.length; index++)
				{
					if (_tds.eq(index).text() == "-")
					{
						_tds.eq(index).text('');
					}
				}
			},

			onColumnSwitch : function(field, checked)
			{
				$("#bootTable tbody").find("tr:last").find("td:first").text("合计");
				var _tds = $("#bootTable tbody").find("tr:last").find("td");
				for (var index = 0; index < _tds.length; index++)
				{
					if (_tds.eq(index).text() == "-")
					{
						_tds.eq(index).text('');
					}
				}
				
				// 在使用筛选增加减少列时重置表格列拖动效果
				bootstrapTable_ColDrag($("#bootTable"));
			}
		});
		$("#bootTable").on('load-success.bs.table column-switch.bs.table page-change.bs.table search.bs.table', function()
		{
			/* 表格工具栏 */
			if ($(".glyphicon-th").next().html() == '')
			{
				$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
				// 控制筛选菜单金额选择
				if (!hasPermisson)
				{
					$("div[title='列'] ul[role=menu]").find("input[value=9]").parent().parent().remove();
					$("div[title='列'] ul[role=menu]").find("input[value=10]").parent().parent().remove();
					$("div[title='列'] ul[role=menu]").find("input[value=11]").parent().parent().remove();
				}

				if (!Helper.basic.hasPermission('sale:order:export'))
				{
					$(".export.btn-group").remove();
				}
				$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
			}
		});
	});
	// 分页
	function responseHandler(res)
	{
		return {
			rows : res.result,
			total : res.count
		};
	}
	// 查询条件
	function queryParams(params)
	{
		params['dateMin'] = $("#dateMin").val();
		params['dateMax'] = $("#dateMax").val();
		params['billNo'] = $("#billNo").val();
		params['customerName'] = $("#customerName").val();
		params['customerBillNo'] = $("#customerBillNo").val();
		params['auditFlag'] = $("input[name='auditFlag']:checked").val() == "-1" ? null : $("input[name='auditFlag']:checked").val();
		return params;
	}
	// 获取客户返回信息（回调-客户名称弹出窗口）
	function getCallInfo_customer(obj)
	{
		$("#customerName").val(obj.name);
	}
	// 编辑
	function order_edit(id)
	{
		if (!Helper.isNotEmpty(id))
		{
			return;
		}
		var url = Helper.basePath + '/sale/order/edit/' + id;
		var title = "销售订单";
		admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
	}
	// 查看
	function order_view(id)
	{
		if (!Helper.isNotEmpty(id))
		{
			return;
		}
		var url = Helper.basePath + '/sale/order/view/' + id;
		var title = "销售订单";
		admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
	}
	// 复制
	function order_copy(id)
	{
		var url = Helper.basePath + '/sale/order/copy/' + id;
		var title = "销售订单";
		admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
	}
	// 删除
	function order_del(obj, id)
	{
		Helper.message.confirm('确认要删除吗？', function(index)
		{
			$.ajax({
				cache : true,
				type : "POST",
				url : Helper.basePath + '/sale/order/del/' + id,
				async : false,
				dataType : "json",
				error : function(request)
				{
					layer.alert("Connection error");
				},
				success : function(data)
				{
					if (data.success)
					{
						$("#bootTable").bootstrapTable("removeByUniqueId", id);
						$("#bootTable").bootstrapTable("refresh");
						Helper.message.suc('已删除!');
						// window.location.href=Helper.basePath + '/sale/order/list';
					} else
					{
						Helper.message.warn("操作失败");
					}
				}
			});
		});
	}
	// 新增
	function create()
	{
		var url = Helper.basePath + '/sale/order/create';
		var title = "销售订单";
		admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
	}