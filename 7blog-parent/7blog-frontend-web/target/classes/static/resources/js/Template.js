var isLogin;
var profilePhoto;
var nickname;
function initParams(isLogin,profilePhoto,nickname){
	isLogin = isLogin;
	profilePhoto = profilePhoto;
	nickname = nickname;
}

layui.use(['element', 'laypage', 'form', 'util', 'layer', 'flow','table','layedit'], function () {
	// JavaScript Document
	var form = layui.form;

$(function(){
	setStyle(isLogin);
})

//登录图标
var anim = setInterval(function () {
    if ($(".animated-circles").hasClass("animated")) {
        $(".animated-circles").removeClass("animated");
    } else {
        $(".animated-circles").addClass('animated');
    }
}, 3000);
var wait = setInterval(function () {
    $(".livechat-hint").removeClass("show_hint").addClass("hide_hint");
    clearInterval(wait);
}, 4500);
$(".livechat-girl").hover(function () {
    clearInterval(wait);
    $(".livechat-hint").removeClass("hide_hint").addClass("show_hint");
}, function () {
    $(".livechat-hint").removeClass("show_hint").addClass("hide_hint");
})


//登录事件
function login() {
	window.location.href = "/qq/oauth";
}

//设置样式
function setStyle(flag) {
    if (!flag) { //未登录
        $('.girl').attr("src", "/resources/images/a.png").css("border-radius", "0px");
        $('.livechat-girl').css({ right: "-35px", bottom: "75px" }).removeClass("red-dot");
        $('.rd-notice-content').text('嘿，来试试登录吧！');
        
        $(".girl").unbind("click"); // 先移除掉之前的单击事件
        
        $('.girl').click(function () { // 添加新的单击事件
            login(); // 未登录时的单击事件
        });
        return;
    }
    clearInterval(anim);
	$('.girl').attr("src", profilePhoto).css("border-radius", "50px");
	$('.rd-notice-content').text('欢迎您,'+nickname+"!");
	$('.livechat-girl').css({ right: "0px", bottom: "80px" });
	
	var html = '<form class="layui-form" lay-filter="personalInfoFilter" style="padding:20px 30px 0px 0px;">'
			  + '<div class="layui-form-item">'
			  + '<label class="layui-form-label">邮箱</label>'
			  + '<div class="layui-input-inline">'
			  + '<input type="text" name="email" autocomplete="off" class="layui-input" lay-verify="email" placeholder="请输入邮箱">'
			  + '</div>'
			  + '<div class="layui-form-mid layui-word-aux">仅用于留言回复通知</div>'
			  + '</div>'
			  
			  + '<div class="layui-form-item">'
			  + '<label class="layui-form-label">网站</label>'
			  + '<div class="layui-input-inline">'
			  + '<input type="text" name="personalWebsite" placeholder="请输入个人网站"  autocomplete="off" class="layui-input">'
			  + '</div>'
			  + '<div class="layui-form-mid layui-word-aux">若填写此项，其他用户点击您的头像时，可访问您的网站</div>'
			  + '</div>'
			  
			  + '<div class="layui-form-item">'
			  + '<label class="layui-form-label">QQ</label>'
			  + '<div class="layui-input-inline">'
			  + '<input type="text" name="qqAccount" placeholder="请输入QQ账号" autocomplete="off" class="layui-input">'
			  + '</div>'
			  + '<div class="layui-form-mid layui-word-aux">仅用于交流学习，绝无其他用途</div>'
			  + '</div>'
			  
			  + '<div class="layui-form-item">'
			  + '<label class="layui-form-label">微信</label>'
			  + '<div class="layui-input-inline">'
			  + '<input type="text" name="wechatAccount" placeholder="请输入微信账号" autocomplete="off" class="layui-input">'
			  + '</div>'
			  + '<div class="layui-form-mid layui-word-aux">仅用于交流学习，绝无其他用途</div>'
			  + '</div>'
			  
			  + '<div class="layui-form-item">'
			  + '<label class="layui-form-label">通知</label>'
			  + '<div class="layui-input-inline">'
			  + '<input type="checkbox" name="allowInform" lay-skin="switch" lay-text="允许|不允许" value="1">'
			  + '</div>'
			  + '<div class="layui-form-mid layui-word-aux">是否允许向邮箱发送通知</div>'
			  + '</div>'
			  
			  + '<div class="layui-form-item layui-hide">'
			  + '<input type="text" name="visitorId">'
			  + '<button class="layui-btn" lay-submit lay-filter="personalInfoSub">验证提交</button>'
			  + '</div>'
			  + '</form>'
			  
	$(".girl").unbind("click"); // 先移除掉之前的单击事件
	$('.girl').click(function () {
		var layerInx = layer.open({
        	type: 1,
        	content: html,
        	area: ['680px','450px'],
        	btn: ['提交','退出登录','取消'],
        	btnAlign: 'c',
        	closeBtn: 2,
        	success: function(layero, index){
        		$.get("/currVisitorinfo",function(rs){
        			if(rs.code != 200){
        				layer.msg("获取个人信息失败")
        				return false;
        			}
        			form.val('personalInfoFilter',rs.data);
        		})
        		 // 添加form标识
                layero.addClass('layui-form');
                // 将弹层的提交按钮改变为表单中的submit按钮
                layero.find('.layui-layer-btn0').attr({
                    'lay-filter': 'personalInfoSub',
                    'lay-submit': ''
                });
                form.render();
        	},
        	btn3:function(layero,index){ // '取消'
        		layer.close(layerInx);
        		layer.msg("取消");
        		return false;
        	},
        	btn2:function(layero,index){ // '退出登录'
        		layer.confirm('确定退出？', {icon: 3, title:'提示'}, function(index){
        			 $.get("/loginOut",function(rs){
        				 if(rs.code != 200){
        					 layer.msg("退出失败！");
        					 return false;
        				 }
        				 setStyle(false);
        				 layer.msg("您已退出");
        			 })
        		});
        	},
        	yes:function(index, layero){ '提交'
        		form.on('submit(personalInfoSub)',function(data){
        		 	var params = JSON.stringify(data.field); // 将json格式对象转换为json格式字符串
        		 	$.ajax({
						url: "/currVisitorinfo",
						type: "PUT",
						data: params,
						contentType:"application/json",
			            success:function(rs){
			            	if(rs.code == 200){
			            		layer.msg("提交成功！")
			            		layer.close(layerInx); // 关闭弹层
			            	}else{
			            		layer.msg(rs.msg);
			            	}
			            }
					});
        		})
        	}
        }) // 已登陆时的单击事件
    });
}
	
    try {
        var util = layui.util, layer = layui.layer;
        console.log("7Blog官网：http://scaler7.online");	
        $(document).ready(function () { //DOM树加载完毕执行，不必等到页面中图片或其他外部文件都加载完毕
            //页面加载完成后，速度太快会导到loading层闪烁，影响体验，所以在此加上500毫秒延迟
            setTimeout(function () { $("#loading").hide(); }, 500);
        });

        //初始化WOW.js
        new WOW().init();

        //导航点击效果
        $('header nav > ul > li a').click(function () {
            $('header nav > ul > li').removeClass("nav-select-this").find("a").removeClass("nav-a-click");
            $(this).addClass("nav-a-click").parent().addClass("nav-select-this");
        });

        //固定块
        util.fixbar({
            css: { right: 10, bottom: 40, },
            bar1: "&#xe602e;",
            click: function (type) {
                if (type === 'bar1') {
                    layer.tab({
                        area: ['300', '290px'],
                        resize: false, 
                        shadeClose: true,                  
                        scrollbar: false,
                        anim: 4,                 
                        tab: [{
                            title: '微信',
                            content: '<img src="images/zsm.jpg" style="width:255px;" oncontextmenu="return false;" ondragstart="return false;" />',
                        },
                        {
                            title: '支付宝',
                            content: '<img src="images/zfb.jpg" style="width:255px;" oncontextmenu="return false;" ondragstart="return false;" />',
                        }],
                        success: function (layero, index) {
                            $("#" + layero[0].id + " .layui-layer-content").css("overflow", "hidden");
                            $("#" + layero[0].id + " .layui-layer-title span").css("padding", "0px");
                            layer.tips('本站收获的所有打赏费用均只用于服务器日常维护以及本站开发用途，感谢您的支持！', "#" + layero[0].id, {
                                tips: [1, '#FFB800'],
                                time: 0, 
                            });
                        },
                        end: function () {
                            layer.closeAll('tips'); 
                        }
                    });
                }
            }
        });

        //使刷新页面后，此页面导航仍然选中高亮显示，自己根据你实际情况修改
        var pathnameArr = window.location.pathname.split("/");
        var pathname = pathnameArr[pathnameArr.length - 1];
            if (pathname.indexOf(".html") < 0)
                pathname += ".html";
        if (!!pathname) {
            $('header nav > ul > li').removeClass("nav-select-this").find("a").removeClass("nav-a-click");
            $('header nav > ul > li').each(function () {
                var hrefArr = $(this).find("a").attr('href').split("/");
                var href = hrefArr[hrefArr.length - 1];
                if (pathname.toLowerCase() === href.toLowerCase()) {
                    $(this).addClass("nav-select-this").find("a").addClass("nav-a-click");
                    return false;
                }
            });
        }


    }
    catch (e) {
        layui.hint().error(e);
    }         
});
//百度统计
var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "https://hm.baidu.com/hm.js?2295df439cc39433717a60016297baeb";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();


