nsApp.controller('ProductAddController',function($scope,$routeParams) {
	
	$("#image").fileinput({
        showUpload: false,
        browseClass: "btn btn-success",
		browseLabel: "请选择图片",
		removeClass: "btn btn-danger",
		removeLabel: "删除",
		maxFileCount: 1,
		maxFileSize: 1000,
		allowedFileTypes: ["image"],
		allowedFileExtensions: ["jpg", "gif", "png"]
    });
  
	var type = $routeParams.type;
	$scope.type = type;
	$scope.typeName = ['品类1','品类2','品类3','品类4'][type-1];
	
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