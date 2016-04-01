nsApp.controller('ProductUpdateController',function($scope,$routeParams) {  
	$.ajax({
		url:'/product/find',
		data:{'id':$routeParams.id},
		dataType:'json',
		success:function(data){
			if(data&&data.s == 0){
				return;
			}
			$('#ptypeName').text(['品类1','品类2','品类3','品类4'][data.productType-1]);
			$('#ptype').val(data.productType);
			$('#id').val(data.id);
			$('#name').val(data.name);
			$('#sku').val(data.sku);
			$("#image").fileinput({
		        showUpload: false,
		        browseClass: "btn btn-success",
        		browseLabel: "请选择图片",
        		removeClass: "btn btn-danger",
        		removeLabel: "删除",
        		maxFileCount: 1,
        		maxFileSize: 1000,
        		allowedFileTypes: ["image"],
        		allowedFileExtensions: ["jpg", "gif", "png"],
        		overwriteInitial: true,
        		previewFileType: "image",
        		initialCaption: data.image,
		        initialPreview: [
		            "<img src='/picture/"+data.image+"' class='file-preview-image'"
		        ]
		    });
		}
	});
	
	$(".productupdate-btn").click(function(){
		if(_.isEmpty($("#name").val())){
			layer.open({
				content : '商品名称不能为空',
				btn : [ '确定' ]
			});
			return;
		}
		$("#productupdateform").ajaxSubmit({
			type:'post',
            url:'/product/update',
            success:function(data){
              	if(data&&data.s == 0){
					return;
				}
				showAlert('保存成功');
				window.location.href = "#/product-type1?type="+$('#ptype').val();
            }
		});
	});
	
}); 