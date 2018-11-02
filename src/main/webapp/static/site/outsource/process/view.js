$(function()
	{
		// 当前详情id列表数据（）
		var currentDataIdList = [];
		// 当前发外到货id列表
		var currentProcessIdList = [];
		/* 判断当前用户是否有权限查看金额 */
		var hasPermission = Helper.basic.hasPermission('outsource:process:money');
		var isProduct = $("#isProduct").val();
		var orderDetailList = $.parseJSON($("#orderDetailList").val());
		// 解析【生成发外到货】
		for (var i = 0; i < orderDetailList.length; i++)
		{
			var obj = orderDetailList[i];
			currentDataIdList.push(obj.id);
			// 加工数 大于 到货数
			if (obj.qty > obj.arriveQty)
			{
				currentProcessIdList.push(obj.id);
			}
		}
		// 隐藏【生成发外到货】
		if (currentProcessIdList.length <= 0)
		{
			$("#btn_transmit_process").hide();
			$("#generateTransmit").hide();
		}
		// 订单详情table
		$("#detailList").bootstrapTable({
			data : orderDetailList,
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
			cookieIdTable : "print_outsource_process_master_view",// 必须制定唯一的表格cookieID
			columns : [ {
				field : 'state',
				title : '单选',
				radio : true,
				visible : true,
				width : 60
			}, {
				field : 'index',
				title : '序号',
				width : 30,
				formatter : function(value, row, index)
				{
					return (index + 1);
				}
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
				field : 'productName',
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
				field : 'style',
				title : '加工规格',
				width : 100
			}, {
				field : 'partName',
				title : '部件名称',
				width : 100
			}, {
				field : 'qty',
				title : '加工数量',
				width : 100
			}, {
				field : 'arriveQty',
				title : '到货数量',
				width : 100
			}, {
				field : 'price',
				title : '单价',
				visible : hasPermission,
				width : 100,
				formatter : function(value, row, index)
				{
					return value + '<input class="tab_input" type="hidden" name="detailList.price" value="' + (value || "") + '"/>'
				}
			}, {
				field : 'money',
				title : '金额',
				visible : hasPermission,
				width : 100,
				formatter : function(value, row, index)
				{
					return value + '<input class="tab_input" type="hidden" name="detailList.money" value="' + (value || "") + '"/>';
				}
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
				field : 'produceNum',
				title : '生产数量',
				width : 100
			}, {
				field : 'tax',
				title : '税额',
				visible : hasPermission,
				width : 100,
				formatter : function(value, row, index)
				{
					return value + '<input type="hidden" name="detailList.tax" readonly="readonly" value="' + (value || "") + '"/>';
				}
			}, {
				field : 'taxRateId',
				title : '税收',
				width : 100,
				formatter : function(value, row, index)
				{
					return Helper.basic.info('TAXRATE', value).name;
				}
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
				field : 'workProcedures',
				title : '工艺信息',
				'class' : 'otherView',
				width : 100,
				visible : isProduct
			}, {
				field : 'workMaterials',
				title : '材料信息',
				'class' : 'otherView',
				width : 100,
				visible : isProduct
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
			$("div[title='列'] ul[role=menu]").find("input[value=9]").parent().parent().remove();
			$("div[title='列'] ul[role=menu]").find("input[value=10]").parent().parent().remove();
			$("div[title='列'] ul[role=menu]").find("input[value=11]").parent().parent().remove();
			$("div[title='列'] ul[role=menu]").find("input[value=12]").parent().parent().remove();
			$("div[title='列'] ul[role=menu]").find("input[value=14]").parent().parent().remove();
		}
		/* 返回显示列表 */
		$("#btn_back").on("click", function()
		{
			var url = Helper.basePath + '/outsource/process/list';
			var title = "发外加工列表";
			admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
		});
		/* 编辑 */
		$("#btn_edit").on("click", function()
		{
			location.href = Helper.basePath + '/outsource/process/edit/' + $("#order_id").val();
		});
		// 审核
		$("#btn_audit").on("click", function()
		{
			var order_id = $("#order_id").val();
			Helper.post(Helper.basePath + '/outsource/process/audit/' + order_id, function(data)
			{
				if (data.success)
				{
					Helper.message.suc(data.message);
					location.href = Helper.basePath + "/outsource/process/view/" + order_id;
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
			Helper.post(Helper.basePath + '/outsource/process/audit_cancel/' + order_id, function(data)
			{
				if (data.success)
				{
					Helper.message.suc(data.message);
					location.href = Helper.basePath + "/outsource/process/view/" + order_id;
				} else
				{
					if (data.obj)
					{
						var msg = "";
						var index = 1;
						$.each(data.obj, function(i, n)
						{
							msg += (index++) + ".反审核失败,已被下游单据引用：<br/>";
							$.each(n, function(k, j)
							{
								if (i == 'OA')
								{
									msg += '&emsp;&emsp;发外到货单<a href="javascript:;" onclick="arrive_view(' + j.id + ')">' + j.billNo + '</a><br/>';
								}
								
								if (i == "oem")
								{
									msg = "反审核失败,已被代工平台引用";
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
			Helper.post(Helper.basePath + '/outsource/process/complete', {
				"tableType" : "MASTER",
				"ids" : [ order_id ]
			}, function(data)
			{
				if (data.success)
				{
					Helper.message.suc(data.message);
					location.href = Helper.basePath + "/outsource/process/view/" + order_id;
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

			Helper.post(Helper.basePath + '/outsource/process/complete_cancel', {
				"tableType" : "MASTER",
				"ids" : [ order_id ]
			}, function(data)
			{
				if (data.success)
				{
					Helper.message.suc(data.message);
					location.href = Helper.basePath + "/outsource/process/view/" + order_id;
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
				Helper.post(Helper.basePath + '/outsource/process/del/' + $("#order_id").val(), function(data)
				{
					if (data.success)
					{
						Helper.message.suc(data.message);
						closeTabAndJump("发外加工列表");
					} else
					{
						Helper.message.warn(data.message);
					}
				});
			});
		});
		// 生成发外到货
		$("#btn_transmit_process").click(function()
		{
			if (currentProcessIdList.length > 0)
			{
				var _paramStr = "";
				$(currentProcessIdList).each(function(index, item)
				{
					if (index == 0)
					{
						_paramStr = "ids=" + item;
					} else
					{
						_paramStr = _paramStr + "&ids=" + item;
					}
				});
				_paramStr += "&supplierId=" + $("#supplierId").val();
				var url = Helper.basePath + '/outsource/arrive/create?' + _paramStr;
				var title = "生成发外到货";
				admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
			}
		});
		// 单价变更
		$("#priceChange").on("click", function()
		{
			var row = $("#detailList").bootstrapTable('getSelections');
			if (Helper.isEmpty(row))
			{
				Helper.message.warn("请先选择一个产品");
				return;
			}
			Helper.popup.show('更新单价交期', Helper.basePath + '/outsource/process/editPrice/' + row[0].id, '390', '300');
		});
		// 打印
		$("#btn_print").loadTemplate('OUTSOURCE_OP', '/outsource/process/printAjax/'+$("#processId").val());

		// 表格列拖动效果
		bootstrapTable_ColDrag($("#detailList"));
	});

	/* 查看 */
	function arrive_view(id)
	{
		if (!Helper.isNotEmpty(id))
		{
			return;
		}
		var url = Helper.basePath + '/outsource/arrive/view/' + id;
		var title = "发外到货单";
		admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
	}