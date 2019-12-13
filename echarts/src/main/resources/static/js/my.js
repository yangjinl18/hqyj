$(function() {
	$(".button1").bind("click",function(){
		var elm = $(this).val();
		// 基于准备好的dom，初始化echarts实例
	    var myChart = echarts.init(document.getElementById('mydiv')); 
		
		if (elm == 1) {
			$.ajax({
				url:"/echart",
				type:"post",
				data:{
					num:elm
				},
				success:function(data){
					// 指定图表的配置项和数据
				    var option = {
				    	    xAxis: {
				    	        type: 'category',
				    	        data: data.x
				    	    },
				    	    yAxis: {
				    	        type: 'value'
				    	    },
				    	    series: [{
				    	        data: data.y,
				    	        type: 'bar'
				    	    }]
				    	};
				    // 使用刚指定的配置项和数据显示图表。
				    myChart.setOption(option);
				},
				error:function(data){
					alert("请求失败")
				}
			})
		}else if (elm == 2) {
			$.ajax({
				url:"/echart",
				type:"post",
				data:{
					num:elm
				},
				success:function(data){
					// 指定图表的配置项和数据
				    var option = {
				    	    xAxis: {
				    	        type: 'category',
				    	        data: data.x
				    	    },
				    	    yAxis: {
				    	        type: 'value'
				    	    },
				    	    series: [{
				    	        data: data.y,
				    	        type: 'line'
				    	    }]
				    	};
				    // 使用刚指定的配置项和数据显示图表。
				    myChart.setOption(option);
				},
				error:function(data){
					alert("请求失败")
				}
			})
		}else if (elm == 3) {
			$.ajax({
				url:"/echart",
				type:"post",
				data:{
					num:elm
				},
				success:function(data){
					// 指定图表的配置项和数据
				    var option = {
				    	    title : {
				    	        text: '某站点用户访问来源',
				    	        subtext: '纯属虚构',
				    	        x:'center'
				    	    },
				    	    tooltip : {
				    	        trigger: 'item',
				    	        formatter: "{a} <br/>{b} : {c} ({d}%)"
				    	    },
				    	    legend: {
				    	        orient: 'vertical',
				    	        left: 'left',
				    	        data: data.x
				    	    },
				    	    series : [
				    	        {
				    	            name: '访问来源',
				    	            type: 'pie',
				    	            radius : '55%',
				    	            center: ['50%', '60%'],
				    	            data:data.y,
				    	            itemStyle: {
				    	                emphasis: {
				    	                    shadowBlur: 10,
				    	                    shadowOffsetX: 0,
				    	                    shadowColor: 'rgba(0, 0, 0, 0.5)'
				    	                }
				    	            }
				    	        }
				    	    ]
				    	};

				    // 使用刚指定的配置项和数据显示图表。
				    myChart.setOption(option);
				},
				error:function(data){
					alert("请求失败")
				}
			})
		}
	})
	
	
	$(".button2").bind("click",function(){		
		var elm = $(this).val();
		// 基于准备好的dom，初始化echarts实例
	    var myChart = echarts.init(document.getElementById('mydiv')); 
	    $.ajax({
			url:"/sc",
			type:"post",
			data:{
				province:elm
			},
			success:function(data){
				var option = {
					    title: {
					        text: ''
					    },
					    tooltip: {
					        trigger: 'axis'
					    },
					    legend: {
					        data:[''+elm+'理科一本',''+elm+'理科二本',''+elm+'文科一本',''+elm+'文科二本']
					    },
					    grid: {
					        left: '3%',
					        right: '4%',
					        bottom: '3%',
					        containLabel: true
					    },
					    toolbox: {
					        feature: {
					            saveAsImage: {}
					        }
					    },
					    xAxis: {
					        type: 'category',
					        name:'年份',
					        boundaryGap: false,
					        data:data.x
					    },
					    yAxis: {
					        type: 'value',
					        name:'分数',
					        min:330,
					        max:630,
					        splitNumber:10
					    },
					    series: [
					        {
					            name:''+elm+'理科一本',
					            type:'line',
					            stack: '',
					            data:data.sly
					        },
					        {
					            name:''+elm+'理科二本',
					            type:'line',
					            stack: '',
					            data:data.sle
					        },
					        {
					            name:''+elm+'文科一本',
					            type:'line',
					            stack: '',
					            data:data.swl
					        },
					        {
					            name:''+elm+'文科二本',
					            type:'line',
					            stack: '',
					            data:data.swe
					        }
					    ]
					};
				 myChart.setOption(option);
			},
			error:function(){
				alert("请求失败")
			}
		})
	})
	
	
})





