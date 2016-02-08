// JavaScript Document

$(function(){
	function sx(){
		history.go(0);
	}
	var t = setInterval(sx,50000);
	//刚进页面请求列表
	var driverID = "";
	var src = window.location.href;
	if(src.indexOf("?driverId=") == -1){
			driverID = window.localStorage.driverId;
			if(driverID == "undefined"){
				window.location.href = "/test_login.html";
				return;
			}else if(driverID == undefined){
				window.location.href = "/test_login.html";
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
	var num_cur = {};
	var ys_money_max;
	var orders = 0;
	    
	$.ajax({
		url:'/dcart/driverapp/current/detail',
		data:{'driverId':driverID},
		dataType:'json',
		success:function(data){
			
			if(data.s == 0){
				alert(data.m);
				return;
			}
			
			if(data.dCart == null){
				$(".content").append("<section class='rem3'>当前没有派车任务</section>");
				return;
			}
			orders = data.orderCount
			var dCartID = data.dCart.id;
			
			var orderMoney = data.orderPayMoney;
			var html="";
			var flag;
			var	reqPickupTimeText2 = "";
			var reqPickupTimeText = data.dCart.reqPickupTimeText;
			if(reqPickupTimeText){
				reqPickupTimeText = reqPickupTimeText.substring(5,reqPickupTimeText.length-3);
				reqPickupTimeText2 = reqPickupTimeText?reqPickupTimeText:"";
			}
			var qyts = "请于";
			if(reqPickupTimeText2 == ""){
				qyts = "";
			}
			var state = "<section class='state state-wait'><span>"+qyts+reqPickupTimeText2+"到"+data.dCart.storageName+"提货</span>	   </section>";
			// 判断库房状态
			
			//判断库房状态 如果库房准备好 返回Pickuped
			
			if(data.dCart.state == "Pickuped"){
				state = "<section class='state state-lv'><span>应交回"+data.orderPayMoney+"元</span></section>"
				clearInterval(t);
				$(".xq-button").show();
				flag = true;
			}
			if(data.dCart.state == "Delivered"){
				state = "<section class='state state-lv'><span>应交回"+data.orderPayMoney+"元</span></section>"
				clearInterval(t);
				flag = true;
			}
			if(data.dCart.state == "AssignedDriver"){
				if(data.isPickupAble == true){
					state = "<section class='state state-zc'><span>出货完毕请装车</span><a href='javascript:void(0)' class='state-button fr zc'>装车</a>	   </section>";
					clearInterval(t);
					flag = true;
				}else{
					flag = false;
					t;
				}
			}
			if(data.dCart.receivables){
				state = "<section class='state state-lv'><span>您已交回货款，感谢配合</span></section>"
				clearInterval(t);
				flag = true;
			}
			html += "<section class='crumb' ><p>派车单编号:"+data.dCart.id+"</p><P>"+data.dCart.storageName+"，"+data.buyerCount+"户，"+data.orderCount+"单</p></section><section class='index-div'>";
			$.each(data.dOrderBuyerList,function(a,b){
				  var dz = b.receiverAddress;
				  html += "<div class='list'><ul><li><span class='m-tb m-tb1'></span><span class='f2'>"+b.buyerName+"</span><span class='fr ddd'><a href='tel:"+b.receiverMobile+"' class='m-tel'></a>|&nbsp;<a href='sms:"+b.receiverMobile+"' class='m-sms'></a></span></li><li><span class='m-tb m-tb2'></span><span class='time'>"+b.reqDeliveryTimeSlot+"</span>送达</li><li><span class='m-tb m-tb3'></span><span class='f1'>"+dz+"</span></li>";
					   
					  $.each(b.dOrderList,function(c,d){
					   var singType = "";
					   var yth = "";
					   var yjh = "";
					   if(data.dCart.state == "AssignedDriver"){
						   if(d.outTakeMoney){
								singType = "<a href='#' class='xq-button fr xq-an'>详情</a>";
								flag = true;
							}
					  }else{
						  if(d.signType == "Full"){
								  singType = "<a href='#' class='xq-button fr xq-an'>详情</a>";
								  yth = "";
								  yjh = "应交回"+d.money+"元";
							  }else if(d.signType == "Part"){
								  singType = "<a href='#' class='xq-button fr xq-an'>详情</a>";
								  yth = "有退货";
								  yjh = "应交回"+d.money+"元";
							  }else if(d.signType == null){
								  singType = "<a href='#' class='xq-button fr qs-an'>签收</a>";
								  yth="未签收";
							  }else if(d.signType == "Refuse"){
								  singType = "<a href='#' class='xq-button fr xq-an'>详情</a>";
								  yth = "有退货";
								  yjh = "应交回0元";
							  }
						}
						   html += "<li id="+d.id+" class='a"+d.id+"'><p class='fl'>订单号：<span class='ddh'>"+d.orderNo+"</span><br/>"+yjh+"<span class='time'>"+yth+"</span></p>"+singType+"</li>";
					  })
					 

				  html += "</ul></div>";
				  
			})
			 html += "</section><section class='rem3'></section>";
			 html += state;
			 
			 $(".content").append(html);
			 if(flag){
				 $(".xq-button").show();
			 }else{
				 $(".xq-button").hide();
			 }
			
			// if(data.dCart.state == "Delivered"){
				// clearInterval(t);
				// $(".xq-button").html("详情");
				// $(".xq-button").removeClass("qs-an");
				// $(".xq-button").addClass("xq-an");
			// }
			//点击装车按钮
			$(".zc").live("click",function(){
				 var _thiss = $(this);
				$.ajax({
					url:'/dcart/driverapp/check/pickup/orders',
						data:{dCartId:dCartID},
						dataType:'json',
						success:function(data){
							if(data.s == 0){
								alert("装车失败：" + data.m);
								return;
							}
							if(data.orderSize == orders){
								if(confirm("您已经核对已装商品了吗？装车确定后不可再修改")){
									$.ajax({
										url:'/dcart/driverapp/pickup',
										data:{dCartId:dCartID},
										dataType:'json',
										success:function(data){
											if(data.s == 0){
												alert("装车失败：" + data.m);
												return;
											}
											_thiss.parent().addClass("state-lv");
											_thiss.parent().html("应交回"+orderMoney+"元");
											$(".xq-button").html("签收");
											$(".xq-button").removeClass("xq-an");
											$(".xq-button").addClass("qs-an");
										}
									})
								}
							}else{
								alert("有订单已删除，请重新装车");
								location.reload();
							}
						}
				})
				
				
			})
			
			//点击签收按钮
			$(".qs-an").live("click",function(){
				var ID = $(this).parent().attr("id");
				var xqHtml = "";
				var ddh = $(this).siblings(".ddh").text();
				$.ajax({
					url:'/dcart/driverapp/goods/list',
					data:{dOrderId:ID},
					dataType:'json',
					success:function(data){
						if(data.s == 0){
							if(data.t == 6){
								alert("该订单已取消");
								location.reload();
							}
							alert("失败信息：" + data.m);
							return;
						}
						ys_money_max = data.money;
						xqHtml +="<p><a href='javascript:void(0)' class='fh-an'>返回</a></p><section class='list'><ul><li class='border-bottom'>		订单号："+data.orderNo+"</li></ul></section><section class='list'><ul><li>应收款：<span class='time sj-ysk sjlookysk'><span class='yjkze'>"+data.money.toFixed(2)+"</span>元</span></li><li class='border-bottom'><span class='m-tb m-tb4'></span>客户未签收的货物需交回发货仓</li>";
						
						if(data.orderType == "Repair"){
							$.each(data.goodsList,function(d,e){
							num_cur[e.id] = Number(e.num);
							var dww = e.goodsUnitName;
							
							var dw = "<input type='tel' id="+ e.id +" name='number' value="+e.num+" dj="+e.price+" ids="+e.num+" class='text'>"+dww+"<a href='javascript:void(0)' class='time fr js-an2'>拒收</a>"
							
							if(e.goodsUnitType == "Type1"){
								dw = "<input type='tel' id="+ e.id +" name='number' value="+e.num+" dj="+e.price+" ids="+e.num+" class='text'>"+dww+"<a href='javascript:void(0)' class='fr xg-an2'>修改</a>";
							}
							
							xqHtml +="<li id="+e.id+" class='border-bottom lh15'><span class='f1 heise'>"+e.goodsName+"</span><br>"+dw+"</li>";
							})
							
						//	qsan();
							
						}else{
							$.each(data.goodsList,function(d,e){
							num_cur[e.id] = Number(e.num);
							var dww = e.goodsUnitName;
							
							var dw = "<input type='tel' id="+ e.id +" name='number' value="+e.num+" dj="+e.price+" ids="+e.num+" class='text'>"+dww+"<a href='javascript:void(0)' class='time fr js-an'>拒收</a>"
							
							if(e.goodsUnitType == "Type1"){
								dw = "<input type='tel' id="+ e.id +" name='number' value="+e.num+" dj="+e.price+" ids="+e.num+" class='text'>"+dww+"<a href='javascript:void(0)' class='fr xg-an'>修改</a>";
							}
							
							xqHtml +="<li id="+e.id+" class='border-bottom lh15'><span class='f1 heise'>"+e.goodsName+"</span><br>"+dw+"</li>";
							})
						}
						
						
						
						xqHtml +="</ul></section><button class='qd-button' >确定</button><div class='rem3'></div>";
						$(".content-xq").html(xqHtml);
						$(".content-xq").attr("ids",ID);
						$(".text").click(function(){
							$(this).blur();
						})
					}
				})
				
				$(".content-xq").animate({"margin-left":"0","left":"0","top":"0","width":"100%"},400);
				$(".content").animate({"margin-left":"-100%","width":"0"},400);
				$(".state").hide();
			})
			//点击详情按钮
			$(".xq-an").live("click",function(){
				var ID = $(this).parent().attr("id");
			    var xqHtml = "";
				$.ajax({
					url:'/dcart/driverapp/goods/list',
					data:{dOrderId:ID},
					dataType:'json',
					success:function(data){
						if(data.s == 0){
							if(data.t == 6){
								alert("该订单已取消");
								location.reload();
							}
						}
						var returnOrderNo = data.returnOrderNo?data.returnOrderNo:"";
						if(data.signType == undefined){
							xqHtml += "<p><a href='javascript:void(0)' class='fh-an'>返回</a></p><section class='list'><ul><li class='border-bottom'>订单号："+data.orderNo+"</li><li class='border-bottom'>订单金额："+data.orderGoodsMoney.toFixed(2)+"元</li>";
							$.each(data.goodsList,function(d,e){
								var jldw = e.goodsUnitName;
								var dw = ""+e.num+""+jldw+"";
								xqHtml +="<li id="+e.id+" class='border-bottom'>"+e.goodsName+"<span class='fr'>"+dw+"</span></li>";
								// xqHtml +="<li id="+e.id+" class='border-bottom lh15'><span class='f1'>"+e.goodsName+"</span><br>"+dw+"</li>";
								
							})
							xqHtml +="</ul></section>"
						  }else if(data.signType == "Part"){
							xqHtml += "<p><a href='javascript:void(0)' class='fh-an'>返回</a></p><section class='list'><ul><li class='border-bottom time'>客户部分签收</li><li class='border-bottom'>订单号："+data.orderNo+"</li>";
							$.each(data.goodsList,function(d,e){
								var jldw = e.goodsUnitName
								var dw = ""+e.signNum+""+jldw+"";
								xqHtml +="<li id="+e.id+" class='border-bottom'>"+e.goodsName+"<span class='fr'>"+dw+"</span></li>";
							})
							xqHtml += "</ul></section><section class='list'><ul><li class='border-bottom time'>需交回款项</li><li class='border-bottom'>收款<span class='fr'>"+data.money.toFixed(2)+"元</span></li><li class='border-bottom'>退货单号："+returnOrderNo+"</li>";
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
						}else if(data.signType == "Refuse"){
							xqHtml += "<p><a href='javascript:void(0)' class='fh-an'>返回</a></p><section class='list'><ul><li class='border-bottom time'>客户全部拒收</li><li class='border-bottom'>订单号："+data.orderNo+"</li>";
							
							xqHtml += "</ul></section><section class='list'><ul><li class='border-bottom time'>需交回款项</li><li class='border-bottom'>退货单号："+returnOrderNo+"</li>";
							$.each(data.backGoodsList,function(h,j){
								var sl = j.num - j.signNum;
								var jldw = j.goodsUnitName;
								var dw = ""+sl+""+jldw+"";
								xqHtml += "<li id="+j.id+" class='border-bottom'>"+j.goodsName+"<span class='fr'>"+dw+"</span></li>"
								
							})
							xqHtml +="</ul></section>";
							
						}
					$(".content-xq").html(xqHtml);
					$(".content-xq").animate({"margin-left":"0","left":"0","top":"0","width":"100%"},400);
					$(".content").animate({"margin-left":"-100%","width":"0"},400);
					$(".state").hide();
					}
				})
			})
			
			//返回列表
			$(".fh-an").live("click",function(){
				$(".content-xq").animate({"margin-left":"100%","left":"100%","top":"0","width":"0"},400);
				$(".content").animate({"margin-left":"0","width":"100%"},400);
				$(".state").show();
			})
			var ysJe = 0;
			// 拒收按钮
			$(".js-an").live("click",function(){
				// ysJe = Number($(".yjkze").text())
				if(confirm("客户确定拒收?")){
					$(this).siblings(".text")[0].value = 0;
					$(".text").trigger("input");
					$(this).siblings(".text").addClass("huise");
					$(this).siblings("span").addClass("huise");
					$(this).siblings("span").removeClass("heise");
					$(this).text("恢复");
					$(this).removeClass("js-an");
					$(this).addClass("hf-js");
					// var ysMoney = Number($(".yjkze").text());
					// if(ysMoney == 0){
						
					// }else if(ysMoney == "0"){
						
					// }else{
						// ysMoney = ysMoney - Number($(this).siblings(".text").attr("ids"))*Number($(this).siblings(".text").attr("dj"));
						// if(ysMoney < 0 ){
							// ysMoney = 0;
						// } 
						// $(".sjlookysk").html("<span class='yjkze'>" + ysMoney.toFixed(2) +"</span>" + "元");
					// }
					
				}
			})
			//撤销拒收
			$(".hf-js").live("click",function(){
				var ids = $(this).siblings(".text").attr("ids");
				$(this).siblings(".text")[0].value = ids;
				$(".text").trigger("input");
				$(this).siblings(".text").removeClass("huise");
				$(this).siblings("span").removeClass("huise");
				$(this).siblings("span").addClass("heise");
				$(this).text("拒收");
				$(this).removeClass("hf-js");
				$(this).addClass("js-an");
				// var ysMoney = Number($(".yjkze").text());
				// if(ysJe == 0){
					
				// }else if(ysJe == "0"){
					
				// }else{
					// ysMoney = ysMoney + Number($(this).siblings(".text").attr("ids"))*Number($(this).siblings(".text").attr("dj"));
					// if(ysMoney < 0 ){
						// ysMoney = 0;
					// } 
					// $(".sjlookysk").html("<span class='yjkze'>" + ysMoney.toFixed(2) +"</span>" + "元");
				// }
			})
			
			
			$(".js-an2").live("click",function(){
				alert("补货单只能全部签收，如有疑问请联系客服")
			})
			$(".xg-an2").live("click",function(){
				alert("补货单只能全部签收，如有疑问请联系客服")
			})
			//修改按钮
			$(".xg-an").live("click",function(){
				$(this).siblings(".text").focus();
			})
			
			//输入签收数量验证
			$(".text").live("input",function(){
				var a = Number($(this).val());
				  if(a == ""){
					  a = 0;
				  }
				  if(isNaN(a)||a<0){
				  	 $(this).val(num_cur[$(this).attr("id")]);
				  	 return ;
				  }
				var b = Number($(this).attr("ids"));
				var djjj = Number($(this).attr("dj"));
				if( a > b ){
					alert("输入的数量大于出货数量");
						$(this).val($(this).attr("ids"));
						a = b;
				}
				if(a != b){
					$(this).addClass("redd");
					$(this).removeClass("heise");
					$(this).siblings("span").addClass("redd");
					$(this).siblings("span").removeClass("heise");
				}
				if(a == b){
					$(this).addClass("heise");
					$(this).removeClass("redd");
					$(this).removeClass("huise");
					$(this).siblings("span").removeClass("huise");
					$(this).siblings("span").removeClass("redd");
					$(this).siblings("span").addClass("heise");
				}
				if(a == 0){
					$(this).addClass("huise");
					$(this).siblings("span").addClass("huise");
				}
				var ysMoney = Number($(".yjkze").text());
				ysMoney = ysMoney - (num_cur[$(this).attr("id")]-Number(a))*Number(djjj);
				if(ysMoney < 0 ){
					ysMoney = 0;
				}
				if(ysMoney > ys_money_max){
					ysMoney = ys_money_max;
				}
				$(".sjlookysk").html("<span class='yjkze'>" + ysMoney.toFixed(2) +"</span>" + "元");
				num_cur[$(this).attr("id")] = Number(a);
			})
			
			//签收页面确定按钮
			$(".qd-button").live("click",function(){
				var IdArrey = "";
				var bler = true;
				var num = "";
				var ID = $(".content-xq").attr("ids");
				$.each($(".content-xq .lh15"),function(f,g){
					if($(this).find(".text").val() == null){
						 $(this).find(".text").val(0);
						 bler = false;
					 }
					 if($(this).find(".text").val() == ""){
						 $(this).find(".text").val(0);
						 bler = false;
					 }
					 IdArrey += $(this).attr("id") + ",";
					 num += $(this).find(".text").val() + ",";
					 
				})
				if(bler){
					
				}else{
					alert("有商品未填写签收数量");
					return;
				}
				IdArrey = IdArrey.substring(0,IdArrey.length-1);
				IdArrey = IdArrey.split(",");
				num = num.substring(0,num.length-1);
				num = num.split(",");
				var bl = false;
				if($(".content .qs-an").length == 1){
					bl = true;
				}
				if(confirm("您已经核对订单数量了吗？签收确定后不可再修改")){
				$.ajax({
					url:'/dcart/driverapp/deliver',
					data:{dCartId:dCartID,dOrderId:ID,isLast:bl,dOrderGoodsIds:IdArrey,dOrderGoodsNums:num},
					dataType:'json',
					success:function(data){
						if(data.s == 0 ){
							alert("签收失败"+data.m);
							return;
						}
						alert("操作成功");
						$(".content-xq").animate({"left":"100%","width":"0"},400);
						$(".content").animate({"margin-left":"0%","width":"100%"},400,function(){
							 location.reload(); 
						});
						$(".state").show();
						
					}
				})					
				}
			})
				
				
				
		}
		
	})
		// function qsan(){
				
			// }
			
})
