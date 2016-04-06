nsApp.controller('MsgListController',function($scope,$routeParams) {  
	var page;
	var msg_query = function(pageNo){
		$.ajax({
			url:'/msg/list',
			data:{'pageNo':pageNo},
			dataType:'json',
			success:function(data){
				if(data&&data.s == 0){
					return;
				}
				$(".msglist_count").html("");
				$(".table_datas").html("");
				page.clear();
				if(data.msgs ==""){
					$(".msglist_count").html("此条件下没有数据");
					$(".pagination").hide();
					return;
				}
				$(".pagination").show();
				var table_datas = "";
				$.each(data.msgs,function(i,msg){
					table_datas += "<tr><td>"+(i+1)+"</td>"
						+"<td>"+msg.title+"</td>"
						+"<td>"+msg.userName+"</td>"
						+"<td>"+msg.createTimeFormat+"</td>"
						+"<td>"+(['未接收','已接收','接收失败'][msg.sendStatus])+"</td>"
						+"<td>"+(['未回复','已回复','回复失败'][msg.replyStatus])+"</td>"
						+"<td><a class='msgdetail-btn' data-msgid='"+msg.id+"'><i style='font-size:30px;' class='iconfont'>&#xe646;</i></a></td>"
				        +"</tr>";
				});
				$(".table_datas").html(table_datas);
				$(".msglist_count").html('共有'+data.pager.count+'个信件');
				page.refresh(data.pager);
				$('.msgdetail-btn').click(function(){
					window.location.href = "#/msgdetail?id="+$(this).data('msgid');
				});
			}
		});
	}
	page = new Pagination(msg_query);
	msg_query(1);
}); 
