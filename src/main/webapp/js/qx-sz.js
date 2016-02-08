$(function(){
	document.title = "权限设置";
	$(".yg-query-button").click(function(){
		var valu = $(".xzyg").val();
		var peopleId = "";
		if(valu == ""){
			alert("请输入员工姓名");
			return;
		}
		$.ajax({
			url:'/people/list',
			data:{peopleName:$(".xzyg").val()},
			dataType:'json',
			success:function(data){
				if(data.s == 0){
					return ;
				}
				if(data.userlist == ""){
					alert("该员工不存在，请输入正确的员工姓名")
					return;
				}
				peopleId = data.userlist[0].user_id;
				$(".qx-query-button").attr("peopleId",peopleId)
				$("#qx-sz").show();
				$(".yg").html("正在对"+data.userlist[0].real_name+"进行设置");
				$(".qx-query-button").attr("ids",data.userlist[0].real_name);
				//加载员工以有库房
				$.ajax({
					url:'/storage/with/people',
					data:{'peopleId':peopleId},
					dataType:'json',
					success:function(data){
						console.log(data);
						if(data.s == 0){
							return ;
						}
						var _html = "";
						if(data.storageList == ""){
							
						}else{
							$.each(data.storageList,function(a,b){
								_html += "<p id='"+b.id+"'>"+b.name+"&nbsp;&nbsp;<a href='javascript:void(0)' class='redd deleteKf'>X</a></p>"
							})
						}
						$(".xzkf").html(_html);
						$(".deleteKf").unbind('click').click(function(){
							if(confirm("确认移除该库房权限?")){
								$(this).parent().remove();
							}
						})
					}
				})
				//加载员工已有导航
				$.ajax({
					url:'/system/module/people',
					data:{'peopleId':peopleId},
					dataType:'json',
					success:function(data){
						if(data.s == 0){
							return ;
						}
						var _html2 = "";
						$.each(data.peopleModuleTree,function(c,d){
							_html2 += "<div class='item-all pr' ><a href='javascript:void(0);' class='item-button qx-item-button pa' style='background:url(images/item-button1.jpg)'></a><div class='item'><input type='checkbox' value="+d.moduleId+" class='parent"+d.moduleId+"' />"+d.moduleName+"</div><div class='kf-item'><ul>"
							
							$.each(d.childs,function(a,b){
								var s = "";
									if(b.peopleId == null){
										s = "";
									}else{
										s = "checked='checked'";
										$(".parent"+b.parentId).attr("checked","checked");
									}
									
								_html2 += "<li><input type='checkbox' value='"+b.moduleId+"' "+s+" />"+b.moduleName+"</li>"
							})
							_html2 += "</ul></div></div>";
						})
					
						
						$(".qy-all").html(_html2);
						qxSzinput()
						$.each($(".kf-item input:checked"),function(u,i){
							$(this).closest(".item-all").find(".item input").attr("checkdata","checked")
							$(this).closest(".item-all").find(".item input").attr("checked","checked")
						})
					}
				})
			}
		})
		
		
		
		
		
		
	})
	// function qxSzbc(){
	$(".qx-query-button").click(function(){
			var xzkfId = "";
			var xzQx = "";
			$.each($(".xzkf p"),function(f,g){
				xzkfId += $(this).attr("id") + ",";
			})
			$.each($(".qy-all input:checked"),function(h,j){
				xzQx += $(this).val() + ",";
			})
			var valuee = $(this).attr("ids");
			var peopleId = $(this).attr("peopleId");
			xzkfId = xzkfId.substring(0,xzkfId.length-1);
			xzQx = xzQx.substring(0,xzQx.length-1);
			$.ajax({
				url:'/people/auth',
				data:{'peopleId':peopleId,'peopleName':valuee,'storageIds':xzkfId,'moduleIds':xzQx},
				dataType:'json',
				success:function(data){
					if(data.s == 0){
						return ;
					}
					alert("保存成功");
					$(".xzyg").val("");
					$("#qx-sz").hide();
					
					
					
				}
				
				
				
			})
			
			
		})
	
	// }
	
	$.ajax({
			url:'/storage/list/all',
			dataType:'json',
			success:function(data){
				if(data.s == 0){
					return ;
				}
				var ck = "";
				$.each(data.storageList,function(a,b){
					ck += "<option value='"+b.id+"' code="+b.code+">"+b.name+"</option>"
				})
				$(".thkf").html(ck);
			}
		})

	$(".thkf-an").click(function(){
		var text = $(".thkf option:selected").text();
		var kfId = $(".thkf").val();
		var flag = true;
		$.each($(".xzkf p"),function(a,b){
			if($(this).attr("id") == kfId){
				flag = false;
			}
		})
		if(flag){
			$(".xzkf").append("<p id='"+kfId+"'>"+text+"&nbsp;&nbsp;<a href='javascript:void(0)' class='redd deleteKf2'>X</a></p>");
		}else{
			alert("该库房已存在");
		}
		$(".deleteKf2").unbind('click').click(function(){
			if(confirm("确认移除该库房权限?")){
				$(this).parent().remove();
			}
		})
		
	})
	
	$.each($(".kf-item input:checked"),function(u,i){
		$(this).closest(".item-all").find(".item input").attr("checkdata","checked")
		$(this).closest(".item-all").find(".item input").attr("checked","checked")
	})
	
	
	function qxSzinput(){
	
		$(".kf-item input").click(function(){
			if($(this).attr("checked")){
				$(this).closest(".item-all").find(".item input").attr("checkdata","checked")
				$(this).closest(".item-all").find(".item input").attr("checked","checked")
			}

		})
			

		
		$(".item-all .qx-item-button").click(function(){
				if($(this).attr("style") == "background:url(images/item-button1.jpg)"){
					$(this).attr("style","background:url(images/item-button2.jpg)");
					$(this).parent().find(".kf-item").slideDown(100);
				}else{
					$(this).attr("style","background:url(images/item-button1.jpg)");
					$(this).parent().find(".kf-item").slideUp(100);
				}
		})
		
		// $(".kf-item li input").each(function(k,v){
				// if($(this).attr("checked") == "checked"){
					// alert($(this).attr("ids"))
				// }
		// })
		$(".item input").click(function(){
			if($(this).attr("checkdata") == "checked"){
			$(this).attr("checked",false)
			$(this).closest(".item-all").find(".kf-item input").attr("checked",false)
			$(this).attr("checkdata","")
		}else{
			$(this).attr("checked",true)
			$(this).closest(".item-all").find(".kf-item input").attr("checked",true)
			$(this).attr("checkdata","checked")
		}
		})
	}
})