var rowrow;
$(function()
{
	// 查询，刷新table
	$("#btn_search").click(function()
	{
		$("#bootTable").bootstrapTable("refreshOptions", {
			pageNumber : 1
		});
	});

	/* 更多 */
	$("#btn_more").click(function()
	{
		$("#more_div").toggle();
		$("#more_div2").toggle();
	});

	/**加载数据*/
	// 订单详情table
	$("#bootTable").bootstrapTable({
		url : Helper.basePath + "/produce/work/ajaxDetailList",

		method : "post",
		contentType : 'application/json', // 设置请求头信息
		dataType : "json",
		pagination : true, // 是否显示分页（*）
		sidePagination : 'server',// 设置为服务器端分页
		pageList : Helper.bootPageList,
		queryParamsType : "",
		pageSize : 20,
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
		detailView : true, // 是否显示父子表
		detailFormatter : detailFormatter,
		showExport : true,// 是否显示导出按钮
		// exportDataType: 'all',
		exportTypes : [ 'csv', 'txt', 'excel', 'doc' ],
		cookie : true,// 是否启用COOKIE
		cookiesEnabled : [ 'bs.table.columns' ],// 默认会记住很多属性，这里控制只记住列选择属性
		cookieIdTable : "print_work_schedule_list",// 必须制定唯一的表格cookieID
		uniqueId : "id",// 定义列表唯一键
		columns : [ {
			field : 'id',
			title : '序号',
			width : 40,
			formatter : function(value, row, index)
			{
				return index + 1;
			}
		}, {
			field : 'operate',
			title : '生产进度',
			width : 100,
			formatter : function(value, row, index)
			{

				if (row.produceQty * row.inStockQty >= 0)
				{
					var op_schedule = parseInt(row.inStockQty / row.produceQty * 100)
					if (Number(op_schedule) >= 100)
					{
						op_schedule = 100;
					}
					var tmpleOp_Schedule = '<div style="position: relative;width:100%;height:100%;"><span style="position: absolute;left: 0; width: 100%;">' + op_schedule + '%</span><div style="background-color:#14A67E;height:100%;width:' + op_schedule + '%;"> </div></div>'
					return tmpleOp_Schedule
				} else
				{
					return '<div style="position: relative;height:100%"><span style="position: absolute;left: 0; width: 100%;">0%</span><div style="width:100%;height:100%;text-align:center;"> </div></div>'
				}
			}
		}, {
			field : 'billTypeText',
			title : '工单类型',
			width : 100,
			formatter : function(value, row, index)
			{
				return row.master.billTypeText;
			}
		}, {
			field : 'createTime',
			title : '制单日期',
			width : 90,
			formatter : function(value, row, index)
			{
				return new Date(row.master.createTime).format("yyyy-MM-dd");
			}
		}, {
			field : 'billNo',
			title : '生产单号',
			width : 100,
			formatter : function(value, row, index)
			{
				return billNoTransToUrl(row.master.billNo);
			}
		}, {
			field : 'deliveryTime',
			title : '交货日期',
			width : 90,
			formatter : function(value, row, index)
			{
				return new Date(value).format("yyyy-MM-dd");
			}
		}, {
			field : 'sourceBillNo',
			title : '销售单号',
			width : 120,
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
			width : 120
		}, {
			field : 'customerMaterialCode',
			title : '客户料号',
			width : 90
		}, {
			field : 'productName',
			title : '成品名称',
			width : 120
		}, {
			field : 'productCode',
			title : '产品编号',
			width : 120
		}, {
			field : 'style',
			title : '产品规格',
			width : 90
		}, {
			field : 'sourceQty',
			title : '订单数量',
			width : 90
		}, {
			field : 'spareProduceQty',
			title : '备品数量',
			width : 90
		}, {
			field : 'produceQty',
			title : '生产数量',
			width : 90
		}, {
			field : 'inStockQty',
			title : '入库数量',
			width : 90
		}, {
			field : 'state',
			title : '状态',
			width : 90,
			formatter : function(value, row, index)
			{

				if (row.inStockQty >= row.produceQty && row.inStockQty != 0)
				{
					return '已完工';
				} else if (row.inStockQty + row.completeQty >= row.produceQty)
				{
					return '已完工';
				} else if (row.isForceComplete == "YES")
				{
					return '已完工';
				} else
				{
					return '未完工';
				}
			},
			cellStyle : function(value, row, index, field)
			{
				if (value == '未完工')
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
		}

		],
		onDblClickRow : function(row)
		{

			work_view(row.masterId);
		},
		// 注册加载子表的事件。注意下这里的三个参数！
		onExpandRow : function(index, row, $detail)
		{
			$detail.html('<div style="width:751px;float:left;"><table class="tb1"></table></div><div style="width:751px;float:left;"><table class="tb2"></table></div>');
			InitSubTable(index, row, $detail);
			InitSubMaterialTable(index, row, $detail);
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

	/* 查看 */
	function work_view(id)
	{
		var url = Helper.basePath + '/produce/work/view/' + id;
		var title = "生产工单";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"))

	}

	// 初始化子表格(无线循环)
	InitSubTable = function(index, row, $detail)
	{
		var parentid = row.master.billNo;
		$detail.css('pointer-events', 'none');

		var cur_table = $detail.find('table.tb1');
		$(cur_table).bootstrapTable({
			url : Helper.basePath + "/produce/work/ajaxDetailItemPc?",
			method : "get",
			contentType : 'application/json', // 设置请求头信息
			dataType : "json",
			pagination : false, // 是否显示分页（*）
			sidePagination : 'server',// 设置为服务器端分页
			queryParams : {
				billNo : parentid
			},
			pageList : Helper.bootPageList,
			// queryParamsType: "",
			// queryParamsType: "limit",
			pageSize : 20,
			pageNumber : 1,
			// queryParams: queryParams,//参数
			responseHandler : responseHandlerSecent,
			minimumCountColumns : 2, // 最少允许的列数
			striped : true, // 是否显示行间隔色
			cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			sortable : false, // 是否启用排序
			clickToSelect : true, // 是否启用点击选中行
			height : 'inherit', // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			cardView : false, // 是否显示详细视图
			detailView : true, // 是否显示父子表
			uniqueId : "id",// 定义列表唯一键,
			columns : [ {
				field : 'productName',
				title : '部件名称',
				formatter : function(value, row, index)
				{
					return row.partName
				}
			}, {
				field : 'rate',
				title : '完成进度',
				formatter : function(value, row, index)
				{
					if (row.rate.split('%')[0] > 0)
					{
						var rateSchedule = (parseInt(row.rate.split('%')[0] * 100))
						if (Number(rateSchedule) >= 100)
						{
							rateSchedule = 100;
						}
						var tmpleSchedule = '<div style="position: relative;height:100%;width:100%;"><span style="position: absolute;left: 0; width: 100%;">' + rateSchedule + '%</span><div style="background-color:#14A67E;height:100%;width:' + rateSchedule + '%;"> </div></div>'
						return tmpleSchedule
					} else
					{
						return '<div style="position: relative;height:100%;width:100%;"><span style="position: absolute;left: 0; width: 100%;">0%</span><div style="width:100%;height:100%;text-align:center;"> </div></div>'
					}
				}
			}, {
				field : 'flowOfCraft',
				title : '工艺流程',
				formatter : function(value, row, index)
				{
					var courseTampl = '';
					var ifOutSource = 'none'
					for (var i = 0; i < row.child.length; i++)
					{
						var redcolor = 'black';
						if (row.child[i].reportQty != 0)
						{
							redcolor = 'red';
						}
						// 判断是否发外
						if (row.child[i].isOutSource)
						{
							ifOutSource = 'line'
						} else
						{
							ifOutSource = 'none'
						}
						if (i < row.child.length - 1)
						{
							courseTampl += '<span style="color:' + redcolor + '">' + row.child[i].procedureName + '<span style="display:' + ifOutSource + '">(发外)</span>→</span>'
						} else
						{

							courseTampl += '<span style="color:' + redcolor + '">' + row.child[i].procedureName + '<span style="display:' + ifOutSource + '">(发外)</span></span>'
						}
					}
					return courseTampl;

				}
			} ],
			// 注册加载子表的事件。注意下这里的三个参数！
			onExpandRow : function(index, row, $detail)
			{

				InitSubTable1(index, row, $detail);
			},

		});
	};

	InitSubMaterialTable = function(index, row, $detail)
	{
		var parentid = row.master.id;
		$detail.css('pointer-events', 'none');

		var cur_table2 = $detail.find('table.tb2');
		$(cur_table2).bootstrapTable({
			url : Helper.basePath + "/produce/work/ajaxDetailMaterialItemPc?",
			method : "get",
			contentType : 'application/json', // 设置请求头信息
			dataType : "json",
			pagination : false, // 是否显示分页（*）
			sidePagination : 'server',// 设置为服务器端分页
			queryParams : {
				workId : parentid
			},
			pageList : Helper.bootPageList,
			// queryParamsType: "",
			// queryParamsType: "limit",
			pageSize : 20,
			pageNumber : 1,
			// queryParams: queryParams,//参数
			responseHandler : responseHandlerSecent,
			minimumCountColumns : 2, // 最少允许的列数
			striped : true, // 是否显示行间隔色
			cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			sortable : false, // 是否启用排序
			clickToSelect : true, // 是否启用点击选中行
			height : 'inherit', // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			cardView : false, // 是否显示详细视图
			detailView : false, // 是否显示父子表
			uniqueId : "id",// 定义列表唯一键,
			columns : [ {
				field : 'partName',
				title : '部件名称',
				width : 50,
				formatter : function(value, row, index)
				{
					return $.trim(value) == "" ? "成品工序" : value;
				}
			}, {
				field : 'materialName',
				title : '材料名称',
				width : 50
			}, {
				field : 'style',
				title : '材料规格',
				width : 50
			}, {
				field : 'weight',
				title : '材料克重',
				width : 50
			}, {
				field : 'qty',
				title : '材料用量',
				width : 50
			}, {
				field : 'purchQty',
				title : '采购数量',
				width : 50
			}, {
				field : 'stockQty',
				title : '入库数量',
				width : 50
			}, {
				field : 'takeQty',
				title : '领料数量',
				width : 50
			} ]
		});
	};

	/**返回数据chu'li*/
	function responseHandler(res)
	{

		return {
			rows : res.result,
			total : res.count
		};
	}

	function responseHandlerSecent(res)
	{
		return {
			rows : res.result,
			total : res.count
		};
	}
	// 初始化子表格(无线循环)三表
	InitSubTable1 = function(index, row, $detail)
	{
		$detail.css('pointer-events', 'auto');
		$detail.attr('title', ' ');
		var cur_table = $detail.html('<table></table>').find('table');
		$(cur_table).bootstrapTable({
			data : row.child,
			method : "get",
			// contentType: 'application/json', //设置请求头信息
			// dataType: "json",
			pagination : false, // 是否显示分页（*）
			// sidePagination: 'server',//设置为服务器端分页
			pageList : Helper.bootPageList,
			queryParamsType : "",
			queryParamsType : "limit",
			pageSize : 20,
			pageNumber : 1,
			// queryParams: queryParams,//参数
			// responseHandler: responseHandlerThree,
			minimumCountColumns : 2, // 最少允许的列数
			striped : true, // 是否显示行间隔色
			cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			sortable : false, // 是否启用排序
			clickToSelect : true, // 是否启用点击选中行
			height : 'inherit', // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			cardView : false, // 是否显示详细视图
			detailView : false, // 是否显示父子表
			uniqueId : "id",// 定义列表唯一键,
			columns : [ {
				field : 'procedureName',
				title : '工序名称',
				formatter : function(value, row, index)
				{
					// 判断是否发外

					if (row.isOutSource)
					{
						return row.procedureName + '(发外)';
					} else
					{
						return row.procedureName
					}

				}
			}, {
				field : 'yieldQty',
				title : '应产数',
				formatter : function(value, row, index)
				{

					return row.yieldQty
				}
			}, {
				field : 'reportQty',
				title : '上报数',
				formatter : function(value, row, index)
				{

					return row.reportQty
				}
			}, {
				field : 'qualifiedQty',
				title : '合格数',
				formatter : function(value, row, index)
				{

					return row.qualifiedQty
				}
			}, {
				field : 'unqualified',
				title : '不合格数',
				formatter : function(value, row, index)
				{

					return row.unqualified
				}
			} ],

		// //无线循环取子表，直到子表里面没有记录
		// onExpandRow: function (index, row, $Subdetail) {
		//	 		            
		// InitSubTable(index, row, $Subdetail);
		// }
		});
	};
	// function responseHandlerThree(res,row){

	// return {rows:res.child}
	// }

	/**查询内容*/
	function queryParams(params)
	{
		params['dateMin'] = $("#dateMin").val();
		params['dateMax'] = $("#dateMax").val();
		params['billNo'] = $("#billNo").val();
		params['customerName'] = $("#customerName").val();
		params['productName'] = $("#productName").val();
		params['deliverDateMin'] = $("#deliverDateMin").val();
		params['deliverDateMax'] = $("#deliverDateMax").val();
		params['customerMaterialCode'] = $("#customerMaterialCode").val();
		params['saleBillNo'] = $("#saleBillNo").val();
		params['auditFlag'] = "YES";

		params['productStyle'] = $("#style").val();
		params['customerBillNo'] = $("#customerBillNo").val();
		return params;
	}

	/** table 工具栏 类似行为应该抽取为公共js*/
	$("#bootTable").on('load-success.bs.table column-switch.bs.table page-change.bs.table search.bs.table', function()
	{
		{
			/* 表格工具栏 */
			if ($(".glyphicon-th").next().html() == '')
			{
				$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
				if (!Helper.basic.hasPermission('produce:schedule:export'))
				{
					$(".export.btn-group").remove();
				}
				$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
			}
		}
		;
	});

});

function detailFormatter(index, row)
{
	var html = [];
	$.each(row, function(key, value)
	{
		html.push('<p><b>' + key + ':</b> ' + value + '</p>');
	});
	return html.join('');
}