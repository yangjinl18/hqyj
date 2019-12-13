package com.hqyj.echarts.test.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hqyj.echarts.test.dao.EchartsDao;
import com.hqyj.echarts.test.entity.Score;
import com.hqyj.echarts.test.service.EchartsService;

@Service
public class EchartsServiceImpl implements EchartsService {

	@Autowired
	private EchartsDao echartsDao;

	@Override
	public List<Score> selectScore() {
		return echartsDao.selectScore();
	}

	@Override
	public List<Integer> selectYears() {
		List<Integer> y =new LinkedList<Integer>();
		List<Score> scores = echartsDao.selectScore();
		for (Score score : scores) {
			int ye = score.getYear();
			if(y.indexOf(ye)==-1) {
				y.add(ye);
			}
		}
		List<Integer> y2 =new LinkedList<Integer>();
		for (int i = y.size()-1; i >= 0 ; i--) {
			y2.add(y.get(i));
		}
		return y2;
	}

	@Override
	public List<Integer> selectScores(String province, String project, String ben) {
		List<Integer> scores = new LinkedList<Integer>();
		List<Integer> y2 = selectYears();
		for (Integer year : y2) {
			int score = echartsDao.selectScoreByTe(province, project, ben, year);
			scores.add(score);
		}
		return scores;
	}

}
