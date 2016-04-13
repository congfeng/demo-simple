var cur_tab_id;
var main_content = {};
$(function(){
	$('.maintab div').click(function(){
		if(cur_tab_id == this.id){
			return ;
		}
		cur_tab_id = this.id;
		$('.maintab div i,span').css('color','#333');
		$(this.querySelectorAll('i,span')).css('color','#00CC99');
		var cur_tab = $(this);
		var main_page = cur_tab.data('page');
		var main_cb = cur_tab.data('cb');
		$('.main').load(main_page,function(){
			eval(main_cb)();
			var main_content_y = 0;
			if(!_.isEmpty(main_content[cur_tab_id])){
				main_content_y = main_content[cur_tab_id].y;
			}
			main_content[cur_tab_id] = new IScroll('.main_content', {mouseWheel: true});
			main_content[cur_tab_id].scrollBy(0,main_content_y);
		});
	});
	$('.maintab #tab1').click();
})
document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);

