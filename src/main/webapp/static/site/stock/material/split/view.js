$(function()
{
	/* 判断当前用户是否有权限查看金额 */
	var hasPermission = Helper.basic.hasPermission('stock:materialSplit:money');

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
		cookieIdTable : "print_stockmaterial_split_master_view",// 必须制定唯一的表格cookieID
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
			title : '材料编号',
			width : 140

		}, {
			field : 'materialName',
			title : '材料名称',
			width : 120

		}, {
			field : 'specifications',
			title : '分切规格',
			width : 100
		}, {
			field : 'weight',
			title : '克重',
			width : 60
		}, {
			field : 'stockUnitId',
			title : '单位',
			width : 60,
			formatter : function(value, row, index)
			{
				return Helper.basic.info('UNIT', value).name;
			}
		},

		{
			field : 'qty',
			title : '数量',
			width : 80
		}, {
			field : 'valuationUnitId',
			title : '计价单位',
			width : 80,
			formatter : function(value, row, index)
			{
				return Helper.basic.info('UNIT', value).name;
			}
		}, {
			field : 'valuationQty',
			title : '计价数量',
			width : 80
		}, {
			field : 'warehouseId',
			title : '入库仓',
			width : 80,
			formatter : function(value, row, index)
			{
				return Helper.basic.info('WAREHOUSE', value).name;
			}
		},

		{
			field : 'price',
			title : '成本单价',
			visible : hasPermission,
			width : 80
		}, {
			field : 'money',
			title : '成本金额',
			visible : hasPermission,
			width : 100
		}, {
			field : 'memo',
			title : '备注',
			'class' : 'memoView',
			width : 200
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
	// 控制筛选菜单金额选择,删除已隐藏字段的选项
	if (!hasPermission)
	{
		$("div[title='列'] ul[role=menu]").find("input[value=10]").parent().parent().remove();
		$("div[title='列'] ul[role=menu]").find("input[value=11]").parent().parent().remove();
	}
	;
	$("#btn_edit").click(function()
	{
		window.location.href = Helper.basePath + '/stockmaterial/split/edit/' + $(this).val();
	});
	$("#btn_back").click(function()
	{
		var url = Helper.basePath + '/stockmaterial/split/list';
		var title = "材料分切列表";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	});

	$("#btn_audit").click(function()
	{
		var $id = $(this).val();
		$.ajax({
			cache : true,
			type : "POST",
			dataType : "json",
			url : Helper.basePath + '/stockmaterial/split/check/' + $id,
			async : false,
			error : function(request)
			{
				layer.alert("Connection error");
			},
			success : function(data)
			{
				console.log(data)
				if (data.success)
				{
					if (data.obj == null)
					{
						location.reload();
					} else
					{
						var str = '审核失败,以下材料库存数量不足\n';
						for (var i = 0; i < data.obj.length; i++)
						{
							str = str + '名称:' + data.obj[i].material.name + (data.obj[i].specifications || '') + '   目前库存数量' + data.obj[i].qty + '\n';
						}
						Helper.message.confirm(str, function(index)
						{
							Helper.message.confirm("确认操作会引起负库存，是否允许负库存?", function(index)
							{
								forceCheck($id);
							});
						});

					}
				} else
				{
					Helper.message.warn(data.message);
				}

			}
		});
	});

	$("#btn_audit_cancel").click(function()
	{
		var $id = $(this).val();
		$.ajax({
			cache : true,
			type : "POST",
			dataType : "json",
			url : Helper.basePath + '/stockmaterial/split/checkBack/' + $id,
			async : false,
			error : function(request)
			{
				layer.alert("Connection error");
			},
			success : function(data)
			{
				if (data.success)
				{
					if (data.obj == null)
					{
						location.reload();
					} else
					{
						var str = '反审失败,以下材料库存数量不足\n';
						for (var i = 0; i < data.obj.length; i++)
						{
							str = str + '名称:' + data.obj[i].material.name + (data.obj[i].specifications || '') + '   目前库存数量' + data.obj[i].qty + '\n';
						}
						Helper.message.confirm(str, function(index)
						{
							Helper.message.confirm("确认操作会引起负库存，是否允许负库存?", function(index)
							{
								forceCheckBack($id);
							});
						});

					}
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
				url : Helper.basePath + '/stockmaterial/split/delete/' + this_.val(),
				async : false,
				error : function(request)
				{
					// console.log(request);
				},
				success : function(data)
				{
					// console.log(data.success);
					if (data.success)
					{
						closeTabAndJump("材料分切列表");
						/*
						 * window.location.href=Helper.basePath +
						 * '/stockmaterial/split/list';
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
	$("#btn_print").loadTemplate('STOCK_SLT', '/stockmaterial/split/printAjax/' + $("#orderId").val());

	// 表格列拖动效果
	bootstrapTable_ColDrag($("#detailList"));
})

/**
 * 强制审核
 */
function forceCheck(id)
{
	$.ajax({
		cache : true,
		type : "POST",
		dataType : "json",
		url : Helper.basePath + '/stockmaterial/split/forceCheck/' + id,
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
				layer.alert("审核失败")
			}
		}
	});
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
		url : Helper.basePath + '/stockmaterial/split/forceCheckBack/' + id,
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
				layer.alert("审核失败")
			}
		}
	});
}