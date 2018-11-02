<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic }/site/transmit.js?v=${v }"></script>
<title>代工平台</title>
<script type="text/javascript">
	$(function()
	{
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
				var customerId = "", originCompanyExit = "";
				var companyId = "";
				var companyArr = new Array();
				var oemCompany;
				$(rows).each(function()
				{
					// 过滤合计
					if(null == this.id) 
					{
						return;
					}
					
					// 代工平台公司
					oemCompany = this.oemCompany;

					if (this.oemCompany)
					{
						companyId = this.oemCompany.id;
						if (!companyArr.contains(companyId))
						{
							companyArr.push(companyId);
						}
					}

					// 代工平台客户
					if (this.oemCustomer)
					{
						customerId = this.oemCustomer.id;
						originCompanyExit = this.oemCustomer.originCompanyExit;
					}
				});

				//判断是否统一客户
				if (companyArr.length == 1)
				{

					var _paramStr = "";
					$(rows).each(function(index, item)
					{
						if (item.id)
						{
							_paramStr = _paramStr + "&ids=" + item.id;
						}
					});

					// 检查基础资料是否存在代工平台供应商（用于代工平台）
					if (originCompanyExit === 'YES')
					{
						Helper.message.confirm("客户名称已存在，是否与代工平台客户绑定？", function(index)
						{
							var _data = {
								id : customerId,
								name : oemCompany.name,
								originCompanyId : oemCompany.id
							};

							Helper.request({
								url : Helper.basePath + "/basic/customer/updateOem",
								data : _data,
								async : false,
								success : function(data)
								{
									if (data.success)
									{
										_paramStr += "&oemCustomerId=" + customerId;
										createFromTransmitOrder(_paramStr);
									} else
									{
										Helper.message.alert('更新失败：' + data.message);
									}
								}
							});
						});
					}
					// 客户未创建，则提示快捷创建
					else if (customerId === "")
					{
						Helper.message.confirm("是否需要创建代工平台客户", function(index)
						{
							// 存放起来
							$("#customerId").data("param", _paramStr);
							quickCustomerAdd(oemCompany.name, oemCompany.address, oemCompany.linkName, oemCompany.tel, oemCompany.id);
							layer.close(index);
						});
					} else
					{
						_paramStr += "&oemCustomerId=" + customerId;
						createFromTransmitOrder(_paramStr);
					}
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
			var ids = $(rows).map(function()
			{
				return this.id;
			}).get();
			if (Helper.isNotEmpty(rows))
			{
				Helper.post(Helper.basePath + '/oem/transmit/completeOutsourceOrder', {
					"tableType" : "DETAIL",
					"ids" : ids
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
			var ids = $(rows).map(function()
			{
				return this.id;
			}).get();
			if (Helper.isNotEmpty(rows))
			{
				Helper.post(Helper.basePath + '/oem/transmit/completeOutsourceOrderCancel', {
					"tableType" : "DETAIL",
					"ids" : ids
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
			url : Helper.basePath + "/oem/transmit/toOrderList",
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
			cookieIdTable : "print_oem_transmit_order",//必须制定唯一的表格cookieID

			uniqueId : "id",//定义列表唯一键
			columns : [ {
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
				width : 80,
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
				field : 'master_deliveryTime',
				title : '交货日期',
				width : 80,
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return;
					}
					if (Helper.isNotEmpty(row.master.deliveryTime))
					{
						return new Date(row.master.deliveryTime).format("yyyy-MM-dd");
					}
				}
			}, {
				field : 'typeText',
				title : '发外类型',
				width : 80
			}, {
				field : 'master_billNo',
				title : '加工单号',
				width : 100,
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return;
					}
					return row.master.billNo;
				}
			}, {
				field : 'workBillNo',
				title : '生产单号',
				width : 100
			}, {
				field : 'produceNum',
				title : '生产数量',
				width : 80
			}, {
				field : 'productNames',
				title : '成品名称',
				width : 80
			}, {
				field : 'name',
				title : '工序名称',
				width : 120,
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return;
					}
					return row.procedureName;
				}
			}, {
				field : 'style',
				title : '加工规格',
				width : 80
			}, {
				field : 'customerName',
				title : '客户',
				width : 180,
				formatter : function(value, row, index)
				{
					if (row.oemCompany == null)
					{
						return;
					}

					return row.oemCompany.name;
				}
			}, {
				field : 'customerAddress',
				title : '客户地址',
				width : 250,
				formatter : function(value, row, index)
				{
					if (row.oemCompany == null)
					{
						return;
					}

					return row.oemCompany.address;
				}
			}, {
				field : 'linkName',
				title : '联系人',
				width : 80,
				formatter : function(value, row, index)
				{
					if (row.oemCompany == null)
					{
						return;
					}

					return row.oemCompany.linkName;
				}
			}, {
				field : 'mobile',
				title : '联系电话',
				width : 100,
				formatter : function(value, row, index)
				{
					if (row.oemCompany == null)
					{
						return;
					}

					return row.oemCompany.tel;
				}
			}, {
				field : 'partName',
				title : '部件名称',
				width : 80
			}, {
				field : 'qty',
				title : '代工数量',
				width : 80
			}, {
				field : 'memo',
				title : '备注',
				'class' : 'memoView',
				width : 200
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
				if (!Helper.basic.hasPermission('oem:order:transmit:export'))
				{
					$(".export.btn-group").remove();
				}
				$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
			}
		});

	});

	function queryParams(params)
	{
		params['dateMin'] = $("#dateMin").val().trim();
		params['dateMax'] = $("#dateMax").val().trim();
		params['deliverDateMin'] = $("#deliverDateMin").val().trim();
		params['deliverDateMax'] = $("#deliverDateMax").val().trim();
		params['billNo'] = $("#billNo").val().trim();
		params['workBillNo'] = $("#workBillNo").val().trim();
		params['procedureName'] = $("#procedureName").val().trim();
		params['customerName'] = $("#customerName").val().trim();
		params['completeFlag'] = $("input[name='completeFlag']:checked").val().trim();
		return params;
	}

	/**
	 * 客户快速添加
	 * @param params
	 * @returns {___anonymous5070_5075}
	 * @since 1.0, 2018年3月19日 下午3:13:32, think
	 */
	function quickCustomerAdd(customerName, customerAddress, customerLinkName, customerMobile, originCompanyId)
	{
		var add_url = Helper.basePath + "/quick/customer_add?";
		if (Helper.isNotEmpty(customerName))
		{
			add_url = add_url + "&customerName=" + customerName;
		}
		if (Helper.isNotEmpty(customerAddress))
		{
			add_url = add_url + "&customerAddress=" + customerAddress;
		}
		if (Helper.isNotEmpty(customerLinkName))
		{
			add_url = add_url + "&customerLinkName=" + customerLinkName;
		}
		if (Helper.isNotEmpty(customerMobile))
		{
			add_url = add_url + "&customerMobile=" + customerMobile;
		}
		if (Helper.isNotEmpty(originCompanyId))
		{
			add_url = add_url + "&originCompanyId=" + originCompanyId;
		}
		// 保存后调用getCallInfo_customerCallAfter
		add_url = add_url + "&callAfter=YES";

		Helper.popup.show('快速添加客户信息', add_url, '530', '300');
	}

	/**
	 * 客户快速添加后返回信息
	 * @param params
	 * @returns {___anonymous5070_5075}
	 * @since 1.0, 2018年3月20日 下午3:13:32, think
	 */
	function getCallInfo_customerCallAfter(data)
	{
		var _paramStr = $("#customerId").data("param");
		_paramStr += "&oemCustomerId=" + data.id;
		createFromTransmitOrder(_paramStr);
	}

	/**
	 * 生成代工订单
	 * @param params
	 * @returns {___anonymous5070_5075}
	 * @since 1.0, 2018年3月20日 下午3:13:32, think
	 */
	function createFromTransmitOrder(paramStr)
	{
		var url = Helper.basePath + '/oem/order/createFromTransmitOrder?1=1' + paramStr;
		var title = "代工订单";
		admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
	}
</script>
</head>
<body>
	<div class="page-container">
		<input type="hidden" id="customerId" />
		<div class="page-border">
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="代工管理-转单功能-代工平台"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="oem:order:create">
						<button class="nav_btn table_nav_btn" id="btn_transmit">生成代工订单</button>
					</shiro:hasPermission>
					<shiro:hasPermission name="oem:order:transmit:complete">
						<button class="nav_btn table_nav_btn" id="btn_complete">强制完工</button>
					</shiro:hasPermission>
					<shiro:hasPermission name="oem:order:transmit:completecancel">
						<button class="nav_btn table_nav_btn" id="btn_complete_cancel" style="display: none;">取消强制完工</button>
					</shiro:hasPermission>
				</div>
			</div>
			<div class="cl">
				<div class="cl order-info">
					<dl class="cl row-dl">
						<dd class="row-dd">
							<sys:dateConfine label="制单日期" />
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">客户名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_9" id="customerName" name="customerName" />
								<div class="select-btn" id="customer_quick_oem">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">加工单号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_9" name="billNo" id="billNo" />
							</span>
						</dd>
						<dd class="row-dd">
							<button type="button" class="nav_btn table_nav_btn search_btn" id="btn_search">
								<i class="fa fa-search"></i>
								查询
							</button>
							<button class="nav_btn table_nav_btn more_show" id="btn_more">更多</button>
						</dd>
					</dl>
					<dl class="cl hide_container" style="display: none;" id="more_div">
						<dd class="row-dd">
							<sys:dateConfine label="交货日期" dateMaxId="deliverDateMax" dateMinId="deliverDateMin"/>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">工序名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_9" id="procedureName" name="procedureName" />
								<div class="select-btn" id="procedure_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">生产单号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_9" id="workBillNo" />
							</span>
						</dd>
					</dl>
				</div>
				<div>
					<div class="radio-box">
						<label>
							<input type="radio" value="YES" name="completeFlag" />
							已强制完工
						</label>
						<label>
							<input type="radio" value="NO" name="completeFlag" checked="checked" />
							未强制完工
						</label>
					</div>
				</div>
			</div>
			<!--表格Start-->
			<div class="boot-mar">
				<table class="border-table resizable" id="bootTable">
				</table>
			</div>
			<!--表格End-->
		</div>
	</div>
</body>
</html>