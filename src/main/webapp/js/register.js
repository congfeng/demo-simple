$(function(){
	$("#register-button").click(function(){
		if($("#username").val() == ""){
			layer.open({
				content : '用户名不能为空',
				btn : [ '确定' ]
			});
			return;
		}
		if($("#password").val() == ""){
			layer.open({
				content : '密码不能为空',
				btn : [ '确定' ]
			});
			return;
		}
		if($("#repassword").val() !== $("#password").val()){
			layer.open({
				content : '两次输入密码不一致',
				btn : [ '确定' ]
			});
			return;
		}
		$.ajax({
			url:'/user/register',
			data:{'username':$("#username").val(),'password':$("#password").val()},
			dataType:'json',
			success:function(data){
				if(data&&data.s == 0){
					layer.open({
						content : data.m,
						btn : [ '确定' ]
					});
					return;
				}
				window.localStorage.removeItem('remember');
				window.localStorage.removeItem('password');
				window.localStorage.username = $("#username").val();
				alert('注册成功');
				window.location.href = "login.html";
			}
		})
	})
	$(window).keydown(function (e) { 
		if (e.which == 13) { 
			$("#register-button").click();
		} 
	})
})