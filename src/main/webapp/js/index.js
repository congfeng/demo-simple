var cur_tab_id;
var default_cb_fn;
$(function(){
	FastClick.attach(document.body);
	$('.main_tab div').click(function(){
		if(cur_tab_id == this.id){
			return ;
		}
		cur_tab_id = this.id;
		$('.main_tab div i,span').css('color','#333');
		$(this.querySelectorAll('i,span')).css('color','#00CC99');
		$('.main').load($(this).data('page'),function(){
			$(this).focus();
			default_cb_fn();
		});
	});
	$('.main_tab #tab1').click();
})
