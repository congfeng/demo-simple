nsApp.controller('ProductAddController',function($scope,$routeParams) {  
	var type = $routeParams.type;
	$scope.type = $routeParams.type;
	$(".productadd-btn").click(function(){
		if(_.isEmpty($("#name").val())){
			layer.open({
				content : '商品名称不能为空',
				btn : [ '确定' ]
			});
			return;
		}
		$("#productform").ajaxSubmit({
			type:'post',
            url:'/product/add',
            success:function(data){
              	if(data&&data.s == 0){
					return;
				}
				showAlert('保存成功');
				window.location.href = "#/product-type1?type="+type;
            }
		});
	});
});