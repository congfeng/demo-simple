nsApp.controller('ProductUpdateController',function($scope,$routeParams) {  
	var id = $routeParams.id; 
	var type = $routeParams.type;
	$scope.id = id;
	$scope.type = type;
	$scope.typeName = ['品类1','品类2','品类3','品类4'][type-1];
	var ue = UE.getEditor('richText',{
    	//autoHeight: false,
    	initialContent : '',
        autoClearinitialContent : true,
        catchRemoteImageEnable: true,
        enableAutoSave: false,
        elementPathEnabled: false,
        emotionLocalization: true,
    	toolbars: [['bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript', 'formatmatch', '|', 
    		'forecolor', 'backcolor', '|','insertorderedlist', 'insertunorderedlist', '|','rowspacingtop', 'rowspacingbottom', 'lineheight', '|',
            'customstyle', 'paragraph', 'fontfamily', 'fontsize', '|', 'justifyleft', 'justifycenter', 'justifyright', '|','link', 'unlink', '|', 
            'imagenone', 'imageleft', 'imageright', 'imagecenter', '|','simpleupload', 'emotion', 'scrawl', 'insertvideo', 'background', '|',
            'horizontal', 'spechars', '|','inserttable', 'deletetable', 'mergecells','|','template','|','preview','help','fullscreen'
        ]],
        labelMap: {
			imageleft:'图片居左',imageright:'图片居右',imagecenter:'图片居中'
        }
    });
	$.ajax({
		url:'/product/find',
		data:{'id':id},
		dataType:'json',
		success:function(data){
			if(data&&data.s == 0){
				return ;
			}
			var product = data.product;
			if(_.isEmpty(product)){
				showAlert('商品不存在');
				return ;
			}
			$('#name').val(product.name);
			$('#sku').val(product.sku);
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
        		allowedFileExtensions: ["jpg", "gif", "png"],
        		overwriteInitial: true,
        		previewFileType: "image",
        		initialPreviewShowDelete: false,
        		initialCaption: product.image,
		        initialPreview: _.isEmpty(product.image)?[]:[
		            "<img src='"+data.UploadBasePath+product.image+"' class='file-preview-image'>"
		        ]
		    }).on('change', function() {
    			$('#imageChange').val(true);
			}).on('fileclear', function() {
				$('#imageChange').val(true);
			});
			if(!_.isEmpty(product.richText)){
				$.ajax({
					url:data.UploadBasePath+product.richText,
					//dataType:'json',
					success:function(richText){
						ue.ready(function(){
							ue.setContent(richText);
					    });
					}
				});
			}
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
				window.location.href = "#/productmanage?type="+type;
            }
		});
	});
	
}); 