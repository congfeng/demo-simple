// JavaScript Document

$(function(){
	
	var href = location.href;
	var index = href.indexOf("=");
	var str = href.substring(index+1);
	sjXqjz()
	function sjXqjz(){
	$.ajax({
		url:'/driver/'+str,
		dataType:'json',
		success:function(data){
			if(data.s == 0){
				return ;
			}
			var sex = data.driver.sex;
			var shipperId = data.driver.shipperId;
			var modelId = data.driver.modelId;
			var plateNumber = data.driver.plateNumber?data.driver.plateNumber:"";
			var driverState = data.driver.driverState;
			var driverstate = "";
			var  type = data.driver.jobType;
			var jobType = "";
			var shengId = data.province.id;
			var cityId = data.driver.cityId;
			if(data.driver.jobType == "FULL_TIME"){
				jobType = "全职";
			}else if(data.driver.jobType == "PART_TIME"){
				jobType = "兼职";
			}
			if(data.driver.driverState == "Normal"){
							driverstate = "正常";
						}else if(data.driver.driverState == "Freeze"){
							driverstate = "冻结";
						}else if(data.driver.driverState == "Finish"){
							driverstate = "停用";
						}
			var sex2 = data.driver.sex
			var sex = data.driver.sex;
			if(sex == 0){
				sex = "男";
			}else{
				sex = "女";
			}
			var _html2 = "<li><p>司机姓名：</p><span>"+data.driver.name+"</span></li><li><p>性别：</p><span>"+sex+"</span></li><li><p>身份证号：</p><span>"+data.driver.idNumber+"</span></li><li><p>车  型：</p><span>"+data.driver.modelName+"</span></li><li><p>车牌号码：</p><span>"+plateNumber+"</span></li><li><p>手机号码：</p><span>"+data.driver.phone+"</span></li><li><p>服务城市：</p><span>"+data.driver.cityName+"</span></li><li><p>司机类型：</p><span>"+jobType+"</span></li><li><p>司机状态：</p><span>"+driverstate+"</span></li><li><p>所属单位：</p><span>"+data.driver.shipperName+"</span></li>";
			var _html3 = "";
			for (var key in data.stateMap){
				_html3 += "<option value="+key+">"+data.stateMap[key]+"</option>";
			} 
			$("#sj-type").html(_html3);
			$(".xinxi5 ul").html(_html2);
			$("#driverName").val(data.driver.name);
			$("#driverNo").val(data.driver.idNumber);
			$("#sj-cphm").val(plateNumber);
			$("#sj-phone").val(data.driver.phone);
			$("#sj-xb").find("option[value="+sex2+"]").attr("selected",true);
			$("#sj-state").find("option[value="+type+"]").attr("selected",true);
			$("#sj-type").find("option[value="+driverState+"]").attr("selected",true);
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
					$("#sj-cx").find("option[value="+modelId+"]").attr("selected",true);
				}
			})
			//所属单位列表
			$.ajax({
				url:'/shipper/list',
				dataType:'json',
				success:function(data){
					if(data.s == 0){
						return ;
					}
					var _html = "";
					$.each(data.shipperList,function(a,b){
						_html += "<option value="+b.id+">"+b.name+"</option>"
					})
					$("#sj-dw").html(_html);
					$("#sj-dw").find("option[value="+shipperId+"]").attr("selected",true);
				}
			})
				//加载省列表
				var shengList = "";
				$.each(data.provinceList,function(e,f){
					shengList += "<option value="+f.id+" code="+f.code+">"+f.name+"</option>"
				})
				$("#sheng").html(shengList);
				$("#sheng").find("option[value="+shengId+"]").attr("selected",true);
				$("#sheng").change(function(){
					xzCity($(this).val());
				})
				//初始化市列表
				var shi = "";
				$.each(data.cityList,function(e,f){
					shi += "<option value="+f.id+" code="+f.code+">"+f.name+"</option>"
				})
				$("#city").html(shi);
				$("#city").find("option[value="+cityId+"]").attr("selected",true);
				//加载市列表
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
				
			
			
			
		}
		
	})
	
	}
	
	$(".xq-xg-an").click(function(){
		$(".sj-xq-warp").show();
	})
	
	$(".sj-xq-an").click(function(){
		
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
			url:'/driver/update',
			data:{
				driverId:str,
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
				shipperName:$("#sj-dw option:selected").text(),
				driverState:$("#sj-type").val()
			},
			dataType:'json',
			success:function(data){
				if(data.s == 0){
					return ;
				}
				alert("修改成功");
				sjXqjz();
				$(".sj-xq-warp").hide();
			}
		})
	})
	$(".sj-qx-an").click(function(){
		sjXqjz();
		$(".sj-xq-warp").hide();
	})
})
	
