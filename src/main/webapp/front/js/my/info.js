$(function(){
	var pageNo = getQueryString('pageNo');
	if(_.isEmpty(pageNo)){
		pageNo = 1;
	}
	var type = getQueryString('type');
	if(_.isEmpty(type)){
		type = '';
	}
	var title = getQueryString('title');
	if(_.isEmpty(title)){
		title = '';
	}
	$.ajax({
		url:'/notice/list',
		data:{'pageNo':pageNo,pageSize:5,'ntype':type,'title':title},
		dataType:'json',
		success:function(data){
			if(data&&data.s == 0){
				return;
			}
			if(data.notices == ""){
				$("#blog_cont").html("<div class='archive-header'><div class='archive-title'>"+"无公告数据"+"<br/></div></div>");
				return;
			}
			$("#blog_cont").html('');
			if(!_.isEmpty(type)){
				var typeName = ['分类1','分类2','分类3'][type-1];
				$("#blog_cont").html("<div class='archive-header'><div class='archive-title'>"+typeName+"<br/></div></div>");
			}
			$.each(data.notices,function(i,notice){
				var noticeId = notice.id;
				var articleObj = $("<article id='article_"+noticeId+"' class='blog_box'>"
					+"<header class='blog_entry'>"
					+"<h1><a href='info4blog.html?id="+noticeId+"'>title</a><span>date</span></h1>"
					+"</header>"
					+"<div class='blog_text'></div>"
					+"</article>");
				$("#blog_cont").append(articleObj);	
				$('#article_'+noticeId+' >header>h1>a').text(notice.title);
				$('#article_'+noticeId+' >header>h1>span').text(notice.createTimeFormat.substring(0,10));
				if(!_.isEmpty(notice.richText)){
					if(_.startsWith(data.UploadBasePath,'http')){
						$.ajax({
							url:'/demo/crossdomain/convert',
							data:{'remoteUrl':data.UploadBasePath+notice.richText},
							success:function(richText){
								$('#article_'+noticeId+' >.blog_text').html(richText);
								uParse('.blog_text', {rootPath: '/resources/ueditor1_4_3_2/'});
							}
						});
					}else{
						$('#article_'+noticeId+' >.blog_text').load(data.UploadBasePath+notice.richText,function(){
							uParse('.blog_text', {rootPath: '/resources/ueditor1_4_3_2/'});
						});
					}
				}
			});
			uParse('.blog_text', {rootPath: '/resources/ueditor1_4_3_2/'});
			var pagination = "<div class='wp-pagenavi'><span class='pages'>"+data.pager.pageNo+" / "+data.pager.maxPageNo+"</span>";
			if(!data.pager.firstPage){
				pagination += "<a class='previouspostslink' rel='prev' href='info.html?type="+type+"&pageNo="+(data.pager.pageNo-1)+"'>«</a>";
			}		
			$.each(data.pager.pageIndexs,function(i,p){
				if(p == data.pager.pageNo){
					pagination += "<span class='current'>"+p+"</span>";
				}else{
					pagination += "<a class='page larger' href='info.html?type="+type+"&pageNo="+p+"'>"+p+"</a>";
				}
			});
			if(!data.pager.lastPage){
				pagination += "<a class='nextpostslink' rel='next' href='info.html?type="+type+"&pageNo="+(data.pager.pageNo+1)+"'>»</a>";
			}	
			pagination += "</div>";
			$("#blog_cont").append(pagination);
		}
	});
})
