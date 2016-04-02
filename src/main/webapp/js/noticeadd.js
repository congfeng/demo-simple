nsApp.controller('NoticeAddController',function($scope,$routeParams) {
	var type = $routeParams.type;
	$scope.type = type;
	$scope.typeName = ['分类1','分类2','分类3'][type-1];
	$(".noticeadd-btn").click(function(){
		if(_.isEmpty($("#title").val())){
			layer.open({
				content : '公告标题不能为空',
				btn : [ '确定' ]
			});
			return;
		}
		$("#noticeform").ajaxSubmit({
			type:'post',
            url:'/notice/add',
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