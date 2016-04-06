nsApp.controller('MsgReceiverAddController',function($scope,$routeParams) {  
	$(".msgreceiveradd-btn").click(function(){
		if(_.isEmpty($("#address").val())){
			layer.open({
				content : '邮件地址不能为空',
				btn : [ '确定' ]
			});
			return;
		}
		$.ajax({
			url:'/msg/receiver/add',
			data:{'address':$("#address").val()},
			dataType:'json',
			success:function(data){
				if(data&&data.s == 0){
					return;
				}
				showAlert('添加成功');
				window.location.href = "#/msgreceiver";
			}
		});
	});
}); 
