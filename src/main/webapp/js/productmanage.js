nsApp.controller('ProductManageController',function($scope,$routeParams) {  
	var page;
	var type = $routeParams.type;
	$scope.type = type;
	$scope.typeName = ['品类1','品类2','品类3','品类4'][type-1];
	var pageNo = $routeParams.pageNo;
	if(_.isEmpty(pageNo)){
		pageNo = 1;
	}
	$scope.pageNo = pageNo;
	var product_query = function(pn){
		$.ajax({
			url:'/product/list',
			data:{'pageNo':pn,'ptype':type},
			dataType:'json',
			success:function(data){
				if(data&&data.s == 0){
					return;
				}
				$(".product_count").html("");
				$(".table_datas").html("");
				page.clear();
				if(data.products ==""){
					$(".product_count").html("此条件下没有数据");
					$(".pagination").hide();
					return;
				}
				$(".productadd_btn").attr("href","#/productadd?type="+type+"&pageNo="+pn);
				$(".pagination").show();
				var table_datas = "";
				$.each(data.products,function(i,product){
					table_datas += "<tr><td>"+(i+1)+"</td>"
						+"<td><img width='50px;' height='50px;' onerror=\"this.style.display='none'\" src='"+data.UploadBasePath+product.image+"'></td>"
						+"<td><a><img class='image_scale' width='50px;' height='50px;' onerror=\"this.style.display='none'\" src='"+data.UploadBasePath+product.qrcode+"'></a></td>"
						+"<td>"+product.name+"</td>"
						+"<td><a class='productupdate-btn' data-productid='"+product.id+"'><i style='font-size:30px;' class='iconfont'>&#xe641;</i></a></td>"
						+"<td><a class='productdelete-btn' data-productid='"+product.id+"'><i style='font-size:30px;' class='iconfont'>&#xe642;</i></a></td>"
				        +"</tr>";
				});
				$(".table_datas").html(table_datas);
				$(".product_count").html('共有'+data.pager.count+'个商品');
				page.refresh(data.pager);
				$('.productupdate-btn').click(function(){
					window.location.href = "#/productupdate?type="+type+"&pageNo="+pn+"&id="+$(this).data('productid');
				});
				$('.productdelete-btn').click(function(){
					var pid = $(this).data('productid');
					toIF("确定需要删除么？",function(){
						$.ajax({
							url:'/product/delete',
							data:{'id':pid},
							dataType:'json',
							success:function(data){
								if(data&&data.s == 0){
									return;
								}
								showAlert('删除成功');
								product_query(pn);
							}
						});	
					});
				});
			}
		});
	}
	page = new Pagination(product_query);
	product_query(pageNo);
}); 
