// JavaScript Document
$(function(){
	var href = location.href
	var index = href.indexOf("=")
	var str = href.substring(index+1);
	var bztext = {};
	$.ajax({
		 url:'/dcart/collection/detail',
		 data:{dcartId:str},
		 dataType:'json',
		 success:function(data){
			console.log(data);
		 	if(data.s == 0){
				return ;
			}
			var html1 = "<h4>派车单明细</h4><table border='0' width='100%' style='border:1px solid #dcdcdc;border-collapse:collapse;'><tr style='background:#f0f0f0;height:30px;'><td style='padding-left:20px;'>派车单</td><td style='text-align:right;padding-right:20px;'></td></tr><tr style='height:70px;'><td style='padding-left:20px;line-height:20px;'><p>派车单编号：<a href='2pc-xq.html?id="+str+"' target='_blank'>"+ str +"</a></p><p>应交款金额："+ data.money +"元</p><p>已交款金额："+data.collection+"元</p></td><td style='text-align:right;padding-right:20px;'>"
			var skzt = "<a href='javascript:void(0)' ids="+ str +" class='skend' >收款完毕</a>"
			if(data.collected){
				skzt = "收款完毕";
			}
			
			html1 += skzt + "</td></tr></table><h4>订单应交款明细</h4><table border='0' width='100%' style='border:1px solid #dcdcdc;border-collapse:collapse;'><tr style='background:#f0f0f0;height:35px;'><td align='center' width='90'>商户名称</td><td align='center'>订单编号</td><td align='center'>签收类型</td><td align='center'>应交款金额</td><td align='center'>已收款金额</td><td align='center' width='200'>新增收款金额</td><td align='center' width='150'>备注</td></tr>"
			
			if(data.collected){
				$.each(data.detailList,function(a,b){
					bztext[b.deliveryOrderId] = b.comment;
					var comment = b.comment?b.comment:"新增";
					var signType = b.signType;
					if(signType == "Full"){
						signType = "全部签收"
					}else if(signType == "Part"){
						signType = "部分签收"
					}else if(signType ==  "Refuse"){
						signType = "拒收"
					}else{
						signType = ""
					}
					html1 += "<tr style='height:40px;'><td align='center'>"+b.buyerName+"</td><td align='center'><a href='2dd-xq.html?dOrderId="+ b.deliveryOrderId +"' target='_blank'> "+b.orderNo+"</a></td><td align='center'>"+signType+"</td><td align='center'>"+b.orderReceivablesMoney+"元</td><td align='center'>"+b.collectionAmount+"元</td><td align='center'><input type='text' placeholder='保留两位小数数字' class='sk-text2' disabled='disabled'><input type='button' ids="+b.deliveryOrderId+"  yjk="+b.collectionAmount+" class='sk-an2' value='确定' disabled='disabled'></td><td align='center' ><a href='javascript:void(0)' class='sk-xz' ids ="+ b.deliveryOrderId +" style='width:150px;text-overflow: ellipsis;overflow:hidden;white-space: nowrap;display:block;'>"+comment+"</a></td></tr>"
				})				
			}else{
				$.each(data.detailList,function(a,b){
					bztext[b.deliveryOrderId] = b.comment;
					var comment = b.comment?b.comment:"新增";
					var signType = b.signType;
					if(signType == "Full"){
						signType = "全部签收"
					}else if(signType == "Part"){
						signType = "部分签收"
					}else if(signType ==  "Refuse"){
						signType = "拒收"
					}else{
						signType = ""
					}
					html1 += "<tr style='height:40px;'><td align='center'>"+b.buyerName+"</td><td align='center'><a href='2dd-xq.html?dOrderId="+ b.deliveryOrderId +"' target='_blank'> "+b.orderNo+" </a></td><td align='center'>"+signType+"</td><td align='center'>"+b.orderReceivablesMoney+"元</td><td align='center'>"+b.collectionAmount+"元</td><td align='center'><input type='text' placeholder='保留两位小数数字' class='sk-text2'><input type='button' ids="+b.deliveryOrderId+" yjk="+b.collectionAmount+" class='sk-an2' value='确定'></td><td align='center'><a href='javascript:void(0)' class='sk-xz' ids ="+ b.deliveryOrderId +"  style='width:150px;text-overflow: ellipsis;overflow:hidden;white-space: nowrap;display:block;'>"+comment+"</a></td></tr>"
				})
			}
			html1 += "</table>";
			$(".sk-con").html(html1);
			skAn();
		 }
		});
	function skAn(){
		
		$(".sk-text2").keydown(function (e) { 
			var _this2 = $(this)
			if (e.which == 13) { 
				_this2.siblings(".sk-an2").click();
			} 
		})
		
		
		$(".sk-an2").click(function(){
			var _this = $(this);
			_this.attr("disabled","disabled");
			var ids = _this.attr("ids");
			var val = _this.siblings(".sk-text2").val();
			var yjk = _this.attr("yjk");
			var he = Number(val) + Number(yjk);
			// if(isNaN(val) || val ==  ""){
				// alert("收款金额只能为数字");
				// $(this).siblings(".sk-text2").val("");
				// return;
			// }
			if(val == ""){
				alert("请输入收款金额");
				_this.attr("disabled",false);
				return;
			}
			if(val == null){
				alert("请输入收款金额");
				_this.attr("disabled",false);
				return;
			}
			if(!isNaN(val)){
            var dot = val.indexOf(".");
				if(dot != -1){
					var dotCnt = val.substring(dot+1,val.length);
					if(dotCnt.length > 2){
						alert("收款金额只能为两位小数数字");
						_this.attr("disabled",false);
						return;
					}
				}
			}else{
				alert("收款金额只能为两位小数数字");
				_this.attr("disabled",false);
				return;
			}
			
			if(he < 0){
				alert("收款金额超限，请重新输入");
				_this.siblings(".sk-text2").val("");
				_this.attr("disabled",false);
				return;
			}
			
			if(confirm("确认收款吗？")){
				$.ajax({
					url:'/dcart/collection',
					data:{'dcartId':str,'dorderId':ids,'amount':val},
					success:function(data){
						if(data.s == 0){
							return;
						}
						alert("收款成功");
						location.reload();
					}
					
				})
			}else{
				_this.attr("disabled",false);
			}
			
		})
		
		$(".sk-xz").click(function(){
		ids = $(this).attr("ids");
		textval = bztext[ids]?bztext[ids]:""
		var html2 ="<h4>收款备注：</h4><textarea class='textarea' style='width:310px;height:105px;border:1px solid #ccc;padding:10px;'>"+ textval +"</textarea><button ids="+ids+" class='sk-bc fr'>保存</button><button class='sk-gb fr'>关闭</button>"
		$(".sk-bz").html(html2)
		$(".sk-bz").show();
		skbzf()
		})
		
		$(".skend").click(function(){
			ids = $(this).attr("ids")
			if(confirm("确认收款吗？")){
				$.ajax({
					url:'/dcart/receivables',
					data:{'dCartId':ids},
					dataType:'json',
					success:function(data){
						if(data.s == 0){
							return ;
						}
						location.reload();
					}
				})
			}
		})
		
	}
	
	
	
	function skbzf(){
		$(".sk-gb").click(function(){
			$(".sk-bz").hide();
		})
		
		$(".sk-bc").click(function(){
			ids = $(this).attr("ids");
			if($(".textarea").val().length > 30){
				alert("备注信息最多可输入30个字符");
				return;
			}
			$.ajax({
				url:'/dcart/comment',
				data:{'dorderId':ids,'comment':$(".textarea").val()},
				dataType:'json',
				success:function(data){
					if(data.s == 0){
						alert("保存失败：" + data.m);
						return;
					}
					alert("保存成功");
					location.reload();
				}
			})			
			$(".sk-bz").hide()
		})
		
		
		
	}
		

})
	
 	
