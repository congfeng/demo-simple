var is_login_tip = false;
$(function(){
	
	if(window.localStorage.remember == 'checked'){
		$("#remember").attr("checked","checked")
	}
	$("#username").val(window.localStorage.username);
	$("#password").val(window.localStorage.password);
	
	$("#login-button").click(function(){
		if($("#username").val() == ""){
			layer.open({
				content : '用户名不能为空',
				btn : [ '确定' ],
				success:function(){
					is_login_tip = true;
				},
				yes:function(i){
					layer.close(i);
					is_login_tip = false;
				}
			});
			return;
		}
		if($("#password").val() == ""){
			layer.open({
				content : '密码不能为空',
				btn : [ '确定' ],
				success:function(){
					is_login_tip = true;
				},
				yes:function(i){
					layer.close(i);
					is_login_tip = false;
				}
			});
			return;
		}
		$.ajax({
			url:'/profile/login',
			data:{'username':$("#username").val(),'password':$("#password").val()},
			dataType:'json',
			success:function(data){
				if(data&&data.s == 0){
					layer.open({
						content : data.m,
						btn : [ '确定' ],
						success:function(){
							is_login_tip = true;
						},
						yes:function(i){
							layer.close(i);
							is_login_tip = false;
						}
					});
					return;
				}
				window.localStorage.remember = $("#remember").attr("checked");
				if(window.localStorage.remember == 'checked'){
					window.localStorage.username = $("#username").val();
					window.localStorage.password = $("#password").val();
				}else{
					window.localStorage.removeItem('username');
					window.localStorage.removeItem('password');
				}
				window.location.href = "index.html";
			}
		})
	})
	$(window).keydown(function (e) { 
		if (e.which == 13) { 
			if(is_login_tip){
				layer.closeAll();
				is_login_tip = false;
			}else{
				$("#login-button").click();
			}
		} 
	})
})