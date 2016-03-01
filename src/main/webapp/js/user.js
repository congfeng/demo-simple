var user_query;
$(function(){
	user_query = function(pageNo){
		$.ajax({
			url:'/user/list',
			data:{'pageNo':pageNo},
			dataType:'json',
			success:function(data){
				if(data&&data.s == 0){
					return;
				}
				$(".table_info").html("");
				$(".table_datas").html("");
				$(".page_count").html("");
				$(".page_no").remove();
				$(".page_up").removeClass("disabled");
				$(".page_down").removeClass("disabled");
				if(data.users ==""){
					$(".table_info").html("<div>此条件下没有数据</div>");
					return;
				}
				var table_datas = "";
				$.each(data.users,function(i,user){
					table_datas += "<tr><td>"+(i+1)+"</td><td>"+user.id+"</td><td>"+user.password+"</td>"
						+"<td>"+user.createTimeFormat+"</td>"
						+"<td><a href='user.html'><i class='icon-pencil'></i></a>"
						+"<a href='#myModal' role='button' data-toggle='modal'><i class='icon-remove'></i></a></td>"
				        +"</tr>";
				});
				$(".table_datas").html(table_datas);
				$(".page_count").html('共有'+data.pager.count+'个用户');
				if(data.pager.firstPage){
					$(".page_up").addClass("disabled");
				}else{
					$(".page_up").attr("onclick","user_query("+data.pager.prePage+")");
				}
				if(data.pager.lastPage){
					$(".page_down").addClass("disabled");
				}else{
					$(".page_down").attr("onclick","user_query("+data.pager.nextPage+")");
				}
				$.each(data.pager.pageIndexs,function(q,w){
					if(w == data.pager.pageNo){
						$(".page_down").before("<li class='page_no active'><a href='javascript:void(0)'>"+w+"</a></li>");
					}else{
						$(".page_down").before("<li class='page_no'><a onclick='user_query("+w+");' href='javascript:void(0)'>"+w+"</a></li>");
					}
				});
			}
		});
	}
	user_query(1);
	//$("#user-query-button").click(function(){
		//user_query(1);
	//})
	
	//$("#user-query-button").click();
})