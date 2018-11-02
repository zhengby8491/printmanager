$(function()
		{
			/* 判断当前用户是否有权限查看金额 */
			var hasPermission = Helper.basic.hasPermission('stock:product:searchMoney');

			$("#btn_search").click(function()
			{
				$("#bootTable").bootstrapTable("refreshOptions", {
					pageNumber : 1
				});
			});
			/* 更多*/
			$("#btn_more").click(function()
			{
				$("#more_div").toggle();
			});
			$("#product_quick_select").click(function()
			{
				Helper.popup.show('选择产品', Helper.basePath + '/quick/product_select?multiple=false', '900', '490');
			});
			$("#customer_quick_select").click(function()
			{
				Helper.popup.show('选择客户', Helper.basePath + '/quick/customer_select', '900', '490');
			});

			$("#bootTable").bootstrapTable({
				url : Helper.basePath + "/stock/product/ajaxList",
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
				cookieIdTable : "print_stock_product_list",//必须制定唯一的表格cookieID

				uniqueId : "id",//定义列表唯一键
				columns : [ {
					field : 'index',
					title : '序号',
					width : 40,
					formatter : function(value, row, index)
					{
						return index + 1;
					}
				}, {
					field : 'warehouseName',
					title : '仓库',
					width : 80
				}, {
					field : 'productClassName',
					title : '产品分类',
					width : 80,
					formatter : function(value, row, index)
					{
						return row.product.productClassName;
					}
				}, {
					field : 'code',
					title : '产品编号',
					width : 120,
					formatter : function(value, row, index)
					{
						return row.product.code;
					}
				}, {
					field : 'customerMaterialCode',
					title : '客户料号',
					width : 120,
					formatter : function(value, row, index)
					{
						return row.product.customerMaterialCode;
					}
				}, {
					field : 'productName',
					title : '成品名称',
					width : 120,
					formatter : function(value, row, index)
					{
						return row.product.name;
					}
				}, {
					field : 'specifications',
					title : '产品规格',
					width : 100,
					formatter : function(value, row, index)
					{
						return row.product.specifications;
					}
				}, {
					field : 'unitName',
					title : '单位',
					width : 60,
					formatter : function(value, row, index)
					{
						return row.product.unitName;
					}
				}, {
					field : 'qty',
					title : '库存数量',
					width : 80
				}, {
					field : 'price',
					title : '成本单价',
					visible : hasPermission,
					width : 80
				}, {
					field : 'money',
					title : '成本金额',
					visible : hasPermission,
					width : 80
				}, {
					title : '产品图片',
					width : 60,
					formatter : function(value, row, index)
					{
						if (row.product.imgUrl != "" && row.product.imgUrl != null)
						{
							return '<img class="pimg"  src="'+row.product.imgUrl+'"/>';
						} else
						{
							return "";
						}
					}
				} ],
				onLoadSuccess : function()
				{
					//alert("数据加载完成");
					if ($(".glyphicon-th").next().html() == '')
					{
						$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
						if (!Helper.basic.hasPermission('stock:product:export'))
						{
							$(".export.btn-group").remove();
						}
						$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
						// 控制筛选菜单金额选择,删除已隐藏字段的选项
						if (!hasPermission)
						{
							$("div[title='列'] ul[role=menu]").find("input[value=9]").parent().parent().remove();
							$("div[title='列'] ul[role=menu]").find("input[value=10]").parent().parent().remove();
						};
					}

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
				onLoadError : function()
				{
					// alert("数据加载异常");
				}

			});
		})

		function refresh()
		{
			window.location.reload();
		}

		//获取返回信息
		function getCallInfo_product(obj)
		{
			$("#productName").val(obj.name);
		}
		
		function getCallInfo_customer(obj)
		{
			$("#customerName").val(obj.name);
		}

		//请求参数 
		function queryParams(params)
		{
			params['productName'] = $("#productName").val();
			params['warehouseId'] = $("#warehouseId").val();
			params['productClassId'] = $("#productClassId").val();
			params['isEmptyWare'] = $("#isEmptyWare").is(':checked') == true ? "YES" : null;

			params['code'] = $("#code").val();
			params['specifications'] = $("#specifications").val();
			params['customerMaterialCode'] = $("#customerMaterialCode").val();
			params['customerName'] = $("#customerName").val();
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