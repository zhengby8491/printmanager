$(function()
    {
        //查询，刷新table
        $("#btn_search").click(function()
        {
            $("#bootTable").bootstrapTable("refreshOptions", {
                pageNumber: 1
            });
        });

        $("#bootTable").bootstrapTable({
            url: Helper.basePath + "/sys/agentquotient/ajaxList",
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
            cookieIdTable: "print_sys_notice",//必须制定唯一的表格cookieID

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
                   title: '创建时间',
                   width: 80,
                   formatter: function(value, row, index)
                   {
                       if(Helper.isNotEmpty(value)) { return new Date(value).format("yyyy-MM-dd  hh:mm:ss"); }
                   }
               	},
                 {
	                field: 'name',
	                title: '名称',
	                width: 150
            	},  {
                  field: 'photoUrl',
                  title: 'LOGO',
                  width: 150,
	               formatter: function(value, row, index){
	                   return '<a href='+value+' target=_blank>'+value+'</a>';
	               }
            	},
	            {
	               field: 'linkName',
	               title: '联系人',
	               width: 150
	         	},
            	{
                  field: 'telNum',
                  title: '联系电话',
                  width: 40
              	},
              	{
	                field: 'address',
	                title: '地址',
	                width: 60
            	}, {
	                field: 'area',
	                title: '区域',
	                width: 60
            	}, 
            	{
	                field: 'agentTypeText',
	                title: '代理商属性',
	                width: 60
            	}, 
               {
                field: 'operator',
                title: '操作',
                width: 80,
                formatter: function(value, row, index)
                {
					var operator = "";
					if(Helper.basic.hasPermission("sys:agentquotient:edit")){
						operator += '<a title="编辑" href="javascript:;" onclick="notice_edit('
						        + row.id
						        + ')" style="margin-right:20px"><i class="fa fa-pencil"></i></a>';
					}
					
					if(Helper.basic.hasPermission("sys:agentquotient:del")){
						operator += '<a title="删除" href="javascript:;" onclick="notice_del(this,'
						        + row.id
						        + ')" style="margin-right:20px"><i class="fa fa-trash-o"></i></a>';
					}
					return operator;
                }
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
    // 修改
    function notice_edit(id)
    {
      var url= Helper.basePath + '/sys/agentquotient/edit/' + id;
      Helper.popup.show("编辑材料商", url, "600", "400");
    }
    /*新增*/
  	function create() {
  	    var url = Helper.basePath + '/sys/agentquotient/create';
  	    Helper.popup.show("新增材料商", url, "600", "400");
  	}
    
  	/* 删除 */
  	function notice_del(obj, id) {
  		Helper.message.confirm('确认要删除吗？', function(index) {
  		  $.ajax({
     	                cache: true,
     	                type: "POST",
     	                url:Helper.basePath + '/sys/agentquotient/del/'+id,		              
     	                async: false,
     					dataType:"json", 
     	                error: function(request) {
     	                    layer.alert("Connection error");
     	                },
     	                success: function(data) {
     						if(data.success){
     							Helper.message.suc("已删除！")
     						    window.location.href=Helper.basePath + '/sys/agentquotient/list';
     						}else{
     						 	Helper.message.warn("删除失败！")
     						}
     	                }
     	            });
  		});
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
        params['name'] = $("#name").val();
        return params;
    }
    