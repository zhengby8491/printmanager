<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>

<script type="text/javascript" src="${ctxStatic}/site/sys/company/permissions_batch.js?v=${v }"></script>
<title>公司权限</title>

<script type="text/javascript">
    $(function()
    {
        //查询，刷新table
        $("#btn_search").click(function()
        {
            $("#bootTable").bootstrapTable("refreshOptions", {
                pageNumber: 1
            });
        });

        //订单详情table
        $("#bootTable").bootstrapTable({
            url: Helper.basePath + "/sys/company/ajaxList",
            method: "post",
            contentType: 'application/json', //设置请求头信息  
            dataType: "json",
            pagination: false, // 是否显示分页（*）
            sidePagination: 'server',//设置为服务器端分页
            queryParamsType: "",
            queryParams: queryParams,//参数
            responseHandler: responseHandler,

            //resizable : true, //是否启用列拖动
            showColumns: true, //是否显示所有的列
            minimumCountColumns: 2, //最少允许的列数
            striped: true, // 是否显示行间隔色
            cache: false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            sortable: false, // 是否启用排序
            clickToSelect: false, // 是否启用点击选中行
            height: 430, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            cardView: false, // 是否显示详细视图
            detailView: false, // 是否显示父子表

            showExport: false,//是否显示导出按钮
            //exportDataType: 'all',
            exportTypes: ['csv', 'txt', 'excel', 'doc'],

            cookie: true,//是否启用COOKIE
            cookiesEnabled: ['bs.table.columns'],//默认会记住很多属性，这里控制只记住列选择属性
            cookieIdTable: "print_sys_company",//必须制定唯一的表格cookieID

            uniqueId: "id",//定义列表唯一键
            columns: [
                {
                field: 'id',
                checkbox: true,
                width:100,
                align: 'center',
                valign: 'middle'
            	}, {
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
            onDblClickRow: function(row)
            {
                company_view(row.company.id);
            },
            onClickRow: function(row, $element)
            {
                $element.addClass("tr_active").siblings().removeClass("tr_active");
            }
        });
    });
    
    function responseHandler(res)
    {
        return {
            rows: res.result,
            total: res.count
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

<script type="text/javascript">
    $(function()
    {
        $("#btn_save").click(function()
        {
            $("#btn_save").attr("disabled","disabled");
            var companys = $("#bootTable").bootstrapTable('getSelections');
            if(companys.length === 0) 
            {
            	Helper.message.view('请至少选择一个公司');
            	$("#btn_save").removeAttr("disabled");
            	return false;
            }
            var ids = [];
            for(var i = 0; i < companys.length; i++) 
            {
            	ids.push(companys[i]['company']['id']);
            }
            var menuIdList = $(".table_cont table input[type='checkbox']:checked").map(function(){ return $(this).val();}).get();
            var uuid = $("#uid").data("uid") + "-" + new Date().getTime();
            Helper.post(Helper.basePath + '/sys/company/permissions_batch_save',
            	{"key": uuid, "ids":ids, "menuIds":menuIdList},
            	function(data) {
	    			if(data.success){
	    				parent.location.href = Helper.basePath + "/sys/company/list";
	    			}else
	    			{
	    				Helper.message.warn('操作失败：' + data.message);
	                    $("#btn_save").removeAttr("disabled");
	    			}
	    		},
	    		function(error){}, true);	
            
            // 实时远程获取数据
            saveProcessing(uuid, ids.length);
        });
    });
    
    function saveProcessing(uuid, total) 
    {
    	var str = "正在处理中（_cur_/" +total+ "）"
    	setInterval(function(){
    		Helper.post(Helper.basePath + '/public/permissions_batch_process',
            		{"key": uuid},function(data) {
    			$("#submitmsg").html(str.replace("_cur_", data));
    		});	
    	}, 1000);
    }
</script>
<style type="text/css">
.panel_set {
	border: 1px outset #AAAAAA;
	padding: 15px 5px;
}
</style>
</head>
<body>
	<div class="page-container">
		<div class="page-border">

			<div class="cl">
				<div class="row-div">
					<label style="display: none;" id="uid" data-uid="${loginUser.id}"></label>
					<label class="form-label label_ui label_2"></label>
					<input class="nav_btn table_nav_btn" type="button" id="btn_save" value="提交&nbsp;&nbsp;">
					<span id="submitmsg" style="color: red; font-size: 18px;"></span>
				</div>
			</div>

			<div class="cl panel_set">
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
					<div class="search_container">
						<!--表格-->
						<div>
							<div class="boot-mar">
								<table class="border-table" coldrag="false" id="bootTable">
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div id="node" class="table_cont panel_set" style="margin-top: 30px;">
				<input type="checkbox" id="checkbox_all" />
				<label for="checkbox_all" class="c-red">全选</label>
				<c:forEach items="${allMenuList}" var="node1">
					<table class="border-table first_table" coldrag="false" style="margin-bottom: 20px">
						<tr>
							<td rowspan="${fn:length(node1.childrens)+1}" width="100" class="node1">
								<span>
									<input type="checkbox" value="${node1.id }" id="${ node1.id }" <c:if test="${not empty hasMenuIdMap[node1.id]}"> checked</c:if> />
									<label for="${ node1.id }">${node1.name }</label>
								</span>
							</td>
						</tr>
						<c:forEach items="${node1.childrens}" var="node2">
							<tr>
								<td>
									<table class="second_table" coldrag="false" rules="all">
										<tr>
											<td rowspan="${fn:length(node2.childrens)+1}" width="120" class="node2">
												<span>
													<input type="checkbox" value="${node2.id }" id="${ node2.id }" <c:if test="${not empty hasMenuIdMap[node2.id]}"> checked</c:if> />
													<label for="${ node2.id }">${node2.name }</label>
												</span>
											</td>
										</tr>
										<c:forEach items="${node2.childrens}" var="node3">
											<tr>
												<td width="160" align="left" class="node3">
													<span>
														<input type="checkbox" value="${node3.id }" id="${ node3.id }" <c:if test="${not empty hasMenuIdMap[node3.id]}"> checked</c:if> />
														<label for="${node3.id }">${node3.name }</label>
													</span>
												</td>
												<td align="left" class="node4">
													<c:forEach items="${node3.childrens}" var="node4">
														<!-- 如果还有childrens，则需要新开table并设置rowspan -->
														<c:if test="${not empty node4.childrens }">
															<table class="third_table" coldrag="false" rules="all" style="border: 1px solid #ddd;">
																<tr>
																	<td rowspan="2" width="120" class="node4">
																		<span>
																			<input type="checkbox" value="${node4.id }" id="${ node4.id }" <c:if test="${not empty hasMenuIdMap[node4.id]}"> checked</c:if> />
																			<label for="${ node4.id }">${node4.name }</label>
																		</span>
																	</td>
																</tr>
																<c:if test="${not empty node4.childrens }">
																	<tr>
																		<td align="left" class="node5">
																			<c:forEach items="${node4.childrens}" var="node5">
																				<span>
																					<input type="checkbox" value="${node5.id }" id="${ node5.id }" <c:if test="${not empty hasMenuIdMap[node5.id]}"> checked</c:if> />
																					<label for="${node5.id }">${node5.name }</label>
																				</span>
																			</c:forEach>
																		</td>
																	</tr>
																</c:if>
															</table>
														</c:if>
														<!-- 不在有子节点直接显示 -->
														<c:if test="${empty node4.childrens }">
															<span>
																<input type="checkbox" value="${node4.id }" id="${ node4.id }" <c:if test="${not empty hasMenuIdMap[node4.id]}"> checked</c:if> />
																<label for="${ node4.id }">${node4.name }</label>
															</span>
														</c:if>
													</c:forEach>
												</td>
											</tr>
										</c:forEach>
									</table>
								</td>
							</tr>
						</c:forEach>
					</table>
				</c:forEach>

			</div>
		</div>
	</div>
</body>
</html>