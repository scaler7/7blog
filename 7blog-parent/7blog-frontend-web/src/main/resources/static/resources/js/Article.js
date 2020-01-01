// JavaScript Document
iniParam();
var laypage;
function categoryClick(categoryId){
   getPage(1,5,categoryId);
}

function getPage(curr,size,categoryId){
	$.get('/article/page', {current: curr, size: size,categoryId: categoryId}, function (rs) {
          setHtml(rs.data.records);
          tableRender(rs.data);
    });
}

function tableRender(data,categoryId){
	laypage = layui.laypage;;
	laypage.render({
		elem: 'page',
		count: data.total, //数据总数通过服务端得到
		limit: 5, //每页显示的条数。laypage将会借助 count 和 limit 计算出分页数。
		curr: data.current, //起始页。一般用于刷新类型的跳页以及HASH跳页。
		first: '首页',
		last: '尾页',
		layout: ['prev', 'page', 'next', 'skip'],
		//theme: "page",
		jump: function (obj, first) {
			if (!first) { //首次不执行
				$.get("/article/page",{current: obj.curr, size: obj.limit,categoryId: categoryId},function(rs){
					if(rs.code == 200){
						setHtml(rs.data.records);
					}else{
						layer.msg(rs.msg);
					}
				})
			}
		}
	});
}

function setHtml(data){
	var strHtml = "";
	$.each(data,function(index,item){
		strHtml += '<div class="single-post">'
		         + '<div class="inner-post">'
		         + '<div class="post-img">'
		         + '<a href="ArticleDetails.html">'
		         + '<img src="'+getImgSrc(item.articleImg)+'" alt="">'
		         + '</a>'
		         + '</div>'
		         + '<div class="post-info">' 
			     + '<div class="post-title">'
			     + '<h3>'
			     + '<a href="/articleDetail/info/'+item.articleId+'">'+item.title+'</a>'
			     + '</h3>'
			     + '</div>'
			     + '<div class="post-content">'
			     + '<p>'
			     + removeHtmlTagAndSubstr(item.contentHtml,50)
			     + '</p>'
			     + '</div>'
			     + '<div class="blog-meta">'
			     + '<ul>'
			     + '<li>'
			     + '<i class="layui-icon layui-icon-note"></i>'
			     + '<a href="ArticleDetails.html">' + '分类'+ '</a>'
			     + '<li>'
			     + '<i class="layui-icon layui-icon-log"></i>'+item.createTime
			     + '</li>'
			     + '<li>'
		         + '<i class="layui-icon layui-extend-wiappfangwenliang"></i>浏览（<a href="ArticleDetails.html">'+item.pageView+'</a>）'
		         + '</li>'
		         + '</ul>'
		         + '<div class="post-readmore">'
		         + '<a href="/articleDetail/info/'+item.articleId+'">阅读更多</a>'
		         + '</div>'
		         + '</div>'
		         + '</div>'
		         + '</div>'
		         + '<div class="post-date one">'
		         + '<span>0'+(index+1)+'</span>'
		         + '</div>'
		         + '</div>'
	});
	$("#articleArea").html('');
	$("#articleArea").append(strHtml)
}

function removeHtmlTagAndSubstr(str,len){
	str = str.replace(/<[^>]+>/g,"");  //正则去掉所有的html标记
	if(str.length > 50){
		str = str.substr(0,len)+"...";
	}
	return str;
}

function mysubStr(str){
	str = str.length>50 ? str.substr(0,50)+"..." : str.substr(0,50);
	return str;
}

function getImgSrc(articleImg){
	if(articleImg == null || articleImg == ''){
		return "/resources/images/img03.jpg"
	}
	return articleImg;
}

function iniParam() {
    //页面效果
    $("#keyWord").focus(function () {
        $(this).parent().addClass("search-border");
    }).blur(function () {
        $(this).parent().removeClass("search-border");
    }).keydown(function (e) { 
        if (e.which == 13) { //监听回车事件
            search($(this).val());
            return false;
        }
    });

    //搜索
    $('#search').click(function () {
        var value = $("#keyWord").val();
        search(value);
    });

    function search(value) {
        if (!value) {
            layer.tips('关键字都没输入想搜啥呢...', '#keyWord', { tips: [1, '#659FFD'] });
            $("#keyWord")[0].focus(); //使文本框获得焦点
            return;
        }
      	layer.msg("没想到你居然搜这种东西："+value+"！！！");
    }
    
    getPage(1,5);
  
//	<div class="single-post">
//	<div class="inner-post">
//		<div class="post-img">
//			<a href="ArticleDetails.html">
//				<img src="/resources/images/test.jpg" alt="">
//			</a>
//		</div>
//		<div class="post-info">
//			<div class="post-title">
//				<h3>
//					<a href="ArticleDetails.html">C# 学习笔记（56）可空类型</a>
//				</h3>
//			</div>
//			<div class="post-content">
//				<p>
//					可空类型也是值类型，它是包含null值的值类型。例如：int? num=null; int?就是可空的int类型。"?"修饰符只是C#提供的一个语法糖，所谓语法糖，就是C#提供的一种方便的表示形式。C#中肯定没有int？这个类型，对于编译器而言，int？会被编译成Nullable...
//				</p>
//			</div>
//			<div class="blog-meta">
//				<ul>
//					<li>
//						<i class="layui-icon layui-icon-note"></i>
//						<a href="ArticleDetails.html">C#</a>
//					</li>
//					<li>
//						<i class="layui-icon layui-icon-log"></i>2019-06-25 14:37
//					</li>
//					<li>
//						<i class="layui-icon layui-extend-wiappfangwenliang"></i>浏览（<a href="ArticleDetails.html">50</a>）
//					</li>
//				</ul>
//				<div class="post-readmore">
//					<a href="ArticleDetails.html">阅读更多</a>
//				</div>
//			</div>
//		</div>
//	</div>
//	<div class="post-date one">
//		<span>01</span>
//	</div>
//</div>

}
