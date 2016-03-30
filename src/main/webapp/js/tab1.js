var myScroll_tab1;
$(function(){
	var y = 0;
	if(!_.isEmpty(myScroll_tab1)){
		y = myScroll_tab1.y;
	}
	myScroll_tab1 = new IScroll('.main_tab1', {mouseWheel: true});
	myScroll_tab1.scrollBy(0,y);
	new IScroll('.main_nav_tab1', { 
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
})
