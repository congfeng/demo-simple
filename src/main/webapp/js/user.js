$(function(){
	
	function user_query(pageNo){
		$.ajax({
			url:'/user/list',
			data:{'pageNo':pageNo},
			dataType:'json',
			success:function(data){
				if(data&&data.s == 0){
					layer.open({
						content : data.m,
						btn : [ '确定' ]
					});
					return;
				}
				$(".table_info").html("");
				$(".table_datas").html("");
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
			}
		});
	}
	user_query(1);
	//$("#user-query-button").click(function(){
		//user_query(1);
	//})
	
	//$("#user-query-button").click();
})