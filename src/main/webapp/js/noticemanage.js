nsApp.controller('NoticeManageController',function($scope,$routeParams) {  
	var page;
	var type = $routeParams.type;
	$scope.type = $routeParams.type;
	var notice_query = function(pageNo){
		$.ajax({
			url:'/notice/list',
			data:{'pageNo':pageNo,'ntype':type},
			dataType:'json',
			success:function(data){
				if(data&&data.s == 0){
					return;
				}
				$(".table_info").html("");
				$(".table_datas").html("");
				$(".notice_count").html("");
				page.clear();
				if(data.notices ==""){
					$(".table_info").html("<div>此条件下没有数据</div>");
					return;
				}
				var table_datas = "";
				$.each(data.notices,function(i,notice){
					table_datas += "<tr><td>"+(i+1)+"</td>"
						+"<td>"+notice.title+"</td>"
						+"<td>"+notice.createTime+"</td>"
						+"<td><a class='noticeupdate-btn' data-noticeid='"+notice.id+"'><i style='font-size:30px;' class='iconfont'>&#xe641;</i></a></td>"
						+"<td><a class='noticedelete-btn' data-noticeid='"+notice.id+"'><i style='font-size:30px;' class='iconfont'>&#xe642;</i></a></td>"
				        +"</tr>";
				});
				$(".table_datas").html(table_datas);
				$(".notice_count").html('共有'+data.pager.count+'个商品');
				page.refresh(data.pager);
				$('.noticeupdate-btn').click(function(){
					window.location.href = "#/noticeupdate?id="+$(this).data('noticeid');
				});
				$('.noticedelete-btn').click(function(){
					var nid = $(this).data('noticeid');
					toIF("确定需要删除么？",function(){
						$.ajax({
							url:'/notice/delete',
							data:{'id':nid},
							dataType:'json',
							success:function(data){
								if(data&&data.s == 0){
									return;
								}
								showAlert('删除成功');
								notice_query(1);
							}
						});	
					});
				});
			}
		});
	}
	page = new Pagination(notice_query);
	notice_query(1);
}); 
