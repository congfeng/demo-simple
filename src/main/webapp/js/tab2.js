var myScroll_tab2;
$(function(){
	var y = 0;
	if(!_.isEmpty(myScroll_tab2)){
		y = myScroll_tab2.y;
	}
	myScroll_tab2 = new IScroll('.main_tab2', {mouseWheel: true});
	myScroll_tab2.scrollBy(0,y);
})