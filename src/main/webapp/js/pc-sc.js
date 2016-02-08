// JavaScript Document
$(function(){

	$(".sc-input,.sc-xzqy").on("click", function(e){
		$(".sc-xzqy").show();
		
		$(document).one("click", function(){
			$(".sc-xzqy").hide();
		});
		
		e.stopPropagation();
	});
	
	function scXzqy(){
		$(".sc-xzqy-div input").click(function(){
			var ids = $(this).attr("ids");
			var _test = $(this).parent().text();
			if($(this).attr("checked")){
				$(".moren").remove();
				$(".sc-input").append("<span ids="+ ids +">"+ _test +"</span>");
				var a = 0;
				$.each($(".sc-xzqy-div input:checked"),function(e,f){
					a += 1;
				})
				if(a == $(".sc-xzqy-div li").length){
					$(".sc-xzan input").attr("checked",true)
					$(".sc-input").html("<span ids='a' class='moren'>全部</span>");
					$(".sc-xzqy-div input").attr("checked",true);
					$(".sc-xzan input").attr("checkdata","checked")
				}
				
			}else{
				$.each($(".sc-input span"),function(a,b){
					if($(this).attr("ids") == ids){
						$(this).remove();
					}
				})
				
				var _html = "";
				$.each($(".sc-xzqy-div input:checked"),function(b,c){
					var ids = $(this).attr("ids");
					var _test = $(this).parent().text();
					_html += "<span ids="+ ids +">"+ _test +"</span>"
				
				})
				
				$(".sc-input").html(_html);
				$(".sc-xzan input").attr("checked",false)
				$(".sc-xzan input").attr("checkdata","")
			}
		})
	}
	
	$(".sc-xzan input").click(function(){
			if($(this).attr("checkdata") == "checked"){
			$(this).attr("checked",false)
			$(this).attr("checkdata","")
			$(".sc-xzqy-div input").attr("checked",false)
			$(".sc-input span").remove();
		}else{
			$(this).attr("checked",true)
			$(".sc-input").html("<span ids='a' class='moren'>全部</span>");
			$(".sc-xzqy-div input").attr("checked",true);
			$(this).attr("checkdata","checked")
		}
		
		})
	document.title = "派车单生成";
	var d = new Date();
	var moth = (d.getMonth()+1);
		moth = moth<10?"0"+moth:moth;
	var dD = d.getDate()
		dD = dD<10?"0"+dD:dD;
	var date = d.getFullYear() + "-" + moth +"-" + dD ;
	$(".time1").val(date);
	$(".time2").val(date);
	var storageList = window.localStorage.storage;
	if(storageList == null){
		$(".query-button").hide();
		alert("库房列表已被清除，请您重新登录")
		return;
	}
	if(storageList == "null"){
		$(".query-button").hide();
		alert("库房列表已被清除，请您重新登录")
		return;
	}
	if(storageList == ""){
		$(".query-button").hide();
		return;
	}
	jzKf();
	function jzKf(){
		$(".thkf").html(storageList);
		xzSheng();
	}
	
	
	$(".thkf").change(function(){
		$(".city").html("<option value=''><option>");
		$(".sc-xzqy-div").html("");
		$(".sc-xzqy").hide();
		$(".sc-input").html("<span ids='a' class='moren'>全部</span>");
		$(".stree").html("<option value=''><option>");
		$(".table-list").html("");
		$(".g-all").html("");
		$(".sc-xzan input").attr("checked","checked")
		$(".sc-xzan input").attr("checkdata","checked")
		
		xzSheng();
	})
	
	function xzSheng(Sheng){
		$.ajax({
			url:'/region/storage/'+$(".thkf").val()+'/1/0',
			dataType:'json',
			success:function(data){
				if(data.s == 0){
					return ;
				}
				var sheng = "";
				if(data.regionList == ""){
					alert("请先设置该库房覆盖区域");
					$(".sheng").html("");
					$(".city").html("");
					$(".sc-xzqy-div").html("");
					$(".sc-xzqy").hide();
					$(".sc-input").html("<span ids='a' class='moren'>全部</span>");
					$(".sc-query-button").hide();
					$(".table-list").html("");
					$(".sc-xzan input").attr("checked","checked")
					$(".sc-xzan input").attr("checkdata","checked")
					return;
				}else{
					$(".sc-query-button").show();
				}
				$.each(data.regionList,function(e,f){
					sheng += "<option value="+f.id+" code="+f.code+">"+f.name+"</option>"
				})
				$(".sheng").html(sheng);
				xzCity();
				
				
			}
		})
	}
	$(".sheng").change(function(){
		$(".city").html("<option value=''><option>");
		$(".sc-xzqy-div").html("");
		$(".sc-xzqy").hide();
		$(".sc-input").html("<span ids='a' class='moren'>全部</span>");
		$(".stree").html("<option value=''><option>");
		$(".table-list").html("");
		$(".g-all").html("");
		$(".sc-xzan input").attr("checked","checked")
		$(".sc-xzan input").attr("checkdata","checked")
		xzCity();
	})
	
	function xzCity(city){
		$.ajax({
			url:'/region/storage/'+$(".thkf").val()+'/2/'+$(".sheng").val(),
			dataType:'json',
			success:function(data){
				if(data.s == 0){
					return ;
				}
				var shi = "";
				$.each(data.regionList,function(e,f){
					shi += "<option value="+f.id+" code="+f.code+">"+f.name+"</option>"
				})
				$(".city").html(shi);
				xzQy();
				
			}
		})
	}
	$(".city").change(function(){
		$(".sc-xzqy-div").html("");
		$(".sc-input").html("<span ids='a' class='moren'>全部</span>");
		$(".sc-xzqy").hide();
		$(".stree").html("<option value=''><option>");
		$(".table-list").html("");
		$(".g-all").html("");
		$(".sc-xzan input").attr("checked","checked")
		$(".sc-xzan input").attr("checkdata","checked")
		xzQy();
	})
	function xzQy(Qy){
		$.ajax({
			url:'/region/storage/'+$(".thkf").val()+'/3/'+$(".city").val(),
			dataType:'json',
			success:function(data){
				if(data.s == 0){
					return ;
				}
				var qy = "";
				$.each(data.regionList,function(e,f){
					qy += "<li><input type='checkbox' ids="+f.id+" checked='checked'>"+f.name+"</li>"
					
				})
				$(".sc-xzqy-div").html(qy);
				scXzqy()
				
			}
		})
	}
	// $(".district").change(function(){
		// if($(this).val() == ""){
			// $(".stree").html("<option value=''></option>");
			// return;
		// }
		// xzStree();
	// })
	
	// function xzStree(){
		// $.ajax({
			// url:'/region/storage/'+$(".thkf").val()+'/4/'+$(".district").val(),
			// dataType:'json',
			// success:function(data){
				// if(data.s == 0){
					// return ;
				// }
				// var stree = "<option value=''>全部</option>";
				// $.each(data.regionList,function(e,f){
					// stree += "<option value="+f.id+" code="+f.code+">"+f.name+"</option>"
				// })
				// $(".stree").html(stree);
			// }
		// })
	// }
	
    $(".sc-query-button").click(function(){
		// $(".table-list").html("");
		var districtId = new String;
		if($(".sc-input span")){
			$.each($(".sc-input span"),function(a,b){
				districtId += $(this).attr("ids") + ","
			})
			districtId = districtId.substring(0,districtId.length-1);
		}
		if(districtId == ""){
			alert("请选择区域");
			return;
		}else if(districtId == "a"){
			districtId = "";
		}
		$.ajax({
			 url:'/dcart/allocate',
			 data:{'storageId':$(".thkf").val(),'cityId':$(".city").val(),'districtId':districtId,'timeSlot':$(".shsj").val(),'reqDeliveryDayStart':$(".time1").val(),'reqDeliveryDayEnd':$(".time2").val(),'orderSourceText':$(".ddly").val(),'orderTypeText':$(".pslx").val()},
			 dataType:'json',
			 success:function(data){
			 	if(data.s == 0){
					return ;
				}
				var that = "table"; _html = "";	_html2 = "";
				var dogrouplist = data.doGrouplist;
				if(dogrouplist == null){
					$(".table-list").html("<div class='m20'>没有符合条件的数据</div>");
					return;
				}
				$.each(dogrouplist,function(k,v){
					//alert(v.streetName)
					var q = k+1;
					var o =  "<div class='list'><div class='list-many'><div class='list-table'><div class='border w70 h60'>"+q +"</div><div class='steer w100 h60 border close-button2'>"+ v.streetName +"<span class='close-button'></span></div>"
					_html +=o;
					$.each(v.buyerItems,function(k1,v1){
							var ID =""+v1.ids+""
							while(ID.indexOf(',') >= 0)
							ID = ID.replace(',','-');
							var m = v.buyerItems.length;
							var n ="<div class='rest' draggable='true' id='list-"+ID+"' ids="+v1.ids+" ><div class='border w100 h60 shmc'>"+v1.buyerName+"</div><div class='border w100 h60 qy'>"+v.districtName+"</div><div class='border w100 h60 xxdz'>"+v1.address+"</div><div class='border w70 h60'>"+v1.storageName+"</div><div class='border w100 h60 shsj'>"+v1.timeSlot+"</div><div class='border w70 h60 ds'>1</div><div class='border w70 h60'>"+v1.orderItemCount+"</div><div class='border w100 h60'>"+v1.orderItemMoney+"</div></div>"
							
							var b = "<div class='rest'><div class='border w100 h60'>"+v1.buyerName+"</div><div class='border w100 h60'>"+v.districtName+"</div><div class='border w100 h60'>"+v1.address+"</div><div class='border w70 h60'>"+v1.storageName+"</div><div class='border w100 h60'>"+v1.timeSlot+"</div><div class='border w70 h60 sc-shs'>"+m+"</div><div class='border w70 h60'>"+v.orderCount+"</div><div class='border w100 h60'>"+v1.orderItemMoney+"</div></div>"
							
						_html+=n;
						_html2 = b;
						
					})
					
					var o2 = "</div></div>"
					_html +=o2;
					var o3 = "<div class='list-one'><div class='list-table'><div class='border w70 h60'>"+q+"</div><div class='steer w100 h60 border open-button2'>"+ v.streetName +"<span   class='open-button'></span></div>"+_html2+""
					_html +=o3;
					_html += "</div></div></div>"
				})
				
				$(".table-list").html(_html);
				Rest();
				//如果只有一条则默认显示many
				// $(".table-list .sc-shs").each(function(v,b){
					// if($(this).text() == 1){
						// $(this).closest(".list").find(".list-many").show();
						// $(this).closest(".list").find(".list-one").hide();
					// }
				// })
			 }
			});
		
	 })
	 
	 
	
	 
	 
		
	function Rest(){
		var aLi = $('.rest');
		for (var i = 0; i < aLi.length; i++) {
		
        aLi[i].ondragstart = function(ev) { 
            $(this).css("background","#ccc")
			var ids = $(this).attr("ids")
			var qy = $(this).find(".qy").text();
			var ds = $(this).find(".ds").text();
			var shmc = $(this).find(".shmc").text();
			var xxdz = $(this).find(".xxdz").text();
			var shsj = $(this).find(".shsj").text();
			while(ids.indexOf(',') >= 0)
					ids = ids.replace(',','-');
			var xx =[ids,qy,ds,shmc,xxdz,shsj]
            ev.dataTransfer.setData("a", xx); 
            ev.dataTransfer.effectAllowed = 'all';
         //   ev.dataTransfer.setDragImage(this, 0, 0);
        };
        aLi[i].ondragend = function() { 
				$(this).css("background","none")
        };
		}
	}
	



		// 拖放到对应item 下，生成item元素
		function hh(){
			var inset = $('.item-all');
			for(i=0;i<inset.length;i++){
				inset[i].ondragover = function(ev) { 
					$(this).css("background","#ccc")
					ev.preventDefault(); 
				};
				inset[i].ondragleave = function() { 
					$(this).css("background","");
				};
				inset[i].ondrop = function(ev) { 
					$(this).css("background","");
					var h = $(this).find(".item-many .item").length + 1;
					
					var xx = ev.dataTransfer.getData('a').split(",");
					if(!bl(xx[0])){
						return;
					}
					$(this).find(".item-many").append("<div class='item a"+ xx[0] +"' ids='"+ xx[0] +"'><div class='w70 h30'>"+xx[1]+"</div><div class='w30 h30'>"+xx[2]+"</div><div class='w70 h30'>"+xx[3]+"</div><div class='w110 h30'>"+xx[5]+"</div><div class='w70 h30'>"+xx[4]+"</div><div class='w30 h30'><a href='javascript:void(0);' class='x'>X</a></div></div>");
					$(this).find(".item-one").append("<div class='item a"+ xx[0] +"' ids='"+ xx[0] +"'><div class='w70 h30'>"+xx[1]+"</div><div class='w30 h30'>"+h+"</div><div class='w70 h30'>"+xx[3]+"</div><div class='w110 h30'>"+xx[5]+"</div><div class='w70 h30'>"+xx[4]+"</div><div class='w30 h30'><a href='javascript:void(0);' class='x'></a></div></div>");	
					listHide($("#list-"+xx[0]));
					
					
				//alert(ev.dataTransfer.getData('a'));
				//alert(typeof ev.dataTransfer.getData('a'))
				//alert(ev.dataTransfer.types);
				
				};
				
			}
			
		}
		
		var New = $(".new-table");
		for(i=0;i<New.length;i++){
				New[i].ondragover = function(ev) { 
					$(this).css("background","#000")
					ev.preventDefault(); 
						// ev.dataTransfer.dropEffect = 'link'; 
				};
				New[i].ondragleave = function(ev) {  
					$(this).css("background","");
				};
			// 拖放到生成框下，新建item-all元素
			   New[i].ondrop = function(ev) { 
					$(this).css("background","");
					var xx = ev.dataTransfer.getData('a').split(",");
					if(!bl(xx[0])){
						return;
					}				
					$(this).parent().find(".g-all").append("<div class='item-all pr' ><a href='javascript:void(0)' class='item-button sc-item-button pa' style='background:url(images/item-button1.jpg)'></a><div class='item-many'><div class='item a"+ xx[0] +"' ids='"+ xx[0] +"'><div class='w70 h30'>"+xx[1]+"</div><div class='w30 h30'>"+xx[2]+"</div><div class='w70 h30'>"+xx[3]+"</div><div class='w110 h30'>"+xx[5]+"</div><div class='w70 h30'>"+xx[4]+"</div><div class='w30 h30'><a href='javascript:void(0);' class='x'>X</a></div></div></div><div class='item-one'><div class='item a"+ xx[0] +"' ids='"+ xx[0] +"'><div class='w70 h30'>"+xx[1]+"</div><div class='w30 h30'>"+xx[2]+"</div><div class='w70 h30'>"+xx[3]+"</div><div class='w110 h30'>"+xx[5]+"</div><div class='w70 h30'>"+xx[4]+"</div><div class='w30 h30'><a href='javascript:void(0);' class='x'></a></div></div></div></div>");
					hh();
					listHide($("#list-"+xx[0]));
					
					
					// alert(ev.dataTransfer.getData('a'));
					// alert(ev.dataTransfer.types);
					
				};
			};
		
		function bl(ids){
			var tru = true;
			$.each($(".g-all .item-all"),function(i){
				$.each($(this).find(".item-many .item"),function(a,b){
					if(ids == $(this).attr("ids")){
						alert("商户信息重复");
						var Ids = $(this).attr("ids")
						listHide($("#list-"+Ids));
						tru = false;
						return false;
					}
				})
				if(!tru){
					return false;
				}
			})
			return tru;			
		}
		
		function listHide(ev){
			$(ev).hide();
			var flag = true;
			$(ev).siblings(".rest").each(function(v,k){
				if(!$(this).is(":hidden")){
					flag = false;
				}
			})
			if(flag){
				$(ev).closest(".list").hide();
			}
		}
	
	$(".x").live("click",function(){
		var ids = $(this).closest(".item").attr("ids");
		
		$("#list-" + ids ).show();
		$("#list-" + ids ).closest(".list").show();
	//	$("#list-" + ids).focus();
		$("#list-" + ids ).css("background","none")
		$(this).closest(".item-all").find(".a"+ids).remove();
		  
		if($(".item-all .item-many .item").length == 0){
			$(".item-all").remove();
		}
		
	})
	
		var scpd = true;
		$(".sc-button").bind("click",function(){
			if(scpd){
				scpd = false;
				_this = $(this);
				var s = $(".g-scroll .item-many")
				var h = [];
				
				for(i=0;i<s.length;i++){
					var b = [];
					for(j=0;j<s.eq(i).find(".item").length;j++){
					 var q = s.eq(i).find(".item").eq(j).attr("ids");
					     q = q.replace(/-/g,",");
						b.push(q);
					}
					h.push(b.toString());
				}
				
				if(h == ""){
					alert("请添加派车单");
					scpd = true;
					return;
				}
				
				 $.post('/dcart/create',{carts:h},function(data){
						 alert("生成成功")
						$(".g-scroll .item-all").remove();
						scpd = true;
				 })
				 
			}else{
				return;
			}
				
		})
		
	

		
		

})

	
 	
