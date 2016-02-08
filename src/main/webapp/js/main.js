// JavaScript Document
$(document).ajaxSuccess(function(e,cmd,b){
					var str = JSON.parse(cmd.responseText);
					if(str.s == 0){
						if(str.t == 1){
							alert("登录超时，请重新登录");
							window.location.href = "/login.html";
							return;
						}else{
							alert("失败信息:" + str.m);
						}
						return;
					}
			   });
$(function(){
	var Name = window.localStorage.Name;
	var Phone = window.localStorage.Phone;
	var _html = window.localStorage.data;
	$(".leftmenu").html(_html);
	if(Phone){
		$(".dlxx").html(Name+"("+Phone+")欢迎您！")
	}else{
		$(".dlxx").html(Name+"欢迎您！")
	}
	
	
	//左侧导航伸缩
	$(".parentMenu span").click(function(){
		 $(this).parent().find(".second").slideToggle(200);	
	})
	//右上角本地时间
	setInterval(function() {
    var now = (new Date()).toLocaleString();
    $('.time').text(now);
	}, 1000);
	
	
	
	$(".leftmenu a").click(function(){
		if($(this).parent().attr("class") == "active"){
			$("#sesx-an").click();
		}
		$(".leftmenu li").removeClass("active");
		$(this).parent().addClass("active");
		$(".crumb").html("<a href='javascript:void(0)'>"+$(this).closest(".parentMenu").find("span").text()+"</a>&gt;<a href='javascript:void(0)'>"+$(this).text()+"</a>")
	})
	
	// 订单全选
	$(".list-title-check").click(function(){
		if($(this).attr("checkdata") == "true"){
		 $(this).attr("checked",false);
		 $(".list-check").attr("checked",false);
		 $(this).attr("checkdata" ,"");
		}else{
			$(this).attr("checked",true);
			$(".list-check").attr("checked",true);
			$(this).attr("checkdata" ,"true")
	     
		}
			
	})
	
	// 订单伸缩
	$(".open-button2").live("click",function(){
		$(this).closest(".list").find(".list-many").show();
		$(this).closest(".list").find(".list-one").hide();
	})
	$(".close-button2").live("click",function(){
		$(this).closest(".list").find(".list-many").hide();
		$(this).closest(".list").find(".list-one").show();
	})
		
	//  控制 nr 左移
	$(".nr-move-button").live("click",function(){
			if($(this).attr("style") == "left: 173px;"){
				$(".nr").animate({left:20,width:914},500);
				$(this).animate({left:0},500);
				$(".leftmenu").animate({left:-174},500);
				$(this).removeClass("nr-move-button1");
				$(this).addClass("nr-move-button2");
				
			}else{
				$(".nr").animate({left:174,width:760},500);
				$(this).animate({left:173},500);
				$(".leftmenu").animate({left:0},500)
				$(this).removeClass("nr-move-button2");
				$(this).addClass("nr-move-button1");
			}
		})	
	
	
	//pc-sc 页面右侧按钮
	$(".item-all .sc-item-button").live("click",function(){
			if($(this).attr("style") == "background:url(images/item-button1.jpg)"){
				$(this).attr("style","background:url(images/item-button2.jpg)");
				$(this).parent().find(".item-many").slideDown(200);
				$(this).parent().find(".item-one") .hide();
			}else{
				$(this).attr("style","background:url(images/item-button1.jpg)");
				$(this).parent().find(".item-many").slideUp(200);
				$(this).parent().find(".item-one") .show();
			}
	})
		
	//库房设置按钮
	$(".item-all .kf-item-button").live("click",function(){
			if($(this).attr("style") == "background:url(images/item-button1.jpg)"){
				$(this).attr("style","background:url(images/item-button2.jpg)");
				$(this).parent().find(".kf-item").slideDown(100);
			}else{
				$(this).attr("style","background:url(images/item-button1.jpg)");
				$(this).parent().find(".kf-item").slideUp(100);
			}
	})
	
	$(".tc-dl").click(function(){
		$(".login-jz").show();
		$.ajax({
			url:'/people/logout',
			dataType:'json',
			success:function(data){
				if(data.s == 0){
					alert("退出失败");
					$(".login-jz").hide();
					return;
				}
				window.location.href = "/login.html";
			}
		})
	})
	
	
	$(window.document).scroll(function () {
		var scrolltop = $(document).scrollTop();
		if(scrolltop > 70){
			$(".leftmenu").css("position","fixed");
		}else{
			$(".leftmenu").css("position","absolute")
		}
    });
	
	
})

	
