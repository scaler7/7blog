// JavaScript Document
var momentId;
function spread(id){ // momentId是用来唯一标识评论区的html代码块，1个说说对应的评论区代码块只有1个
	//name = $("#spread").className; jquery的id选择器此处不生效，原因未知
	momentId = id;
	name = document.getElementById("comment"+momentId).className;
	if(name == "review-version layui-hide"){
		document.getElementById("comment"+momentId).className = "review-version";
	}

	if(name == "review-version"){
		document.getElementById("comment"+momentId).className = "review-version layui-hide";
	}
}

layui.use(['layer','layedit','laypage','form'],function () {
	console.log("layui init!")
	var layer = layui.layer;
	var layedit = layui.layedit;
	var form = layui.form;
	var idArr = [0];
	var editIndexMap = new Map();

	renderEdit(idArr);

	function renderEdit(idArr){
		for(var i=0;i<idArr.length;i++){
			console.log(idArr.length);
			var index = layedit.build('demo-'+0,{
				height: 150,
				tool: ['face', '|', 'link'],
			});
			editIndexMap[idArr[i].toString()] = index;
		}
	}
})

/*
function iniParam() {
    var form = layui.form,laypage = layui.laypage,layedit = layui.layedit;
	
    //评论和留言的编辑器
	for(var i=1;i<9;i++){
		layedit.build('demo-'+i.toString(), {
			height: 150,
			tool: ['face', '|', 'link'],
		});
	}
	
	for(var i=1;i<3;i++){
		layer.photos({
			photos: '#img-box'+i.toString(),
			anim: 5 //0-6的选择，指定弹出图片动画类型，默认随机（请注意，3.0之前的版本用shift参数）
		});
	}
	
	$(".btn-reply").click(function(){
		 if ($(this).text() == '回复') {
       		$(this).parent().next().removeClass("layui-hide");
        	$('.btn-reply').text('回复');
		    $(this).text('收起');
		}
		else {
			$(this).parent().next().addClass("layui-hide");
			$(this).text('回复');
		}
	});
	
	$('.off').unbind("click").click(function () {
		var off = $(this).attr('off');
		var chi = $(this).children('i');
		var text = $(this).children('span');
		var cont = $(this).parents('.item').siblings('.review-version');
		if (off) {
			$(text).text('展开');
			$(chi).attr('class', 'layui-icon layui-icon-down');
			$(this).attr('off', '');
			$(cont).addClass('layui-hide');
		} else {
			$(text).text('收起');
			$(chi).attr('class', 'layui-icon layui-icon-up');
			$(this).attr('off', 'true');
			$(cont).removeClass('layui-hide')
		}
	});


	laypage.render({
		elem: 'page',
		count: 10, //数据总数通过服务端得到
		limit: 5, //每页显示的条数。laypage将会借助 count 和 limit 计算出分页数。
		curr: 1,
		first: '首页',
		last: '尾页',
		layout: ['prev', 'page', 'next', 'skip'],
		//theme: "page",
		jump: function (obj, first) {
			if (!first) { //首次不执行
				layer.msg("第"+obj.curr+"页");

			}
		}
	});


}



*/
