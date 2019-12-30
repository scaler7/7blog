// JavaScript Document

var articleId;

function getParams(articleId){
	articleId = articleId;
};

layui.use(['layer','layedit','laypage','form'], function(){
	    var layer = layui.layer;
	    var form = layui.form;
	    var layedit = layui.layedit;
	    var idArr = [0];
	    var editIndexMap = new Map();
	    
			//var form = layui.form,laypage = layui.laypage,layedit = layui.layedit;
		    
		 	layer.photos({
				photos: '#details-content',
				anim: 5 //0-6的选择，指定弹出图片动画类型，默认随机（请注意，3.0之前的版本用shift参数）
			});   
			
			getPage(1,3,articleId);
			
			CodeHighlighting(); //代码高亮
			
			form.on('submit(formComment)', function(data){
				var index = editIndexMap[0];
				var content = layedit.getContent(index);
				$.get("/checkLogin",function(rs){
					if(rs.code != 200){
						layer.msg("您还没有登录，请先点击右侧小人登陆");
						return false; // 阻止表单提交
					}
					var params = {
							visitorId : rs.data,
							articleId : articleId,
							parentId : 0,
							content : content
							}
					params = JSON.stringify(params);
					$.ajax({
						url: "/comment",
						type: "POST",
						data: params,
						contentType:"application/json",
			            success:function(rs){
			            	if(rs.code == 200){
			            		layer.msg("评论成功！")
			            		layedit.setContent(index,''); //清空编辑器- 这里有bug,需要把layedit.js里的setContent 函数的layedit.sync改成this.sync!
			            		getPage(1,3,articleId); // 局部刷新评论区域
			            	}else{
			            		layer.msg(rs.msg);
			            	}
			            }
					});
				});
			});
			
			form.on('submit(formReply)', function(data){
				var editKey = data.field.editKey;
				var index = editIndexMap[editKey];
				
				$.get("/checkLogin",function(rs){
					if(rs.code != 200){
						layer.msg("您还没有登录，请先点击右侧小人登陆");
						return false; // 阻止表单提交
					}
					
					var visitorId = rs.data;
					var articleId = data.field.articleId;
					var parentId = data.field.parentId;
					var replyId = data.field.replyId;
					var content = layedit.getContent(index);
					
					var params = {
							visitorId : visitorId,
							articleId : articleId,
							parentId : parentId,
							replyId : replyId,
							content : content
							}
					params = JSON.stringify(params);
					$.ajax({
						url: "/comment",
						type: "POST",
						data: params,
						contentType:"application/json",
			            success:function(rs){
			            	if(rs.code == 200){
			            		layer.msg("评论成功！")
			            		layedit.setContent(index,''); //清空编辑器- 这里有bug,需要把layedit.js里的setContent 函数的layedit.sync改成this.sync!
			            		getPage(1,3,articleId); // 局部刷新评论区域
			            	}else{
			            		layer.msg(rs.msg);
			            	}
			            }
					});
				});
			});
			
			
	    
	    function getPage(curr,size,articleId){
			$.get('/comment/'+articleId, {current: curr, size: size,articleId: articleId}, function (rs) {
				  setHtml(rs.data.records);
		          tableRender(rs.data,articleId);
		    });
		}

		function tableRender(data,articleId){
			var laypage = layui.laypage;
			laypage.render({
				elem: 'page',
				count: data.total, //数据总数通过服务端得到
				limit: 3, //每页显示的条数。laypage将会借助 count 和 limit 计算出分页数。
				curr: data.current,
				first: '首页',
				last: '尾页',
				layout: ['prev', 'page', 'next', 'skip'],
				//theme: "page",
				jump: function (obj, first) {
					if (!first) { //首次不执行
						$.get('/comment/'+articleId, {current: obj.curr, size: obj.limit,articleId: articleId}, function (rs) {
							  if(rs.code == 200){
								  setHtml(rs.data.records);
							  }else{
								  layer.msg(rs.msg)
							  }
					    });
					}
				}
			});
		}

		function setHtml(data){
			var strHtml = "";
			$.each(data,function(indexParent,parent){
				idArr.push(parent.commentId);
				strHtml += '<li>'
						 + '<div class="comment-parent">'
						 + '<a href="http://www.qbl.link"><img src="'+parent.visitorProfilePhoto+'" alt="" /></a>'
						 + '<div class="info">'
						 + '<span class="username"><a href="http://www.qbl.link">'+parent.visitorName+'</a></span>'
						 + isBlogger(parent.visitorId)
						 + '</div>'
						 + '<div class="content comment-text">'
						 + parent.content
						 + '</div>'
						 + '<p class="info info-footer">'
						 + '<span class="aux-word">'
					 	 + '<i class="layui-icon layui-icon-log"></i>2019-07-27 17:44:21'
					 	 + '</span>' 
					 	 + '<span class="aux-word">'
						 + '<i class="layui-icon layui-icon-location"></i>西虹市 移通'
						 + '</span>'
						 + '<span class="aux-word">'
						 + '<i class="layui-icon layui-extend-liulanqi"></i>Chrome 75.0'
						 + '</span>'
						 + '<a class="btn-reply" id="btn-reply" href="javascript:;"  onclick="replyBtnClick(this)">回复</a>'
						 + '</p>'
						 + '<div class="replycontainer layui-hide">'
						 + '<form class="layui-form blog-editor" lay-filter="formReply">'
						 + '<input type="hidden" name="articleId" value="'+articleId+'">'
						 + '<input type="hidden" name="parentId" value="'+parent.commentId+'">'
						 + '<input type="hidden" name="replyId" value="'+parent.commentId+'">'
						 + '<input type="hidden" name="editKey" value="'+parent.commentId+'">'
						 + '<textarea name="content" id="demo-'+parent.commentId+'" placeholder="请输入内容" class="layui-textarea" lay-verify="content"></textarea>'
						 + '<button type="button" class="layui-btn layui-btn-sm" lay-submit lay-filter="formReply">提交</button>'
						 + '</form>'  
						 + '</div>'
						 + '</div>'
						 + '<hr />';
				var childs = parent.childrens;
				if(childs != null && childs.length != 0){
					$.each(childs,function(indexChild,child){
						idArr.push(child.commentId);
						strHtml += '<div class="comment-child">'
								 + '<a href="http://www.qbl.link"><img src="'+child.visitorProfilePhoto+'" alt="" /></a>'
								 + '<div class="info">'
								 + '<span class="username"><a href="http://www.qbl.link">'+child.visitorName+'</a></span>'
								 + isBlogger(child.visitorId)
								 + '<span>回复<a href="http://www.qbl.link" class="to-username">'+child.replyVisitorName+'</a>：</span>'
								 + '<span class="comment-text">'+child.content+'</span>'
								 + '</div>'
								 + '<p class="info">'
								 + '<span class="aux-word">'
								 + '<i class="layui-icon layui-icon-log"></i>'+child.createdTime
								 + '</span>'
								 + '<span class="aux-word">'
								 + '<i class="layui-icon layui-icon-location"></i>西虹市 移通'
								 + '</span>'
								 + '<span class="aux-word">'
								 + '<i class="layui-icon layui-extend-liulanqi"></i>Chrome 75.0'
								 + '</span>'
								 + '<a class="btn-reply" id="btn-reply" href="javascript:;" onclick="replyBtnClick(this)">回复</a>'
								 + '</p>'
								 + '<div class="replycontainer layui-hide">'
								 + '<form class="layui-form blog-editor" lay-filter="formReply">'
								 + '<input type="hidden" name="articleId" value="'+articleId+'">'
								 + '<input type="hidden" name="parentId" value="'+parent.commentId+'">'
								 + '<input type="hidden" name="replyId" value="'+child.commentId+'">'
								 + '<input type="hidden" name="editKey" value="'+child.commentId+'">'
								 + '<textarea name="content" id="demo-'+child.commentId+'" placeholder="请输入内容"  class="layui-textarea" lay-verify="content"></textarea>'
								 + '<button type="button" class="layui-btn layui-btn-sm" lay-submit lay-filter="formReply">提交</button>'
								 + '</form>'
								 + '</div>'
								 + '</div>'
					});
				}
			});
			$("#blog-comment").html('');
			$("#blog-comment").append(strHtml);
			renderEdit(idArr);
		}


		function renderEdit(idArr){
			//初始化评论的编辑器
			for(var i=0 ; i<idArr.length ; i++){
				var index = layedit.build('demo-'+idArr[i], {
					height: 150,
					tool: ['face', '|', 'link'],
				});
				editIndexMap[idArr[i].toString()] = index;
			}
		}

		function isBlogger(visitorId){
			if(visitorId != null && visitorId == 1){
				return '<span class="layui-badge layui-bg-red">博主</span>';
			}
			return '';
		}


		function CodeHighlighting() {
		    //添加code标签
		    var allPre = document.getElementsByTagName("pre");
		    for (i = 0; i < allPre.length; i++) {
		        var onePre = document.getElementsByTagName("pre")[i];
		        var myCode = document.getElementsByTagName("pre")[i].innerHTML;
		        onePre.innerHTML = '<div class="pre-title">Code</div><code class="' + onePre.className.substring((onePre.className.indexOf(":") + 1), onePre.className.indexOf(";")) + '">' + myCode + '</code>';
		    }
		    //添加行号
		    $("code").each(function () {
		        $(this).html("<ol><li>" + $(this).html().replace(/\n/g, "\n</li><li>") + "\n</li></ol>");
		    });
			hljs.initHighlighting(); //对页面上的所有块应用突出显示
		    //hljs.initHighlightingOnLoad(); //页面加载时执行代码高亮
		}

}); 

function replyBtnClick(t){
	if ($(t).text() == '回复') {
		$('.btn-reply').text('回复'); // 先将所有评论区域的文本设置为'回复'
		$('.btn-reply').parent().next().addClass("layui-hide"); // 全部隐藏
		
		$(t).parent().next().removeClass("layui-hide"); // 再将当前节点的隐藏取消
	    $(t).text('收起'); // 改变文本
	}
	else {
		$(t).parent().next().addClass("layui-hide");
		$(t).text('回复');
	}
}
	


