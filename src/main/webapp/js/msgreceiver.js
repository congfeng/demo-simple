nsApp.controller('MsgReceiverController',function($scope,$routeParams) {  
	var page;
	var msgreceiver_query = function(pageNo){
		$.ajax({
			url:'/msg/receiver/list',
			data:{'pageNo':pageNo},
			dataType:'json',
			success:function(data){
				if(data&&data.s == 0){
					return;
				}
				$(".msgreceiver_count").html("");
				$(".table_datas").html("");
				page.clear();
				if(data.msgreceivers ==""){
					$(".msgreceiver_count").html("此条件下没有数据");
					$(".pagination").hide();
					return;
				}
				$(".pagination").show();
				var table_datas = "";
				$.each(data.msgreceivers,function(i,msgreceiver){
					table_datas += "<tr><td>"+(i+1)+"</td>"
						+"<td>"+msgreceiver.address+"</td>"
						+"<td>"+msgreceiver.createTimeFormat+"</td>"
						+"<td><a class='msgreceiverdelete-btn' data-msgreceiverid='"+msgreceiver.id+"'><i style='font-size:30px;' class='iconfont'>&#xe642;</i></a></td>"
				        +"</tr>";
				});
				$(".table_datas").html(table_datas);
				$(".msgreceiver_count").html('共有'+data.pager.count+'个收件邮箱');
				page.refresh(data.pager);
				$('.msgreceiverdelete-btn').click(function(){
					var msgreceiverid = $(this).data('msgreceiverid');
					toIF("确定需要删除么？",function(){
						$.ajax({
							url:'/msg/receiver/delete',
							data:{'id':msgreceiverid},
							dataType:'json',
							success:function(data){
								if(data&&data.s == 0){
									return;
								}
								showAlert('删除成功');
								msgreceiver_query(1);
							}
						});	
					});
				});
			}
		});
	}
	page = new Pagination(msgreceiver_query);
	msgreceiver_query(1);
}); 
