$(function()
    {
        /* 查询审核状态 */
        $("input[name='auditFlag']").change(function()
        {
            $("#detailList").bootstrapTable("refreshOptions", {
                pageNumber: 1
            });
        });
        //查询，刷新table
        $("#btn_search").click(function()
        {
            $("#detailList").bootstrapTable("refreshOptions", {
                pageNumber: 1
            });
        });

        $("#detailList").bootstrapTable({
            url: Helper.basePath + "/sys/comment/listDetail",
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

            // 				resizable : true, //是否启用列拖动
            showColumns: false, //是否显示所有的列
            minimumCountColumns: 2, //最少允许的列数
            striped: true, // 是否显示行间隔色
            cache: false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            sortable: false, // 是否启用排序
            clickToSelect: true, // 是否启用点击选中行
            height: 400, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            cardView: false, // 是否显示详细视图
            detailView: false, // 是否显示父子表		
            uniqueId: "id",//定义列表唯一键
            
            columns: [{
                field: 'index',
                title: '序号',
                width: 40,
                formatter: function(value, row, index)
                {
                    return index + 1;
                }
            }, {
                field: 'title',
                title: '标题',
                width: 200
            },{
                field: 'userName',
                title: '留言人',
                width: 70
            }, {
                field: 'ip',
                title: '留言人IP',
                width: 70
            },{
                field: 'contact',
                title: '联系方式',
                width: 70
            },{
                field: 'replyState',
                title: '状态',
                width: 70,
                formatter: function(value, row, index)
                {
                    if(value == 'YES') {
                        return '已回复';
                    }else {
                        return '未回复';
                    }
                },
                cellStyle: function(value,row,index,field){
                    if(value == '未回复'){
				        return {css:{"color":"#f00"}};   
				    }
                    else{
				        return {css:{"color":"#080"}};   
				    } 
                }

            }, {
                field: 'createTime',
                title: '创建时间',
                width: 100,
                formatter: function(value, row, index)
                {
                    return new Date(value).format("yyyy-MM-dd");
                }

            }, {
                field: 'updateTime',
                title: '最后回复',
                width: 100,
                formatter: function(value, row, index)
                {
                    if(value != null) {
                        return new Date(value).format("yyyy-MM-dd");
                    }else {
                        return "-";
                    }

                }

            }

            ],
            onDblClickRow: function(row)
            {
                detail(row.id)
            },
            onLoadError: function()
            {
                // alert("数据加载异常");
            },
            onClickRow: function(row, $element)
            {
                $element.addClass("tr_active").siblings().removeClass("tr_active");
            }

        });

    })

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
  		params['udateMin'] = $("#udateMin").val();
  		params['udateMax'] = $("#udateMax").val();
  		params['title'] = $("#title").val().trim();//
  		params['companyLinkName'] = $("#userName").val().trim();
  		params['companyTel'] = $("#contact").val().trim();
  		params['auditFlag'] = $("input[name='auditFlag']:checked").val()=="-1"?null:$("input[name='auditFlag']:checked").val();
        return params;
    }

    function detail(id)
    {
        var url=Helper.basePath + "/sys/comment/ask_detail/"+id;
	    var title="留言版";
	    admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"))
    }