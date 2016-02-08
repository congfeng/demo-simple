// JavaScript Document

$(function(){
	document.title = "司机监控";
	var d = new Date();
	var moth = (d.getMonth()+1);
		moth = moth<10?"0"+moth:moth;
	var dD = d.getDate()
		dD = dD<10?"0"+dD:dD;
	var date = d.getFullYear() + "-" + moth +"-" + dD ;
	$(".time1").val(date);
	$(".time2").val(date);
	var jktj = {};
	//加载司机单位列表
	$.ajax({
			url:'/shipper/list',
			dataType:'json',
			success:function(data){
				if(data.s == 0){
					return ;
				}
				var _html = "<option value=''>全部</option>";
				var xh = 0;
				$.each(data.shipperList,function(a,b){
					xh += 1;
					_html += "<option value="+b.id+">"+b.name+"</option>"
				})
				
				$("#jk-dw").html(_html);
			}
		})
	
	// 加载车型列表
	$.ajax({
			url:'/model/list',
			dataType:'json',
			success:function(data){
				if(data.s == 0){
					return ;
				}
				var _html = "<option value=''>全部</option>";
				var xh = 0;
				$.each(data.modelList,function(a,b){
					xh += 1;
					_html += "<option value="+b.id+">"+b.name+"</option>"
				})
				
				$("#jk-cx").html(_html);
			}
		})
	
	
	function xzCity(){
		$.ajax({
			url:'/region/citys/storage/list',
			dataType:'json',
			success:function(data){
				if(data.s == 0){
					return ;
				}
				var shi = "<option value=''>全部</option>";
				$.each(data.citysForStorageList,function(e,f){
					shi += "<option value="+f.id+" code="+f.code+">"+f.name+"</option>"
				})
				$("#city").html(shi);
			}
		})
	}
	xzCity();
	
	function sjJk(num){
		
		
		
		$.ajax({
			 url:'/driver/performance/list',
			 data:{
				 'shipperId':jktj.shipperId,
				 'cityId':jktj.cityId,
				 'startTime':jktj.startTime,
				 'endTime':jktj.endTime,
				 'status':jktj.status,
				 'modelId':jktj.modelId,
				 'driverName':jktj.driverName,
				 'pageNo':num
				 },
			 dataType:'json',
			 success:function(data){
			 	if(data.s == 0){
					return ;
				}
				 var _html = "";
				 var jkXh = 0;
				 if(data.driverList ==""){$(".table-list-jk").html("<div class='m20'>此条件下没有数据</div>");return;}
			    
			     // var isTimeEmpty = false;
				 // if($(".time1").val() == "" || $(".time2").val() == ""){
					// isTimeEmpty = true;
				 // }
				 
				 $.each(data.driverList,function(a,b){
						jkXh += 1;
						var driverState = b.driverState;
						// var driverName = b.driverName?b.driverName:'';
						if(driverState == "Normal"){
							driverState = "正常";
						}else if(driverState == "Freeze"){
							driverState = "冻结";
						}else if(driverState == "Finish"){
							driverState = "停用";
						}
						var streetName = b.streetName?b.streetName:"";
					  _html += "<div class='rest'><div class='w100 h60 border' >"+jkXh+"</div><div class='border w100 h60'><a href='2sj-xq.html?driverId="+b.driverId+"' target='_blank'>"+b.driverId+"</a></div><div class='border w100 h60'>"+b.driverName+"</div><div class='border w100 h60'>"+driverState+"</div><div class='border w100 h60'>"+streetName+"</div><div class='border w100 h60'>"+b.merchantNum+"</div><div class='border w100 h60'>"+b.assignmentBillNum+"</div><div class='border w100 h60'>"+b.completedAssignmentNum+"</div><div class='border w100 h60'>"+b.completedOrderNum+"</div></div>"
				 })
				 $(".table-list-jk").html(_html);
				 
				 var fy = "<a href='javascript:void(0)' class='page-sy'>首页</a><a href='javascript:void(0)' class='page-up'>上一页</a><span class='fy-an'>";
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
					 sjJk($(this).attr("ids"));
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
						 sjJk(pageUp);
					 })
					 $(".page-sy").click(function(){
					 sjJk(1);
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
						 sjJk(pageDown);
					 })
					 $(".page-wy").click(function(){
					 sjJk(data.pageMaxNo);
				 })
				 }
				 
				 
			 }
		});
		
		
	}
	
	
	$(".jk-query-button").click(function(){
		if($(".text-pc").val() == ""){
		}else{
			var myReg = /^[\u4e00-\u9fa5]+$/;
			if (myReg.test($(".text-pc").val())) {
				
			}else{
				alert("司机姓名只能输入汉字");
				$(".text-pc").val("");
				return;
			}
		}
		jktjj();
		sjJk(1);})
	function jktjj(){
		 jktj.shipperId = $("#jk-dw").val()
		 jktj.cityId = $("#city").val()
		 jktj.startTime = $(".time1").val()
		 jktj.endTime = $(".time2").val()
		 jktj.status = $(".jk-state").val()
		 jktj.modelId = $("#jk-cx").val()
		 jktj.driverName = $("#driverNameInput").val()
	}
//	$("#form1").submit(function(){
//				alert("导出成功");
//		
//	})
	$(".sj-dc").click(function(){
		 if(jktj.driverName == undefined){
			alert("请先查询")
			return;
		}
		$("#jk-dw4").val(jktj.shipperId)
		$("#city4").val(jktj.cityId)
		$(".time14").val(jktj.startTime)
		$(".time24").val(jktj.endTime)
		$(".jk-state4").val(jktj.status)
		$("#jk-cx4").val(jktj.modelId)
		$(".text-pc4").val(jktj.driverName)
		$.ajax({
	  		async:false,
			 url:'/driver/performance/export',
			 data:{
				 'shipperId':jktj.shipperId,
				 'cityId':jktj.cityId,
				 'startTime':jktj.startTime,
				 'endTime':jktj.endTime,
				 'status':jktj.status,
				 'modelId':jktj.modelId,
				 'driverName':jktj.driverName,
				 'num':1
				 },
			 dataType:'json',
			 success:function(data){
			 	if(data.s == 0){
					return;
				}
				if(data.driverList ==""){
					alert("此条件无数据可导出")
					return;
				}
				$("#form3").submit();
				
			 }
	   })
	});
	
})
	
 	
