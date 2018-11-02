 $(function()
    {
        $("#serviceTable").bootstrapTable({
            url: Helper.basePath + "/sys/comment/myListDetail",
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
            //resizable : true, //是否启用列拖动
            showColumns: false, //是否显示所有的列
            minimumCountColumns: 2, //最少允许的列数
            striped: true, // 是否显示行间隔色
            cache: false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            clickToSelect: true, // 是否启用点击选中行
            height: 400, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            cardView: false, // 是否显示详细视图
            detailView: false, // 是否显示父子表

            uniqueId: "id",//定义列表唯一键
            columns: [{
                field: 'space',
                title: '',
                width: 25,
                formatter: function(value, row, index)
                {
                    return ""
                }
            }, {
                field: 'title',
                title: '标题',
                width: 300,
                align: "left",
                formatter: function(value, row, index){
                    return '<span name="title" class="t_active">'+value+'<input type="hidden" value="'+row.id+'"/></span>';
                }
            }, {
                field: 'replyState',
                title: '状态',
                width: 100,
                formatter: function(value, row, index)
                {
                    if(value == 'YES') {
                        return '已回复';
                    }else {
                        return '未回复';
                    }
                },

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
                    if(value) {
                        return new Date(value).format("yyyy-MM-dd");
                    }else {
                        return "-";
                    }

                }

            }

            ],
            onClickRow: function(row)
            {
            },
            onLoadSuccess: function(data)
            {
                // console.log(data)
            }

        });
        
        $(document).on("click","span[name='title']",function(){
            detail($(this).find("input").eq(0).val());
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

        return params;
    }
	
    function detail(id)
    {
        var url = Helper.basePath + "/sys/comment/question_detail/" + id;
        var title = "服务支持";
        admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"))
    }