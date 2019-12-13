package com.hqyj.echarts.test.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.hqyj.echarts.test.entity.Score;

@Repository
@Mapper
public interface EchartsDao {

	@Select("select * from gaokao")
	List<Score> selectScore();

	@Select("select year from gaokao")
	List<Score> selectYears();

	@Select("select score from gaokao where province =#{province}"
			+ " and  project=#{project} and ben=#{ben} and year=#{year}")
	Integer selectScoreByTe(String province, String project, String ben, int year);
	
	
}
