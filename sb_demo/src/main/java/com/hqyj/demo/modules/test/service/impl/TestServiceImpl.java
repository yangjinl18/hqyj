package com.hqyj.demo.modules.test.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hqyj.demo.modules.test.dao.TestDao;
import com.hqyj.demo.modules.test.entity.City;
import com.hqyj.demo.modules.test.entity.Country;
import com.hqyj.demo.modules.test.service.TestService;

@Service
public class TestServiceImpl implements TestService {

	@Autowired
	private TestDao testDao;
	
	@Override
	public List<City> getCitiesByCountryId(int countryId) {
		//Optional.ofNullable(A).orElse(B)
		//如查询结果为null，就给他返回一个空集合[]
		return Optional.ofNullable(testDao.getCitiesByCountryId2(countryId)).orElse(Collections.emptyList());
	}

	@Override
	public Country selectCountryByCountryId(int countryId) {
//		//先通过国家id查到国家，再通过国家id查到所属城市，再返回，dao那里就不需要查询结果集
//		Country country = testDao.selectCountryByCoubtryId(countryId);
//		country.setCities(testDao.getCitiesByCountryId(countryId));
//		return country;
		return testDao.selectCountryByCountryId(countryId);
	}

	@Override
	public Country selectCountryByCountryName(String countryName) {
		//直接查出国家信息和其属的城市
		return testDao.selectCountryByCountryName(countryName);
	}

	
	@Override
	public PageInfo<City> selectCityByPage(int currentPage, int pageSize) {
		//开启分页
		PageHelper.startPage(currentPage, pageSize);
		List<City> cities = testDao.selectCityByPage();
		return new PageInfo<City>(cities);
	}

	@Override
	public City insertCity(City city) {
		testDao.insertCity(city);
		return city;
	}

	@Override
	public City updateCity(City city) {
		testDao.updateCity(city);
		return city;
	}

	/**
	 * @Transactional---开启事务注解
	 * 发生运行时异常或者错误，回滚事务
	 * 其属性：noRollbackFor=xxException.class-----发生某个异常，不会回滚事务
	 * rollbackFor=xxException.class------发生某个异常才会回滚事务
	 * 异常分为编译时异常（例如IO异常，使用try catch捕获或者throw抛出）和运行时异常
	 * 
	 */
	@Override
	@Transactional
	public void deleteCity(int cityId) {
		testDao.deleteCityByCityId(cityId);
//		int i= 1/0;
	}

}
