// JavaScript Document

$(function(){
	document.title = "派车单分配";
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
					$(".fp-query-button").hide();
					$(".table-list-fp").html("");
					return;
				}else{
					$(".fp-query-button").show();
				}
				$.each(data.regionList,function(e,f){
					sheng += "<option value="+f.id+" code="+f.code+">"+f.name+"</option>"
				})
				$(".sheng").html(sheng);
				
				xzCity();
				
			}
		})
	}
	
	
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
	$(".sheng").change(function(){
		$(".city").html("<option value=''><option>");
		$(".district").html("<option value=''><option>");
		$(".stree").html("<option value=''><option>");
		$(".table-list-fp").html("");
		$(".driver-list").html("");
		xzCity();
	})
	$(".city").change(function(){
		$(".district").html("<option value=''><option>");
		$(".stree").html("<option value=''><option>");
		$(".driver-list").html("");
		$(".table-list-fp").html("");
		xzQy();
	})
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

	//加载司机筛选列表
	$.ajax({
		url:'/shipper/list',
		dataType:'json',
		success:function(data){
			if(data.s == 0){
				return ;
			}
			var _html = "<option value=''>全部</option>";
			$.each(data.shipperList,function(a,b){
				_html += "<option value='"+b.id+"'>"+b.name+"</option>";
			})
			
			$(".ssdw").html(_html)
		}
	})
	
	$.ajax({
		url:'/model/list',
		dataType:'json',
		success:function(data){
			if(data.s == 0){
				return ;
			}
			var _html = "<option value=''>全部</option>";
			$.each(data.modelList,function(a,b){
				_html += "<option value='"+b.id+"'>"+b.name+"</option>";
			})
			
			$(".xzcx").html(_html)
		}
	})
	
	
	
	
	//加载司机列表
	function driverList(){
		$.ajax({
				url:'/driver/unassign/list',
				data:{'cityId':$(".city").val(),'modelId':$(".xzcx").val(),'shipperId':$(".ssdw").val(),'driverName':$(".drivername").val()},
				dataType:'json',
				success:function(data){
					if(data.s == 0){
						return ;
					}
					var driverlist = "";
					$(data.driverList).each(function(a,b){
						var modelName = b.modelName?b.modelName:'';
						var streetName = b.streetName?b.streetName:'';
						var shipperName = b.shipperName?b.shipperName:'';
						driverlist += "<div class='rest driver"+b.id+"' draggable='true' id='"+b.id+"'><div class='w70 h30 border driverName'>"+b.name+"</div><div class='w70 h30 border'>"+modelName+"</div><div class='w110 h30 border'>"+streetName+"</div><div class='w110 h30 border'>"+shipperName+"</div></div>"
					
					})
					$(".driver-list").html(driverlist);
					Rest();
				}
			})
	}
	$(".driver-query-button").click(driverList);
	
	function pcFplist(){
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
		$.ajax({
			 url:'/dcart/unpickup/list',
			 data:{
				 'dCartStateText':$(".pc-state").val(),
				 'dCartId':$(".pcNo").val(),
				 'storageId':$(".thkf").val(),
				 'cityId':$(".city").val(),
				 'districtId':$(".district").val(),
				 'createDateStratText':$(".time1").val(),
				 'createDateEndText':$(".time2").val(),
				 'orderNo':$(".ddId").val(),
				 'driverName':$(".driverName").val(),
				 'shipperId':$(".driver-dw").val(),
				 'streetId':$(".stree").val(),
				 pi:0,ps:10000},
			 dataType:'json',
			 success:function(data){
				 if(data.s == 0){
					 return;
				 }
				if(data.dCartList == ""){
					$(".table-list-fp").html("<div class='m20'>此条件没有可分配的派车单</div>");
					return;
				}
				var that = "table"; _html = "";	_html2 = "";
				var dCarList = data.dCartList;
				$.each(dCarList,function(k,v){
					
					var ID = v.id;
					var Qy = "";
					var Address = "";
					var Shs = 0;
					var Dds = 0;
					var Money = "";
					var del = "<a href='javascript:void(0);' class='deleteDd'>删除</a>"
					var state = v.state;
					if(state == "Init"){
						state = "已生成";
					}else if(state == "AssignedDriver"){
						state = "已分配";
						del = "<a href='javascript:void(0);' class='deleteFp'>取消分配</a>"
					}
					var driverid = v.driverId;
					var driverName = v.driverName
					if(driverName == null){
						driverName = "";
					}
					var o =  "<div class='list' id="+ID+">";
					_html +=o;
					Shs += v.buyerCount; //商户数
					Dds += v.orderCount//订单数
					_html += "<div class='list-table'><div class='steer w100 h60 border'><a href='2pc-xq.html?id="+ID+"' target='_black'>"+ ID +"</a></div><div class='rest'><div class='border w100 h60'>"+v.districtName+"</div><div class='border w100 h60'>"+v.streetNames+"</div><div class='border w100 h60'>"+Dds+"</div><div class='border w100 h60'>"+Shs+"</div><div class='border w100 h60'>"+v.orderMoney.toFixed(2)+"</div><div class='border w100 h60'><p class='driverName' id='"+driverid+"'>"+driverName+"</p><p class='color-hs state'>"+ state +"</p><p class='operation'>"+ del +"</p></div></div></div></div>"
					
					})
					
					$(".table-list-fp").html(_html)
					hh();
					deleteRest();
					
				
				
			 }
		});
	}
	//加载派车单列表
	$(".fp-query-button").click(function(){
		pcFplist();
	})
	 function Rest(){
		var aLi = $('.driver-table .rest');
		for (var i = 0; i < aLi.length; i++) {
        aLi[i].ondragstart = function(ev) { //拖拽开始事件
            $(this).css("background","#ccc")
			var id = $(this).attr("id")
			
			var driverName = $(this).find(".driverName").text();
			var xx =[id,driverName]
            ev.dataTransfer.setData("a", xx); //传值到容器
            ev.dataTransfer.effectAllowed = 'all';
         //   ev.dataTransfer.setDragImage(this, 0, 0);
        };
        aLi[i].ondragend = function() { //拖拽之后 等于onmouseout
				$(this).css("background","")
        };
		}
	 }
	
	
	
	
		// 拖放到对应item 下，生成item元素
		 function hh(){
			var inset = $('.list .rest');
			for(i=0;i<inset.length;i++){
				inset[i].ondragover = function(ev) { //
					$(this).css("background","#ccc")
					ev.preventDefault(); //
				};
				inset[i].ondragleave = function() { //
					$(this).css("background","");
				};
				inset[i].ondrop = function(ev) { //
					$(this).css("background","");
						
					var xx = ev.dataTransfer.getData('a').split(","); // 
					var _this = $(this)
					if(_this.find(".driverName").text() == ""){
						if(confirm("确定要将此派车单分派给"+ xx[1] +"？")){
							$.ajax({
								url:'/dcart/driver/assign',
								data:{dCartId:_this.closest(".list").attr("id"),driverId:xx[0]},
								dataType:'json',
								success:function(data){
									if(data.s == 0){
										return ;
									}
									// _this.find(".driverName").text(xx[1]);
									// _this.find(".driverName").attr("id",xx[0]);
									// _this.find(".state").text("已分配");
									// _this.find(".operation").html("<a href='javascript:void(0);' class='deleteFp'>取消分配</a>");
									$(".driver"+xx[0]).hide();
									pcFplist();
								}
								
							})	
							
						}
					}else{
						alert("操作失败：此派车单已分派司机")
					}
					
					//alert(ev.dataTransfer.getData('a'));
					//alert(typeof ev.dataTransfer.getData('a'));
					//alert(ev.dataTransfer.types);
					
				};
				
			}
			deleteFp();
			
		 }

		 function deleteFp(){
			$(".list .deleteFp").click(function(){
				var thiss = $(this);
				var driverID = $(this).parent().siblings(".driverName").attr("id");
				var dCartID = $(this).closest(".list").attr("id");
				if(confirm("是否确定取消分配")){
							
						}else{
							return;
						}
				$.ajax({
					url:'/dcart/driver/cancel',
					data:{dCartId:dCartID,driverId:driverID},
					dataType:'json',
					success:function(data){
						if(data.s == 0){
							return ;
						}
						thiss.parent().siblings(".driverName").text("");
						thiss.parent().siblings(".state").text("已生成");
						thiss.parent().html("<a href='javascript:void(0);' class='deleteDd'>删除</a>");
						driverList();
						pcFplist();
						
					}
				})
				
			
			})
		 }		
		
		function deleteRest(){
			$(".list .deleteDd").click(function(){
				if(confirm("确定删除此订单？")){
					var ID = $(this).closest(".list").attr("id");
					var _this = $(this);
					$.post('/dcart/delete',{dCartId:ID},function(data){
								_this.closest(".list").remove();
								alert("删除成功")							
					})
				}
			})
			
		}
		
})
