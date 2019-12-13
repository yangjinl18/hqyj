package com.hqyj.echarts.test.entity;

public class exc {

	private int value;
	private String name;
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public exc(int value, String name) {
		super();
		this.value = value;
		this.name = name;
	}
	public exc() {
		super();
	}
	@Override
	public String toString() {
		return "exc [value=" + value + ", name=" + name + "]";
	}
	
}
