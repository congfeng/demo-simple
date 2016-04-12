nsApp.controller('ProductAddController',function($scope,$routeParams) {
	
	var type = $routeParams.type;
	$scope.type = type;
	$scope.typeName = ['品类1','品类2','品类3','品类4'][type-1];
	var pageNo = $routeParams.pageNo;
	if(_.isEmpty(pageNo)){
		pageNo = 1;
	}
	$scope.pageNo = pageNo;
	
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
		allowedFileExtensions: ["jpg","jpeg", "gif", "png" , "bmp"]
    });
  
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
            'imagenone', 'imageleft', 'imageright', 'imagecenter', '|','simpleupload', 'emotion', 'scrawl', 'insertvideo', '|',
            'horizontal', 'spechars', '|','inserttable', 'deletetable', 'mergecells','|','template','|','help','fullscreen'
        ]],
        labelMap: {
			imageleft:'图片居左',imageright:'图片居右',imagecenter:'图片居中'
        }
    });
	
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
				window.location.href = "#/productmanage?type="+type+"&pageNo="+pageNo;
            }
		});
	});
	
	$(".p-preview-btn").click(function(){
		//showAlert('功能建设中....');
		layer.open({
			type: 2,
			offset:'10px',
			area: '716px',
			shadeClose: true,
			title: ['商品详情预览', 'font-size:18px;color:green;'],
			content:['/pages/preview2product.html','no'],
			success:function(l,i){
				var previewJQdom = $($($(l[0]).find('iframe')[0]).contents().get(0));
				$(previewJQdom.find('#productName')[0]).text($('#name').val());
				$(previewJQdom.find('#richtext')[0]).html(ue.getContent());
				layer.iframeAuto(i);
			}
		});
	});
	
});