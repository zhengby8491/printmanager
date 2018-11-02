$(function()
{
	/* 判断当前用户是否有权限查看金额 */
	var hasPermission = Helper.basic.hasPermission('stock:productInventory:money');

	$("#detailList").bootstrapTable({
		data : $.parseJSON($("#orderDetailList").val()),
		showColumns : true, // 是否显示所有的列
		minimumCountColumns : 2, // 最少允许的列数
		striped : true, // 是否显示行间隔色
		cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
		pagination : false, // 是否显示分页（*）
		sortable : false, // 是否启用排序
		clickToSelect : true, // 是否启用点击选中行
		cardView : false, // 是否显示详细视图
		detailView : false, // 是否显示父子表
		toolbar : "#toolbar", // 设置工具栏的Id或者class
		// resizable : true, //是否启用列拖动
		cookie : true,// 是否启用COOKIE
		cookieIdTable : "print_stockproduct_inventory_master_view",// 必须制定唯一的表格cookieID
		columns : [ {
			field : 'id',
			title : '序号',
			width : 40,
			formatter : function(value, row, index)
			{
				return index + 1;
			}
		}, {
			field : 'code',
			title : '产品编号',
			width : 140

		}, {
			field : 'productName',
			title : '成品名称',
			width : 160

		}, {
			field : 'specifications',
			title : '产品规格',
			width : 100
		},

		{
			field : 'unitId',
			title : '单位',
			width : 60,
			formatter : function(value, row, index)
			{
				return Helper.basic.info('UNIT', value).name;
			}
		}, {
			field : 'qty',
			title : '盘点数量',
			width : 80
		},

		{
			field : 'stockQty',
			title : '库存数量',
			width : 80
		},

		{
			field : 'price',
			title : '成本单价',
			visible : hasPermission,
			width : 60
		}, {
			field : 'money',
			title : '盘点金额',
			visible : hasPermission,
			width : 100
		}, {
			field : 'profitAndLossQty',
			title : '盈亏数量',
			width : 80
		}, {
			field : 'profitAndLossMoney',
			title : '盈亏金额',
			visible : hasPermission,
			width : 100
		}, {
			field : 'inventoryType',
			title : '盘点类型',
			width : 80,
			formatter : function(value, row, index)
			{
				return Helper.basic.getEnumText('com.huayin.printmanager.persist.enumerate.InventoryType', value, 'text');
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
					return '<img class="pimg"  src="' + value + '"/>';
				} else
				{
					return "";
				}
			}
		} ],
		onLoadSuccess : function()
		{
			// layer.alert("数据加载完成");
		},
		onLoadError : function()
		{
			// layer.alert("数据加载异常");
		},
		onColumnSwitch : function(field, checked)
		{
			// 在使用筛选增加或减少列时重置表格列拖动效果
			bootstrapTable_ColDrag($("#detailList"));
		}
	});

	/* 表格工具栏 */
	$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
	// 控制筛选菜单金额选择,删除已隐藏字段的选项
	if (!hasPermission)
	{
		$("div[title='列'] ul[role=menu]").find("input[value=7]").parent().parent().remove();
		$("div[title='列'] ul[role=menu]").find("input[value=8]").parent().parent().remove();
		$("div[title='列'] ul[role=menu]").find("input[value=10]").parent().parent().remove();
	}
	;

	$("#btn_edit").click(function()
	{
		window.location.href = Helper.basePath + '/stockproduct/inventory/edit/' + $(this).val();
	});
	$("#btn_back").click(function()
	{
		var url = Helper.basePath + '/stockproduct/inventory/list';
		var title = "成品库存盘点列表";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	});
	$("#btn_audit").click(function()
	{
		$.ajax({
			cache : true,
			type : "POST",
			dataType : "json",
			url : Helper.basePath + '/stockproduct/inventory/check/' + $(this).val(),
			async : false,
			error : function(request)
			{
				layer.alert("Connection error");
			},
			success : function(data)
			{
				// console.log(data.success);
				if (data.success)
				{
					location.reload();
				} else
				{
					Helper.message.warn(data.message);
				}
			}
		});
	});
	$("#btn_del").click(function()
	{
		var this_ = $(this);
		Helper.message.confirm('确认要删除吗？', function(index)
		{
			$.ajax({
				cache : true,
				type : "POST",
				dataType : "json",
				url : Helper.basePath + '/stockproduct/inventory/delete/' + this_.val(),
				async : false,
				error : function(request)
				{
					// console.log(request);
				},
				success : function(data)
				{
					if (data.success)
					{
						closeTabAndJump("成品库存盘点列表");
						/*
						 * window.location.href=Helper.basePath +
						 * '/stockproduct/inventory/list';
						 */
					} else
					{
						layer.alert("失败");
					}
				}
			});
		});
	});
	// 打印模板加载
	$("#btn_print").loadTemplate('STOCK_SPI', '/stockproduct/inventory/printAjax/' + $("#orderId").val());

	// 表格列拖动效果
	bootstrapTable_ColDrag($("#detailList"));
})