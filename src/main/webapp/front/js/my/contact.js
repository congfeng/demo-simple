$(function(){
	$(".wpcf7-submit").click(function(){
		if(_.isEmpty($("#userName").val())){
			layer.open({
				content : '用户名不能为空',
				btn : [ '确定' ]
			});
			return;
		}
		if(_.isEmpty($("#userEmail").val())){
			layer.open({
				content : '邮件地址不能为空',
				btn : [ '确定' ]
			});
			return;
		}
		$.ajax({
			url:'/msg/add',
			data:{'userName':$("#userName").val(),'userEmail':$("#userEmail").val(),
				'title':$("#title").val(),'content':$("#content").val()},
			dataType:'json',
			success:function(data){
				if(data&&data.s == 0){
					return;
				}
				showAlert('发送成功');
				$("#userName").val('');
				$("#userEmail").val('');
				$("#title").val('');
				$("#content").val('');
			}
		});
	});
	
})
