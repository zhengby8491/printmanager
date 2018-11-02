$(function()
        {
            //查询，刷新table
            $("#btn_search").click(function()
            {
                $("#bootTable").bootstrapTable("refreshOptions", {
                    pageNumber: 1
                });
            });

            //系统公告table
            $("#sysMessage").bootstrapTable({
                url: Helper.basePath + "/sys/notice/ajaxPublisList",
                method: "post",
                contentType: 'application/json', //设置请求头信息  
                dataType: "json",
                pagination: true, // 是否显示分页（*）
                sidePagination: 'server',//设置为服务器端分页
                pageList: [10, 20, 50],
                queryParamsType: "",
                pageSize: 10,
                pageNumber: 1,
                //queryParams: queryParams,//参数
                responseHandler: responseHandler,

                //resizable : true, //是否启用列拖动
                showColumns: false, //是否显示所有的列
                minimumCountColumns: 2, //最少允许的列数
                striped: true, // 是否显示行间隔色
                cache: false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
                sortable: false, // 是否启用排序
                clickToSelect: true, // 是否启用点击选中行
                height: 400,	 // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
                cardView: false, // 是否显示详细视图
                detailView: false, // 是否显示父子表

                cookie: true,//是否启用COOKIE
                cookiesEnabled: ['bs.table.columns'],//默认会记住很多属性，这里控制只记住列选择属性
                cookieIdTable: "print_sys_notice",//必须制定唯一的表格cookieID

                uniqueId: "id",//定义列表唯一键
                columns: [
					{
					    field: 'space',
					    title: '',
					    width: 20,
					    align: "left",
					    formatter : function(value, row, index) {
							return ""
						}
					},
                     {
    	                field: 'title',
    	                title: '标题',
    	                width: 300,
    	                align: "left",
    	                'class': "t_active",
    	                cellStyle: function(value,row,index,field){
    	                    return {css:{"margin-left":"20px"}}
    	                },
						formatter: function(value, row, index)
			            {
			              //return "<a href='javascript:;' onclick=view("+row.id+")>"+value+"</a>"
			              return '<span name="title" class="t_active">'+value+'<input type="hidden" value="'+row.id+'"/></span>';
			            }
                	},
                  	{
    	                field: 'userName',
    	                title: '发布人',
    	                width: 60
                	},
                	{
                    field: 'noticeTime',
                    title: '发布时间',
                    width: 80,
                    formatter: function(value, row, index)
                    {
                        if(Helper.isNotEmpty(value)) { return new Date(value).format("yyyy-MM-dd hh:mm:ss"); }
                    }
                	}],
                onClickRow: function(row, $element)
                {
                  $element.addClass("tr_active").siblings().removeClass("tr_active");
                }
            });
            $(document).on("click","span[name='title']",function(){
              view($(this).find("input").eq(0).val());
          	});
    });
    
function responseHandler(res)
{
    return {
        rows: res.result,
        total: res.count
    };
}

function view(id)
{
    var url = Helper.basePath + "/sys/notice/view/" + id;
    var title = "服务支持";
	admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
}