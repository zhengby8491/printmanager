<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxHYUI}/plugins/ueditor/dialogs/internal.js"></script>
<title>自定义报表-选择数据源</title>
</head>
<body>
	<!-- 表格 -->
	<div class="table-wrap" style="height: 390px;">
		<table class="layer_table" id="bootTable">

		</table>
	</div>
	<script type="text/javascript">
        $(function()
        {
            $("#bootTable").bootstrapTable({
                url: Helper.basePath + "/template/data/ajaxList",
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
                striped: true, // 是否显示行间隔色
                cache: false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
                sortable: false, // 是否启用排序
                clickToSelect: true, // 是否启用点击选中行
                height: 360, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
                cardView: false, // 是否显示详细视图
                detailView: false, // 是否显示父子表
                checkbox: false,//开启多选
                uniqueId: "id",//定义列表唯一键
                columns: [{
                    field: 'name',
                    title: '属性名称',
                    width: 80
                }, {
                    field: 'code',
                    title: '属性值',
                    width: 80
                }],
                onDblClickRow: function(row)
                {
                    selectRow(row);
                }
            });
           
        });
        //请求参数
        function queryParams(params)
        {
            params['billType'] = "${params.billType}";
            return params;
        }
        function selectRow(row)
        {
            //console.log(row)
            editor.execCommand('insertHtml',  row.code);
            dialog.close(true);
        }
        //ajax结果
        function responseHandler(res)
        {
            return {
                rows: res.result,
                total: res.count
            };
        }
    </script>
</body>

</html>