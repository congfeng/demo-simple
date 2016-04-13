var tab1_fn = function(){
	console.log('tab1_fn');
	new IScroll('.main_content_nav', { 
		mouseWheel:true,
		//bounce:false,
		momentum: false,
		//bindToWrapper:true,
		click: true, 
		scrollX: true,
		scrollY: false,
		snap: true,
		snapSpeed: 400
	}).on('scrollEnd', function(){
		if(this.currentPage.x == 0){
			$('.dotty1').css('background','#06c1ae');
			$('.dotty2').css('background','#777');
		}else{
			$('.dotty1').css('background','#777');
			$('.dotty2').css('background','#06c1ae');
		}
	});
	$('#countdowner').scojs_countdown({
		until: _.round(_.now()/1000)+36000,
		strings: {h: '<span>:</span>', m: '<span>:</span>', s: ''}
	});
}
