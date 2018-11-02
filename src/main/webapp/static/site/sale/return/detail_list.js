$(function()
	{
		/* 判断当前用户是否有权限查看金额 */
		var hasPermission = Helper.basic.hasPermission('sale:return_detail:money');
		/* 选择客户 */
		$("#customer_quick_select").click(function()
		{
			Helper.popup.show('选择客户', Helper.basePath + '/quick/customer_select?multiple=false', '900', '500');
		});
		/* 选择产品 */
		$("#product_quick_select").click(function()
		{
			Helper.popup.show('选择产品', Helper.basePath + '/quick/product_select?multiple=false', '900', '490');
		});

		/* 更多 */
		$("#btn_more").click(function()
		{
			$("#more_div").toggle();
			$("#more_div2").toggle();
		});

		/* 搜索 */
		$("#btn_search").click(function()
		{
			$("#bootTable").bootstrapTable("refreshOptions", {
				pageNumber : 1
			});
		});
		$("input[type='radio']").change(function()
		{
			$("#btn_search").click();
		})
		// 订单详情table
		$("#bootTable").bootstrapTable({
			url : Helper.basePath + "/sale/return/ajaxDetailList",
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

			// resizable: true, //是否启用列拖动
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
			cookieIdTable : "print_sale_return_detail",// 必须制定唯一的表格cookieID

			uniqueId : "id",// 定义列表唯一键
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
					if (row.id == null)
					{
						return;
					}
					if (row.master.isCheck == 'YES')
					{
						return '已审核';
					} else
					{
						return '未审核';
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
				field : 'warehouseName',
				title : '仓库',
				width : 80
			}, {
				field : 'createTime',
				title : '制单日期',
				width : 80,
				formatter : function(value, row, index)
				{
					if (row.master.createTime)
					{
						return new Date(row.master.createTime).format("yyyy-MM-dd");
					}
				}
			}, {
				field : 'sourceBillNo',
				title : '源单单号',
				width : 100,
				formatter : function(value, row, index)
				{
					if (row.id == null)return
					var url = Helper.basePath + '/sale/deliver/view/' + row.sourceId;
					var title = "销售送货单";
					return '<a class="jump-to" onclick="jumpTo(&quot;' + url + '&quot;,&quot;' + title + '&quot;)">' + value + '</a>';
				}
			}, {
				field : 'billNo',
				title : '退货单号',
				width : 100,
				formatter : function(value, row, index)
				{
					return idTransToUrl(row.master.id, row.master.billNo);
				}
			}, {
				field : 'saleOrderBillNo',
				title : '销售单号',
				width : 140,
				formatter : function(value, row, index)
				{
					if (row.id == null)
						return

					var url = "";
					var title = "";
					// 生产工单
					if (/^MO/g.test(value))
					{
						url = Helper.basePath + '/produce/work/toView/' + value;
						title = "生产工单";
					}
					// 销售订单
					else if (/^SO/g.test(value))
					{
						url = Helper.basePath + '/sale/order/view/' + value;
						title = "销售订单";
					}
					return '<a class="jump-to" onclick="jumpTo(&quot;' + url + '&quot;,&quot;' + title + '&quot;)">' + value + '</a>';
				}
			}, {
				field : 'customerName',
				title : '客户名称',
				width : 220,
				formatter : function(value, row, index)
				{
					return row.master.customerName
				}
			}, {
				field : 'customerBillNo',
				title : '客户单号',
				width : 100
			}, {
				field : 'customerMaterialCode',
				title : '客户料号',
				width : 120
			}, {
				field : 'employeeName',
				title : '销售人员',
				width : 80,
				formatter : function(value, row, index)
				{
					return row.master.employeeName;
				}
			}, {
				field : 'currencyType',
				title : '币别',
				width : 60,
				formatter : function(value, row, index)
				{
					return row.master.currencyTypeText;
				}
			}, {
				field : 'productCode',
				title : '产品编号',
				width : 100
			}, {
				field : 'productName',
				title : '成品名称',
				width : 140
			}, {
				field : 'style',
				title : '产品规格',
				width : 80
			}, {
				field : 'unitName',
				title : '单位',
				width : 60
			}, {
				field : 'qty',
				title : '数量',
				width : 80
			}, {
				field : 'spareQty',
				title : '备品数量',
				width : 80
			}, {
				field : 'reconcilQty',
				title : '已对账数量',
				width : 80
			}, {
				field : 'reconcilQty2',
				title : '未对账数量',
				width : 80,
				formatter : function(value, row, index)
				{
					return row.qty - row.reconcilQty;
				}
			}, {
				field : 'price',
				title : '单价',
				visible : hasPermission,
				width : 60
			}, {
				field : 'money',
				title : '金额',
				visible : hasPermission,
				width : 80
			}, {
				field : 'tax',
				title : '税额',
				visible : hasPermission,
				width : 80
			}, {
				field : 'noTaxMoney',
				title : '不含税金额',
				visible : hasPermission,
				width : 80
			}, {
				field : 'memo',
				title : '备注',
				'class' : 'memoView',
				width : 80
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
				
				// 在使用筛选增加或减少列时重置表格列拖动效果
				bootstrapTable_ColDrag($("#bootTable"));
			},
			onDblClickRow : function(row)
			{
				if (row.id == null)
				{
					return;
				}
				var url = Helper.basePath + '/sale/return/view/' + row.master.id;
				var title = "销售退货";
				admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
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
				// 控制筛选菜单金额选择
				if (!hasPermission)
				{
					$("div[title='列'] ul[role=menu]").find("input[value=20]").parent().parent().remove();
					$("div[title='列'] ul[role=menu]").find("input[value=21]").parent().parent().remove();
					$("div[title='列'] ul[role=menu]").find("input[value=22]").parent().parent().remove();
					$("div[title='列'] ul[role=menu]").find("input[value=23]").parent().parent().remove();
				}
				if (!Helper.basic.hasPermission('sale:return_detail:export'))
				{
					$(".export.btn-group").remove();
				}
				$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
			}
		});

	});

	// 获取返回信息
	function getCallInfo_customer(obj)
	{
		$("#customerName").val(obj.name);
	}

	// 获取返回信息
	function getCallInfo_product(obj)
	{
		$("#productName").val(obj.name);
	}

	// 请求参数
	function queryParams(params)
	{
		params['dateMin'] = $("#dateMin").val();
		params['dateMax'] = $("#dateMax").val();
		params['billNo'] = $("#billNo").val();
		params['customerName'] = $("#customerName").val();
		params['productName'] = $("#productName").val();
		params['warehouseId'] = $("#warehouseId").val();
		params['employeeId'] = $("#employeeId").val();
		params['customerClassId'] = $("#customerClassId").val();
		params['productClassId'] = $("#productClassId").val();
		params['auditFlag'] = $("input[name='auditFlag']:checked").val() == "-1" ? null : $("input[name='auditFlag']:checked").val();

		params['saleBillNo'] = $("#saleBillNo").val().trim();
		params['customerBillNo'] = $("#customerBillNo").val().trim();
		params['customerMaterialCode'] = $("#customerMaterialCode").val().trim();
		params['productStyle'] = $("#style").val().trim();
		params['sourceBillNo'] = $("#sourceBillNo").val().trim();
		return params;
	}

	// ajax结果
	function responseHandler(res)
	{
		return {
			rows : res.result,
			total : res.count
		};
	}