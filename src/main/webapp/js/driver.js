// JavaScript Document

$(function(){
	document.title = "添加司机";
	$.ajax({
			url:'/shipper/list',
			dataType:'json',
			success:function(data){
				if(data.s == 0){
					return ;
				}
				var _html = "";
				var xh = 0;
				$.each(data.shipperList,function(a,b){
					xh += 1;
					_html += "<option value="+b.id+">"+b.name+"</option>"
				})
				$("#sj-dw").html(_html);
			}
		})
	//加载车型列表
	$.ajax({
			url:'/model/list',
			dataType:'json',
			success:function(data){
				if(data.s == 0){
					return ;
				}
				var _html = "";
				var xh = 0;
				$.each(data.modelList,function(a,b){
					xh += 1;
					_html += "<option value="+b.id+">"+b.name+"</option>"
				})
				
				$("#sj-cx").html(_html);
			}
		})
	
	function xzSheng(Sheng){
		$.ajax({
			url:'/region/provinces/enabled',
			dataType:'json',
			success:function(data){
				if(data.s == 0){
					return ;
				}
				var sheng = "";
				$.each(data.regionList,function(e,f){
					sheng += "<option value="+f.id+" code="+f.code+">"+f.name+"</option>"
				})
				$("#sheng").html(sheng);
				xzCity($("#sheng").val());
				$("#sheng").change(function(){
					xzCity($(this).val());
				})
			}
		})
	}
	
	xzSheng();
	function xzCity(city){
		$.ajax({
			url:'/region/childs/'+city,
			dataType:'json',
			success:function(data){
				if(data.s == 0){
					return ;
				}
				var shi = "";
				$.each(data.regionList,function(e,f){
					shi += "<option value="+f.id+" code="+f.code+">"+f.name+"</option>"
				})
				$("#city").html(shi);
				
			}
		})
	}
	
	$(".adddriver").click(function(){
		
		if($("#driverName").val() == ""){
			alert("司机姓名不能为空");
			return;
		}
		var myReg = /^[\u4e00-\u9fa5]+$/;
		if (myReg.test($("#driverName").val())) {
			
		}else{
			alert("司机姓名只能输入汉字");
			$("#driverName").val("");
			return;
		}
		var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/
			if(reg.test($("#driverNo").val())){
			}else{
				alert("请输入正确的身份证号码");
				return;
			}
		var ss = /^0?1[3|4|5|8|7][0-9]\d{8}$/;
			if(ss.test($("#sj-phone").val())){
			}else{
				alert("请输入正确的手机号码");
				return;
			}
		$.ajax({
			url:'/driver/create',
			data:{
				name:$("#driverName").val(),
				phone:$("#sj-phone").val(),
				sex:$("#sj-xb").val(),
				idNumber:$("#driverNo").val(),
				modelId:$("#sj-cx").val(),
				modelName:$("#sj-cx option:selected").text(),
				plateNumber:$("#sj-cphm").val(),
				cityId:$("#city").val(),
				cityName:$("#city option:selected").text(),
				jobType:$("#sj-state").val(),
				shipperId:$("#sj-dw").val()?$("#sj-dw").val():'',
				shipperName:$("#sj-dw option:selected").text()
			},
			dataType:'json',
			success:function(data){
				if(data.s == 0){
					return;
				}
				alert("添加成功");
				location.reload();
			}
		})
	})
})
	
