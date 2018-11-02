$(function()
	{
		/* 判断当前用户是否有权限查看金额 */
		var hasPermisson = Helper.basic.hasPermission('sale:reconcil:transmitMoney');
		/* 更多*/
		$("#btn_more").click(function()
		{
			$("#more_div").toggle();
		});

		/* 转对账*/
		$("#btn_transmit").click(function()
		{
			var rows = getSelectedRows();
			if (Helper.isNotEmpty(rows))
			{
				var customerId = "";
				var supplierArr = new Array();
				$(rows).each(function()
				{
					if (this.master)
					{
						customerId = this.master.customerId;
						if (!supplierArr.contains(customerId))
						{
							supplierArr.push(customerId);
						}
					}
				});
				if (supplierArr.length == 1)
				{//判断是否统一供应商
					var _paramStr = "";
					$(rows).each(function(index, item)
					{
						if (item.id)
						{
							if (index == 0)
							{
								if (item.master.billType == 'SALE_IV')
								{
									_paramStr = "deliverIds=" + item.id;
								} else if (item.master.billType == 'SALE_IR')
								{
									_paramStr = "returnIds=" + item.id;
								}
							} else
							{
								if (item.master.billType == 'SALE_IV')
								{
									_paramStr = _paramStr + "&deliverIds=" + item.id;
								} else if (item.master.billType == 'SALE_IR')
								{
									_paramStr = _paramStr + "&returnIds=" + item.id;
								}
							}
						}
					});
					var url = Helper.basePath + '/sale/reconcil/create?customerId=' + customerId + '&' + _paramStr;
					var title = "销售对账";
					admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));

				} else
				{
					Helper.message.warn("请选择同一客户");
				}
			} else
			{
				Helper.message.warn("至少选择1项");
			}
		});
		//强制完工
		$("#btn_complete").on("click", function()
		{
			var rows = getSelectedRows();
			var deliverIds = $(rows).map(function()
			{
				if (this.master && this.master.billType == 'SALE_IV')
					return this.id;
				else
					return null;
			}).get();
			var returnIds = $(rows).map(function()
			{
				if (this.master && this.master.billType == 'SALE_IR')
					return this.id;
				else
					return null;
			}).get();
			if (Helper.isNotEmpty(rows))
			{
				Helper.post(Helper.basePath + '/sale/reconcil/complete', {
					"tableType" : "DETAIL",
					"deliverIds" : deliverIds,
					"returnIds" : returnIds
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
		//取消强制完工
		$("#btn_complete_cancel").on("click", function()
		{
			var rows = getSelectedRows();
			var deliverIds = $(rows).map(function()
			{
				if (this.master && this.master.billType == 'SALE_IV')
					return this.id;
				else
					return null;
			}).get();
			var returnIds = $(rows).map(function()
			{
				if (this.master && this.master.billType == 'SALE_IR')
					return this.id;
				else
					return null;
			}).get();
			if (Helper.isNotEmpty(rows))
			{
				Helper.post(Helper.basePath + '/sale/reconcil/complete_cancel', {
					"tableType" : "DETAIL",
					"deliverIds" : deliverIds,
					"returnIds" : returnIds
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

		//订单详情table
		$("#bootTable").bootstrapTable({
			url : Helper.basePath + "/sale/transmit/to_reconcil_list",
			method : "post",
			contentType : 'application/json', //设置请求头信息  
			dataType : "json",
			pagination : false, // 是否显示分页（*）
			sidePagination : 'server',//设置为服务器端分页
			pageList : Helper.bootPageList,
			queryParamsType : "",
			pageSize : 20,
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
			exportTypes : [ 'csv', 'txt', 'excel', 'doc' ],

			cookie : true,//是否启用COOKIE
			cookiesEnabled : [ 'bs.table.columns' ],//默认会记住很多属性，这里控制只记住列选择属性
			cookieIdTable : "print_sale_transmit_reconcil",//必须制定唯一的表格cookieID

			uniqueId : "id",//定义列表唯一键
			columns : [{
				field : 'index',
				title : '序号',
				width : 40,
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return;
					}
					return index + 1;
				}
			}, {
				field : 'state',
				checkbox : true,
				visible : true,
				width : 60
			}, {
				field : 'master_createTime',
				title : '制单日期',
				width : 100,
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return;
					}
					if (Helper.isNotEmpty(row.master.createTime))
					{
						return new Date(row.master.createTime).format("yyyy-MM-dd");
					}
				}
			}, {
				field : 'master_billType',
				title : '单据类型',
				width : 120,
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return;
					}
					if (Helper.isNotEmpty(row.master.billType))
					{
						return Helper.basic.getEnumText("com.huayin.printmanager.persist.enumerate.BillType", row.master.billType, "text");
					}
				}
			}, {
				field : 'master_billNo',
				title : '源单单号',
				width : 120,
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return;
					}
					return idTransToUrl(row.masterId,row.master.billNo);
				}
			}, {
				field : 'saleOrderBillNo',
				title : '销售单号',
				width : 120,
				formatter:function(value,row,index)
				{
					return billNoTransToUrl(value)
				}
			}, {
				field : 'customerBillNo',
				title : '客户单号',
				width : 120
			}, {
				field : 'master.customerName',
				title : '客户名称',
				width : 200,
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return;
					}
					return row.master.customerName;
				}
			}, {
				field : 'customerMaterialCode',
				title : '客户料号',
				width : 120
			}, {
				field : 'productName',
				title : '成品名称',
				width : 120
			}, {
				field : 'style',
				title : '产品规格',
				width : 100
			}, {
				field : 'unitName',
				title : '单位',
				width : 100
			}, {
				field : 'qty',
				title : '送/退货数量',
				width : 120,
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return  value;
					}
					if (row.master.billType == "SALE_IR")
					{
						return "-" + value;
					} else
					{
						return value;
					}
				}
			}, {
				field : 'reconcilQty',
				title : '已对账数量',
				width : 120,
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return  value;
					}
					
					if (row.master.billType == "SALE_IR" && value != 0)
					{
						return "-" + value;
					} else
					{
						return value;
					}
				}
			}, {
				field : 'qty_reconcilQty',
				title : '未对账数量',
				width : 100,
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return  Number(row.qty).subtr(Number(row.reconcilQty));
					}
					if (row.master.billType == "SALE_IR")
					{
						return "-" + Number(row.qty).subtr(Number(row.reconcilQty));
					} else
					{
						return Number(row.qty).subtr(Number(row.reconcilQty));
					}

				}
			}, {
				field : 'price',
				title : '单价',
				visible : hasPermisson,
				width : 100
			}, {
				field : 'money',
				title : '金额',
				visible : hasPermisson,
				width : 100,
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return Number(Number(row.qty).subtr(Number(row.reconcilQty))).mul(Number(row.price));
					}
					if (row.master.billType == "SALE_IR")
					{
						if (!row.price)
						{
							return 0;
						} else
						{
							return "-" + Number(Number(row.qty).subtr(Number(row.reconcilQty))).mul(Number(row.price));
						}

					} else
					{
						return Number(Number(row.qty).subtr(Number(row.reconcilQty))).mul(Number(row.price));
					}

				}
			}, {
				field : 'memo',
				title : '备注',
				'class' : 'memoView',
				width : 200
			}, {
				field : 'imgUrl',
				title : '产品图片',
				width : 60,
				formatter : function(value, row, index)
				{
					if (row.imgUrl != "" && row.imgUrl != null)
					{
						return '<img class="pimg" src="'+row.imgUrl+'"/>';
					} else
					{
						return "";
					}
				}
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
				if (!hasPermisson)
				{
					$("div[title='列'] ul[role=menu]").find("input[value=14]").parent().parent().remove();
					$("div[title='列'] ul[role=menu]").find("input[value=15]").parent().parent().remove();
				}
				if (!Helper.basic.hasPermission('trandsmit:reconcil:export'))
				{
					$(".export.btn-group").remove();
				}
				$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
			}
		});

	});
	function queryParams(params)
	{
		params['billNo'] = $("#billNo").val().trim();
		params['productName'] = $("#productName").val().trim();
		params['customerName'] = $("#customerName").val().trim();
		params['completeFlag'] = $("input[name='completeFlag']:checked").val().trim();

		params['createTimeMin'] = $("#dateMin").val().trim();
		params['createTimeMax'] = $("#dateMax").val().trim();
		params['saleBillNo'] = $("#saleOrderBillNo").val().trim();
		params['customerBillNo'] = $("#customerBillNo").val().trim();
		params['productStyle'] = $("#style").val().trim();
		params['customerMaterialCode'] = $("#customerMaterialCode").val().trim();
		return params;
	}
	
	function responseHandler(res)
	{
		return {
			rows : res.result,
			total : res.count
		};
	}