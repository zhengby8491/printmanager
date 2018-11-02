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
					field : 'index',
					title : '序号',
					width : 40,
					formatter : function(value, row, index) {
						return index + 1;
					}
				}, 
                {
                field: 'name',
                title: '版本名称',
                width:100
                }, 
                {
                field: 'price',
                title: '销售价格',
                width: 100
            	}, 
            	{
                  field: 'originalPrice',
                  title: '版本原价',
                  width: 100
              	},
              	{
                    field: 'bonus',
                    title: '奖金',
                    width: 100
                },
              	{
                field: 'type',
                title: '服务类型',
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

            },{
                field: 'operator',
                title: '操作',
                width: 350,
                formatter: function(value, row, index)
                {
                    var operator = "";
                  
                    
                        operator += '&nbsp;&nbsp;&nbsp;<a title="编辑" href="javascript:;" onclick="product_edit(' + row.id + ')" style="padding: 0 4px; color: green">编辑</a>';
                        
                        operator += '<a title="删除" href="javascript:;" style="padding: 0 4px; color: green" onclick="notice_del(this,'
					        + row.id
					        + ')">删除</a>';
                        
					    operator += '<a title="权限分配" href="javascript:;" onclick="company_permissions(' + row.id + ')" style="padding: 0 4px; color: green">权限分配</a> &nbsp;&nbsp;&nbsp;';
                  
                        
                    return operator;
                }
            }],
            onDblClickRow: function(row)
            {
            	product_edit(row.id);
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
        $("#btn_batch_permission").on('click', function()
        {
        	Helper.popup.show('批量分配权限',Helper.basePath+'/sys/company/permissionsBatch','800','500',true);
        });
    });
    
	/* 新增 */
    function product_create() {
      var url = Helper.basePath + '/sys/buy/create';
	  Helper.popup.show("新增版本", url, "600", "400");
    }


    // 修改
    function product_edit(id)
    {
      var url = Helper.basePath + '/sys/buy/edit/' + id;
        Helper.popup.show("编辑版本", url, "600", "400");
    }
    /*菜单-删除*/
    function notice_del(obj,id){
        Helper.message.confirm('确认要删除吗？',function(index){
            //此处请求后台程序，下方是成功后的前台处理……
            $.post(Helper.basePath+'/sys/buy/del/'+id,function(result){
                if(result)
                {
                    $(obj).parents("tr").remove();
                    layer.msg('已删除!',{icon:1,time:1000});
                }
            });
        });
    }

    /* 权限分配 */
    function company_permissions(id)
    {
        Helper.popup.show('分配权限',Helper.basePath+'/sys/buy/editMenu/'+id,'800','500',true);
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

        return params;
    }
    
    function triggerClick(){
        $("#bootTable").bootstrapTable("refreshOptions", {
            pageNumber: 1
        });
    }
  	