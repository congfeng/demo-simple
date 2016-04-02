nsApp.controller('ProductAddController',function($scope,$routeParams) {
	
	$("#image").fileinput({
		language: "zh",
		showCaption: false,
        showUpload: false,
        showClose: false,
        browseClass: "btn btn-success",
		browseLabel: "请选择图片",
		removeClass: "btn btn-danger",
		removeLabel: "删除",
		maxFileCount: 1,
		maxFileSize: 1000,
		minImageWidth:50,
		minImageHeight:50,
		maxImageWidth:250,
		maxImageHeight:250,
		resizePreference: 'height',
		resizeImage:true,
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
				window.location.href = "#/productmanage?type="+type;
            }
		});
	});
});