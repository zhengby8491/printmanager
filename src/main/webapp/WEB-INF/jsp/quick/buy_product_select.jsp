<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>产品管理</title>
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
            url: Helper.basePath + "/sys/buy/ajaxList",
            method: "post",
            contentType: 'application/json', //设置请求头信息  
            dataType: "json",
            pagination: true, // 是否显示分页（*）
            sidePagination: 'server',//设置为服务器端分页
            pageList: Helper.bootPageList,
            queryParamsType: "",
            pageSize: 10,
            pageNumber: 1,
            queryParams: queryParams,//参数
            responseHandler: responseHandler,

            //resizable : true, //是否启用列拖动
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
            cookieIdTable: "print_sys_product",//必须制定唯一的表格cookieID

            uniqueId: "id",//定义列表唯一键
            columns: [
                {
                field: 'name',
                title: '购买版本',
                width:100
                }, {
                field: 'price',
                title: '销售价',
                width: 100
            	}, {
                  field: 'originalPrice',
                  title: '原价',
                  width: 100
              	},{
                field: 'type',
                title: '类型',
                width: 100,
                formatter: function(value, row, index)
                {
                    if(value == 1) { 
                      return "购买"; 
                    }
                    
                    if(value == 2) { 
                        return "维护"; 
                    }
                }

            }],
            onDblClickRow: function(row)
            {
            		if('${type}'){
            			Helper.message.confirm('你将升级版本为'+row.name+',确认要升级吗？', function(index) {
                    		product_edit(row);
                        });
            		}else{
            			product_edit(row);
            		}
            },
            onClickRow: function(row, $element)
            {
                $element.addClass("tr_active").siblings().removeClass("tr_active");
            }
        });
        $("#bootTable").on('load-success.bs.table column-switch.bs.table page-change.bs.table search.bs.table', function()
        {
            /* 表格工具栏 */
            if($(".glyphicon-th").next().html() == '') {
                $(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
            
                $(".export.btn-group").remove();
                  
                $(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
            }
        });
    });
    
    // 修改
    function product_edit(row)
    {
    	parent.getCallInfo_buyOrder(row,'${param.orderId}');
		Helper.popup.close();
    }

    function responseHandler(res)
    {
        return {
            rows: res.result,
            total: res.count
        };
    }
    function queryParams(params)
    {
    	if('${type}'){
    		params['buyType']=1;
    	}
        return params;
    }
    
    function triggerClick(){
        $("#bootTable").bootstrapTable("refreshOptions", {
            pageNumber: 1
        });
    }
  	

</script>

</head>
<body>
	<div class="page-container">
		<div class="page-border" style="overflow-y: hidden;">
			<!--顶部导航-->
			<div class="cl topPath"></div>
			<!--搜索栏-->
			<div class="cl">
				<div class="cl order-info" style="width: 1140px">
					<dl class="cl row-dl">

					</dl>

				</div>
				<div>

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