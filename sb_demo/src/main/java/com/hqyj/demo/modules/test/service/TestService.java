package com.hqyj.demo.modules.test.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.hqyj.demo.modules.test.entity.City;
import com.hqyj.demo.modules.test.entity.Country;

public interface TestService {

	List<City> getCitiesByCountryId(int countryId);

	Country selectCountryByCountryId(int countryId);

	Country selectCountryByCountryName(String countryName);

	PageInfo<City> selectCityByPage(int currentPage, int pageSize);

	City insertCity(City city);

	City updateCity(City city);

	void deleteCity(int cityId);

}
