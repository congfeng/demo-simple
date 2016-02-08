// JavaScript Document

$(function(){
	document.title = "派车单查询";
	var d = new Date();
	var moth = (d.getMonth()+1);
		moth = moth<10?"0"+moth:moth;
	var dD = d.getDate()
		dD = dD<10?"0"+dD:dD;
	var date = d.getFullYear() + "-" + moth +"-" + dD ;
	
	$(".time1").val(date);
	$(".time2").val(date);
	
	var tj = {};
	var storageList = window.localStorage.storage;
	if(storageList == null){
		$(".query-button2").hide();
		alert("库房列表已被清除，请您重新登录")
		return;
	}
	if(storageList == "null"){
		$(".query-button2").hide();
		alert("库房列表已被清除，请您重新登录")
		return;
	}
	if(storageList == ""){
		$(".query-button2").hide();
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
		$(".stree").html("<option value=''><option>");
		$(".table-list-cx").html(""); 
		$(".fy").html(""); 
		xzSheng();
	})
	//加载司机单位列表
	$.ajax({
			url:'/shipper/list',
			dataType:'json',
			success:function(data){
				if(data.s == 0){
					return ;
				}
				var _html = "<option value=''>选择司机单位</option>";
				var xh = 0;
				$.each(data.shipperList,function(a,b){
					xh += 1;
					_html += "<option value="+b.id+">"+b.name+"</option>"
				})
				
				$(".driver-dw").html(_html);
			}
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
					$(".table-list-cx").html("");
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
		$(".stree").html("<option value=''><option>");
		$(".table-list-cx").html(""); 
		$(".fy").html(""); 
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
		$(".stree").html("<option value=''><option>");
		$(".table-list-cx").html(""); 
		$(".fy").html(""); 
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
	$(".district").change(function(){
		$(".table-list-cx").html(""); 
		$(".fy").html(""); 
		if($(this).val() == ""){
			$(".stree").html("<option value=''></option>");
			return;
		}
		xzStree();
	})
	function xzStree(){
		$.ajax({
			url:'/region/storage/'+$(".thkf").val()+'/4/'+$(".district").val(),
			dataType:'json',
			success:function(data){
				if(data.s == 0){
					return ;
				}
				var stree = "<option value=''>全部</option>";
				$.each(data.regionList,function(e,f){
					stree += "<option value="+f.id+" code="+f.code+">"+f.name+"</option>"
				})
				$(".stree").html(stree);
			}
		})
	}

	function pcCx(num){
		
		
		$.ajax({
			 url:'/dcart/statistics/list',
			 data:{
				 'dCartStateText':tj.dCartStateText,
				 'dCartId':tj.dCartId,
				 'cityId':tj.cityId,
				 'storageId':tj.storageId,
				 'districtId':tj.districtId,
				 'createDateStratText':tj.createDateStratText,
				 'createDateEndText':tj.createDateEndText,
				 'orderNo':tj.orderNo,
				 'driverName':tj.driverName,
				 'shipperId':tj.shipperId,
				 'streetId':tj.streetId,
				 //'dCartDelayText':$(".pc-yc").val(),
				 'pageNo':num},
			 dataType:'json',
			 success:function(data){
				 if(data.s == 0){
					 return;
				 }
				 var _html = "";
				 if(data.dCartList ==""){
					$(".table-list-cx").html("<div class='m20'>此条件下没有数据</div>");
					$(".fy").html("");
				 return;
				 }
				 $.each(data.dCartList,function(a,b){
						var state = b.state;
						if(state == "Init"){
							state = "已生成";
						}else if(state == "WaitAllocateCart"){
							state = "已生成";
						}else if(state == "AssignedDriver"){
							state = "已分配";
						}else if(state == "Pickuped"){
							state = "已提货";
						}else if(state == "Delivered"){
							state = "已送达";
						}else if(state == "Finish"){
							state = "已返回";
						}
						var driverName = b.driverName?b.driverName:'';
						var pickupTimeText = b.pickupTimeText?b.pickupTimeText:'';
						var deliveryTimeText = b.deliveryTimeText?b.deliveryTimeText:'';
						var outTakeMoney = b.outTakeMoney?b.outTakeMoney.toFixed(2):0;
						if(pickupTimeText){
							pickupTimeText = pickupTimeText.replace(' ','<br />')
						}
						if(deliveryTimeText){
							deliveryTimeText = deliveryTimeText.replace(' ','<br />')
						}
						
					  _html += "<div class='rest'><div class='w100 h60 border' ><a href='2pc-xq.html?id="+b.id+"' target='_blank'>"+b.id+"</a></div><div class='border w100 h60'>"+b.storageName+"</div><div class='border w100 h60'>"+b.districtName+"</div><div class='border w100 h60'>"+b.orderCount+"</div><div class='border w100 h60'>"+b.buyerCount+"</div><div class='border w100 h60'>"+b.orderMoney.toFixed(2)+"</div><div class='border w100 h60'>"+outTakeMoney+"</div><div class='border w100 h60'><a href='3sj-xq.html?driverId="+ b.driverId +"' target='_blank'>"+driverName+"</a><br /><span class='color-hs'>"+state+"</span></div><div class='border w100 h60'>"+pickupTimeText+"</div><div class='border w100 h60'>"+deliveryTimeText+"</div><div class='border w100 h60'>"+b.deliveredOrderCount+"</div></div>"
				 })
				 $(".table-list-cx").html(_html); 
				 
				 var fy = "<span class='fl ml20'>共有"+data.dCartCountInfo.count+"条派车单</span><span  class='fl ml5'>"+data.dCartCountInfo.countOrder+"条订单</span>&nbsp;<a href='javascript:void(0)' class='page-sy'>首页</a><a href='javascript:void(0)' class='page-up'>上一页</a><span class='fy-an'>";
				 $.each(data.pageIndex,function(q,w){
					 if(w == data.pageNo){
						fy += "<a href='javascript:void(0)' ids="+w+" class='on'>"+w+"</a>";
					 }else{
						 fy += "<a href='javascript:void(0)' ids="+w+">"+w+"</a>";
					 }
					 
				 })
				 fy += "</span><a href='javascript:void(0)' class='page-down'>下一页</a><a href='javascript:void(0)' class='page-wy'>尾页</a>共"+data.pageMaxNo+"页";
				 $(".fy").html(fy);
				 $(".fy-an a").click(function(){
					 pcCx($(this).attr("ids"));
				 })
				 var pageUp = Number(data.pageNo) - 1;
				 if(pageUp <= 0){
					 pageUp = 1;
				 }
				 if(data.pageNo == 1){
					 $(".fy .page-up").css("cursor","default")
					 $(".fy .page-sy").css("cursor","default")
				 }else{
					 $(".fy .page-up").click(function(){
						 pcCx(pageUp);
					 })
					 $(".page-sy").click(function(){
					 pcCx(1);
				 })
				 }
				 
				 var pageDown = Number(data.pageNo) +1;
				 if(pageDown >= data.pageMaxNo){
					 pageDown = data.pageMaxNo;
				 }
				 if(data.pageNo == data.pageMaxNo){
					 $(".fy .page-down").css("cursor","default")
					 $(".fy .page-wy").css("cursor","default")
				 }else{
					 $(".fy .page-down").click(function(){
						 pcCx(pageDown);
					 })
					 $(".page-wy").click(function(){
					 pcCx(data.pageMaxNo);
				 })
				 }
				 
				 
			 }
		});
	}
	

	$(".cx-query-button").click(function(){
		if(!isNaN($(".pcNo").val())){
		}else{
			alert("派车单编号只能为数字");
			$(".pcNo").val("");
			return;
		}
		if($(".driverName").val() == ""){
		}else{
			var myReg = /^[\u4e00-\u9fa5]+$/;
			if (myReg.test($(".driverName").val())) {
				
			}else{
				alert("司机姓名只能输入汉字");
				$(".driverName").val("");
				return;
			}
		}
		tjj();
		pcCx(1);
		});
	function tjj(){
		tj.dCartStateText = $(".pc-state").val();
		tj.dCartId = $(".pcNo").val()
		tj.cityId = $(".city").val()
		tj.storageId = $(".thkf").val()
		tj.districtId = $(".district").val()
		tj.createDateStratText = $(".time1").val()
		tj.createDateEndText = $(".time2").val()
		tj.orderNo = $(".ddId").val()
		tj.driverName = $(".driverName").val()
		tj.shipperId = $(".driver-dw").val()
		tj.streetId = $(".stree").val()
	}
	
	$(".pc-dc").click(function(){
		if(tj.dCartStateText == undefined){
			alert("请先查询")
			return;
		}
		$(".pc-state2").val(tj.dCartStateText)
		$(".pcNo2").val(tj.dCartId)
		$(".city2").val(tj.cityId)
		$(".thkf2").val(tj.storageId)
		$(".district2").val(tj.districtId)
		$(".time12").val(tj.createDateStratText)
		$(".time22").val(tj.createDateEndText)
		$(".ddId2").val(tj.orderNo)
		$(".driverName2").val(tj.driverName)
		$(".driver-dw2").val(tj.shipperId)
		$(".stree2").val(tj.streetId)
		$.ajax({
	  		async:false,
			 url:'/dcart/statistics/list',
			 data:{
				 'dCartStateText':tj.dCartStateText,
				 'dCartId':tj.dCartId,
				 'cityId':tj.cityId,
				 'storageId':tj.storageId,
				 'districtId':tj.districtId ,
				 'createDateStratText':tj.createDateStratText,
				 'createDateEndText':tj.createDateEndText,
				 'orderNo':tj.orderNo,
				 'driverName':tj.driverName,
				 'shipperId':tj.shipperId,
				 'streetId':tj.streetId,
				 'pageNo':1
				 },
			 dataType:'json',
			 success:function(data){
			 	if(data.s == 0){
					return;
				}
				if(data.dCartList ==""){
					alert("此条件无数据可导出")
					return;
				}
				$("#form1").submit();
				
				
			 }
	   })
	});
	
	

})
	
 	
