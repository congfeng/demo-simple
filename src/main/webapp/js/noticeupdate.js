nsApp.controller('NoticeUpdateController',function($scope,$routeParams) { 
	var id = $routeParams.id; 
	var type = $routeParams.type;
	$scope.id = id;
	$scope.type = type;
	$scope.typeName = ['分类1','分类2','分类3'][type-1];
	$.ajax({
		url:'/notice/find',
		data:{'id':id},
		dataType:'json',
		success:function(data){
			if(data&&data.s == 0){
				return;
			}
			$('#title').val(data.title);
			$('#content').val(data.content);
		}
	});
	
	$(".noticeupdate-btn").click(function(){
		if(_.isEmpty($("#title").val())){
			layer.open({
				content : '公告标题不能为空',
				btn : [ '确定' ]
			});
			return;
		}
		$("#noticeupdateform").ajaxSubmit({
			type:'post',
            url:'/notice/update',
            success:function(data){
              	if(data&&data.s == 0){
					return;
				}
				showAlert('保存成功');
				window.location.href = "#/noticemanage?type="+type;
            }
		});
	});
	
}); 