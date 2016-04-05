nsApp.controller('NoticeUpdateController',function($scope,$routeParams) { 
	var id = $routeParams.id; 
	var type = $routeParams.type;
	$scope.id = id;
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
            'horizontal', 'spechars', '|','inserttable', 'deletetable', 'mergecells','|','template','|','preview','help','fullscreen'
        ]],
        labelMap: {
			imageleft:'图片居左',imageright:'图片居右',imagecenter:'图片居中'
        }
    });
	$.ajax({
		url:'/notice/find',
		data:{'id':id},
		dataType:'json',
		success:function(data){
			if(data&&data.s == 0){
				return;
			}
			var notice = data.notice;
			if(_.isEmpty(notice)){
				showAlert('公告不存在');
				return ;
			}
			$('#title').val(notice.title);
			$('#content').val(notice.content);
			if(!_.isEmpty(notice.richText)){
				if(_.startsWith(data.UploadBasePath),'http'){
					$.ajax({
						url:'/demo/crossdomain/convert',
						data:{'remoteUrl':data.UploadBasePath+notice.richText},
						success:function(richText){
							ue.ready(function(){
								ue.setContent(richText);
						    });
						}
					});	
				}else{
					$.ajax({
						url:data.UploadBasePath+notice.richText,
						//dataType:'json',
						success:function(richText){
							ue.ready(function(){
								ue.setContent(richText);
						    });
						}
					});
				}
			}
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