$(function(){
	document.title = "库房设置";
	$.ajax({
			url:'/storage/list/all',
			dataType:'json',
			success:function(data){		
				if(data.s == 0){
					return ;
				}	
				var ck = "";
				$.each(data.storageList,function(a,b){
					ck += "<option value='"+b.id+"' code="+b.code+">"+b.name+"</option>"
				})
				$(".xzkf").html(ck);
				
			}
		})
	$(".xzkf").change(function(){
		var ID = $(this).val();
		xzSheng2();
		$(".kf-qy").hide();
		$(".qy-all").html("");
	})
	function xzSheng2(){
		$.ajax({
			url:'/region/province',
			dataType:'json',
			success:function(data){
				if(data.s == 0){
					return ;
				}
				$(".city").html("<option value='qxzcs'>请选择城市</option>");
				var sheng = "<option value='xzsf'>请选择省份</option>"
				$.each(data.regionList,function(c,d){
					sheng += "<option value="+d.id+" code="+d.code+">"+d.name+"</option>"
				})
				$(".sheng").html(sheng);
				
			}
		})
	}
	$(".sheng").change(function(){
		$(".city").html("<option value='qxzcs'>请选择城市</option>");
		$(".kf-qy").hide();
		if($(this).val() == "xzsf"){
			return;
		}
		var ID = $(this).val();
		xzCity2(ID);
	})
	xzSheng2();
	function xzCity2(city){
		$.ajax({
			url:'/region/citys/enabled/'+city,
			dataType:'json',
			success:function(data){
				if(data.s == 0){
					return ;
				}
				var shi = "<option value='qxzcs'>请选择城市</option>";
				$.each(data.regionList,function(e,f){
					shi += "<option value="+f.id+" code="+f.code+">"+f.name+"</option>"
				})
				$(".city").html(shi);
				
				
			}
		})
	}
	$(".city").change(function(){
		if($(this).val() == "qxzcs"){
			$(".kf-qy").hide();
			return;
		}
		xzQy2($(this).val());
		$(".login-jz").show();
		$(".kf-qy").show();
	})
	function xzQy2(Qy){
		$.ajax({
			url:'/region/district/tree/storage/'+Qy,
			dataType:'json',
			success:function(data){
				if(data.s == 0){
					$(".login-jz").hide();
					return ;
				}
				var qy = "";
				 $.each(data.districtTreeStorageList,function(g,h){
					
					qy += "<div class='item-all pr' ><a href='javascript:void(0);' class='item-button pa kf-item-button' style='background:url(images/item-button1.jpg)'></a><div class='item' id="+h.regionId+"><input type='checkbox' class='parent"+h.regionId+"' value="+h.regionId+" />"+h.regionName+"</div><div class='kf-item'><ul>"
					var Ids = h.regionId;
								$.each(h.childs,function(j,k){
									var s = "";
									var c = "";
									var n = "";
									if(k.storageId == null){
										s = "";
									}else if(k.storageId == $(".xzkf").val()){
										s = "checked='checked'";
									}else if(k.storageId !== null){
										c = "class='hs' kfName='"+k.storageName+"'";
									}else{
										s = "checked='checked'";
										$(".parent"+h.regionId).attr("checked","checked");
									}
									qy += "<li "+c+"><input type='checkbox' value="+k.regionId+" "+s+" />"+k.regionName+"</li>"
								})
								
								
					qy += "</ul></div></div>"
				 })
				$(".login-jz").hide();
				$(".qy-all").html(qy);
				kfSzinput();
				$.each($(".item-all .kf-item"),function(u,i){
					var numbe = 0;
					var zs = $(this).find("input").length;
					$.each($(this).find("input:checked"),function(a,b){
						numbe += 1;
					})
					if(numbe == zs){
					$(this).closest(".item-all").find(".item input").attr("checkdata","checked")
					$(this).closest(".item-all").find(".item input").attr("checked","checked")
					}
				})
			}
		})
	}
	
	
	
	$(".hs input").live("click",function(){
		var name = $(this).parent().attr("kfname");
		if(confirm("该街道已被"+name+"覆盖，是否更改？")){
			$(this).parent().removeClass("hs");
			$(this).parent().addClass("hss");
		}else{
			$(this).attr("checked",false);
			
			var parentInput = $(this).closest(".item-all").find(".parent44");
			if(parentInput.is(':checked')){
				parentInput.attr("checked",false);
			}
		}
	})
	$(".hss input").live("click",function(){
		$(this).parent().addClass("hs");
		$(this).parent().removeClass("hss");
	})
	$(".kf-query-button").click(function(){
		$(".login-jz").show();
		var KFID = $(".xzkf").val();
		var SHENG = $(".sheng").val();
		var CITY = $(".city").val();
		var ID = "";
		
		
		$.each($(".item-all .kf-item"),function(u,i){
			if($(this).find("input:checked").length >= 1){
				var quId = $(this).parent().find(".item").find("input").val();
				ID += "&regionIds=" + quId + "|"
				var JDID = []
				$.each($(this).find("input:checked"),function(a,b){
					JDID.push($(this).val());
				})
				ID += JDID;
			}
		})
		$.ajax({
			url:'/storage/saveStorageDistrict?storageId='+KFID+'&provinceId='+SHENG+'&cityId='+CITY+ID,
			dataType:'json',
			success:function(data){
				if(data.s == 0){
					$(".login-jz").hide();
					return ;
				}
				$(".login-jz").hide();
				alert("保存成功")
			}
		})
	})
	
	
	
	function kfSzinput(){
		$(".kf-item input").click(function(){
			
			$.each($(this).closest(".item-all").find(".kf-item"),function(u,i){
						var numbe = 0;
						var zs = $(this).find("input").length;
						$.each($(this).find("input:checked"),function(a,b){
							numbe += 1;
						})
						if(numbe == zs){
						$(this).closest(".item-all").find(".item input").attr("checkdata","checked")
						$(this).closest(".item-all").find(".item input").attr("checked","checked")
						}else{
							$(this).closest(".item-all").find(".item input").attr("checkdata","")
							$(this).closest(".item-all").find(".item input").attr("checked",false)
						}
			})
		})
			

		
		
		// $(".kf-item li input").each(function(k,v){
				// if($(this).attr("checked") == "checked"){
					// alert($(this).attr("ids"))
				// }
		// })
		$(".item input").click(function(){
			if($(this).attr("checkdata") == "checked"){
			$(this).attr("checked",false)
			$(this).closest(".item-all").find(".kf-item input").attr("checked",false)
			$(this).attr("checkdata","")
		}else{
			
			$(this).attr("checked",true)
			$(this).closest(".item-all").find(".kf-item input").attr("checked",true)
			$(this).attr("checkdata","checked")
			$.each($(this).closest(".item-all").find(".kf-item").find("li"),function(a,b){
				if($(this).attr("class") == "hs"){
					var name = $(this).attr("kfname");
					if(confirm("该街道已被"+name+"覆盖，是否更改？")){
						$(this).parent().find("li").removeClass("hs");
						$(this).parent().find("li").addClass("hss");
					}else{
						$(this).parent().find("input").attr("checked",false);
						$(this).closest(".item-all").find(".item input").attr("checked",false);
						$(this).closest(".item-all").find(".item input").attr("checkdata","");
						return false;
					}
				}
			})
			
		}
		
		})
	}
})