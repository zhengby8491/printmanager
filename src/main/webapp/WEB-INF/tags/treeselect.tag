<%@ tag language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<%@ attribute name="id" type="java.lang.String" required="false" description="编号"%>
<%@ attribute name="name" type="java.lang.String" required="true" description="名称"%>
<%@ attribute name="value" type="java.lang.String" required="false" description="值"%>
<%@ attribute name="showText" type="java.lang.String" required="false" description="显示值"%>
<%@ attribute name="url" type="java.lang.String" required="true" description="树结构数据地址"%>
<%@ attribute name="notAllowSelectRoot" type="java.lang.Boolean" required="false" description="不允许选择根节点"%>
<%@ attribute name="multiple" type="java.lang.Boolean" required="false" description="是否多选"%>
<%@ attribute name="cssStyle" type="java.lang.String" required="false" description="树结构数据地址"%>
<div class="content_wrap">
	<div class="zTreeDemoBackground left">
		<ul class="list">
			<li class="title">
				&nbsp;&nbsp;
				<input type="text" onclick="showMenu()" class="${cssStyle }" id="${id}_text" name="${name }_text" value="${showText }" readonly="readonly" />
				<input type="hidden" name="${name }" id="${id }" value="${value }" />
				<a id="selectTreeBtn" href="javascript:;">选择</a>
			</li>

		</ul>
	</div>


	<div id="treeContent" class="treeContent">
		<ul id="tree_ul" class="ztree"></ul>
	</div>
	<style>
<!--
div.content_wrap {
	height: auto;
}

div.content_wrap div.left {
	float: left
}

div.content_wrap div.right {
	float: right;
	width: 340px;
}

div.zTreeDemoBackground {
	height: auto;
	text-align: left;
}

div.zTreeDemoBackground ul li input {
	float: left;
	width: 180px;
	height: 22px;
	line-height: 20px;
	border: 1px solid #aaa
}

div.zTreeDemoBackground ul li a {
	line-height: 25px;
}

div.treeContent {
	top: 24px;
	z-index: 99;
	left: 1px;
	display: none;
	position: absolute;
	z-index: 9999999
}

ul.ztree {
	border: 1px solid #617775;
	background: #f0f6e4;
	overflow-y: scroll;
	overflow-x: auto;
	margin-top: 0;
	height: 160px;
	padding-bottom: 40px;
	width: 168px;
}
-->
</style>
	<script type="text/javascript">
		var setting = {
			view : {
				dblClickExpand : false
			},
			data : {
				simpleData : {
					enable : true
				}
			},
			callback : {
				beforeClick : beforeClick,
				onClick : onClick
			}
		};
		function beforeClick(treeId, treeNode) {
			/* var check = (treeNode && !treeNode.isParent);
			if (!check)
				alert("只能选择菜单...");  */
			return true;
		}
		function onClick(e, treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("tree_ul");
			var nodes = zTree.getSelectedNodes();
			var v_name = "";
			var v_id="";
			nodes.sort(function compare(a, b) {
				return a.id - b.id;
			});
			for (var i = 0, l = nodes.length; i < l; i++) {
				v_name += nodes[i].name + ",";
				v_id +=nodes[i].id + ",";
			}
			if (v_name.length > 0){
				v_name = v_name.substring(0, v_name.length - 1);
				v_id = v_id.substring(0, v_id.length - 1);
			}
			$("#${id}_text").attr("value", v_name);
			$("#${id}").attr("value", v_id);
			hideMenu();
		}
		function showMenu() {
			var treeObj = $("#${id}");
			var treeOffset = $("#${id} ").offset();
			/**$("#treeContent").css({
				left : treeOffset.left + "px",
				top : treeOffset.top + treeObj.outerHeight() + "px"
			}).slideDown("fast");.**/
			$("#treeContent").show();
			$("body").bind("mousedown", onBodyDown);
		}
		function hideMenu() {
			$("#treeContent").fadeOut("fast");
			$("body").unbind("mousedown", onBodyDown);
		}
		function onBodyDown(event) {
			if (!(event.target.id == "menuBtn"
					|| event.target.id == "treeContent" || $(event.target)
					.parents("#treeContent").length > 0)) {
				hideMenu();
			}
		}

		$(document).ready(function() {
			var zNodes=Helper.Remote.getJson("${url}");
			$.fn.zTree.init($("#tree_ul"), setting, zNodes);
			var treeObj = $.fn.zTree.getZTreeObj("tree_ul");
			treeObj.expandAll(true);
			$("#selectTreeBtn").on("click", function() {
				showMenu();
			});
		});
	</script>