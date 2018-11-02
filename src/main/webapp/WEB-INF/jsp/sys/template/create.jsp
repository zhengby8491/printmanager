<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui_ueditor.jspf"%>
<style type="text/css">
body {
  height: 98%;
}

.template_select2 {
  width: 205px
}

.form-container {
  width: 25%; float: left; margin: 0 5px;
}

.form-wrap {
  padding: 10px;
}

.form-wrap .form_content {
  width: 100%;
}
}
</style>
<title>新增自定义报表</title>
</head>
<body>
  <!-- 加载编辑器的容器 -->
  <div class="page-container">
    <div class="page-border">
      <!--导航按钮-->
      <div class="cl">
        <div class="iframe-top">
          <sys:nav struct="系统管理-模板管理-打印模板"></sys:nav>
        </div>
        <div class="top_nav">
          <shiro:hasPermission name="sys:template:add">
            <button class="nav_btn table_nav_btn" id="btn_save">保存</button>
          </shiro:hasPermission>
          <button class="nav_btn table_nav_btn" id="btn_cancel">返回</button>
        </div>
      </div>

      <!--主表-订单表单-->

      <input type="hidden" name="companyId" id="companyId" />

      <!-- 按钮栏Start -->
      <!-- <div class="btn-billbar" id="toolbar">
					<a id="select_product" class="nav_btn table_nav_btn" ><i
						class="fa fa-plus-square"></i> 选择产品</a>
				</div> -->
      <!--按钮栏End-->
      <!--从表-订单表格-->
      <div>
        <div id="container" name="content"></div>
        <div class="form-container">
          <div class="form-wrap">
            <div class="cl form_content">
              <dl class="cl row-dl">
                <dd class="row-dd">
                  <label class="form-label label_ui label_1">公司名称：</label>
                  <span class="ui-combo-wrap wrap-width">
                    <input type="text" class="input-txt input-txt_7" name="companyName" id="companyName" readonly="readonly" />
                    <div class="select-btn" id="selectCompany">...</div>
                  </span>
                </dd>
              </dl>
              <dl class="cl row-dl">
                <dd class="row-dd">
                  <label class="form-label label_ui label_1">模块类型：</label>
                  <span class="ui-combo-wrap" style="z-index: 0;">
                    <phtml:list name="billType" textProperty="text" selected="${params.title }" cssClass="input-txt input-txt_7 hy_select2 template_select2" type="com.huayin.printmanager.persist.enumerate.PrintModleName"></phtml:list>
                  </span>
                </dd>
              </dl>
              <dl class="cl row-dl">
                <dd class="row-dd">
                  <label class="form-label label_ui label_1">模板名称：</label>
                  <span class="ui-combo-wrap wrap-width">
                    <input type="text" class="input-txt input-txt_7" name="title" id="title" />
                    <div class="select-btn" id="selectTemplate">...</div>
                  </span>
                </dd>
              </dl>
              <dl class="cl row-dl">
                <dd class="row-dd">
                  <label class="form-label label_ui label_1">公共模板：</label>
                  <span class="ui-combo-wrap">
                    <input type="checkbox" id="isPublic" />
                  </span>
                </dd>

              </dl>
              <dl class="cl row-dl">
                <dd class="row-dd">
                  <label class="form-label label_ui label_1">备注：</label>
                  <span class="ui-combo-wrap">
                    <input type="text" class="input-txt input-txt_22" name="memo" id="memo" style="width: 205px">
                  </span>
                </dd>

              </dl>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <script type="text/javascript">
			var ueditor_width = $(".page-container").width() * 0.7;
			function getCallInfo_company(obj)
			{
				$("#companyName").val(obj.company.name);
				$("#companyId").val(obj.company.id);
			}
			$(function()
			{
				/* 选择客户 */
				$("#selectCompany").click(function()
				{
					Helper.popup.show('选择公司', Helper.basePath + '/quick/company_select?multiple=false', '900', '490');
				});

				//弹框输入
				layer.config({
					extend : 'extend/layer.ext.js'
				});
				var templateEditor = UE.getEditor('container', {
					//allowDivTransToP: false,//阻止转换div 为p
					template : true,//是否显示，设计器的 toolbars
					textarea : 'design_content',
					//这里可以选择自己需要的工具按钮名称,此处仅选择如下五个
					toolbars : [ [ 'fullscreen', 'source', '|', 'undo', 'redo', '|', 'bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript', 'removeformat', 'formatmatch', 'autotypeset', 'blockquote', 'pasteplain', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', 'selectall', 'cleardoc', '|', 'rowspacingtop', 'rowspacingbottom', 'lineheight', '|', 'customstyle', 'paragraph', 'fontfamily', 'fontsize', '|', 'directionalityltr', 'directionalityrtl', 'indent', '|', 'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|', 'touppercase', 'tolowercase', '|', 'link', 'unlink', 'anchor', '|', 'imagenone', 'imageleft', 'imageright', 'imagecenter', '|',
							'insertimage', 'emotion', 'scrawl', 'insertvideo', 'music', 'attachment', 'map', 'gmap', 'insertframe', 'insertcode', 'webapp', 'pagebreak', 'background', '|', 'horizontal', 'date', 'time', 'spechars', 'snapscreen', 'wordimage', '|', 'inserttable', 'deletetable', 'insertparagraphbeforetable', 'insertrow', 'deleterow', 'insertcol', 'deletecol', 'mergecells', 'mergeright', 'mergedown', 'splittocells', 'splittorows', 'splittocols', 'charts', '|', 'searchreplace', 'help', 'drafts' ] ],
					//focus时自动清空初始化时的内容
					//autoClearinitialContent:true,
					//关闭字数统计
					wordCount : false,
					//关闭elementPath
					elementPathEnabled : false,
					//默认的编辑区域高度
					initialFrameHeight : 595,
					initialFrameWidth : ueditor_width,
					iframeCssUrl : Helper.staticPath + "/plugins/ueditor/themes/iframe.css" //引入自身 css使编辑器兼容你网站css
					//更多其他参数，请参考ueditor.config.js中的配置项
				});
				UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
				UE.Editor.prototype.getActionUrl = function(action)
				{
					if (action == 'uploadimage' || action == 'uploadscrawl' || action == 'uploadvideo')
					{
						return Helper.basePath + '/attach/uploadFile';
					} else
					{
						return this._bkGetActionUrl.call(this, action);
					}
				}
				UE.registerUI('button_data', function(editor, uiName)
				{
					if (!this.options.template)
					{
						return false;
					}
					//注册按钮执行时的command命令，使用命令默认就会带有回退操作
					editor.registerCommand(uiName, {
						execCommand : function()
						{
							var dialog = new UE.ui.Dialog({
								iframeUrl : Helper.basePath + '/sys/template/selectDataModel?billType=' + $("#billType").val(),
								name : 'data',
								editor : this,
								title : '报表设计器 - 数据源',
								cssRules : "width:700px;height:400px;",
								buttons : [ {
									className : 'edui-okbutton',
									label : '确定',
									onclick : function()
									{
										dialog.close(true);
									}
								} ]
							});
							dialog.render();
							dialog.open();
						}
					});
					//创建一个button
					var btn = new UE.ui.Button({
						//按钮的名字
						name : uiName,
						//提示
						title : "数据源",
						//需要添加的额外样式，指定icon图标，这里默认使用一个重复的icon
						cssRules : 'background-position: -401px -40px;',
						//点击时执行的命令
						onclick : function()
						{
							//这里可以不用执行命令,做你自己的操作也可
							editor.execCommand(uiName);
						}
					});
					return btn;
				});
				UE.registerUI('button_template', function(editor, uiName)
				{
					if (!this.options.template)
					{
						return false;
					}
					editor.registerCommand(uiName, {
						execCommand : function()
						{
							var dialog = new UE.ui.Dialog({
								iframeUrl : Helper.basePath + '/sys/template/select_template?billType=' + $("#billType").val(),
								name : 'template',
								editor : this,
								title : '报表设计器 - 模板',
								cssRules : "width:1200px;height:480px;",
								buttons : [ {
									className : 'edui-okbutton',
									label : '确定',
									onclick : function()
									{
										dialog.close(true);
									}
								} ]
							});
							dialog.render();
							dialog.open();
						}
					});
					//创建一个button
					var btn = new UE.ui.Button({
						//按钮的名字
						name : uiName,
						//提示
						title : "报表模板",
						//需要添加的额外样式，指定icon图标，这里默认使用一个重复的icon
						cssRules : 'background-position: -339px -40px;',
						//点击时执行的命令
						onclick : function()
						{
							//这里可以不用执行命令,做你自己的操作也可
							editor.execCommand(uiName);
						}
					});

					return btn;
				});

			});
		</script>
  <script type="text/javascript">
			$(function()
			{
				var my = UE.getEditor('container');
				my.ready(function()
				{//编辑器初始化完成再赋值  
					var id = '${params.id}';
					if (id == '')
					{
						return;
					}
					$.ajax({
						type : "POST",
						url : Helper.basePath + '/template/getByAdmin',
						data : {
							id : '${params.id}'
						},
						async : false,
						dataType : "json",
						success : function(data)
						{
							if (data.success)
							{
								$("#companyName").val(data.obj.companyName);
								$("#memo").val(data.obj.memo);
								$("#title").val(data.obj.title);
								$("#companyId").val(data.obj.companyId);
								if (data.obj.isPublic == 'YES')
								{
									$("#isPublic").attr("checked", "true");
								}
								var obj = {
									html : HTMLDecode(data.obj.context || '')
								};

								my.execCommand("template", obj);
							}
						},
						error : function(data)
						{

						},
						beforeSend : function()
						{
							layer.load(1);
						},
						complete : function()
						{
							layer.closeAll('loading');
						}
					});
				});
				$("#selectTemplate").click(function()
				{
					if ($("#billType").val() == "DEFAULT")
					{
						Helper.message.warn("请选择模块类型!");
						return false;
					}
					$("#edui272_body").trigger('click');
				});
				//返回
				$("#btn_cancel").click(function()
				{
					var url = Helper.basePath + '/sys/template/list';
					var title = "打印模板";
					admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
				})
				//保存
				$("#btn_save").click(function()
				{
					if (Helper.isEmpty($("#companyName").val()) && !$("#isPublic").prop("checked"))
					{
						Helper.message.warn("请选择公司信息!");
						return false;
					}
					if ($("#billType").val() == "DEFAULT")
					{
						Helper.message.warn("请选择模块类型!");
						return false;
					}
					if (Helper.isEmpty($("#title").val()))
					{
						Helper.message.warn("请输入模板名称!");
						return false;
					}
					if (my.getContent() == '')
					{
						Helper.message.warn("模板内容不能为空!");
						return false
					}
					$.ajax({
						type : "POST",
						url : Helper.basePath + '/template/addByAdmin',
						data : {
							companyId : $("#companyId").val() || '',
							id : ($("#templateId").val() || ''),
							billType : $("#billType").val(),
							context : my.getContent(),
							title : $("#title").val(),
							memo : $("#memo").val(),
							isPublic : $("#isPublic").prop("checked") ? 'YES' : 'NO'
						},
						dataType : "json",
						success : function(data)
						{
							if (data.success)
							{
								$("#templateId").val(data.obj);
								layer.msg($("#title").val() + "保存成功！", {
									icon : 1
								});
							} else
							{
								layer.msg(data.message, {
									icon : 7
								});
							}
						},
						error : function(data)
						{

						},
						beforeSend : function()
						{
							layer.load(1);
						},
						complete : function()
						{
							layer.closeAll('loading');
						},
					});

				});
			});
		</script>
</body>

</html>