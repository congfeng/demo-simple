// JavaScript Document

$(function(){
	document.title = "承运商";
	var func = function(){
		$.ajax({
			url:'/shipper/list',
			dataType:'json',
			success:function(data){
				console.log(data);
				if(data.s == 0){
					return ;
				}
				var _html = "";
				var xh = 0;
				$.each(data.shipperList,function(a,b){
					xh += 1;
					_html += "<div class='cxsz-list'><div class='border w150 h60'>"+b.id+"</div><div class='border w150 h60'>"+b.name+"</div><div class='border w150 h60'>"+b.driverTotal+"</div><div class='border w150 h60'>"+b.normalDriver+"</div>"
				})
				
				$(".table-list").html(_html);
				
				
			}
		})
	}
	func();
	
	$(".cys-qd").die("click");
	$(".cys-qd").live("click",function(){
		if($(".sk-name").val() == ""){
			alert("请输入承运商名称");
			return;
		}else if($(".sk-name").val().length > 15){
				alert("承运商名称 不能超过15个字");
				return;
		}
		var myReg = /^[\u4e00-\u9fa5]+$/;
		if (myReg.test($(".sk-name").val())) {
			
		}else{
			alert("司机姓名只能输入汉字");
			$(".sk-name").val("");
			return;
		}
		
		
		$.ajax({
			url:'/shipper/save',
			data:{'shipperName':$(".sk-name").val()},
			dataType:'json',
			success:function(data){
				if(data.s == 0){
					return ;
				}
				alert("添加成功");
				$(".sk").hide();
				$(".sk-name").val("");
				func();
			}
			
		})
		
	})
	
	
	
	$(".cxsz-input").live("change",function(){
		$(this).closest(".cxsz-list").find(".cxsz-qd").show();
	})
	$(".xz-cx").click(function(){
		$(".sk").show();
	})
	$(".cx-close").click(function(){
		$(".sk").hide();
	})
	

		
})
