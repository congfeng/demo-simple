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
			if($.inArray("product-menu",menus) == -1){
				$("#product-menu-header").hide();
				$("#product-menu").hide();
			}else{
				if($.inArray("product-type1",menus) == -1){
					$("#product-type1").hide();
				}
				if($.inArray("product-type2",menus) == -1){
					$("#product-type2").hide();
				}
				if($.inArray("product-type3",menus) == -1){
					$("#product-type3").hide();
				}
				if($.inArray("product-type4",menus) == -1){
					$("#product-type4").hide();
				}
			}
			if($.inArray("notice-menu",menus) == -1){
				$("#notice-menu-header").hide();
				$("#notice-menu").hide();
			}else{
				if($.inArray("notice-type1",menus) == -1){
					$("#notice-type1").hide();
				}
				if($.inArray("notice-type2",menus) == -1){
					$("#notice-type2").hide();
				}
				if($.inArray("notice-type3",menus) == -1){
					$("#notice-type3").hide();
				}
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
