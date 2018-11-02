<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>

<title>销售送货单表</title>
<script type="text/javascript">
    $(function()
    {
        /* 选择客户 */
        $("#customer_quick_select").click(function()
        {
            Helper.popup.show('选择客户信息', Helper.basePath + '/quick/customer_select?multiple=false', '900', '490');
        });
        /* 查询审核状态 */
        $("input[name='auditflag']").change(function()
        {
            $("#bootTable").bootstrapTable("refreshOptions", {
                pageNumber: 1
            });
        });
        //查询，刷新table
        $("#btn_search").click(function()
        {
            $("#bootTable").bootstrapTable("refreshOptions", {
                pageNumber: 1
            });
        });
        /* 查询审核状态 */
   		$("input[name='auditFlag']").change(function() {
   			$("#bootTable").bootstrapTable("refresh");
   		});

        //订单详情table
        $("#bootTable").bootstrapTable({
            url: Helper.basePath + "/sale/deliver/ajaxList",
            method: "post",
            contentType: 'application/json', //设置请求头信息  
            dataType: "json",
            pagination: true, // 是否显示分页（*）
            sidePagination: 'server',//设置为服务器端分页
            pageList: [10, 20, 50],
            queryParamsType: "",
            pageSize: 10,
            pageNumber: 1,
            queryParams: queryParams,//参数
            responseHandler: responseHandler,

            //resizable: true, //是否启用列拖动
            showColumns: true, //是否显示所有的列
            minimumCountColumns: 2, //最少允许的列数
            striped: true, // 是否显示行间隔色
            cache: false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            sortable: false, // 是否启用排序
            clickToSelect: true, // 是否启用点击选中行
            height: 430, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            cardView: false, // 是否显示详细视图
            detailView: false, // 是否显示父子表

            showExport: true,//是否显示导出按钮
            //exportDataType: 'all',
            exportTypes: ['csv', 'txt', 'excel', 'doc'],

            cookie: true,//是否启用COOKIE
            cookiesEnabled: ['bs.table.columns'],//默认会记住很多属性，这里控制只记住列选择属性
            cookieIdTable: "print_sale_deliver_master",//必须制定唯一的表格cookieID

            uniqueId: "id",//定义列表唯一键
            columns: [{
                field: 'index',
                title: '序号',
                width: 40,
                formatter: function(value, row, index)
                {
                    //console.log($(this).pageSixze)
                    return index + 1;
                }
            }, {
                field: 'isCheck',
                title: '单据状态',
                width: 70,
                formatter: function(value, row, index)
                {
                    return value == 'NO' ? '未审核' : '已审核';
                },
                cellStyle : function(value,row,index,field){
				    if(value == '未审核'){
				        return {css:{"color":"#f00"}};   
				    }
				    else{
				        return {css:{"color":"#080"}};   
				    } 
				}
            }, {
                field: 'billTypeText',
                title: '单据类型',
                width: 80
            }, {
                field: 'deliveryTime',
                title: '交货日期',
                width: 80,
                formatter: function(value, row, index)
                {
                    if(value) { return new Date(value).format("yyyy-MM-dd"); }
                }
            }, {
                field: 'billNo',
                title: '送货单号',
                width: 140
            },

            {
                field: 'customerName',
                title: '客户名称',
                width: 180
            }, {
                field: 'employeeName',
                title: '销售员',
                width: 80
            }, {
                field: 'linkName',
                title: '联系人',
                width: 80
            }, {
                field: 'mobile',
                title: '联系人电话',
                width: 100
            }, {
                field: 'totalMoney',
                title: '金额',
                width: 100
            }, {
                field: 'totalTax',
                title: '税额',
                width: 80
            }, {
                field: 'noTaxTotalMoney',
                title: '不含税金额',
                width: 120
            }, {
                field: 'createName',
                title: '制单人',
                width: 80
            }, {
                field: 'createTime',
                title: '制单时间',
                width: 80,
                formatter: function(value, row, index)
                {
                    if(value) { return new Date(value).format("yyyy-MM-dd"); }
                }
            }, {
                field: 'checkUserName',
                title: '审核人',
                width: 80
            }, {
                field: 'checkTime',
                title: '审核时间',
                width: 80,
                formatter: function(value, row, index)
                {
                    if(value) { return new Date(value).format("yyyy-MM-dd"); }
                }
            }, {
                field: 'memo',
                title: '备注',
                width: 120
            }, {
                field: 'operator',
                title: '操作',
                width: 120,
                formatter: function(value, row, index)
                {
                    var operator = '<span class="table_operator">';
				 	if(Helper.basic.hasPermission('sale:deliver:view')){
					    operator += '<a title="查看" href="javascript:;" onclick="deliver_view('
						+ row.id
						+ ')" style="padding: 0 8px; color: green"><i class="fa fa-search"></i></a>';}
                    if(Helper.isNotEmpty(row.isCheck) && row.isCheck == 'NO') {
                        if(Helper.basic.hasPermission('sale:deliver:edit')){
						  operator += '<a title="编辑" href="javascript:;" onclick="deliver_edit('
								+ row.id
								+ ')" style="padding: 0 8px; color: green"><i class="fa fa-pencil"></i></a>';	}	
                        if(Helper.basic.hasPermission('sale:deliver:del')){
						  operator += '<a title="删除" href="javascript:;" onclick="deliver_del(this,'
								+ row.id
								+ ')" style="padding: 0 8px; color: red"><i class="fa fa-trash-o"></i></a>';}
                    }
                    operator+='</span>';
                    return operator;
                }
            }],
            onDblClickRow: function(row)
            {
              	deliver_view(row.id)
            },
            onClickRow : function(row,$element){
			    $element.addClass("tr_active").siblings().removeClass("tr_active");
			}
        });
        
        $("#bootTable").on('load-success.bs.table column-switch.bs.table page-change.bs.table search.bs.table', function () {
  	        /* 表格工具栏 */
  	        if($(".glyphicon-th").next().html() == ''){
  	            $(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
  	  	  	    $(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");  
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

        params['dateMin'] = $("#dateMin").val();
        params['dateMax'] = $("#dateMax").val();
        params['billNo'] = $("#billNo").val();
        params['customerName'] = $("#customerName").val();
        params['billNo'] = $("#billNo").val();
        params['auditFlag'] = $("input[name='auditFlag']:checked").val()=="-1"?null:$("input[name='auditFlag']:checked").val();
        //console.log(params)
        return params;
    }
    // 获取客户返回信息
    function getCallInfo_customer(obj)
    {
        $("#customerName").val(obj.name);
    }
    /* 编辑 */
    function deliver_edit(id)
    {
        var url = Helper.basePath + '/sale/deliver/edit/' + id;
        var title = "销售送货";
        admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
    }
  	//查看
  	function deliver_view(id){
  	  	var url = Helper.basePath + '/sale/deliver/view/' + id;
  		var title = "销售送货";
  		admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
  	}

    /* 删除 */
    function deliver_del(obj, id)
    {
        Helper.message.confirm('确认要删除吗？', function(index)
        {
            Helper.post(Helper.basePath + '/sale/deliver/del/' + id, function(data)
            {
                if(data.success) {
                    $("#bootTable").bootstrapTable("removeByUniqueId", id);
                    $("#bootTable").bootstrapTable("refresh");
                    Helper.message.suc('已删除!');
                }else {
                	  Helper.message.warn('删除失败：' + data.message);
                }
            });
        });
    }
</script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--头部START-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="销售管理-销售送货列表"></sys:nav>
				</div>
			</div>
			<!--头部END-->
			<!--查询表单START-->
			<div class="cl">
				<div class="cl order-info">
					<dl class="cl row-dl">
						<dd class="row-dd">
							<sys:dateConfine label="单据日期" />
						</dd>

						<dd class="row-dd">
							<label class="form-label label_ui label_1">客户名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_7" id="customerName" name="customerName" />
								<div class="select-btn" id="customer_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">送货单号：</label>
							<span class="ui-combo-wrap">
								<input type="text" class="input-txt input-txt_9" name="billNo" id="billNo" />
							</span>
						</dd>
						<dd class="row-dd">
							<button type="button" class="nav_btn table_nav_btn search_btn" id="btn_search">
								<i class="fa fa-search"></i>
								查询
							</button>
						</dd>
					</dl>
				</div>
				<div>
					<div class="radio-box">
						<label>
							<input type="radio" value="-1" name="auditFlag" checked="checked" />
							全部
						</label>
						<label>
							<input type="radio" value="YES" name="auditFlag" />
							已审核
						</label>
						<label>
							<input type="radio" value="NO" name="auditFlag" />
							未审核
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