<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>公司管理</title>
<script type="text/javascript">
	$(function()
	{
		//查询，刷新table
		$("#btn_search").click(function()
		{
			$("#bootTable").bootstrapTable("refreshOptions", {
				pageNumber : 1
			});
		});

		//订单详情table
		$("#bootTable").bootstrapTable({
			url : Helper.basePath + "/sys/company/ajaxList",
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
			cookieIdTable : "print_sys_company",//必须制定唯一的表格cookieID

			uniqueId : "id",//定义列表唯一键
			columns : [ {
				field : 'userName',
				title : '注册用户名',
				width : 100,
				formatter : function(value, row, index)
				{
					if (Helper.isNotEmpty(row.registerUser))
					{
						return "<a onclick=\"user_view('" + row.registerUser.id + "')\" > " + row.registerUser.userName + "</a>";
					} else
					{
						return "-";
					}
				}
			}, {
				field : 'name',
				title : '公司名称',
				width : 180,
				formatter : function(value, row, index)
				{
					return row.company.name;
				}
			}, {
				field : 'state',
				title : '状态',
				width : 40,
				formatter : function(value, row, index)
				{
					return row.company.stateText;
				}
			}, {
				field : 'expireTime',
				title : '到期时间',
				width : 100,
				formatter : function(value, row, index)
				{
					if (Helper.isNotEmpty(row.company.createTime))
					{
						return "<span name='expireTime_"+row.id+"'>" + new Date(row.company.expireTime).format("yyyy-MM-dd") + "</span>";

					}
				}

			}, {
				field : 'initStep',
				title : '初始化进度',
				width : 80,
				formatter : function(value, row, index)
				{
					return row.company.initStepText;
				}
			}, {
				field : 'isFormal',
				title : '正式用户',
				width : 80,
				formatter : function(value, row, index)
				{
					return row.company.isFormalText;
				}
			}, {
				field : 'type',
				title : '类型',
				width : 100,
				formatter : function(value, row, index)
				{
					return row.company.typeText;
				}
			}, {
				field : 'standardCurrency',
				title : '币别',
				width : 60,
				formatter : function(value, row, index)
				{
					return row.company.standardCurrencyText;
				}
			}, {
				field : 'fax',
				title : '传真',
				width : 100,
				visible : false,
				formatter : function(value, row, index)
				{
					return row.company.fax;
				}
			}, {
				field : 'linkName',
				title : '联系人',
				width : 80,
				formatter : function(value, row, index)
				{
					return row.company.linkName;
				}
			}, {
				field : 'tel',
				title : '电话',
				width : 120,
				formatter : function(value, row, index)
				{
					return row.company.tel;
				}
			}, {
				field : 'email',
				title : '公司邮箱',
				width : 140,
				visible : false,
				formatter : function(value, row, index)
				{
					return row.company.email;
				}
			}, {
				field : 'weixin',
				title : '微信公众号',
				width : 80,
				visible : false,
				formatter : function(value, row, index)
				{
					return row.company.weixin;
				}
			}, {
				field : 'province',
				title : '所在省份',
				width : 80,
				formatter : function(value, row, index)
				{
					return row.company.province;
				}
			}, {
				field : 'city',
				title : '所在城市',
				width : 80,
				visible : false,
				formatter : function(value, row, index)
				{
					return row.company.city;
				}
			}, {
				field : 'county',
				title : '所在区县',
				width : 80,
				visible : false,
				formatter : function(value, row, index)
				{
					return row.company.county;
				}
			}, {
				field : 'address',
				title : '具体地址',
				width : 180,
				visible : false,
				formatter : function(value, row, index)
				{
					return row.company.address;
				}
			}, {
				field : 'website',
				title : '公司网址',
				width : 160,
				visible : false,
				formatter : function(value, row, index)
				{
					return row.company.website;
				}
			}, {
				field : 'introduction',
				title : '公司简介',
				width : 80,
				visible : false,
				formatter : function(value, row, index)
				{
					return row.company.introduction;
				}
			}, {
				field : 'createName',
				title : '创建人',
				width : 80,
				visible : false,
				formatter : function(value, row, index)
				{
					return row.company.createName;
				}
			}, {
				field : 'createTime',
				title : '创建时间',
				width : 80,
				visible : false,
				formatter : function(value, row, index)
				{
					if (Helper.isNotEmpty(row.company.createTime))
					{
						return new Date(row.company.createTime).format("yyyy-MM-dd");
					}
				}
			}, {
				field : 'updateName',
				title : '修改人',
				width : 60,
				visible : false,
				formatter : function(value, row, index)
				{
					return row.company.updateName;
				}
			}, {
				field : 'updateTime',
				title : '修改日期',
				width : 80,
				visible : false,
				formatter : function(value, row, index)
				{
					if (Helper.isNotEmpty(row.company.updateTime))
					{
						return new Date(row.company.updateTime).format("yyyy-MM-dd");
					}
				}
			}, {
				field : 'operator',
				title : '操作',
				width : 480,
				formatter : function(value, row, index)
				{
					var operator = "";
					if (Helper.basic.hasPermission("sys:company:list"))
					{
						operator += '&nbsp;&nbsp;&nbsp;<a title="查看" href="javascript:;" onclick="company_view(\'' + row.company.id + '\')" style="padding: 0 4px; color: green">查看</a>';
					}
					if (Helper.basic.hasPermission("sys:company:edit"))
					{
						operator += '&nbsp;&nbsp;&nbsp;<a title="编辑" href="javascript:;" onclick="company_edit(' + row.company.id + ')" style="padding: 0 4px; color: green">编辑</a>';
					}
					if (Helper.basic.hasPermission("sys:company:maintain"))
					{
						operator += '<a title="维护" href="javascript:;" onclick="company_maintain(' + row.company.id + ')" style="padding: 0 4px; color: green">维护</a>';
					}
					if (Helper.basic.hasPermission("sys:company:clearAllBillData"))
					{
						operator += '<a title="清理所有单据数据" href="javascript:;" onclick="company_clearAllBillData(' + row.company.id + ')" style="padding: 0 4px; color: green">清理所有单据数据</a>';
					}
					if (Helper.basic.hasPermission("sys:company:clearAllBillData"))
					{
						operator += '<a title="清理所有数据" href="javascript:;" onclick="company_clearAllData(' + row.company.id + ')" style="padding: 0 4px; color: green">清理所有数据</a>';
					}
					if (Helper.basic.hasPermission("sys:company:clearAllBillData"))
					{
						operator += '<a title="基础资料初始化" href="javascript:;" onclick="company_resetInitSate(' + row.company.id + ')" style="padding: 0 4px; color: green">基础资料初始化</a>';
					}
					if (Helper.basic.hasPermission("sys:company:permissions"))
					{
						operator += '<a title="权限分配" href="javascript:;" onclick="company_permissions(' + row.company.id + ')" style="padding: 0 4px; color: green">权限分配</a> &nbsp;&nbsp;&nbsp;';
					}
					return operator;
				}
			} ],
			onDblClickRow : function(row)
			{
				company_view(row.company.id);
			},
			onClickRow : function(row, $element)
			{
				$element.addClass("tr_active").siblings().removeClass("tr_active");
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
				if (!Helper.basic.hasPermission('sys:company:export'))
				{
					$(".export.btn-group").remove();
				}
				$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
			}
		});
		$("#btn_batch_permission").on('click', function()
		{
			Helper.popup.show('批量分配权限', Helper.basePath + '/sys/company/permissionsBatch', '800', '500', true);
		});
		$("#btn_append_batch_permission").on('click', function()
		{
			Helper.popup.show('批量追加权限', Helper.basePath + '/sys/company/permissionsAppendBatch', '800', '500', true);
		});
	});

	// 查看
	function company_view(id)
	{
		var url = Helper.basePath + '/sys/company/view/' + id;
		Helper.popup.show("公司查看", url, "700", "350");
	}

	//维护
	function company_maintain(id)
	{
		var url = Helper.basePath + '/sys/company/maintain/' + id;
		Helper.popup.show("维护", url, "600", "300");
	}

	// 修改
	function company_edit(id)
	{
		var url = Helper.basePath + '/sys/company/edit/' + id;
		Helper.popup.show("公司编辑", url, "700", "390");
	}
	/* 清理所有单据数据 */
	function company_clearAllBillData(id)
	{
		Helper.message.confirm('确认要清理所有单据数据吗？', function(index)
		{
			Helper.post(Helper.basePath + '/sys/company/clearAllBillData', {
				"companyId" : id
			}, function(result)
			{
				console.log(result);
				if (result.success)
				{
					Helper.message.suc("清理完成！");
				} else
				{
					Helper.message.warn(result.message);
				}
			}, function(result)
			{
				Helper.message.warn("清理出现异常！")
			});
		});
	}
	/* 清理所有数据 */
	function company_clearAllData(id)
	{
		Helper.message.confirm('确认要清理所有数据吗？', function(index)
		{
			Helper.post(Helper.basePath + '/sys/company/clearAllData', {
				"companyId" : id
			}, function(result)
			{
				if (result.success)
				{
					Helper.message.suc("清理完成！");
				} else
				{
					Helper.message.warn(result.message);
				}
			}, function(result)
			{
				Helper.message.warn("清理出现异常！")
			});
		});
	}
	/* 重设公司初始化状态  */
	function company_resetInitSate(id)
	{
		Helper.message.confirm('确认重置公司初始化状态吗？', function(index)
		{
			Helper.post(Helper.basePath + '/sys/company/resetCompanyState', {
				"companyId" : id
			}, function(result)
			{
				if (result.success)
				{
					Helper.message.suc("重设完成！");
				} else
				{
					Helper.message.warn(result.message);
				}
			}, function(result)
			{
				Helper.message.warn("重设出现异常！")
			});
		});
	}
	/* 权限分配 */
	function company_permissions(id)
	{
		Helper.popup.show('分配权限', Helper.basePath + '/sys/company/permissions/' + id, '800', '500', true);
	}
	/* 获取到期时间返回信息*/
	function getCallInfo_expireTime(id, value)
	{
		console.log(id);
		$("span[name='expireTime_" + id + "']").html(value);
	}

	function responseHandler(res)
	{
		return {
			rows : res.result,
			total : res.count
		};
	}
	function queryParams(params)
	{
		params['companyLinkName'] = $("#companyLinkName").val();
		params['companyTel'] = $("#companyTel").val();
		params['companyName'] = $("#companyName").val();
		params['dateMin'] = $("#dateMin").val();
		params['dateMax'] = $("#dateMax").val();
		params['expireTimeMin'] = $("#expireTimeMin").val();
		params['expireTimeMax'] = $("#expireTimeMax").val();
		params['companyState'] = $("#companyState").val() == "-1" ? null : $("#companyState").val();
		params['isFormal'] = $("#isFormal").val() == "-1" ? null : $("#isFormal").val();
		params['initStep'] = $("#initStep").val() == "-1" ? null : $("#initStep").val();
		params['companyType'] = $("#companyType").val() == "-1" ? null : $("#companyType").val();
		params['userName'] = $("#userName").val();
		return params;
	}

	/* 用户信息-查看 */
	function user_view(id)
	{
		Helper.popup.show('用户信息', Helper.basePath + '/sys/user/view/' + id, '700', '300');
	}
</script>

</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--顶部导航-->
			<div class="cl topPath"></div>
			<!--搜索栏-->
			<div class="cl">
				<div class="cl order-info" style="width: 1140px">
					<dl class="cl row-dl">
						<dd class="row-dd">
							<sys:dateConfine label="创建日期" dateMin="${queryParam.dateMin}" dateMax="${queryParam.dateMax}" initDate="false" />
						</dd>
						<dd class="row-dd">

							<label class="form-label label_ui">到期时间：</label>
							<span>
								<input type="text" onfocus="WdatePicker()" class="input-txt input-txt_0 Wdate" id="expireTimeMin" name="expireTimeMin" value="<fmt:formatDate value="${expireTimeMin}" pattern="yyyy-MM-dd"/>" />
							</span>

							<label class="label_2 align_c">至</label>

							<span>
								<input type="text" onfocus="WdatePicker()" class="input-txt input-txt_0 Wdate" id="expireTimeMax" name="expireTimeMax" value="<fmt:formatDate value="${expireTimeMax}" pattern="yyyy-MM-dd"/>" />
							</span>
						</dd>

						<dd class="row-dd">
							<label class="form-label label_ui label_1">联 系 人：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_0" name="companyLinkName" id="companyLinkName" value="${queryParam.companyLinkName}" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">电话：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_0" name="companyTel" id="companyTel" value="${queryParam.companyTel}" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_6">初始化进度：</label>
							<span class="ui-combo-wrap">
								<phtml:list cssClass="input-txt input-txt_0 hy_select2" defaultOption="全部" textProperty="text" defaultValue="-1" type="com.huayin.printmanager.persist.enumerate.InitStep" name="initStep"></phtml:list>
							</span>
						</dd>
						<dd class="row-dd">
							<button type="button" class="nav_btn table_nav_btn" id="btn_search">
								<i class="fa fa-search"></i>
								查询
							</button>
						</dd>
					</dl>
					<dl class="cl row-dl">
						<dd class="row-dd">
							<label class="form-label label_ui">公司名称：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_13" name="companyName" id="companyName" value="${queryParam.companyName}" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">状 态：</label>
							<span class="ui-combo-wrap">
								<phtml:list cssClass="input-txt input-txt_13 hy_select2" defaultOption="全部" textProperty="text" defaultValue="-1" type="com.huayin.printmanager.persist.enumerate.CompanyState" name="companyState"></phtml:list>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">正式用户：</label>
							<span class="ui-combo-wrap">
								<phtml:list cssClass="input-txt input-txt_0 hy_select2" defaultOption="全部" textProperty="text" defaultValue="-1" type="com.huayin.printmanager.persist.enumerate.BoolValue" name="isFormal"></phtml:list>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">类型：</label>
							<span class="ui-combo-wrap">
								<phtml:list cssClass="input-txt input-txt_0 hy_select2" defaultOption="全部" textProperty="text" defaultValue="-1" type="com.huayin.printmanager.persist.enumerate.CompanyType" name="companyType"></phtml:list>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_6">用 户 名：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_0" name="userName" id="userName" />
							</span>
						</dd>
					</dl>
				</div>
				<div>
					<shiro:hasPermission name="sys:company:permissions">
						<div class="btn-bar">
							<span>
								<a href="javascript:;" id="btn_batch_permission" class="nav_btn table_nav_btn">
									<i class="fa fa-check-square"></i>
									批量分配权限
								</a>
							</span>
							<span>
								<a href="javascript:;" id="btn_append_batch_permission" class="nav_btn table_nav_btn">
									<i class="fa fa-check-square"></i>
									批量追加权限
								</a>
							</span>
						</div>
					</shiro:hasPermission>
					<div class="search_container">
						<!--表格-->
						<div>
							<div class="boot-mar">
								<table class="border-table" id="bootTable">
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
</body>
</html>