$(function(){
	$.ajax({
		type: 'POST',
		dataType: 'json',
		url: '/profile/login',
		data: {username:'丛峰',password:'congfeng'},
		success: function(data){
			console.log(data);
		}
	});
})
