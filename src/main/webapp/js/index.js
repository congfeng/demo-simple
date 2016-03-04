$(function(){

	$.ajax({
		url:'/profile',
		dataType:'json',
		success:function(data){
			if(data&&data.s == 0){
				return;
			}
			$("#username-text").text(data.name);
			var menus = data.menus;
			if($.inArray("dashboard-menu",menus) == -1){
				$("#dashboard-menu-header").hide();
				$("#dashboard-menu").hide();
			}else{
				if($.inArray("home",menus) == -1){
					$("#home").hide();
				}
				if($.inArray("users",menus) == -1){
					$("#users").hide();
				}
				if($.inArray("user",menus) == -1){
					$("#user").hide();
				}
				if($.inArray("gallery",menus) == -1){
					$("#gallery").hide();
				}
				if($.inArray("calendar",menus) == -1){
					$("#calendar").hide();
				}
				if($.inArray("faq",menus) == -1){
					$("#faq").hide();
				}
				if($.inArray("help",menus) == -1){
					$("#help").hide();
				}
			}
			if($.inArray("accounts-menu",menus) == -1){
				$("#accounts-menu-header").hide();
				$("#accounts-menu").hide();
			}else{
				if($.inArray("sign-in",menus) == -1){
					$("#sign-in").hide();
				}
				if($.inArray("sign-up",menus) == -1){
					$("#sign-up").hide();
				}
				if($.inArray("reset-password",menus) == -1){
					$("#reset-password").hide();
				}
			}
			if($.inArray("settings-menu",menus) == -1){
				$("#settings-menu-header").hide();
				$("#settings-menu").hide();
			}else{
				
			}
			if($.inArray("legal-menu",menus) == -1){
				$("#legal-menu-header").hide();
				$("#legal-menu").hide();
			}else{
				
			}
			$('body').show();
		}
	});

	$("#logout-button").click(function(){
		$.ajax({
			url:'/profile/logout',
			dataType:'json',
			success:function(data){
				window.location.href = "login.html";
			}
		})
	});
	
	$("#resetpassword-button").click(function(){
		if($("#newpassword").val() == ""){
			layer.open({
				content : '密码不能为空',
				btn : [ '确定' ]
			});
			return;
		}
		if($("#renewpassword").val() !== $("#newpassword").val()){
			layer.open({
				content : '两次输入密码不一致',
				btn : [ '确定' ]
			});
			return;
		}
		$.ajax({
			url:'/user/resetpassword',
			data:{'newpassword':$("#newpassword").val()},
			dataType:'json',
			success:function(data){
				if(data&&data.s == 0){
					layer.open({
						content : data.m,
						btn : [ '确定' ]
					});	
					return;
				}
				$('#resetpassword-modal').modal('hide');
				layer.open({
					content : '修改成功',
					btn : [ '确定' ]
				});
			}
		})
	});
})
