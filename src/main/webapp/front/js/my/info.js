$(function(){
	var pageNo = getQueryString('pageNo');
	if(_.isEmpty(pageNo)){
		pageNo = 1;
	}
	$.ajax({
		url:'/notice/list',
		data:{'pageNo':pageNo,pageSize:5,'ntype':null},
		dataType:'json',
		success:function(data){
			if(data&&data.s == 0){
				return;
			}
			if(data.notices ==""){
				$("#blog_cont").html("无公告数据");
				return;
			}
			$("#blog_cont").html('');
			$.each(data.notices,function(i,notice){
				var noticeId = notice.id;
				var articleObj = $("<article id='article_"+noticeId+"' class='blog_box'>"
					+"<header class='blog_entry'>"
					+"<h1><a href='info4blog.html?id="+noticeId+"'>4月の臨時休業日</a><span>2016.03.22</span></h1>"
					+"</header>"
					+"<div class='blog_text'></div>"
					+"</article>");
				$("#blog_cont").append(articleObj);	
				$('#article_'+noticeId+' >header>h1>a').text(notice.title);
				$('#article_'+noticeId+' >header>h1>span').text(notice.createTimeFormat.substring(0,10));
				if(!_.isEmpty(notice.richText)){
					$.ajax({
						url:data.UploadBasePath+notice.richText,
						success:function(richText){
							$('#article_'+noticeId+' >.blog_text').html(richText);
							uParse('.blog_text', {rootPath: '/resources/ueditor/'});
						}
					});	
				}
			});
			uParse('.blog_text', {rootPath: '/resources/ueditor/'});
			var pagination = "<div class='wp-pagenavi'><span class='pages'>"+data.pager.pageNo+" / "+data.pager.maxPageNo+"</span>";
			if(!data.pager.firstPage){
				pagination += "<a class='previouspostslink' rel='prev' href='info.html?pageNo="+(data.pager.pageNo-1)+"'>«</a>";
			}		
			$.each(data.pager.pageIndexs,function(i,p){
				if(p == data.pager.pageNo){
					pagination += "<span class='current'>"+p+"</span>";
				}else{
					pagination += "<a class='page larger' href='info.html?pageNo="+p+"'>"+p+"</a>";
				}
			});
			if(!data.pager.lastPage){
				pagination += "<a class='nextpostslink' rel='next' href='info.html?pageNo="+(data.pager.pageNo+1)+"'>»</a>";
			}	
			pagination += "</div>";
			$("#blog_cont").append(pagination);
		}
	});
})
