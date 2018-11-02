<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxHYUI}/plugins/ueditor/dialogs/internal.js"></script>
<title>自定义报表-预览</title>
<style type="text/css">
.wrap {
	padding: 5px;
	font-size: 14px;
}

.left {
	position: relative;
	width: 950px;
}

.right {
	position: fixed;
	top: 0;
	right: 0;
	width: 160px;
	border: 1px solid #ccc;
	float: right;
	padding: 5px;
	margin-right: 5px;
}

.right .pre {
	height: 332px;
	overflow-y: auto;
}

.right .preitem {
	border: white 1px solid;
	margin: 5px 0;
	padding: 2px 4px;
	text-align: center;
	white-space: nowrap;
	text-overflow: ellipsis;
	overflow: hidden;
}

.right .preitem:hover {
	background-color: #e2e1d3;
	cursor: pointer;
	border: #ccc 1px solid;
	border-radius: 15px
}

.right .preitem img {
	display: block;
	margin: 0 auto;
	width: 100px;
}

.clear {
	clear: both;
}

.select_head {
	border-bottom: 0;
	text-align: center;
	width: 70px;
}

.top {
	height: 26px;
	line-height: 26px;
	padding: 5px;
}

.bottom {
	min-height: 320px;
	width: 100%;
	margin: 0 auto;
}

.transparent {
	background: url("template/images/bg.gif") repeat;
}

#colorPicker {
	width: 17px;
	height: 17px;
	border: 1px solid #CCC;
	display: inline-block;
	border-radius: 3px;
	box-shadow: 2px 2px 5px #D3D6DA;
}

.border_style1 {
	padding: 2px;
	border: 1px solid #ccc;
	border-radius: 5px;
	box-shadow: 2px 2px 5px #d3d6da;
}

p {
	margin: 5px 0
}

table {
	clear: both;
	margin-bottom: 10px;
	border-collapse: collapse;
	word-break: break-all;
}

li {
	clear: both
}

ol {
	padding-left: 40px;
}
</style>
</head>
<body>
	<div class="wrap">
		<div class="left">
			<div class="top">
				<label>
					<var id="lang_template_clear">保留原有内容</var>
					：
					<input id="issave" type="checkbox">
				</label>
			</div>
			<div class="bottom border_style1" id="preview"></div>
		</div>
		<fieldset class="right border_style1">
			<legend class="select_head">
				<var id="lang_template_select"></var>
			</legend>
			<div class="pre" id="preitem"></div>
		</fieldset>
		<div class="clear"></div>
	</div>
	<script type="text/javascript">
/**
* Templates.
* 添加模板，以下面配置即可
*/
var templates = [
];

/**
 *  Templates.
 */
(function () {
    $.ajax({
        type: "POST",
        url: Helper.basePath + '/template/listByAdmin',
        data:{billType:'${params.billType}'},
        async:false,
        dataType: "json",
        success: function (data){
            if(data.success){
                 $.each(data.obj,function (index,item){
                     var templateItem={};
                     templateItem.id=item.id;
                     templateItem.title=item.title;
                     templateItem.pre='pre1.png';
                     templateItem.preHtml=HTMLDecode(item.context||'');
                     templateItem.html=HTMLDecode(item.context||'');
                     templates.push(templateItem);
                 }); 
            }
        },
        error:function (data){
            
        },
        beforeSend:function()
        {
            layer.load(1);
        },
        complete:function()
        {
            layer.closeAll('loading');
        }
    });
    var me = editor,
            preview = $G( "preview" ),
            preitem = $G( "preitem" ),
            tmps = templates,
            currentTmp;
    var initPre = function () {
        var str = "";
        for ( var i = 0, tmp; tmp = tmps[i++]; ) {
            str += '<div title="'+tmp.title+'" class="preitem" onclick="pre(' + i + ')">'+tmp.title+'</div>';
        }
        
        preitem.innerHTML = str;
    };
    var pre = function ( n ) {
        var tmp = tmps[n - 1];
        currentTmp = tmp;
        clearItem();
        domUtils.setStyles( preitem.childNodes[n - 1], {
        	"border-radius": "15px",
        	"border": "1px solid rgb(204, 204, 204)",
        	"background-color": "#cacaca"
        } );
        preview.innerHTML = tmp.preHtml ? tmp.preHtml : "";
    };
    var clearItem = function () {
        var items = preitem.children;
        for ( var i = 0, item; item = items[i++]; ) {
            domUtils.setStyles( item, {
                "background-color":"",
                "border":"white 1px solid"
            } );
        }
    };
    dialog.onok = function () {
        if ( !$G( "issave" ).checked ){
            me.execCommand( "cleardoc" );
        }
        var obj = {
            html:currentTmp && currentTmp.html
        };
        //console.log(obj);
        me.execCommand( "template", obj );
        $("#title",parent.document).val(currentTmp.title);
    };
    initPre();
    window.pre = pre;
    pre(2)

})();
    </script>
</body>

</html>