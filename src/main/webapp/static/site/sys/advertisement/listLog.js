$(function()
    {
        //查询，刷新table
        $("#btn_search").click(function()
        {
            $("#bootTable").bootstrapTable("refreshOptions", {
                pageNumber: 1
            });
        });
        /* 返回显示列表 */
    	$("#btn_back").on("click",function(){
    		var url=Helper.basePath + '/sys/advertisement/list';
    		var title="轮播广告";
    		admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
    	});
        //系统公告table
        $("#bootTable").bootstrapTable({
            url: Helper.basePath + "/sys/advertisement/ajaxAccesslogList",
            method: "post",
            contentType: 'application/json', //设置请求头信息  
            dataType: "json",
            pagination: true, // 是否显示分页（*）
            sidePagination: 'server',//设置为服务器端分页
            pageList: [100, 200, 500],
            queryParamsType: "",
            pageSize: 100,
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
            cookieIdTable: "print_sys_notice_log",//必须制定唯一的表格cookieID

            uniqueId: "id",//定义列表唯一键
            columns: [
                 {
                   field: 'index',
                   title: '序号',
                   width: 40,
                   formatter: function(value, row, index)
                   {
                     	return index+1;
                   }
                 },
                 {
                   field: 'createTime',
                   title: '访问时间',
                   width: 80,
                   formatter: function(value, row, index)
                   {
                       if(Helper.isNotEmpty(value)) { return new Date(value).format("yyyy-MM-dd hh:mm:ss"); }
                   }
               	}, 
            	{
                  field: 'iip',
                  title: '内网IP',
                  width: 40
              	}, 
            	{
                  field: 'ip',
                  title: '外网IP',
                  width: 40
              	},
              	{
                  field: 'address',
                  title: '访问区域',
                  width: 80 
              	},
              	{
	                field: 'userName',
	                title: '访问用户名',
	                width: 60
            	}],
            onDblClickRow: function(row)
            {
              notice_edit(row.id);
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
                if(!Helper.basic.hasPermission('sys:systemnotice:export')){
                 	 $(".export.btn-group").remove();
                }
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
        params['id'] = $("#paramsId").val();
        params['dateMin'] = $("#dateMin").val();
        params['dateMax'] = $("#dateMax").val();
        return params;
    }
    