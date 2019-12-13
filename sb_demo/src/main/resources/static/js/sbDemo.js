$(document).ready(function(){

	
	/*
	 * 控制导航栏active属性
	 */
	$('.nav li a').each(function(){  
		if($($(this))[0].href==String(window.location))  
			$(this).parent().addClass('active');  
	});
	$("[name='userEdit']").bind(
			"click",
			function() {
				$("#userList").hide();
				$("#userEdit").show();
				var userId = $(this).parents("tr")
						.children("td[name='userId']").text();
				var userName = $(this).parents("tr").children(
						"td[name='userName']").text();
				$("#userName").val(userName);
				$.ajax({
					type : "post",
					url : "/account/roles/user/" + userId,
					contentType : "application/json",
					success : function(data) {
						$.each(data, function(i, item) {
							$("[name='roleCheckbox'][value="+ item.roleId + "]").attr("checked", "checked");
						});
					},
					error : function() {
						alert("ajax请求失败");
					}
				})
				$("#userId").val(userId);
			})

	$("#userSubmit").bind("click", function() {
		var user = {};
		user.userId = $("#userId").val();
		var roles = new Array();
		$("input[name='roleCheckbox']").each(function() {
			if (this.checked) {
				var role = {};
				role.roleId = $(this).val();
				roles.push(role);
			}
		})
		user.roles = roles;
		$.ajax({
			type : "post",
			contentType : "application/json",
			url : "/account/editUser",
			data : JSON.stringify(user),
			success : function(data) {
				if (data.status==200) {
					//拿到当前页码
					var pageNum = $("button[name=currentPage]").text();
					location.href = "/account/userPage/"+ pageNum;
				} else {
					alert(data.message);
					
				}

			},
			error : function() {
				alert("ajax请求失败");
			}
		})
	})

	$("[name=editRole]").bind("click", function() {
		$("#roleList").hide();
		$("#roleEdit").show();
		var roleId = $(this).parents("tr").children("td[name=roleId]").text();
		var roleName = $(this).parents("tr").children("td[name=roleName]").text();
		$("#roleId").val(roleId);
		$("#roleName").val(roleName);
	})
	
	$("#addRole").bind("click", function() {
		$("#roleId").val(0);
		$("#roleList").hide();
		$("#roleEdit").show();
	})
	
	$("#roleSubmit").bind("click", function() {
		var roleId = $("#roleId").val();
		var roleName = $("#roleName").val();
		$.ajax({
			url:"/account/editRole",
			type:"post",
			data:{
				roleId:roleId,
				roleName:roleName
			},
			success:function(data){
				if (data.status==200) {
					location.href ="/account/roles";
				}else {
					$("div[name=messageDiv]").show();
					$("[name=message]").text(data.message);
				}	
			},
			error:function(data){
				alert("ajax请求失败");
			}
		})
	})
	
	$("#addResource").bind("click",function(){
		$("#resourceList").hide();
		$("#resourceEdit").show();
		$("#resourceId").val(0);
	})
	
	$("#resourceSubmit").bind("click",function(){
		var resource={};
		resource.resourceId = $("#resourceId").val();
		resource.resourceName = $("#resourceName").val();
		resource.resourceUri = $("#resourceUri").val();
		resource.permission = $("#permission").val();
		var roles = new Array();
		$("input[name=roleCheckbox]").each(function() {
			if (this.checked) {
				var role ={};
				role.roleId = $(this).val();
				roles.push(role);
			}
		})
		resource.roles=roles;
		$.ajax({
			type:"post",
			url:"/account/editResource",
			data:JSON.stringify(resource),
			contentType:"application/json",
			success:function(data){
				if (data.status==200) {
					location.href="/account/resources";
				}else{
					$("div[name=messageDiv]").show();
					$("[name=message]").text(data.message);
				}
			},
			error:function(data){
				alert("ajax请求失败");
			}
		})
	})
	
	$("[name=editResource]").bind("click",function(){
		$("#resourceList").hide();
		$("#resourceEdit").show();
		var resourceId = $(this).parents("tr").children("td[name=resourceId]").text();
		var resourceName = $(this).parents("tr").children("td[name=resourceName]").text();
		var resourceUri = $(this).parents("tr").children("td[name=resourceUri]").text();
		var permission = $(this).parents("tr").children("td[name=permission]").text();
		
		$("#resourceId").val(resourceId);
		$("#resourceName").val(resourceName);
		$("#resourceUri").val(resourceUri);
		$("#permission").val(permission);
		
		$.ajax({
			type:"post",
			url:"/account/roles/resource/"+resourceId,
			dataType:"json",
			success:function(data){
				$.each(data,function(i,item){
					$("[name=roleCheckbox][value="+item.roleId+"]").attr("checked","checked")
				})
			},
			error:function(data){
				alert("ajxa请求失败")
			}
		})
	})
	
	
	/**
	 * User
	 * 跳转分页页面
	 */
	$("#selectPage").bind("click", function() {
		//当前页码
		var pageNum = $("input[name=pageNum]").val();
		var s = $("span[id=pages]").text();
		var pages= parseInt(s.replace(/[^0-9]/ig,""));
		//正则表达式匹配正整数
		var re = /^[1-9]+[0-9]*]*$/;
		if (!re.test(pageNum)) {
			alert("is  NaN")
			return;
		}else if (pageNum > pages) {
			alert("has no page");
			return;
		}else{
			location.href="/account/userPage/"+pageNum;
		}
		
	})
})


