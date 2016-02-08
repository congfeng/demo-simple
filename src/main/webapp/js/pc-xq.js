// JavaScript Document

$(function(){
	
	var href = location.href
	var index = href.indexOf("=")
	var str = href.substring(index+1);
	var driverId;
	var cityId;
	function pcXq(){
	$.ajax({
		url:'/dcart/detail',
		data:{'dCartId':str},
		dataType:'json',
		success:function(data){
			if(data.s == 0){
				if(data.t == 6){
					alert("该派车单已删除，请手动刷新列表");
					window.close();
				}
				return ;
			}
			driverId = data.dCart.driverId;
			cityId = data.dCart.cityId;
			var state = data.dCart.state;
			var isHidden_gp = 'hidden';
			if(state == "Init"){
				state = "已生成";
			}else if(state == "WaitAllocateCart"){
				state = "已生成";
			}else if(state == "AssignedDriver"){
				state = "已分配";
				isHidden_gp = '';
			}else if(state == "Pickuped"){
				state = "已提货";
			}else if(state == "Delivered"){
				state = "已送达";
			}else if(state == "Finish"){
				state = "已返回";
			}
			var reqPickupTimeText = data.dCart.reqPickupTimeText?data.dCart.reqPickupTimeText:'';
			$(".xinxi5 ul").html("<!--startprint1--><li><p>编号&nbsp;：</p><span>"+data.dCart.id+"</span></li><!--endprint1--><li><p>创&nbsp;建&nbsp;时&nbsp;间：</p><span>"+data.dCart.createTimeText+"</span></li>       <li><p>更&nbsp;新&nbsp;时&nbsp;间：</p><span>"+data.dCart.updateTimeText+"</span></li>   <li><p>要求提货时间：</p><span>"+reqPickupTimeText+"</span></li>        <li><p>提&nbsp;货&nbsp;库&nbsp;房：</p><span>"+data.dCart.storageName+"</span></li>        <li><p>配&nbsp;送&nbsp;城&nbsp;市：</p><span>"+data.dCart.cityName+"</span></li><li><p>派车单状态：</p><span>"+state+"</span></li>")
			
			var psxq = "<tr><td align='center' class='w30'>序号</td><td align='center' class='w100'>订单编号</td><td align='center' class='w70'>商户名称</td><td align='center' class='w70'>商户编号</td><td align='center' class='w100'>下单日期时间</td><td align='center' class='w70'>订购金额</td><td align='center' class='w130'>详细地址</td><td align='center' class='w100'>要求配送时间</td><td align='center' class='w70'>出库金额</td><td align='center' class='w70'>签收类型</td><td align='center' class='w100'>应交款金额</td><td align='center' class='w30'>退货入库</td><td align='center' class='w70'>配送司机</td><td align='center' class='w30'>操作</td></tr>";
			var driverName = data.dCart.driverName?data.dCart.driverName:'';
			var xh = 0;
			var gs = data.dOrderList.length ;
			$.each(data.dOrderList,function(g,f){
				xh += 1;
				var reqDeliveryTimeSlot = f.reqDeliveryTimeSlot?f.reqDeliveryTimeSlot:'';
				var outTakeMoney = f.outTakeMoney?f.outTakeMoney.toFixed(2):"";
				var signType = f.signType?f.signType:'';
				var isBackNotify;
				if(f.isBackNotify == null){
					isBackNotify = "";
				}
				if(f.isBackNotify == 0){
					isBackNotify = "否";
				}
				if(f.isBackNotify == 1){
					isBackNotify = "是";
				}
				if(signType == 'Full'){
					signType = '全部签收';
				}else if(signType == 'Part'){
					signType = '部分签收';
				}else if(signType == 'Refuse'){
					signType = '拒收';
				}
				var fmoney =""
				if(f.outTakeMoney){
					fmoney = f.money.toFixed(2)
				}
				psxq += "<tr class='xq-tr' id='xq-tr"+f.id+"' datadCartId="+data.dCart.id+" dataoDrderId="+f.id+"><td align='center'>"+xh+"</td><td align='center'><a href='2dd-xq.html?id$"+ gs +"="+f.id+"'>"+f.orderNo+"</a></td><td align='center'>"+f.buyerName+"</td><td align='center'>"+f.buyerId+"</td><td align='center'>"+f.orderCreateTimeText+"</td><td align='center'>"+f.orderGoodsMoney.toFixed(2)+"</td><td align='center'>"+f.receiverAddress+"</td><td align='center'>"+reqDeliveryTimeSlot+"</td><td align='center'>"+outTakeMoney+"</td><td align='center'>"+signType+"</td><td align='center'>"+fmoney+"</td><td align='center'>"+isBackNotify+"</td><td align='center' class='driverName'>"+driverName+"</td><td align='center'><a href='javascript:void(0)' "+isHidden_gp+" class='gp-an'>改派</a></td></tr>"
			})
			psxq += "<tr><td colspan='14' style='padding:10px;'><span>注</span>商户数"+data.buyerCount+"，订单数"+data.orderCount+"，订单金额"+data.orderMoney.toFixed(2)+"元，已交款金额"+data.orderPayedMoney.toFixed(2)+"元。</td></tr>"
			$(".psxq").html(psxq);
			hh();
			
			// 派车单历史状态
			var pcState = "<tr><td align='center'>日期</td><td align='center'>状态</td><td align='center'>备注信息</td></tr>"
			$.each(data.dCartLogList,function(q,w){
				pcState += "<tr><td>"+w.operateTimeText+"</td><td>"+w.stateInfo+"</td><td>"+w.remark+"</td></tr>"
			})
			$(".pc-state").html(pcState);
		}
	})
	}
	pcXq();
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
				url:'/driver/resetableAssign/list',
				data:{'cityId':cityId,'modelId':$(".xzcx").val(),'shipperId':$(".ssdw").val(),'driverName':$(".drivername").val()},
				dataType:'json',
				success:function(data){
					if(data.s == 0){
						return ;
					}
					var driverlist = "";
					$(data.driverList).each(function(a,b){
						if(driverId == b.id){
							return ;
						}
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
	 $(".gp-an").live("click",function(){
		 $(".xq-generation").show();
	 })
	 $(".gp-close").live("click",function(){
		 $(".xq-generation").hide();
	 })
	 
	 
	 
	  function hh(){
			var inset = $('.xq-tr');
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
					if(confirm("确定要将此派车单分派给"+ xx[1] +"？")){
						$.ajax({
							url:'/dcart/driver/dorder/alter',
							data:{'dCartId':_this.attr("datadcartid"),'dOrderId':_this.attr("dataodrderid"),'driverId':xx[0]},
							dataType:'json',
							success:function(data){
								if(data.s == 0){
									return ;
								}
								// _this.find(".driverName").text(xx[1]);
								// $("#xq-tr"+_this.attr("dataodrderid")).hide();
								pcXq();
							}
						})
					}


				
				};
				
			}
			
		 }
	 
	
	
})
