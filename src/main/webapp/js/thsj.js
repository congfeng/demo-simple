// JavaScript Document

$(function(){
	document.title = "提货时间";
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
		$(".district").html("<option value=''><option>");
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
					$(".district").html("");
					$(".cx-query-button").hide();
					$(".table-list").html("");
					return;
				}else{
					$(".cx-query-button").show();
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
		$(".district").html("<option value=''><option>");
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
		$(".district").html("<option value=''><option>");
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
				var qy = "<option value=''>全部</option>";
				$.each(data.regionList,function(e,f){
					qy += "<option value="+f.id+" code="+f.code+">"+f.name+"</option>"
				})
				$(".district").html(qy);
			}
		})
	}
	
	
	$(".thsj-query-button").unbind('click').click(function(){
			
		$.ajax({
			url:'/storageDistrict/list',
			data:{'storageId':$(".thkf").val(),'provinceId':$(".sheng").val(),'cityId':$(".city").val(),'districtId':$(".district").val()},
			dataType:'json',
			success:function(data){
				if(data.s == 0){
					return ;
				}
				var thsj = "";
				var index = 0;
				$.each(data.list,function(a,b){
					var deliverytime = "";
					 if(b.deliveryTime == null){
						 deliverytime = "";
					 }else{
						 deliverytime = b.deliveryTime;
					 }
					 index++;
					 thsj += "<div class='cxsz-list' data-ID="+b.storageDistrictId+" ><div class='border w100 h60'>"+ index +"</div><div class='border w100 h60'>"+b.storageName+"</div><div class='border w100 h60'>"+b.provinceName+"</div><div class='border w100 h60'>"+b.cityName+"</div><div class='border w100 h60'>"+b.districtName+"</div><div class='border w100 h60'>23:59:59</div><div class='border w100 h60'>次日</div><div class='border w150 h60'><input type='text' data-field='time' class='thsj-input' value='"+deliverytime+"' readonly></div></div>";
				})
				$(".table-list").html(thsj);
				$("#dtBox").DateTimePicker({
					dateFormat: "dd-MMM-yyyy"
				});
				
				
			}
		})
	})
	

	$(".thsj-input").die("change");
	$(".thsj-input").live("change",function(){
		var val = $(this).val();
		var dataId = $(this).closest(".cxsz-list").attr("data-ID");
		var isClear = false;
		if(val == ""){
			val = "05:00";
			$(this).val("05:00");
			isClear = true;
		}
		$.ajax({
			url:'/storageDistrict/newDeliveryTime',
			data:{'storageDistrictId':dataId,'newDeliveryTime':val},
			dataType:'json',
			success:function(data){
				if(data.s == 0){
					return ;
				}
	
				if(isClear){
					alert("重置为默认时间成功");
				}else{
					alert("修改成功");
				}
			}
		})
		
	})
	
})
