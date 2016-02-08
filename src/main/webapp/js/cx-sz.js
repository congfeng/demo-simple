// JavaScript Document

$(function(){
	document.title = "车型设置";
	var jzym; 
	 jzym = function(){
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
					_html += "<div class='cxsz-list'><div class='border w100 h60'>"+xh+"</div><div class='border w100 h60'>"+b.name+"</div><div class='border w100 h60'><input type='text' class='cxsz-input cxsz1' value='"+b.load+"'/><input type='hidden' value='"+b.load+"'/></div><div class='border w100 h60'><input type='text' class='cxsz-input cxsz2' value='"+b.volume+"'/><input type='hidden' value='"+b.volume+"'/></div><div class='border w100 h60'><a href='javascript:void(0)' class='cxsz-sc'  ids="+b.id+">X删除</a><a href='javascript:void(0)' class='cxsz-qd' ids="+b.id+" names="+b.name+">确定</a></div></div>"
				})
				
				$(".table-list").html(_html);
				cxSz();
			}
		})
	};
	jzym();
	$(".cx-sz-qd").click(function(){
			if($(".sk-name").val() == ""){
				alert("车型名称不能为空");
				return;
			}
			if($(".sk-load").val() == ""){
				alert("载重重量不能为空");
				return;
			}
			var re = /^\d+(?=\.{0,1}\d+$|$)/
			if(!re.test($(".sk-load").val())){
				alert("载重重量只可以是数字");
				$(".sk-load").val("")
				return;
			}
			if($(".sk-volume").val() == ""){
				alert("载重体积不能为空");
				return;
			}
			if(!re.test($(".sk-volume").val())){
				alert("载重体积只可以是数字");
				$(".sk-volume").val("");
				return;
			}
			$.ajax({
				url:'/model/add',
				data:{'name':$(".sk-name").val(),'load':$(".sk-load").val(),'volume':$(".sk-volume").val()},
				dataType:'json',
				success:function(data){
					if(data.s == 0){
						return ;
					}
					alert("添加成功");
					$(".sk").hide();
					jzym();
				}
				
			})
			
		})
	
	function cxSz(){
		
	
		$(".cxsz-qd").click(function(){
			_this = $(this);
			var ids = $(this).attr("ids");
			var name = $(this).attr("names");
			var cxsz1 = $(this).closest(".cxsz-list").find(".cxsz1").val();
			var cxsz2 = $(this).closest(".cxsz-list").find(".cxsz2").val();
			$.ajax({
				url:'/model/update',
				data:{'id':ids,'name':name,'load':cxsz1,'volume':cxsz2},
				dataType:'json',
				success:function(data){
					if(data.s == 0){
						return ;
					}
					alert("修改成功");
					_this.hide();
				}
				
			})
		})
		
		
		
		
		$(".cxsz-sc").click(function(){
			var ids = $(this).attr("ids");
			if(confirm("确定要删除该车型？"))
			$.ajax({
				url:'/model/delete',
				data:{modelId:ids},
				dataType:'json',
				success:function(data){
					if(data.s == 0){
						return ;
					}
					alert("删除成功");
					jzym();
				}
			})
		})
		
		$(".cxsz-input").change(function(){
			var re = /^\d+(?=\.{0,1}\d+$|$)/;
			if(!re.test($(this).val())){
			    var isWeightInput = $(this).hasClass("cxsz1");
			    var alertText = isWeightInput? '载重重量' : '载重体积';
				alert(alertText + "只可以是数字");
				$(this).val($(this).next().val());
				
				$(this).closest(".cxsz-list").find(".cxsz-qd").hide();
				return;
			}
			
			$(this).closest(".cxsz-list").find(".cxsz-qd").show();
		})
		$(".xz-cx").click(function(){
			$(".sk-div input").each(function(){
				$(this).val('');
			});
			
			$(".sk").show();
		})
		$(".cx-close").click(function(){
			$(".sk").hide();
		})
	
	}
	
	
		
})
