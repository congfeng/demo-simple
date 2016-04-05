nsApp.controller('MsgDetailController',function($scope,$routeParams) {  
	var id = $routeParams.id; 
	$scope.id = id;
	$.ajax({
		url:'/msg/find',
		data:{'id':id},
		dataType:'json',
		success:function(data){
			if(data&&data.s == 0){
				return;
			}
			var msg = data.msg;
			$('#userName').text(msg.userName);
			$('#title').text(msg.title);
			$('#createTime').text(msg.createTimeFormat);
			$('#content').text(msg.content);
		}
	});
}); 
