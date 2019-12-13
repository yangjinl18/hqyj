$(document).ready(function(){
	
	/*
	 * 控制导航栏active属性
	 */
	$('.nav li a').each(function(){  
		if($($(this))[0].href==String(window.location))  
			$(this).parent().addClass('active');  
	});
   	
    /**
     * 注册
     */
	$("#registerButton").click(function() {
		var userName = $("[name=userName]").val();
		var password = $("[name=password]").val();
		
		if (userName == "" || password == "") {
			alert("用户名密码不能为空");
			return;
		}
		
        //注册请求
		$.ajax({
			url : "/account/doRegister",
			type : "post",
			data : {
				userName : userName,
				password : password
			},
			success : function(data) {
				if (data.status == 200) {			
					var userName = data.object.userName;
					var password = data.object.password;
					//注册成功让他自动登录
					$.ajax({
						url : "/account/doLogin",
						type : "post",
						data : {
							userName : userName,
							password : password
						},
						success : function(data) {
							if (data.status == 200) {
								location.href = "/account/dashboard";
							} else {
								alert(data.message);
							}
						},
						error : function(data) {
							alert("ajax请求失败");
						}
					})
				} else {
					alert(data.message);
				}
			},
			error : function(data) {
				alert("ajax请求失败");
			}
		})
	})
})


