// JavaScript Document

$(function(){
	
	//刚进页面请求列表
	var driverID = "";
	var src = window.location.href;
	if(src.indexOf("?driverId=") == -1){

			driverID = window.localStorage.driverId;
			if(driverID == "undefined"){
				window.location.href = "/test_bind2.html";
				return;
			}else if(driverID == undefined){
				window.location.href = "/test_bind2.html";
				return;
			}
	}else if(src.substr(src.length-1,1) == "#"){
			src = src.substr(0,src.length-1);
			driverID = src.substr(src.indexOf("?driverId=")+10)
	}else{
		driverID = src.substr(src.indexOf("?driverId=")+10)
	}
	if(driverID.indexOf("&") != -1){
		driverID = driverID.substr(0,driverID.indexOf("&"));
	}
	
	var cur_dcart_id;
	
	$(".history-an").click(function(){
		var datea = $(".date").val();
		datea = datea.replace(/\//g,'-');
		$.ajax({
		url:'/dcart/driverapp/history/list',
		data:{'driverId':driverID,'historyDay':datea},
		dataType:'json',
		success:function(data){
			if(data.s == 0){
				alert(data.m);
				return;
			}
			if(data.dCartMapList == null){
				$(".historyk").html("当前日期没有派车任务");
				return;
			}
			if(data.dCartMapList == ""){
				$(".historyk").html("当前日期没有派车任务");
				return;
			}
			var html="";
			$(".historyk").html("");
			$.each(data.dCartMapList,function(o,p){
				var dCartID = p.dCart.id;
				var orderMoney = p.orderPayMoney;
				var orderPayedMoney = p.orderPayedMoney;
				var state = "<section class='history-yjh' style='position:static'><span>交回"+orderPayedMoney+"元</span> </section>";
				
				html += "<section class='crumb' id='focus_"+dCartID+"'><p>派车单编号:"+dCartID+"</p><p>"+p.dCart.storageName+"，"+p.buyerCount+"户，"+p.orderCount+"单</p></section><section class='index-div'>";
				$.each(p.dOrderBuyerList,function(a,b){
					  var dz = b.receiverAddress;
					  html += "<div class='list'><ul><li><span class='m-tb m-tb1'></span><span class='f2'>"+b.buyerName+"</span></li><li><span class='m-tb m-tb2'></span><span class='time'>"+b.reqDeliveryTimeSlot+"</span>送达</li><li><span class='m-tb m-tb3'></span><span class='f1'>"+dz+"</span></li>";
						   
						  $.each(b.dOrderList,function(c,d){
						   var singType = "<a href='javascript:void(0)' class='xq-button fr xq-an'>详情</a>";
						   var yth = "";
						   var yjh = "";
						   if(p.dCart.state == "AssignedDriver"){
							   if(p.isPickupAble == true){
									singType = "<a href='javascript:void(0)' class='xq-button fr xq-an'>详情</a>";
								}
						  }else{
							  if(d.signType == "Full"){
									  singType = "<a href='javascript:void(0)' class='xq-button fr xq-an'>详情</a>";
									  yth = "";
									  yjh = "应交回"+d.money+"元";
								  }else if(d.signType == "Part" || d.signType == "Refuse"){
									  singType = "<a href='javascript:void(0)' class='xq-button fr xq-an'>详情</a>";
									  yth = "有退货";
									  yjh = "应交回"+d.money+"元";
									  state = "<section class='history-yjh' style='position:static'><span>交回"+orderPayedMoney+"元</span><span class='redd'>有退货</span> </section>";
								  }else if(d.signType == null){
									  singType = "<a href='#' class='xq-button fr qs-an'>签收</a>";
									  yth="未签收";
									  state = "<section class='history-yjh' style='position:static'><span>交回"+orderPayedMoney+"元</span><span class='redd'>有退货</span> </section>"
									}
							}
							   html += "<li id="+d.id+" dCartId="+dCartID+" class='a"+d.id+"'><p class='fl'>订单号：<span class='ddh'>"+d.orderNo+"</span><br/>"+yjh+"<span class='time ml2'>"+yth+"</span></p>"+singType+"</li>";
						  })
						 

					  html += "</ul></div>";
					  
				})
				 html += "</section>"+state;
			})
			$(".historyk").html(html);
			// if(data.dCart.state == "Delivered"){
				// clearInterval(t);
				// $(".xq-button").html("详情");
				// $(".xq-button").removeClass("qs-an");
				// $(".xq-button").addClass("xq-an");
			// }

			//点击详情按钮
			$(".xq-an").live("click",function(){
				var ID = $(this).parent().attr("id");
				cur_dcart_id = $(this).parent().attr("dCartId");
			    var xqHtml = "";
				$.ajax({
					url:'/dcart/driverapp/goods/list',
					data:{dOrderId:ID},
					dataType:'json',
					success:function(data){
						if(data.signType == undefined){
							xqHtml += "<p><a href='javascript:void(0)' class='fh-an'>返回</a></p><section class='list'><ul><li class='border-bottom'>订单号："+data.orderNo+"</li><li class='border-bottom'>订单金额："+data.orderGoodsMoney.toFixed(2)+"元</li>";
							$.each(data.goodsList,function(d,e){
								var jldw = e.goodsUnitName;
								var dw = ""+e.num+""+jldw+"";
								xqHtml +="<li id="+e.id+" class='border-bottom'>"+e.goodsName+"<span class='fr'>"+dw+"</span></li>";
								// xqHtml +="<li id="+e.id+" class='border-bottom lh15'><span class='f1'>"+e.goodsName+"</span><br>"+dw+"</li>";
								
							})
							xqHtml +="</ul></section>"
						  }else if(data.signType == "Part" || data.signType == "Refuse"){
							xqHtml += "<p><a href='javascript:void(0)' class='fh-an'>返回</a></p><section class='list'><ul><li class='border-bottom time'>客户部分签收</li><li class='border-bottom'>订单号："+data.orderNo+"</li>";
							$.each(data.goodsList,function(d,e){
								var jldw = e.goodsUnitName
								var dw = ""+e.signNum+""+jldw+"";
								xqHtml +="<li id="+e.id+" class='border-bottom'>"+e.goodsName+"<span class='fr'>"+dw+"</span></li>";
							})
							xqHtml += "</ul></section><section class='list'><ul><li class='border-bottom time'>需交回款项</li><li class='border-bottom'>收款<span class='fr'>"+data.money.toFixed(2)+"元</span></li>";
							$.each(data.backGoodsList,function(h,j){
								var sl = j.num - j.signNum;
								var jldw = j.goodsUnitName;
								var dw = ""+sl+""+jldw+"";
								xqHtml += "<li id="+j.id+" class='border-bottom'>"+j.goodsName+"<span class='fr'>"+dw+"</span></li>"
								
							})
							xqHtml +="</ul></section>";
							
						}else if(data.signType == "Full"){
							xqHtml += "<p><a href='javascript:void(0)' class='fh-an'>返回</a></p><section class='list'><ul><li class='border-bottom time'>客户已签收</li><li class='border-bottom'>订单号："+data.orderNo+"</li>";
							$.each(data.goodsList,function(d,e){
								var jldw = e.goodsUnitName;
								var dw = ""+e.signNum+""+jldw+"";
								xqHtml +="<li id="+e.id+" class='border-bottom'>"+e.goodsName+"<span class='fr'>"+dw+"</span></li>";
							})
							xqHtml += "</ul></section><section class='list'><ul><li class='border-bottom time'>需交回款项</li><li class='border-bottom'>收款<span class='fr'>"+data.money.toFixed(2)+"元</span></li>";
							xqHtml +="</ul></section>";
						}
						$(".content-xq").html(xqHtml);
						$(".content-xq").animate({"margin-left":"0","left":"0","top":"0","width":"100%"},400);
						$(".content").animate({"margin-left":"-100%","width":"0"},400,function(){
							$(".fh-an").focus();
						});
						$(".state").hide();
					}
				})
			})
	
		}
		
	})
		
	})
	
	
			//返回列表
			$(".fh-an").live("click",function(){
				$(".content-xq").animate({"margin-left":"100%","left":"100%","top":"0","width":"0"},400);
				$(".content").animate({"margin-left":"0","width":"100%"},400,function(){
					//location.hash="#focus_"+cur_dcart_id;
				});
				$(".state").show();
			});	
			
})
