package com.hqyj.echarts.test.entity;

import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan
public class Score {

	private int id;
	private String province;
	private String project;
	private String ben;
	private int year;
	private int score;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getBen() {
		return ben;
	}
	public void setBen(String ben) {
		this.ben = ben;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	@Override
	public String toString() {
		return "Score [id=" + id + ", province=" + province + ", project=" + project + ", ben=" + ben + ", year=" + year
				+ ", score=" + score + "]";
	}
	public Score(int id, String province, String project, String ben, int year, int score) {
		super();
		this.id = id;
		this.province = province;
		this.project = project;
		this.ben = ben;
		this.year = year;
		this.score = score;
	}
	public Score() {
		super();
	}
	
}
