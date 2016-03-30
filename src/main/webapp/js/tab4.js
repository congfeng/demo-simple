var myScroll_tab4;
$(function(){
	var y = 0;
	if(!_.isEmpty(myScroll_tab4)){
		y = myScroll_tab4.y;
	}
	myScroll_tab4 = new IScroll('.main_tab4', {mouseWheel: true});
	myScroll_tab4.scrollBy(0,y);
})
