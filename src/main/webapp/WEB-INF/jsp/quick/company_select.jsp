<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>

<meta charset="UTF-8">
<title>选择客户</title>
<script type="text/javascript">
	$(function() {
		//查询
		$("#btn_search").click(function() {
			$("#bootTable").bootstrapTable("refreshOptions", {
				pageNumber : 1
			});
		});

		//选择确认
		$("#btn_ok").click(
				function() {
					if ($("#multiple").val().toBoolean()) {//多选,则双击事件
						parent.getCallInfo_customerArray($("#bootTable")
								.bootstrapTable('getAllSelections'));
					}
					Helper.popup.close();
				});

		$("#btn_cancel").click(function() {
			Helper.popup.close();
		});

		//console.log($("#multiple").val().toBoolean());
		//订单详情table
		$("#bootTable")
				.bootstrapTable(
						{
							url : Helper.basePath + "/sys/company/ajaxList",
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
							checkbox : $("#multiple").val().toBoolean(),//开启多选
							uniqueId : "id",//定义列表唯一键
							//resizable : true, //是否启用列拖动
							columns : [{
				                field: 'userName',
				                title: '注册用户名',
				                width:100,
				                formatter: function(value, row, index)
				                {
				                    if(Helper.isNotEmpty(row.registerUser))
				                    {
				                        return "<a onclick=\"user_view('"+row.registerUser.id+"')\" > "+row.registerUser.userName+"</a>";
				                    }else
				                    {
				                        return "-";
				                    }
				                }
				            	}, {
				                field: 'name',
				                title: '公司名称',
				                width: 180,
				                formatter: function(value, row, index)
				                {
				                    return row.company.name;
				                }
				            	}, {
				                  field: 'state',
				                  title: '状态',
				                  width: 40,
				                  formatter: function(value, row, index)
				                  {
				                      return  row.company.stateText;
				                  }
				              	},{
				                field: 'expireTime',
				                title: '到期时间',
				                width: 100,
				                formatter: function(value, row, index)
				                {
				                    if(Helper.isNotEmpty(row.company.createTime)) { 
				                      return "<span name='expireTime_"+row.id+"'>" +new Date(row.company.expireTime).format("yyyy-MM-dd")+"</span>"; 
				                      
				                    }
				                }

				            },  {
				                field: 'initStep',
				                title: '初始化进度',
				                width: 80,
				                formatter: function(value, row, index)
				                {
				                    return  row.company.initStepText;
				                }
				            }, {
				                field: 'isFormal',
				                title: '正式用户',
				                width: 80,
				                formatter: function(value, row, index)
				                {
				                    return  row.company.isFormalText;
				                }
				            }, {
				                field: 'type',
				                title: '类型',
				                width: 100,
				                formatter: function(value, row, index)
				                {
				                    return  row.company.typeText;
				                }
				            },{
				                field: 'standardCurrency',
				                title: '币别',
				                width: 60,
				                formatter: function(value, row, index)
				                {
				                    return  row.company.standardCurrencyText;
				                }
				            }, {
				                field: 'fax',
				                title: '传真',
				                width: 100,
				                visible:false,
				                formatter: function(value, row, index)
				                {
				                    return  row.company.fax;
				                }
				            }, {
				                field: 'linkName',
				                title: '联系人',
				                width: 80,
				                formatter: function(value, row, index)
				                {
				                    return  row.company.linkName;
				                }
				            }, {
				                field: 'tel',
				                title: '电话',
				                width: 120,
				                formatter: function(value, row, index)
				                {
				                    return  row.company.tel;
				                }
				            }, {
				                field: 'email',
				                title: '公司邮箱',
				                width: 140,
				                visible:false,
				                formatter: function(value, row, index)
				                {
				                    return  row.company.email;
				                }
				            }, {
				                field: 'weixin',
				                title: '微信公众号',
				                width: 80,
				                visible:false,
				                formatter: function(value, row, index)
				                {
				                    return  row.company.weixin;
				                }
				            }, {
				                field: 'province',
				                title: '所在省份',
				                width: 80,
				                formatter: function(value, row, index)
				                {
				                    return  row.company.province;
				                }
				            }, {
				                field: 'city',
				                title: '所在城市',
				                width: 80,
				                visible:false,
				                formatter: function(value, row, index)
				                {
				                    return  row.company.city;
				                }
				            }, {
				                field: 'county',
				                title: '所在区县',
				                width: 80,
				                visible:false,
				                formatter: function(value, row, index)
				                {
				                    return  row.company.county;
				                }
				            }, {
				                field: 'address',
				                title: '具体地址',
				                width: 180,
				                visible:false,
				                formatter: function(value, row, index)
				                {
				                    return  row.company.address;
				                }
				            }, {
				                field: 'website',
				                title: '公司网址',
				                width: 160,
				                visible:false,
				                formatter: function(value, row, index)
				                {
				                    return row.company.website;
				                }
				            }, {
				                field: 'introduction',
				                title: '公司简介',
				                width: 80,
				                visible:false,
				                formatter: function(value, row, index)
				                {
				                    return  row.company.introduction;
				                }
				            }, {
				                field: 'createName',
				                title: '创建人',
				                width: 80,
				                visible:false,
				                formatter: function(value, row, index)
				                {
				                    return  row.company.createName;
				                }
				            }, {
				                field: 'createTime',
				                title: '创建时间',
				                width: 80,
				                visible:false,
				                formatter: function(value, row, index)
				                {
				                    if(Helper.isNotEmpty(row.company.createTime)) { return new Date(row.company.createTime).format("yyyy-MM-dd"); }
				                }
				            }, {
				                field: 'updateName',
				                title: '修改人',
				                width: 60,
				                visible:false,
				                formatter: function(value, row, index)
				                {
				                    return  row.company.updateName;
				                }
				            }, {
				                field: 'updateTime',
				                title: '修改日期',
				                width: 80,
				                visible:false,
				                formatter: function(value, row, index)
				                {
				                    if(Helper.isNotEmpty(row.company.updateTime)) { return new Date(row.company.updateTime).format("yyyy-MM-dd"); }
				                }
				            }],
							onDblClickRow : function(row) {
								//双击选中事件	
								selectRow(row);
							},
							onPageChange : function(number, size) {
								if (size == 20) {
									setTimeout(
											function() {
												if ($('#bootTable tbody').find(
														'tr').length > 10) {
													$('.fixed-table-header')
															.css(
																	'margin-right',
																	'17px');
												}
											}, 1000);

								} else if (size == 50) {
									setTimeout(
											function() {
												if ($('#bootTable tbody').find(
														'tr').length > 10) {
													$('.fixed-table-header')
															.css(
																	'margin-right',
																	'17px');
												}
											}, 2000);

								}
							}

						});

	});

	function selectRow(row) {
		//console.log(row)
		if ($("#multiple").val().toBoolean()) {//多选,则双击事件无效
			parent.getCallInfo_customerArray([ row ]);
		} else {//单选，双击事件立即返回,处理返回index事件
			parent.getCallInfo_company(row);
			Helper.popup.close();
		}
	}
	//请求参数
	function queryParams(params) {
		params['companyName'] = $("#companyName").val();
		//	console.log(params)
		return params;
	}
	//ajax结果
	function responseHandler(res) {
		return {
			rows : res.result,
			total : res.count
		};
	}
</script>
</head>
<body>
	<input type="hidden" id="multiple" value="${multiple }">
	<div class="layer_container">
		<div class="cl layer_content">
			<!--表格容器左START-->
			<div class="layer_table_container" style="left: 0px; width: 888px;">
				<div class="cl layer_top">
					<div class="row-dd top_bar">
						<label class="form-label label_ui"></label>
						<span class="ui-combo-wrap" class="form_text">
							<input type="text" class="input-txt input-txt_9" id="companyName" placeholder="公司名称" name="companyName" />
						</span>
						<div class="layer_btns">
							<button class="nav_btn table_nav_btn search_btn" id="btn_search">
								<i class="fa fa-search"></i>
								查找
							</button>
							<button type="button" class="nav_btn table_nav_btn" id="btn_ok">确认</button>
							<button type="button" class="nav_btn table_nav_btn" id="btn_cancel">取消</button>
						</div>
					</div>
				</div>
				<!-- 表格 -->
				<div class="table-wrap" style="height: 390px;">
					<table class="layer_table" id="bootTable">
					</table>
				</div>
			</div>
			<!--表格容器左END-->
		</div>
	</div>
</body>
</html>
