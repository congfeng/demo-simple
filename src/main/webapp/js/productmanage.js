var page;

nsApp.controller('ProductManageController',function($scope,$routeParams) {  
	var type = $routeParams.type;
	$scope.type = $routeParams.type;
	var product_query = function(pageNo){
		$.ajax({
			url:'/product/list',
			data:{'pageNo':pageNo,'ptype':type},
			dataType:'json',
			success:function(data){
				if(data&&data.s == 0){
					return;
				}
				$(".table_info").html("");
				$(".table_datas").html("");
				$(".user_count").html("");
				page.clear();
				if(data.products ==""){
					$(".table_info").html("<div>此条件下没有数据</div>");
					return;
				}
				var table_datas = "";
				$.each(data.products,function(i,product){
					table_datas += "<tr><td>"+(i+1)+"</td>"
						+"<td><img width='50px;' height='50px;' src='/picture/"+product.image+"'></td>"
						+"<td>"+product.name+"</td>"
						+"<td>"+product.sku+"</td>"
						+"<td><a class='productupdate-btn' data-productid='"+product.id+"'><i style='font-size:30px;' class='iconfont'>&#xe641;</i></a></td>"
						+"<td><a class='productdelete-btn' data-productid='"+product.id+"'><i style='font-size:30px;' class='iconfont'>&#xe642;</i></a></td>"
				        +"</tr>";
				});
				$(".table_datas").html(table_datas);
				$(".product_count").html('共有'+data.pager.count+'个商品');
				page.refresh(data.pager);
				$('.productupdate-btn').click(function(){
					window.location.href = "#/productupdate?id="+$(this).data('productid');
				});
				$('.productdelete-btn').click(function(){
					$.ajax({
						url:'/product/delete',
						data:{'id':$(this).data('productid')},
						dataType:'json',
						success:function(data){
							if(data&&data.s == 0){
								return;
							}
							showAlert('删除成功');
							product_query(1);
						}
					});	
				});
			}
		});
	}
	page = new Pagination(product_query);
	product_query(1);
}); 
