nsApp.controller('ProductUpdateController',function($scope,$routeParams) {  
	$.ajax({
		url:'/product/find',
		data:{'id':$routeParams.id},
		dataType:'json',
		success:function(data){
			if(data&&data.s == 0){
				return;
			}
			$('#ptype').text(data.productType);
		}
	});
	
}); 