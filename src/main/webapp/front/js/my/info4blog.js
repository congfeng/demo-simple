$(function(){
	var id = getQueryString('id');
	$.ajax({
		url:'/notice/find',
		data:{'id':id},
		dataType:'json',
		success:function(data){
			if(data&&data.s == 0){
				return ;
			}
			var notice = data.notice;
			$('.blog_entry > h1').text(notice.title);
			$('.blog_entry > time').text(notice.createTimeFormat.substring(0,10));
			$('.blog_text').html('');
			if(!_.isEmpty(notice.richText)){
				if(_.startsWith(data.UploadBasePath,'http')){
					$.ajax({
						url:'/demo/crossdomain/convert',
						data:{'remoteUrl':data.UploadBasePath+notice.richText},
						success:function(richText){
							$('.blog_text').html(richText);
							uParse('.blog_text', {rootPath: '/resources/ueditor1_4_3_2/'});
						}
					});
				}else{
					$('.blog_text').load(data.UploadBasePath+notice.richText,function(){
						uParse('.blog_text', {rootPath: '/resources/ueditor1_4_3_2/'});
					});
				}
			}			
		}
	});
})
