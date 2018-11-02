	$(function()
	{
		// 当前详情id列表数据（）
		var currentDataIdList = [];
		// 当前发外退货id列表
		var currentReturnIdList = [];
		// 当前发外对账id列表
		var currentReconcilIdList = [];
		/* 判断当前用户是否有权限查看金额 */
		var hasPermission = Helper.basic.hasPermission('outsource:arrive:money');
		var orderDetailList =$.parseJSON($("#orderDetailList").val());
		// 解析【生成发外退货】【生成发外对账】
		for (var i = 0; i < orderDetailList.length; i++)
		{
			var obj = orderDetailList[i];
			currentDataIdList.push(obj.id);
			// 发外退货 >【入库数量 大于 已退货数量】 并且 【入库数量 大于 已对账数量】
			// console.log(obj)
			// console.log(obj.qty + "," + obj.refundQty + "," + obj.reconcilQty)
			if (obj.qty > obj.returnQty && obj.qty > obj.reconcilQty)
			{
				currentReturnIdList.push(obj.id);
			}
			// 发外对账 >【入库数量 大于 已对账数量】
			if (obj.qty > obj.reconcilQty)
			{
				currentReconcilIdList.push(obj.id);
			}
		}
		// 隐藏【生成发外退货】
		if (currentReturnIdList.length <= 0)
		{
			$("#btn_transmit_return").hide();
		}
		// 隐藏【生成发外对账】
		if (currentReconcilIdList.length <= 0)
		{
			$("#btn_transmit_reconcil").hide();
		}
		// 隐藏所有【生成】
		if (currentReturnIdList.length <= 0 && currentReconcilIdList.length <= 0)
		{
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
			cookieIdTable : "print_outsource_arrive_master_view",// 必须制定唯一的表格cookieID
			columns : [ {
				field : 'index',
				title : '序号',
				width : 30,
				formatter : function(value, row, index)
				{
					return (index + 1);
				}
			}, {
				field : 'warehouseId',
				title : '仓库',
				width : 100,
				formatter : function(value, row, index)
				{
					if (Helper.isNotEmpty(value))
					{
						return Helper.isNotEmpty(Helper.basic.info('WAREHOUSE', value)) ? Helper.basic.info('WAREHOUSE', value).name : '-';
					} else
					{
						return value;
					}
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
				title : '到货数量',
				width : 100
			}, {
				field : 'price',
				title : '单价',
				width : 100,
				visible : hasPermission,
				formatter : function(value, row, index)
				{
					return value + '<input class="tab_input" type="hidden" name="detailList.price" value="' + (value || '') + '"/>'
				}
			}, {
				field : 'money',
				title : '金额',
				width : 100,
				visible : hasPermission,
				formatter : function(value, row, index)
				{
					return value + '<input class="tab_input" type="hidden" name="detailList.money" value="' + (value || '') + '"/>';
				}
			}, {
				field : 'tax',
				title : '税额',
				width : 100,
				visible : hasPermission,
				formatter : function(value, row, index)
				{
					return value + '<input type="hidden" name="detailList.tax" readonly="readonly" value="' + (value || '') + '"/>';
				}
			}, {
				field : 'taxRateName',
				title : '税收',
				width : 100,
				formatter : function(value, row, index)
				{
					return value;
				}
			}, {
				field : 'taxRatePercent',
				title : '税率%',
				width : 100,
				formatter : function(value, row, index)
				{
					return value + '<input type="hidden" name="detailList.taxRatePercent" readonly="readonly" value="' + (value || '') + '%"/>';
				},
				visible : false
			}, {
				field : 'noTaxPrice',
				title : '不含税单价',
				width : 100,
				formatter : function(value, row, index)
				{
					return value + '<input class="tab_input" type="hidden" name="detailList.noTaxPrice" readonly="readonly" value="' + (value || '') + '"/>'
				},
				visible : false
			}, {
				field : 'noTaxMoney',
				title : '不含税金额',
				width : 100,
				formatter : function(value, row, index)
				{
					return value + '<input class="tab_input" type="hidden" name="detailList.noTaxMoney" readonly="readonly" value="' + (value || '') + '"/>';
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
					return billNoTransToUrl(value);
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
			$("div[title='列'] ul[role=menu]").find("input[value=4]").parent().parent().remove();
			$("div[title='列'] ul[role=menu]").find("input[value=5]").parent().parent().remove();
			$("div[title='列'] ul[role=menu]").find("input[value=6]").parent().parent().remove();
			$("div[title='列'] ul[role=menu]").find("input[value=9]").parent().parent().remove();
			$("div[title='列'] ul[role=menu]").find("input[value=10]").parent().parent().remove();
		}
		;
		/* 返回显示列表 */
		$("#btn_back").on("click", function()
		{
			var url = Helper.basePath + '/outsource/arrive/list';
			var title = "发外到货列表";
			admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
		});
		/* 编辑 */
		$("#btn_edit").on("click", function()
		{
			location.href = Helper.basePath + '/outsource/arrive/edit/' + $("#order_id").val();
		});
		// 审核
		$("#btn_audit").on("click", function()
		{
			var order_id = $("#order_id").val();
			Helper.post(Helper.basePath + '/outsource/arrive/check/' + order_id, function(data)
			{
				if (data.success)
				{
					Helper.message.suc(data.message);
					location.href = Helper.basePath + "/outsource/arrive/view/" + order_id;
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
			Helper.post(Helper.basePath + '/outsource/arrive/checkBack/' + order_id, function(data)
			{
				if (data.success)
				{
					if (data.obj == null)
					{
						Helper.message.suc(data.message);
						location.href = Helper.basePath + "/outsource/arrive/view/" + order_id;
					} else
					{
						var str = '反审失败,以下产品库存数量不足\n';
						for (var i = 0; i < data.obj.length; i++)
						{
							str = str + '名称：' + data.obj[i].product.name + '   目前库存数量' + data.obj[i].qty + '\n';
						}
						Helper.message.confirm(str, function(index)
						{
							Helper.message.confirm("确认操作会引起负库存，是否允许负库存?", function(index)
							{
								forceCheckBack(order_id)
							});
						});
					}
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
								if (i == 'OR')
								{
									msg += '&emsp;&emsp;发外退货单<a href="javascript:;" onclick="return_view(' + j.id + ')">' + j.billNo + '</a><br/>';
								}
								if (i == 'OC')
								{
									msg += '&emsp;&emsp;发外对账单<a href="javascript:;" onclick="reconcil_view(' + j.id + ')">' + j.billNo + '</a><br/>';
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
			Helper.post(Helper.basePath + '/outsource/arrive/complete', {
				"tableType" : "MASTER",
				"ids" : [ order_id ]
			}, function(data)
			{
				if (data.success)
				{
					Helper.message.suc(data.message);
					location.href = Helper.basePath + "/outsource/arrive/view/" + order_id;
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
			Helper.post(Helper.basePath + '/outsource/arrive/complete_cancel', {
				"tableType" : "MASTER",
				"ids" : [ order_id ]
			}, function(data)
			{
				if (data.success)
				{
					Helper.message.suc(data.message);
					location.href = Helper.basePath + "/outsource/arrive/view/" + order_id;
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
				Helper.post(Helper.basePath + '/outsource/arrive/del/' + $("#order_id").val(), function(data)
				{
					if (data.success)
					{
						Helper.message.suc(data.message);
						closeTabAndJump("发外到货列表");
					} else
					{
						Helper.message.warn(data.message);
					}
				});
			});
		});
		// 生成发外退货
		$("#btn_transmit_return").click(function()
		{
			if (currentReturnIdList.length > 0)
			{
				var _paramStr = "";
				$(currentReturnIdList).each(function(index, item)
				{
					if (index == 0)
					{
						_paramStr = "arriveIds=" + item;
					} else
					{
						_paramStr = _paramStr + "&arriveIds=" + item;
					}
				});
				_paramStr += "&supplierId=" + $("#supplierId").val();
				var url = Helper.basePath + '/outsource/return/create?' + _paramStr;
				var title = "生成发外退货";
				admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
			}
		});
		// 生成发外对账
		$("#btn_transmit_reconcil").click(function()
		{
			if (currentReconcilIdList.length > 0)
			{
				var _paramStr = "";
				$(currentReconcilIdList).each(function(index, item)
				{
					if (index == 0)
					{
						_paramStr = "arriveIds=" + item;
					} else
					{
						_paramStr = _paramStr + "&arriveIds=" + item;
					}
				});
				_paramStr += "&supplierId=" + $("#supplierId").val();
				var url = Helper.basePath + '/outsource/reconcil/create?' + _paramStr;
				var title = "生成发外对账";
				admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
			}
		});
		// 打印
		$("#btn_print").loadTemplate('OUTSOURCE_OA', '/outsource/arrive/printAjax/'+$("#arriveId").val());
	
		// 表格列拖动效果
		bootstrapTable_ColDrag($("#detailList"));
	});

	/* 查看 */
	function reconcil_view(id)
	{
		if (!Helper.isNotEmpty(id))
		{
			return;
		}
		var url = Helper.basePath + '/outsource/reconcil/view/' + id;
		var title = "发外对账单";
		admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
	}
	/* 查看 */
	function return_view(id)
	{
		if (!Helper.isNotEmpty(id))
		{
			return;
		}
		var url = Helper.basePath + '/outsource/return/view/' + id;
		var title = "发外退货单";
		admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
	}
	
	/**
	 * 强制反审核
	 */
	function forceCheckBack(id)
	{
		$.ajax({
			cache : true,
			type : "POST",
			dataType : "json",
			url : Helper.basePath + '/outsource/arrive/forceCheckBack/' + id,
			async : false,
			error : function(request)
			{
				layer.alert("Connection error");
			},
			success : function(data)
			{
				if (data.success)
				{
					location.reload();
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
								if (i == 'OR')
								{
									msg += '&emsp;&emsp;发外退货单<a href="javascript:;" onclick="return_view(' + j.id + ')">' + j.billNo + '</a><br/>';
								}
								if (i == 'OC')
								{
									msg += '&emsp;&emsp;发外对账单<a href="javascript:;" onclick="reconcil_view(' + j.id + ')">' + j.billNo + '</a><br/>';
								}

							});
						});
						Helper.message.view(msg);
					} else
					{
						Helper.message.warn(data.message);
					}
				}
			}
		});
	}