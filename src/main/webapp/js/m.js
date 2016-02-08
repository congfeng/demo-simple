// JavaScript Document

$(function(){
	// $.ajax({
		// url:'/storage/list/all',
		// dataType:'json',
		// success:function(data){
			// console.log(data.storageList)
		// }
	// })

	$(".xg").click(function(){
		$(this).siblings(".text").focus();
		$(".text").blur(function(){
			if($(this).val() != 5){
				$(this).siblings(".xg").html("恢复");
			}
		})
	})
	
	
	
	
	
})
	
