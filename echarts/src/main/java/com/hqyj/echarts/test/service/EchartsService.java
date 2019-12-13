package com.hqyj.echarts.test.service;

import java.util.List;

import com.hqyj.echarts.test.entity.Score;

public interface EchartsService {

	List<Score> selectScore();

	List<Integer> selectYears();


	List<Integer> selectScores(String province, String project, String ben);

}
