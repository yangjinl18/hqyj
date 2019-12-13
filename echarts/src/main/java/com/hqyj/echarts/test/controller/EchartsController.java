package com.hqyj.echarts.test.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hqyj.echarts.test.entity.exc;
import com.hqyj.echarts.test.service.EchartsService;

@Controller
public class EchartsController {
	
	@Autowired
	private EchartsService echartsService;

	/**
	 * 开始页
	 */
	@RequestMapping("/index")
	public String echartUI() {
		return "index";
	}
	
	@RequestMapping("/echart")
	@ResponseBody
	public Map<String, Object> echart(int num) {
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		
		if (num == 1 || num == 2) {
			
			List<String> xList = new LinkedList<String>();
			xList.add("一月");
			xList.add("二月");
			xList.add("二月");
			map.put("x", xList);
			
			List<Integer> yList = new LinkedList<Integer>();
			yList.add(34);
			yList.add(24);
			yList.add(54);
			map.put("y", yList);
		}else {
			List<String> xList = new LinkedList<String>();
			xList.add("一月");
			xList.add("二月");
			xList.add("三月");
			map.put("x", xList);
			
			List<exc> yList = new LinkedList<exc>();
			yList.add(new exc(23,"一月"));
			yList.add(new exc(34,"二月"));
			yList.add(new exc(69,"三月"));
			map.put("y", yList);
			
		}
		
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/sc")
	public Map<String, Object> getScore(String province) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<Integer> years = echartsService.selectYears();
		map.put("x", years);
		
		List<Integer> scores = echartsService.selectScores(province, "理科", "一本");
		map.put("sly", scores);
		
		List<Integer> scores1 = echartsService.selectScores(province, "理科", "二本");
		map.put("sle", scores1);
		
		List<Integer> scores2 = echartsService.selectScores(province, "文科", "一本");
		map.put("swl", scores2);
		
		List<Integer> scores3 = echartsService.selectScores(province, "文科", "二本");
		map.put("swe", scores3);
		
		
		return map;
	}
	
	
}
