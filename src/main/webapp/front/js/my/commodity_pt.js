$(function(){
	var id = getQueryString('id');
	$.ajax({
		url:'/product/find',
		data:{'id':id},
		dataType:'json',
		success:function(data){
			if(data&&data.s == 0){
				return ;
			}
			var product = data.product;
			$('.blog_entry > h1').text(product.name);
			$('.productimage').attr('src',data.UploadBasePath+product.image);
			$('.productrichtext').html('');
			if(!_.isEmpty(product.richText)){
				$.ajax({
					url:data.UploadBasePath+product.richText,
					//dataType:'json',
					success:function(richText){
						$('.productrichtext').html(richText);
						uParse('.productrichtext', {rootPath: '/resources/ueditor/'});
					}
				});
			}			
		}
	});
})
