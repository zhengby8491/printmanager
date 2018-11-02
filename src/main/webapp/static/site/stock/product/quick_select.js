$(function()
	{
		$(".tree").on("click", "a[name='productClassId']", function()
		{
			$(".tree a[name='productClassId']").css("color", "");
			$(this).css("color", "red");
			var id = $(this).attr("_id");
			$("#productClassId").val(id);
			$("#bootTable").bootstrapTable("refreshOptions", {
				pageNumber : 1
			});
		});

		//查询
		$("#btn_search").click(function()
		{
			$("#bootTable").bootstrapTable("refreshOptions", {
				pageNumber : 1
			});
		});

		//选择确认
		$("#btn_ok").click(function()
		{
			parent.getCallInfo_product_stock($("#bootTable").bootstrapTable('getAllSelections'));
			Helper.popup.close();
		});

		$("#btn_cancel").click(function()
		{
			Helper.popup.close();
		});

		//console.log($("#multiple").val().toBoolean());
		//订单详情table
		$("#bootTable").bootstrapTable({
			url : Helper.basePath + "/stock/product/quick_ajaxList",
			method : "post",
			contentType : 'application/json', //设置请求头信息  
			dataType : "json",
			pagination : true, // 是否显示分页（*）
			sidePagination : 'server',//设置为服务器端分页
			pageList : [ 10, 20, 50 ],
			queryParamsType : "",
			pageSize : 10,
			pageNumber : 1,
			queryParams : queryParams,//参数
			responseHandler : responseHandler,

			//showColumns : true, //是否显示所有的列
			//minimumCountColumns : 2, //最少允许的列数
			striped : true, // 是否显示行间隔色
			cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			sortable : false, // 是否启用排序
			clickToSelect : true, // 是否启用点击选中行
			height : 360, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			cardView : false, // 是否显示详细视图
			detailView : false, // 是否显示父子表
			checkbox : true,//开启多选
			uniqueId : "id",//定义列表唯一键
			//resizable : true, //是否启用列拖动
			columns : [ {
				field : 'state',
				checkbox : true,
				visible : true,
				width : 60
			}, {
				field : 'warehouseName',
				title : '仓库',
				width : 80
			}, {
				field : 'productClassId',
				title : '分类',
				width : 100,
				formatter : function(value, row, index)
				{
					if (Helper.isNotEmpty(row.product.productClassId))
					{
						return Helper.basic.info('PRODUCTCLASS', row.product.productClassId).name;
					} else
					{
						return "";
					}
				}
			}, {
				field : 'code',
				title : '产品编号',
				width : 160,
				formatter : function(value, row, index)
				{
					return row.product.code;
				}
			}, {
				field : 'productName',
				title : '成品名称',
				width : 160,
				formatter : function(value, row, index)
				{
					return row.product.name;
				}
			}, {
				field : 'specifications',
				title : '产品规格',
				width : 120,
				formatter : function(value, row, index)
				{
					return row.product.specifications;
				}
			}, {
				field : 'unitName',
				title : '单位',
				width : 80,
				formatter : function(value, row, index)
				{
					return row.product.unitName;
				}
			}, {
				field : 'qty',
				title : '库存数量',
				width : 80
			}, {
				field : 'imgUrl',
				visible : false
			} ],
			onDblClickRow : function(row)
			{
				//双击选中事件	
				selectRow(row);
			}

		});

	});

	function selectRow(row)
	{
		parent.getCallInfo_product_stock([ row ]);
	}
	//请求参数
	function queryParams(params)
	{
		params['productClassId'] = $("#productClassId").val();
		params['warehouseId'] = $("#warehouseId").val();
		params['productName'] = $("#productName").val();
		params['isEmptyWare'] = $("#isEmptyWare").val();
		params['productStyle'] = $("#specifications").val();
		return params;
	}
	//ajax结果
	function responseHandler(res)
	{
		return {
			rows : res.result,
			total : res.count
		};
	}