// JavaScript Document

$(function(){
	document.title="启用城市";
	function xzSheng(){
		$.ajax({
			url:'region/childs/all/0',
			dataType:'json',
			success:function(data){
				if(data.s == 0){
					return ;
				}
				var sheng = "";
				$.each(data.regionList,function(e,f){
					sheng += "<option value="+f.id+" code="+f.code+">"+f.name+"</option>"
				})
				$(".sheng").html(sheng);
				xzCity();
				$(".sheng").change(function(){
					xzCity();
				})
			}
		})
	}
	xzSheng();
	function xzCity(){
		$.ajax({
			url:'region/childs/all/'+$(".sheng").val(),
			dataType:'json',
			success:function(data){
				if(data.s == 0){
					return ;
				}
				var shi = "<option value=''>全部</option>";
				$.each(data.regionList,function(e,f){
					shi += "<option value="+f.id+" code="+f.code+">"+f.name+"</option>"
				})
				$(".city").html(shi);
			}
		})
	}
	
	var cxFunc = function(){
		var qycs_cx_url;
		if($(".city").val()){
			qycs_cx_url = 'region/city/all/'+$(".city").val();
		}else{
			qycs_cx_url = 'region/childs/all/'+$(".sheng").val();
		}
		$.ajax({
			url:qycs_cx_url,
			dataType:'json',
			success:function(data){
				if(data.s == 0){
					return;
				}
				var thsj = "";
				var i = 0;
				var shengfen = $(".sheng option:selected").text();
				if($(".city").val()){
					var anniu = "<a href='javascript:void(0)' ids="+data.cityRegion.id+" class='qy-an'>启用</a>";
					if(data.cityRegion.status == "ENABLE"){
						anniu = "<a href='javascript:void(0)' ids="+data.cityRegion.id+" class='jy-an redd'>禁用</a>"
					}
					thsj = "<div class='cxsz-list'><div class='border w100 h60'>"+1+"</div><div class='border w150 h60'>"+shengfen+"</div><div class='border w150 h60'>"+data.cityRegion.name+"</div><div class='border w150 h60'>"+anniu+"</div></div> ";
				}else{
					$.each(data.regionList,function(a,b){
						i += 1;
						var anniu = "<a href='javascript:void(0)' ids="+b.id+" class='qy-an'>启用</a>";
						if(b.status == "ENABLE"){
							anniu = "<a href='javascript:void(0)' ids="+b.id+" class='jy-an redd'>禁用</a>"
						}
						thsj += "<div class='cxsz-list'><div class='border w100 h60'>"+i+"</div><div class='border w150 h60'>"+shengfen+"</div><div class='border w150 h60'>"+b.name+"</div><div class='border w150 h60'>"+anniu+"</div></div> ";
					})
				}
				
				$(".table-list").html(thsj);
				qycsan();
			}
		})
	}
	
	$(".query-button").click(cxFunc)
	
	function qycsan(){
	$(".qy-an").click(function(){
		var Id = $(this).attr("ids");
		$.ajax({
			url:'/region/city/'+ Id +'/enabled',
			dataType:'json',
			success:function(data){
				if(data.s == 0){
					return ;
				}
				alert("启用成功");
				cxFunc();
			}
		})
	})
	$(".jy-an").click(function(){
		var Id = $(this).attr("ids");
		$.ajax({
			url:'/region/city/'+ Id +'/disabled',
			dataType:'json',
			success:function(data){
				if(data.s == 0){
					return ;
				}
				alert("禁用成功");
				cxFunc();
			}
		})
	})
	}
	
})
