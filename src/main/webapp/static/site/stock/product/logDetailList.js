$(function()
	{
		/* 判断当前用户是否有权限查看金额 */
		var hasPermission = Helper.basic.hasPermission('stock:product:logDetailMoney');
		/* 选择成品 */
		$("#product_quick_select").click(function()
		{
			Helper.popup.show('选择成品', Helper.basePath + '/quick/product_select?multiple=false', '800', '500');
		});

		/* 搜索*/
		$("#btn_search").click(function()
		{
			$("#bootTable").bootstrapTable("refreshOptions", {
				pageNumber : 1
			});
		});
		//订单详情table
		$("#bootTable").bootstrapTable({
			url : Helper.basePath + "/stock/product/ajaxLogDetailList",
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
			cookieIdTable : "print_stock_product_logDetailList",//必须制定唯一的表格cookieID

			uniqueId : "id",//定义列表唯一键
			columns : [ {
				field : 'billTypeText',
				title : '单据类型',
				width : 80
			}, {
				field : 'billNo',
				title : '单据编号',
				width : 120,
				formatter : function (value, row, index)
				{
					return idTransToUrl(row.billId, value); 
				}
			}, {
				field : 'createTime',
				title : '单据日期',
				width : 80,
				formatter : function(value, row, index)
				{
					if (value)
					{
						return new Date(value).format("yyyy-MM-dd");
					}
				}
			}, {
				field : 'warehouseName',
				title : '仓库',
				width : 80
			}, {
				field : 'productClassName',
				title : '产品分类',
				width : 80
			}, {
				field : 'code',
				title : '产品编号',
				width : 120
			}, {
				field : 'customerMaterialCode',
				title : '客户料号',
				width : 80
			}, {
				field : 'customerName',
				title : '客户名称',
				width : 120
			}, {
				field : 'productName',
				title : '成品名称',
				width : 120
			}, {
				field : 'specifications',
				title : '产品规格',
				width : 100
			}, {
				field : 'unitName',
				title : '单位',
				width : 60
			}, {
				field : 'price',
				title : '成本单价',
				visible : hasPermission,
				width : 80
			}, {
				field : 'inQty',
				title : '入库数量',
				width : 80
			}, {
				field : 'inMoney',
				title : '入库金额',
				visible : hasPermission,
				width : 80
			}, {
				field : 'outQty',
				title : '出库数量',
				width : 80
			}, {
				field : 'outMoney',
				title : '出库金额',
				visible : hasPermission,
				width : 80
			}, {
				field : 'storgeQty',
				title : '库存数量',
				width : 80
			}, {
				field : 'memo',
				title : '备注',
				'class' : 'memoView',
				width : 200
			}, {
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
			onLoadSuccess : function()
			{
				//alert("数据加载完成");
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
			onClickRow : function(row, $element)
			{
				$element.addClass("tr_active").siblings().removeClass("tr_active");
			}

		});
		/* 表格工具栏 */
		$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
		// 控制筛选菜单金额选择,删除已隐藏字段的选项
		if (!hasPermission)
		{
			$("div[title='列'] ul[role=menu]").find("input[value=10]").parent().parent().remove();
			$("div[title='列'] ul[role=menu]").find("input[value=12]").parent().parent().remove();
			$("div[title='列'] ul[role=menu]").find("input[value=14]").parent().parent().remove();
		}
		;
		if (!Helper.basic.hasPermission('stock:productLogdetail:export'))
		{
			$(".export.btn-group").remove();
		}
		$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
	});
	// 获取返回信息
	function getCallInfo_product(obj)
	{
		$("#productName").val(obj.name);
	}
	//请求参数
	function queryParams(params)
	{
		params['dateMin'] = $("#dateMin").val();
		params['dateMax'] = $("#dateMax").val();
		params['productName'] = $("#productName").val();
		params['warehouseId'] = $("#warehouseId").val();
		params['searchContent'] = $("#billType").val();//枚举类型选所有 不能传空字符
		params['customerMaterialCode'] = $("#customerMaterialCode").val();
		//	console.log(params)
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