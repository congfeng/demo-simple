nsApp.controller('MsgSenderController',function($scope,$routeParams) {  
	var id = $routeParams.id; 
	$scope.id = id;
	$(".msgsender-btn").click(function(){
		if(_.isEmpty($("#content").val())){
			layer.open({
				content : '回复内容不能为空',
				btn : [ '确定' ]
			});
			return;
		}
		$("#msgsenderform").ajaxSubmit({
			type:'post',
            url:'/msg/sender',
            success:function(data){
              	if(data&&data.s == 0){
					return;
				}
				showAlert('发送成功');
				window.location.href = "#/msgdetail?id="+id;
            }
		});
	});
}); 
