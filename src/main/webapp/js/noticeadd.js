nsApp.controller('NoticeAddController',function($scope,$routeParams) {
	var type = $routeParams.type;
	$scope.type = type;
	$scope.typeName = ['分类1','分类2','分类3'][type-1];
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
	
	$(".n-preview-btn").click(function(){
		//showAlert('功能建设中....');
		layer.open({
			type: 2,
			offset:'10px',
			area: '716px',
			shadeClose: true,
			title: ['公告详情预览', 'font-size:18px;color:green;'],
			content:['/pages/preview2notice.html','no'],
			success:function(l,i){
				var previewJQdom = $($($(l[0]).find('iframe')[0]).contents().get(0));
				$(previewJQdom.find('#title')[0]).text($('#title').val());
				$(previewJQdom.find('#createTime')[0]).text(dateFormat(new Date(),"yyyy-MM-dd hh:mm:ss"));
				$(previewJQdom.find('#richtext')[0]).html(ue.getContent());
				layer.iframeAuto(i);
			}
		});
	});
	
});