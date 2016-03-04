var page;
$(function(){
	var user_query = function(pageNo){
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
				$(".user_count").html("");
				page.clear();
				if(data.users ==""){
					$(".table_info").html("<div>此条件下没有数据</div>");
					return;
				}
				var table_datas = "";
				$.each(data.users,function(i,user){
					table_datas += "<tr><td>"+(i+1)+"</td><td>"+user.id+"</td><td>"+user.password+"</td>"
						+"<td>"+user.createTimeFormat+"</td>"
						+"<td><a tabindex='-1' data-toggle='modal' data-target='#update-user-modal'><i class='icon-pencil'></i></a>"
						+"&nbsp;&nbsp;<a tabindex='-1' data-toggle='modal' data-target='#remove-user-modal'><i class='icon-remove'></i></a></td>"
				        +"</tr>";
				});
				$(".table_datas").html(table_datas);
				$(".user_count").html('共有'+data.pager.count+'个用户');
				page.refresh(data.pager);
			}
		});
	}
	page = new Pagination(user_query);
	user_query(1);
	//$("#user-query-button").click(function(){
		//user_query(1);
	//})
	
	//$("#user-query-button").click();
	
	$(".user_export").click(function(){
		$("#user_form").submit();
	});
	
})