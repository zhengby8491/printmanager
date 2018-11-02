$(function()
	{
		// 当前详情id列表数据（）
		var currentDataIdList = [];
		// 当前采购付款单id列表
		var currentPaymentIdList = [];
		/* 判断当前用户是否有权限查看金额 */
		var hasPermission = Helper.basic.hasPermission('outsource:reconcil:money');
		var orderDetailList = $.parseJSON($("#orderDetailList").val());
		// 解析【生成发外对账】
		for (var i = 0; i < orderDetailList.length; i++)
		{
			var obj = orderDetailList[i];
			currentDataIdList.push(obj.id);
			// 发外付款单 >【金额 大于 付款金额】
			if (obj.money > obj.paymentMoney)
			{
				currentPaymentIdList.push(obj.id);
			}
			// 发外付款单 【发外退货直接放入】
			if (obj.sourceBillType == 'OUTSOURCE_OR')
			{
				currentPaymentIdList.push(obj.id);
			}
		}
		// 隐藏【生成付款单】
		if (currentPaymentIdList.length <= 0)
		{
			$("#btn_transmit_payment").hide();
			$("#generateTransmit").hide();
		}
		// 订单详情table
		$("#detailList").bootstrapTable({
			data :orderDetailList,
			showColumns : true, // 是否显示所有的列
			minimumCountColumns : 2, // 最少允许的列数
			striped : true, // 是否显示行间隔色
			cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination : false, // 是否显示分页（*）
			sortable : false, // 是否启用排序
			clickToSelect : true, // 是否启用点击选中行
			cardView : false, // 是否显示详细视图
			detailView : false, // 是否显示父子表
			// resizable : true, //是否启用列拖动
			cookie : true,// 是否启用COOKIE
			cookiesEnabled : [ 'bs.table.columns' ],// 默认会记住很多属性，这里控制只记住列选择属性
			cookieIdTable : "print_outsource_reconcil_master_view",// 必须制定唯一的表格cookieID
			columns : [ {
				field : 'index',
				title : '序号',
				width : 30,
				formatter : function(value, row, index)
				{
					return (index + 1);
				}
			}, {
				field : 'productNames',
				title : '成品名称',
				width : 100,
				formatter : function (value, row, index)
				{
					if (row.type == 'PRODUCT')
					{
						return row.productName;
					} else if (row.type == 'PROCESS')
					{
						return row.productNames;
					}
				}
			}, {
				field : 'procedureName',
				title : '工序名称',
				width : 100
			}, {
				field : 'qty',
				title : '对账数量',
				width : 100
			}, {
				field : 'price',
				title : '单价',
				width : 100,
				visible : hasPermission,
				formatter : function(value, row, index)
				{
					return value + '<input class="tab_input" type="hidden" name="detailList.price" value="' + (value || "") + '"/>'
				}
			}, {
				field : 'money',
				title : '金额',
				width : 100,
				visible : hasPermission,
				formatter : function(value, row, index)
				{
					return value + '<input class="tab_input" type="hidden" name="detailList.money" value="' + (value || "") + '"/>';
				}
			}, {
				field : 'tax',
				title : '税额',
				width : 100,
				visible : hasPermission,
				formatter : function(value, row, index)
				{
					return value + '<input type="hidden" name="detailList.tax" readonly="readonly" value="' + (value || "") + '"/>';
				}
			}, {
				field : 'taxRateName',
				title : '税收',
				width : 100
			}, {
				field : 'taxRatePercent',
				title : '税率值%',
				width : 100,
				formatter : function(value, row, index)
				{
					return value + '<input type="hidden" name="detailList.taxRatePercent" readonly="readonly" value="' + (value || "") + '%"/>';
				},
				visible : false
			}, {
				field : 'noTaxPrice',
				title : '不含税单价',
				width : 100,
				formatter : function(value, row, index)
				{
					return value + '<input class="tab_input" type="hidden" name="detailList.noTaxPrice" readonly="readonly" value="' + (value || "") + '"/>'
				},
				visible : false
			}, {
				field : 'noTaxMoney',
				title : '不含税金额',
				width : 100,
				formatter : function(value, row, index)
				{
					return value + '<input class="tab_input" type="hidden" name="detailList.noTaxMoney" readonly="readonly" value="' + (value || "") + '"/>';
				},
				visible : false
			}, {
				field : 'sourceBillType',
				title : '源单类型',
				width : 100,
				formatter : function(value, row, index)
				{
					var show = Helper.basic.getEnumText('com.huayin.printmanager.persist.enumerate.BillType', value, 'text');
					return show;
				}
			}, {
				field : 'type',
				title : '发外类型',
				width : 100,
				formatter : function(value, row, index)
				{
					var show = Helper.basic.getEnumText('com.huayin.printmanager.persist.enumerate.OutSourceType', value, 'text');
					return show;
				}
			}, {
				field : 'sourceBillNo',
				title : '源单单号',
				width : 100,
				formatter : function(value, row, index)
				{
					return idTransToUrl(row.sourceId, value);
				}
			}, {
				field : 'sourceQty',
				title : '源单数量',
				width : 100
			}, {
				field : 'workBillNo',
				title : '生产单号',
				width : 100,
				formatter : function(value, row, index)
				{
					return billNoTransToUrl(value);
				}
			}, {
				field : 'produceNum',
				title : '生产数量',
				width : 100
			}, {
				field : 'style',
				title : '加工规格',
				width : 100
			}, {
				field : 'partName',
				title : '部件名称',
				width : 100
			}, {
				field : 'processRequire',
				title : '加工要求',
				width : 100
			}, {
				field : 'outSourceBillNo',
				title : '加工单号',
				width : 100,
				formatter : function(value, row, index)
				{
					return billNoTransToUrl(value);
				}
			}, {
				field : 'deliveryTime',
				title : '到/退货日期',
				width : 100,
				formatter : function (value, row, index)
				{
					return new Date(value).format("yyyy-MM-dd");
				}
			}, {
				field : 'memo',
				title : '备注',
				'class' : 'memoView',
				width : 120
			} ],
			onLoadSuccess : function()
			{
				// alert("数据加载完成");
			},
			onLoadError : function()
			{
				// alert("数据加载异常");
			},
			onColumnSwitch : function(field, checked)
			{	
				// 在使用筛选增加或减少列时重置表格列拖动效果
				bootstrapTable_ColDrag($("#detailList"));
			}
		});
		/* 表格工具栏 */
		$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
		// 控制筛选菜单金额选择
		if (!hasPermission)
		{
			$("div[title='列'] ul[role=menu]").find("input[value=3]").parent().parent().remove();
			$("div[title='列'] ul[role=menu]").find("input[value=4]").parent().parent().remove();
			$("div[title='列'] ul[role=menu]").find("input[value=5]").parent().parent().remove();
			$("div[title='列'] ul[role=menu]").find("input[value=6]").parent().parent().remove();
			$("div[title='列'] ul[role=menu]").find("input[value=8]").parent().parent().remove();
			$("div[title='列'] ul[role=menu]").find("input[value=9]").parent().parent().remove();
		}
		;
		/* 返回显示列表 */
		$("#btn_back").on("click", function()
		{
			var url = Helper.basePath + '/outsource/reconcil/list';
			var title = "发外对账列表";
			admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
		});
		/* 编辑 */
		$("#btn_edit").on("click", function()
		{
			location.href = Helper.basePath + '/outsource/reconcil/edit/' + $("#order_id").val();
		});
		// 审核
		$("#btn_audit").on("click", function()
		{
			var order_id = $("#order_id").val();
			Helper.post(Helper.basePath + '/outsource/reconcil/audit/' + order_id, function(data)
			{
				if (data.success)
				{
					Helper.message.suc(data.message);
					location.href = Helper.basePath + "/outsource/reconcil/view/" + order_id;
				} else
				{
					Helper.message.warn(data.message);
				}
			});
		});
		// 反审核
		$("#btn_audit_cancel").on("click", function()
		{
			var order_id = $("#order_id").val();
			Helper.post(Helper.basePath + '/outsource/reconcil/audit_cancel/' + order_id, function(data)
			{
				if (data.success)
				{
					Helper.message.suc(data.message);
					location.href = Helper.basePath + "/outsource/reconcil/view/" + order_id;
				} else
				{
					if (data.obj)
					{
						var msg = "";
						var index = 1;
						$.each(data.obj, function(i, n)
						{
							msg += (index++) + ".反审核失败,已被下游单据引用：<br/>"
							$.each(n, function(k, j)
							{
								if (i == 'RV')
								{
									msg += '&emsp;&emsp;付款单<a href="javascript:;" onclick="payment_view(' + j.id + ')">' + j.billNo + '</a><br/>';
								}
								if (i == 'WRV')
								{
									msg += '&emsp;&emsp;付款核销单<a href="javascript:;" onclick="writeoffPayment_view(' + j.id + ')">' + j.billNo + '</a><br/>';
								}

							});
						});
						Helper.message.view(msg);
					} else
					{
						Helper.message.warn(data.message);
					}
				}
			});
		});

		// 强制完工
		$("#btn_complete").on("click", function()
		{
			var order_id = $("#order_id").val();
			Helper.post(Helper.basePath + '/outsource/reconcil/complete', {
				"tableType" : "MASTER",
				"ids" : [ order_id ]
			}, function(data)
			{
				if (data.success)
				{
					Helper.message.suc(data.message);
					location.href = Helper.basePath + "/outsource/reconcil/view/" + order_id;
				} else
				{
					Helper.message.warn(data.message);
				}
			});
		});
		// 取消强制完工
		$("#btn_complete_cancel").on("click", function()
		{
			var order_id = $("#order_id").val();
			Helper.post(Helper.basePath + '/outsource/reconcil/complete_cancel', {
				"tableType" : "MASTER",
				"ids" : [ order_id ]
			}, function(data)
			{
				if (data.success)
				{
					Helper.message.suc(data.message);
					location.href = Helper.basePath + "/outsource/reconcil/view/" + order_id;
				} else
				{
					Helper.message.warn(data.message);
				}
			});
		});

		/* 删除 */
		$("#btn_del").on("click", function()
		{
			Helper.message.confirm('确认要删除吗？', function(index)
			{
				Helper.post(Helper.basePath + '/outsource/reconcil/del/' + $("#order_id").val(), function(data)
				{
					if (data.success)
					{
						Helper.message.suc(data.message);
						closeTabAndJump("发外对账列表");
					} else
					{
						Helper.message.warn(data.message);
					}
				});
			});
		});
		// 生成付款单
		$("#btn_transmit_payment").click(function()
		{
			if (currentPaymentIdList.length > 0)
			{
				var _paramStr = "";
				$(currentPaymentIdList).each(function(index, item)
				{
					if (index == 0)
					{
						_paramStr = "ids=" + item;
					} else
					{
						_paramStr = _paramStr + "&ids=" + item;
					}
				});
				var url = Helper.basePath + '/finance/payment/create?1=1&billType=OUTSOURCE_OC&' + _paramStr;
				var title = "生成付款单";
				admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
			}
		});
		// 打印
		$("#btn_print").loadTemplate('OUTSOURCE_OC', '/outsource/reconcil/printAjax/'+$("#reconcilId").val());

		// 表格列拖动效果
		bootstrapTable_ColDrag($("#detailList"));
	});

	/* 查看 */
	function payment_view(id)
	{
		var url = Helper.basePath + '/finance/payment/view/' + id;
		var title = "付款单";
		admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"))
	}
	/* 查看 */
	function writeoffPayment_view(id)
	{
		var url = Helper.basePath + '/finance/writeoffPayment/view/' + id;
		var title = "付款核销单";
		admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"))
	}