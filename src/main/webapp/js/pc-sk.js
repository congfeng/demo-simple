// JavaScript Document

$(function(){
	document.title = "派车单收款";
	var d = new Date();
	var moth = (d.getMonth()+1);
		moth = moth<10?"0"+moth:moth;
	var dD = d.getDate()
		dD = dD<10?"0"+dD:dD;
	var date = d.getFullYear() + "-" + moth +"-" + dD ;
	$(".time1").val(date);
	$(".time2").val(date);
	var sktj = {};
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
					$(".query-button2").hide();
					$(".table-list-sk").html("");
					return;
				}else{
					$(".query-button2").show();
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
		xzCity();
	})
	
	function xzCity(city){
		$.ajax({
			url:'/region/storage/'+$(".thkf").val()+'/2/'+$(".sheng").val(),
			dataType:'json',
			success:function(data){
				if(data.s == 0){
					return;
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
	
	function pcSk(num){
		
		$.ajax({
			url:'/dcart/receivables/list',
			data:{
				 'dCartStateText':sktj.dCartStateText,
				 'dCartId':sktj.dCartId,
				 'cityId':sktj.cityId,
				 'storageId':sktj.storageId,
				 'districtId':sktj.districtId,
				 'createDateStratText':sktj.createDateStratText,
				 'createDateEndText':sktj.createDateEndText,
				 'orderNo':sktj.orderNo,
				 'driverName':sktj.driverName,
				 'shipperId':sktj.shipperId,
				 'streetId':sktj.streetId,
				 'dCartReceivablesText':sktj.dCartReceivablesText,
				 'pageNo':num},
			dataType:'json',
			success:function(data){
				if(data.s == 0){
					return ;
				}
				var _html = "";
				if(data.dCartList == ""){
					$(".table-list-sk").html("<div class='m20'>该条件无查询结果</div>");
					$(".fy").html("");
					return;
				}
				$.each(data.dCartList,function(a,b){
					var state = b.state;
					var yjk = b.collectionAmount?b.collectionAmount:"0"
					var skan = "";
					var driverName = b.driverName?b.driverName:'';
					var deliveryTimeText = b.deliveryTimeText?b.deliveryTimeText:'';
					var orderReceivablesMoney = b.orderReceivablesMoney;
					if(!orderReceivablesMoney){
						orderReceivablesMoney = 0;
					}else if(orderReceivablesMoney&&orderReceivablesMoney<0){
						orderReceivablesMoney = 0;
					}
					orderReceivablesMoney = orderReceivablesMoney.toFixed(2);
					var receivablesOperator = b.receivablesOperator?b.receivablesOperator:'';
					if(b.receivables){
						skan = "<a href='2sk-xq.html?dCartId="+b.id+"' target='_blank'>收款详情</a>"
					}
					
					if(b.receivablesAble == true){
						skan = "<a href='2sk-xq.html?dCartId="+b.id+"' class='sk-an'>收款</a>"
					}
					if(state == "Pickuped"){
						state = "已提货";
					}else if(state == "Delivered"){
						state = "已送达";
					}else if(state == "Finish"){
						state = "已返回";
					}else if(state == "Init"){
						state = "已生成";
					}else if(state == "WaitAllocateCart"){
						state = "已生成";
					}else if(state == "AssignedDriver"){
						state = "已分配";
					}
					var createTimeText = b.createTimeText;
					createTimeText = createTimeText.replace(' ','<br />')
					_html += "<div class='list'><div class='w100 h60 border'><a href='2pc-xq.html?id="+b.id+"' target='_blank'>"+b.id+"</a></div><div class='border w100 h60'>"+createTimeText+"</div><div class='border w100 h60'>"+b.storageName+"</div><div class='border w100 h60'>"+state+"</div><div class='border w100 h60'>"+driverName+"</div><div class='border w100 h60'>"+deliveryTimeText+"</div><div class='border w100 h60 yjk'>"+orderReceivablesMoney+"</div><div class='border w100 h60'>"+ yjk +"</div><div class='border w100 h60'>"+skan+"</div>	<div class='border w100 h60'>"+receivablesOperator+"</div></div>"
				})
				$(".table-list-sk").html(_html);
				var fy = "<span class='fl ml20'>"+data.dCartCountInfo.count+"条派车单</span><span  class='fl ml5'>应交款"+data.dCartCountInfo.allReceivablesMoney.toFixed(2)+"元</span><span  class='fl ml5'>已交款"+data.dCartCountInfo.receivablesMoney.toFixed(2)+"元</span>&nbsp;"
				fy += "<a href='javascript:void(0)' class='page-sy'>首页</a><a href='javascript:void(0)' class='page-up'>上一页</a><span class='fy-an'>";
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
					 pcSk($(this).attr("ids"));
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
						 pcSk(pageUp);
					 })
					 $(".page-sy").click(function(){
					 pcSk(1);
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
						 pcSk(pageDown);
					 })
					 $(".page-wy").click(function(){
					 pcSk(data.pageMaxNo);
				 })
				 }
				
				
			}
		})
	
	}
	
	$(".sk-query-button").click(function(){
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
		sktjj();
		pcSk(1)})
	function sktjj(){
		sktj.dCartStateText = $(".pc-state").val();
		sktj.dCartId = $(".pcNo").val()
		sktj.cityId = $(".city").val()
		sktj.storageId = $(".thkf").val()
		sktj.districtId = $(".district").val()
		sktj.createDateStratText = $(".time1").val()
		sktj.createDateEndText = $(".time2").val()
		sktj.orderNo = $(".ddId").val()
		sktj.driverName = $(".driverName").val()
		sktj.shipperId = $(".driver-dw").val()
		sktj.streetId = $(".stree").val()
		sktj.dCartReceivablesText = $(".pc-Receivables").val()
	}
	
	
	
	$(".sk-dc").click(function(){
		if(sktj.dCartStateText == undefined){
			alert("请先查询")
			return;
		}
		$(".pc-state3").val(sktj.dCartStateText)
		$(".pcNo3").val(sktj.dCartId)
		$(".city2").val(sktj.cityId)
		$(".thkf3").val(sktj.storageId)
		$(".district3").val(sktj.districtId)
		$(".time13").val(sktj.createDateStratText)
		$(".time23").val(sktj.createDateEndText)
		$(".ddId3").val(sktj.orderNo)
		$(".driverName3").val(sktj.driverName)
		$(".driver-dw3").val(sktj.shipperId)
		$(".stree3").val(sktj.streetId)
		$(".pc-Receivables3").val(sktj.dCartReceivablesText)
		
		$.ajax({
	  		async:false,
			 url:'/dcart/receivables/list',
			 data:{
				 'dCartStateText':sktj.dCartStateText,
				 'dCartId':sktj.dCartId,
				 'cityId':sktj.cityId,
				 'storageId':sktj.storageId,
				 'districtId':sktj.districtId,
				 'createDateStratText':sktj.createDateStratText,
				 'createDateEndText':sktj.createDateEndText,
				 'orderNo':sktj.orderNo,
				 'driverName':sktj.driverName,
				 'shipperId':sktj.shipperId,
				 'streetId':sktj.streetId,
				 'dCartReceivablesText':sktj.dCartReceivablesText,
				 'pageNo':1
				 },
			 dataType:'json',
			 success:function(data){
			 	if(data.s == 0){
					return;
				}
				if(data.dCartList == ""){
					alert("此条件无数据可导出")
					return;
				}
				$("#form2").submit();
				
			 }
	   })
	});
	
	
		
})
