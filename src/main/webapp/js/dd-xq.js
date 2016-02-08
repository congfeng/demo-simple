// JavaScript Document
$(function(){
	var href = location.href
	var index = href.indexOf("=")
	var index2 = href.indexOf("$")
	var str = href.substring(index+1);
	var dcartId = "";
	var gs = href.substring(index2+1,index);
	$.ajax({
		 url:'/dorder/detail',
		 data:{dOrderId:str},
		 dataType:'json',
		 success:function(data){
		 	if(data.s == 0){
				return ;
			}
			dcartId = data.dCart.id;
			var xx1 = "<h4>地址信息</h4><ul><li><p>订单编号：</p><span>"+data.dOrder.orderNo+"</span></li><li><p>商户编号：</p><span>"+data.dOrder.buyerId+"</span></li>      <li><p>商户名称：</p><span>"+data.dOrder.buyerName+"</span></li><li><p>配送城市：</p><span>"+data.dOrder.receiverCityName+"</span></li>  <li><p>配送区域：</p><span>"+data.dOrder.receiverDistrictName+"</span></li><li><p>配送街道：</p><span>"+data.dOrder.receiverStreetName+"</span></li>       <li><p>详细送货地址：</p><span>"+data.dOrder.receiverAddress+"</span></li></ul>"
			$(".xinxi").html(xx1);
			
			var state = data.dCart.state;
			var deletedorder = "";
			if(state == "Init"){
				state = "已生成";
				deletedorder = "<button href='javascript:void(0);' class='deletedorder'>删除订单</button>";
			}else if(state == "WaitAllocateCart"){
				state = "已生成";
				deletedorder = "<button href='javascript:void(0);' class='deletedorder'>删除订单</button>";
			}else if(state == "AssignedDriver"){
				state = "已分配";
				deletedorder = "<button href='javascript:void(0);' class='deletedorder'>删除订单</button>";
			}else if(state == "Pickuped"){
				state = "已提货";
			}else if(state == "Delivered"){
				state = "已送达";
			}else if(state == "Finish"){
				state = "已返回";
			}
			var signType = data.dOrder.signType?data.dOrder.signType:'';
			if(signType == 'Full'){
				signType = '全部签收';
			}else if(signType == 'Part'){
				signType = '部分签收';
			}else if(signType == 'Refuse'){
				signType = '拒收';
			}
			var reqDeliveryTimeSlot = data.dOrder.reqDeliveryTimeSlot?data.dOrder.reqDeliveryTimeSlot:'';
			var driverModelName = data.dCart.driverModelName?data.dCart.driverModelName:'';
			var deliveryTimeText = data.dOrder.deliveryTimeText?data.dOrder.deliveryTimeText:'';
			var driverName = data.dCart.driverName?data.dCart.driverName:'';
			var driverMobile = data.dCart.driverMobile?data.dCart.driverMobile:'';
			var xx2 ="<h4>配送信息</h4><ul><li><p>要求送货时间：</p><span>"+reqDeliveryTimeSlot+"</span></li><li><p>提货库房：</p><span>"+data.dCart.storageName+"</span></li><li><p>派车单编号：</p><span>100022</span></li><li><p>当前司机：</p><span>"+driverName+"</span></li><li><p>配送车型：</p><span>"+driverModelName+"</span></li><li><p>司机电话：</p><span>"+driverMobile+"</span></li><li><p>派车单状态：</p><span>"+state+"</span></li><li><p>签收类型：</p><span>"+signType+"</span></li><li><p>送达时间：</p><span>"+deliveryTimeText+"</span></li><li><p>操作：</p><span>"+deletedorder+"</span></li></ul>"
			
			$(".xinxi2").html(xx2);
			
			dele();
			
			var xx3 = "<h4>订单历史状态</h4><table width='480' border='0'><tr><td align='center'>日期</td><td align='center'>状态</td><td align='center'>司机信息</td></tr>"
			 if(data.dOrderLogList){
				$.each(data.dOrderLogList,function(q,w){
				xx3 += "<tr><td>"+w.operateTimeText+"</td><td>"+w.stateInfo+"</td><td>"+w.remark+"</td></tr>"
				})
			 }
			xx3 += "</table>"
			$(".xinxi3").html(xx3);
			var outTakeNo = data.dOrder.outTakeNo?data.dOrder.outTakeNo:"";
			var returnOrderNo = data.dOrder.returnOrderNo?data.dOrder.returnOrderNo:"";
			var xinxi4 = "<h4>商品与支付信息</h4><h5>支付信息：</h5><ul><li><p>支付方式：</p><span>"+data.dOrder.orderPayType+"</span></li><li><p>应收款金额(代收款金额)：</p><span>"+data.money.toFixed(2)+"</span></li></ul><h5>商品信息：</h5><ul><li><p>订单号：</p><span>"+ data.dOrder.orderNo +"</span></li><li><p>退货单号：</p><span>"+ returnOrderNo +"</span></li><li><p>订单签收：</p></li><table border='0' width='600' style='border:1px solid #dcdcdc;border-collapse:collapse;margin-top:10px;'><tr style='background:#f0f0f0;height:30px;'><td class='td'>商品明细</td><td>出库数量</td><td>客户签收</td><td>退货数量</td></tr>"
			var br = false;
			if(data.dOrder.signType){
				$.each(data.goodsList,function(a,b){
				var thnum = Number(b.num) - Number(b.signNum);
				var signNum = b.signNum;
				if(signNum == null){
					signNum = 0;
				}
				if(thnum == 0){
					thnum = "无";
					xinxi4 += "<tr style='height:30px;'><td class='td'>"+b.goodsName+"</td><td>"+b.num+b.goodsUnitName+"</td><td>"+signNum+b.goodsUnitName+"</td><td>"+ thnum +"</td></tr>"
				}else{
					thnum = thnum + b.goodsUnitName;
					xinxi4 += "<tr style='height:30px;'><td class='td'>"+b.goodsName+"</td><td>"+b.num+b.goodsUnitName+"</td><td class='redd'>"+signNum+b.goodsUnitName+"</td><td class='redd'>"+ thnum +"</td></tr>"
				}

				})
			}else{
				$.each(data.goodsList,function(a,b){
				
					xinxi4 += "<tr style='height:30px;'><td class='td'>"+b.goodsName+"</td><td>"+b.num+b.goodsUnitName+"</td><td class='redd'></td><td class='redd'></td></tr>"
				})

				
			}
			
			// <tr style="height:30px;"><td>商品明细</td><td>订单详情</td><td>客户签收</td><td>退货数量</td></tr>
			
			xinxi4 += "</table></ul>"
			$(".xinxi4").html(xinxi4);
			
		 }
		});
	
		
	function dele(){
		$(".deletedorder").click(function(){
			if(confirm("确定要删除此订单吗？此操作不可恢复")){
				$.ajax({
					url:'/dcart/dorder/cancel',
					data:{'dorderId':str,'dcartId':dcartId},
					dataType:'json',
					success:function(data){
						if(data.s == 0){
							return;
						}
						alert("删除成功");
						if(gs == 1){
							window.close();
						}else{
							window.location.href = "2pc-xq.html?id="+ dcartId ;
						}
					}
				})
			}
		})
		
	}
	
		

})
	
 	
