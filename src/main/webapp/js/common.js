function Pagination(pageClick){
	this.isClear = true;
	this.pageClick = pageClick;
	this.pageUp = $("<li><a href='javascript:void(0)'>上一页</a></li>");
	this.pageDown = $("<li><a href='javascript:void(0)'>下一页</a></li>");
	$(".pagination").append(this.pageUp);
	$(".pagination").append(this.pageDown);
}

Pagination.prototype.refresh = function(pager){
	this.clear();
	var pageClick = this.pageClick;
	if(pager.firstPage){
		this.pageUp.addClass("disabled");
	}else{
		this.pageUp.click(function(){
			pageClick(pager.prePage);
		});
	}
	if(pager.lastPage){
		this.pageDown.addClass("disabled");
	}else{
		this.pageDown.click(function(){
			pageClick(pager.nextPage);
		});
	}
	var pageDown = this.pageDown;
	$.each(pager.pageIndexs,function(i,p){
		if(p == pager.pageNo){
			pageDown.before("<li class='page_no active'><a href='javascript:void(0)'>"+p+"</a></li>");
		}else{
			var pn = $("<li class='page_no'><a href='javascript:void(0)'>"+p+"</a></li>");
			pn.click(function(){
				pageClick(p);
			});
			pageDown.before(pn);
		}
	});
	this.isClear = false;
}

Pagination.prototype.clear = function clear(){
	if(this.isClear){
		return ;
	}
	$(".page_no").remove();
	this.pageUp.removeClass("disabled");
	this.pageDown.removeClass("disabled");
	this.pageUp.unbind('click');
	this.pageDown.unbind('click');
	this.isClear = true;
}
