$(function(){
	var pageNo = getQueryString('pageNo');
	if(_.isEmpty(pageNo)){
		pageNo = 1;
	}
	var type = getQueryString('type');
	if(_.isEmpty(type)){
		type = '';
	}
	var name = getQueryString('name');
	if(_.isEmpty(name)){
		name = '';
	}
	$.ajax({
		url:'/product/list',
		data:{'pageNo':pageNo,pageSize:9,'ptype':type,'name':name},
		dataType:'json',
		success:function(data){
			if(data&&data.s == 0){
				return;
			}
			if(data.products == ""){
				$("#blog_cont").html("<div class='archive-header'><div class='archive-title'>"+"无商品数据"+"<br/></div></div>");
				return;
			}
			var table_datas = "";
			$.each(data.products,function(i,product){
				table_datas += "<article class='commodity-blog_box'><div class='commodityall-blog'>"
					+"<div class='commodityall-box'>"
					+"<a href='commodity_pt.html?id="+product.id+"'>"
					+(_.isEmpty(product.image)?(""):("<img src='"+data.UploadBasePath+product.image+"' width='150' height='150' class='attachment-’full’ size-’full’ colorbox-185  wp-post-image'/>"))
					+"</a></div>"
					+"<h3>"+product.name+"</h3>"
					+"</div></article>";
			});
			$("#blog_cont").html(table_datas);
			var pagination = "<div class='wp-pagenavi'><span class='pages'>"+data.pager.pageNo+" / "+data.pager.maxPageNo+"</span>";
			if(!data.pager.firstPage){
				pagination += "<a class='previouspostslink' rel='prev' href='commodityall.html?pageNo="+(data.pager.pageNo-1)+"'>«</a>";
			}		
			$.each(data.pager.pageIndexs,function(i,p){
				if(p == data.pager.pageNo){
					pagination += "<span class='current'>"+p+"</span>";
				}else{
					pagination += "<a class='page larger' href='commodityall.html?pageNo="+p+"'>"+p+"</a>";
				}
			});
			if(!data.pager.lastPage){
				pagination += "<a class='nextpostslink' rel='next' href='commodityall.html?pageNo="+(data.pager.pageNo+1)+"'>»</a>";
			}
			pagination += "</div>";
			$("#blog_cont").append(pagination);
		}
	});
})
