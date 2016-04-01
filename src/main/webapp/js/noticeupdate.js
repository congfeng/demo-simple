nsApp.controller('NoticeUpdateController',function($scope,$routeParams) {  
	$.ajax({
		url:'/notice/find',
		data:{'id':$routeParams.id},
		dataType:'json',
		success:function(data){
			if(data&&data.s == 0){
				return;
			}
			$('#ntypeName').text(['分类1','分类2','分类3'][data.noticeType-1]);
			$('#ntype').val(data.noticeType);
			$('#id').val(data.id);
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
				window.location.href = "#/notice-type1?type="+$('#ntype').val();
            }
		});
	});
	
}); 