// JavaScript Document

$(function(){
	//左侧导航伸缩
	$(".parentMenu span").click(function(){
		 $(this).parent().find(".second").slideToggle(200);	
	})
	//右上角本地时间
	setInterval(function() {
    var now = (new Date()).toLocaleString();
    $('.time').text(now);
	}, 1000);
	

	// 订单全选
	$(".list-title-check").click(function(){
		if($(this).attr("checkdata") == "true"){
		 $(this).attr("checked",false);
		 $(".list-check").attr("checked",false);
		 $(this).attr("checkdata" ,"");
		}else{
			$(this).attr("checked",true);
			$(".list check").attr("checked",true);
			$(".list-check").attr("checked",true);
			$(this).attr("checkdata" ,"true")
	     
		}
			
	})
	
	// 订单伸缩
	$(".open-button").live("click",function(){
		$(this).closest(".list").find(".list-many").show();
		$(this).closest(".list").find(".list-one").hide();
	})
	$(".close-button").live("click",function(){
		$(this).closest(".list").find(".list-many").hide();
		$(this).closest(".list").find(".list-one").show();
	})
		
		var s = "<div class='list'><div class='list-many'><div class='list-table'><div class='border w100 h60'><input type='checkbox' class='list-check' /></div><div class='steer w100 h60 border'>张江高科技园区<a href='javascript:void(0);'  class='close-button'><img src='images/img.png'  /></a></div><div class='rest' draggable='true' ids='hs1' id='hs1'><div class='border w100 h60'><input type='checkbox' />串串香</div><div class='border w100 h60'>浦东新区</div><div class='border w100 h60'>碧波路300号</div><div class='border w100 h60'>浦东仓</div><div class='border w100 h60'>9:00-10:00</div><div class='border w100 h60'>1</div><div class='border w100 h60'>5</div><div class='border w100 h60'>700.00</div></div><div class='rest' draggable='true' ids='hs2' id='hs2'><div class='border w100 h60'><input type='checkbox' />串串香</div><div class='border w100 h60'>浦东新区</div><div class='border w100 h60'>碧波路300号</div><div class='border w100 h60'>浦东仓</div><div class='border w100 h60'>9:00-10:00</div><div class='border w100 h60'>1</div><div class='border w100 h60'>5</div><div class='border w100 h60'>700.00</div></div><div class='rest' draggable='true' ids='hs3' id='hs3'><div class='border w100 h60'><input type='checkbox' />串串香</div><div class='border w100 h60'>浦东新区</div><div class='border w100 h60'>碧波路300号</div><div class='border w100 h60'>浦东仓</div><div class='border w100 h60'>9:00-10:00</div><div class='border w100 h60'>1</div><div class='border w100 h60'>5</div><div class='border w100 h60'>700.00</div></div><div class='rest' draggable='true' ids='hs4' id='hs4'><div class='border w100 h60'><input type='checkbox' />串串香</div><div class='border w100 h60'>浦东新区</div><div class='border w100 h60'>碧波路300号</div><div class='border w100 h60'>浦东仓</div><div class='border w100 h60'>9:00-10:00</div><div class='border w100 h60'>1</div><div class='border w100 h60'>5</div><div class='border w100 h60'>700.00</div></div><div class='rest' draggable='true' ids='hs5' id='hs5'><div class='border w100 h60'><input type='checkbox' />串串香</div><div class='border w100 h60'>浦东新区</div><div class='border w100 h60'>碧波路300号</div><div class='border w100 h60'>浦东仓</div><div class='border w100 h60'>9:00-10:00</div><div class='border w100 h60'>1</div><div class='border w100 h60'>5</div><div class='border w100 h60'>700.00</div></div></div></div><div class='list-one'><div class='list-table'><div class='border w100 h60'><input type='checkbox' class='list-check' /></div><div class='steer w100 h60 border'>街道名称<a href='javascript:void(0);'  class='open-button'><img src='images/img.png' /></a></div><div class='rest' style='display:block;'><div class='border w100 h60'>串串香</div><div class='border w100 h60'>浦东新区</div><div class='border w100 h60'>碧波路300号</div><div class='border w100 h60'>浦东仓</div><div class='border w100 h60'>9:00-10:00</div><div class='border w100 h60'>1</div><div class='border w100 h60'>5</div><div class='border w100 h60'>700.00</div></div></div></div></div>"
	    
		$(".table").append(s);

    var aLi = $('.rest');
    var aA = $('a');
	var New = $(".new-table");
	
    for (var i = 0; i < aLi.length; i++) {
        aLi[i].ondragstart = function(ev) { //鎷栨嫿鍓嶈Е鍙?
            $(this).css("background","#ccc")
			var ID = $(this).attr("ids")
            ev.dataTransfer.setData('a', ID); //瀛樺偍涓€涓敭鍊煎 : value鍊煎繀椤绘槸瀛楃涓?
            ev.dataTransfer.effectAllowed = 'all';
         //   ev.dataTransfer.setDragImage(this, 0, 0);
        };
        aLi[i].ondragend = function() { //鎷栨嫿缁撴潫瑙﹀彂 
				$(this).hide();
        }; 
		}

		// 拖放到对应item 下，生成item元素
		function hh(){
			var inset = $('.item-all');
			for(i=0;i<inset.length;i++){
				inset[i].ondragover = function(ev) { //杩涘叆鐩爣銆佺寮€鐩爣涔嬮棿锛岃繛缁Е鍙?
					$(this).css("background","#ccc")
					ev.preventDefault(); //闃绘榛樿浜嬩欢锛氬厓绱犲氨鍙互閲婃斁浜?
				};
				inset[i].ondragleave = function() { //鐩稿綋浜巓nmouseout 
					$(this).css("background","");
				};
				inset[i].ondrop = function(ev) { //閲婃斁榧犳爣鐨勬椂鍊欒Е鍙?
					$(this).css("background","");
					var h = $(this).find(".item-many .item").length + 1;
					var ids = ev.dataTransfer.getData('a');
					$(this).find(".item-many").append("<div class='item' ids='"+ ids +"'><div class='w70 h30'>浦东新区222</div><div class='w30 h30'>"+ h +"</div><div class='w70 h30'>串串香</div><div class='w110 h30'>碧波路300号</div><div class='w70 h30'>9:00-10:00</div><div class='w30 h30'><a href='javascript:void(0);' class='x'>X</a></div></div>");
					$(this).find(".item-one").append("<div class='item'><div class='w70 h30'>浦东新区222</div><div class='w30 h30'>"+ h +"</div><div class='w70 h30'>串串香</div><div class='w110 h30'>碧波路300号</div><div class='w70 h30'>9:00-10:00</div><div class='w30 h30'><a href='javascript:void(0);' class='x'>X</a></div></div>");	
				//alert(ev.dataTransfer.getData('a'));
				//alert(ev.dataTransfer.types);
				
				};
				
			}
			
		}
		
		for(i=0;i<New.length;i++){
				New[i].ondragover = function(ev) { //杩涘叆鐩爣銆佺寮€鐩爣涔嬮棿锛岃繛缁Е鍙?
					$(this).css("background","#000")
					ev.preventDefault(); //闃绘榛樿浜嬩欢锛氬厓绱犲氨鍙互閲婃斁浜?
						// ev.dataTransfer.dropEffect = 'link'; //鐪熷澶栭儴鏂囦欢 
				};
				New[i].ondragleave = function(ev) { //鐩稿綋浜巓nmouseout 
					$(this).css("background","");
				};
			// 拖放到生成框下，新建item-all元素
			   New[i].ondrop = function(ev) { //閲婃斁榧犳爣鐨勬椂鍊欒Е鍙?
					$(this).css("background","");
					var ids = ev.dataTransfer.getData('a');
					//ondragleave='g(this)' ondragover='h(this)' ondrop='f(this)'
					$(this).parent().find(".g-table .g-scroll").append("<div class='item-all pr' ><a href='#' class='item-button pa' style='background:url(images/item-button1.jpg)'></a><div class='item-many'><div class='item' ids='"+ids+"'><div class='w70 h30'>浦东新区</div><div class='w30 h30'></div><div class='w70 h30'>串串香</div><div class='w110 h30'>碧波路300号</div><div class='w70 h30'>9:00-10:00</div><div class='w30 h30'><a href='javascript:void(0);' class='x'>X</a></div></div></div><div class='item-one'><div class='item' ids='"+ids+"'><div class='w70 h30'>浦东新区</div><div class='w30 h30'></div><div class='w70 h30'>串串香</div><div class='w110 h30'>碧波路300号</div><div class='w70 h30'>9:00-10:00</div><div class='w30 h30'><a href='javascript:void(0);' class='x'>X</a></div></div></div></div>");
					hh();
					// alert(ev.dataTransfer.getData('a'));
					// alert(ev.dataTransfer.types);
					
				};
			};
		
		
			function X(){
			$(".x").live("click",function(){
				var ids = $(this).closest(".item").attr("ids");
				$("#" + ids ).show();
				$("#" + ids ).css("background","none")
				$(this).closest(".item").remove();
				
			})
			}
			X();
		
		
		

			$(".item-all .item-button").live("click",function(){
				if($(this).attr("style") == "background:url(images/item-button1.jpg)"){
					$(this).attr("style","background:url(images/item-button2.jpg)");
					$(this).parent().find(".item-many").slideDown(200);
					$(this).parent().find(".item-one") .hide();
				}else{
					$(this).attr("style","background:url(images/item-button1.jpg)");
					$(this).parent().find(".item-many").slideUp(200);
					$(this).parent().find(".item-one") .show();
				}
			})
	
	
		
		
		

		
})
	
/* 	$.ajax({
	 url:'http://localhost:8080/data/cart.json',
	 dataType:'json',
	 success:function(data){
		 	alert(data.cartList.length)
		 	for(i=0;i<data.cartList.length;i++){
		  	$(".table").html("<div class='list'><div class='border w100 h60'><input type='checkbox' /></div><div class='steer w100 h60 border'>" + data.cartList[i].name + "<img src='images/img.png' class='rest-an' /></div><div class='rest' style='display:block;'><div class='border w100 h60'>串串香</div><div class='border w100 h60'>浦东新区</div><div class='border w100 h60'>碧波路300号</div><div class='border w100 h60'>浦东仓</div><div class='border w100 h60'>9:00-10:00</div><div class='border w100 h60'>1</div><div class='border w100 h60'>5</div><div class='border w100 h60'>700.00</div></div></div>")

		 }
		 
		 
		
		
		<div class="list">
        <div class="list-many">
			<div class="list-table">
        	<div class="border w100 h60"><input type="checkbox" class="list-check" /></div><div class="steer w100 h60 border">张江高科技园区<a href="javascript:void(0);"  class="close-button"><img src="images/img.png"  /></a></div>
            <div class="rest" draggable="true">
            	<div class="border w100 h60"><input type="checkbox" />串串香</div><div class="border w100 h60">浦东新区</div><div class="border w100 h60">碧波路300号</div><div class="border w100 h60">浦东仓</div><div class="border w100 h60">9:00-10:00</div><div class="border w100 h60">1</div><div class="border w100 h60">5</div><div class="border w100 h60">700.00</div>
            </div>
            <div class="rest" draggable="true">
            	<div class="border w100 h60"><input type="checkbox" />串串香</div><div class="border w100 h60">浦东新区</div><div class="border w100 h60">碧波路300号</div><div class="border w100 h60">浦东仓</div><div class="border w100 h60">9:00-10:00</div><div class="border w100 h60">1</div><div class="border w100 h60">5</div><div class="border w100 h60">700.00</div>
            </div>
            <div class="rest" draggable="true">
            	<div class="border w100 h60"><input type="checkbox" />串串香</div><div class="border w100 h60">浦东新区</div><div class="border w100 h60">碧波路300号</div><div class="border w100 h60">浦东仓</div><div class="border w100 h60">9:00-10:00</div><div class="border w100 h60">1</div><div class="border w100 h60">5</div><div class="border w100 h60">700.00</div>
            </div>
			<div class="rest" draggable="true">
            	<div class="border w100 h60"><input type="checkbox" />串串香</div><div class="border w100 h60">浦东新区</div><div class="border w100 h60">碧波路300号</div><div class="border w100 h60">浦东仓</div><div class="border w100 h60">9:00-10:00</div><div class="border w100 h60">1</div><div class="border w100 h60">5</div><div class="border w100 h60">700.00</div>
            </div>
			<div class="rest" draggable="true">
            	<div class="border w100 h60"><input type="checkbox" />串串香</div><div class="border w100 h60">浦东新区</div><div class="border w100 h60">碧波路300号</div><div class="border w100 h60">浦东仓</div><div class="border w100 h60">9:00-10:00</div><div class="border w100 h60">1</div><div class="border w100 h60">5</div><div class="border w100 h60">700.00</div>
            </div>
			</div>
        </div>
        <div class="list-one">
			<div class="list-table">
        	<div class="border w100 h60"><input type="checkbox" class="list-check" /></div><div class="steer w100 h60 border">街道名称<a href="javascript:void(0);"  class="open-button"><img src="images/img.png" /></a></div>
            <div class="rest" style="display:block;">
            	<div class="border w100 h60">串串香</div><div class="border w100 h60">浦东新区</div><div class="border w100 h60">碧波路300号</div><div class="border w100 h60">浦东仓</div><div class="border w100 h60">9:00-10:00</div><div class="border w100 h60">1</div><div class="border w100 h60">5</div><div class="border w100 h60">700.00</div>
            </div>
			</div>
        </div>
		</div>
		 
		 

	 });
	
		
	*/
	
