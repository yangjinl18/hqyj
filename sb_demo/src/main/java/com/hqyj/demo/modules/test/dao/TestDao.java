package com.hqyj.demo.modules.test.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.hqyj.demo.modules.test.entity.City;
import com.hqyj.demo.modules.test.entity.Country;


/**
 * @Repository--dao仓库注解； @Mapper----相当于这就是一个Mapper.xml文件通过xml里面的namespace接口地址，生成bean，相当于@Repository+@MapperScane；
 */
@Repository
@Mapper
public interface TestDao {
	/**
	 * #{countryId} ---- prepared statement, select * from m_city where country_id =
	 * ?可以防止SQL注入 '${countryId}' ---- statement, select * from m_city where
	 * country_id = 'some id' 根据国家id查询所有城市
	 */
	@Select("select * from m_city where country_id = #{countryId}")
	List<City> getCitiesByCountryId(int countryId);
	List<City> getCitiesByCountryId2(int countryId);
	
	/**
	 * 根据国家id查询国家
	 * 
	 * @Results ---- 封装结果集，对于联表查询的字段，可调用已有的方法getCitiesByCountryId column ---- 对应
	 *          select 查询后的某个字段名，作为映射实体bean属性 或者 作为调用方法的参数 property ---- 对应 实体 bean
	 *          属性
	 *          1、country_id封装了两次，分别对应countryId和cities，而cities属性通过getCitiesByCountryId方法来实现，country_id作为参数
	 *          本身city有countryId，其属性cities集合里也有countryId 2、结果集共享，设置id属性，第二次调用是使用
	 * 
	 */
	@Select("select * from m_country where country_id = #{countryId}")
	@Results(id = "countryResult", value = { @Result(column = "country_id", property = "countryId"),
			@Result(column = "country_id", property = "cities", javaType = List.class, 
			many = @Many(select = "com.hqyj.demo.modules.test.dao.TestDao.getCitiesByCountryId")) })
	Country selectCountryByCountryId(int countryId);

	/**
	 * 通过名字查询国家
	 */
	@Select("select * from m_country where country_name = #{countryName}")
	@ResultMap(value = "countryResult")
	Country selectCountryByCountryName(String countryName);

	/**
	 * 分页查询
	 */
	@Select("select * from m_city")
	List<City> selectCityByPage();

	/**
	 * 插入部分数据
	 * Options:插入后返回id
	 */
	@Options(useGeneratedKeys=true,keyColumn="city_id",keyProperty="cityId")
	@Insert("insert into m_city (city_name, country_id, date_created) values(#{cityName},#{countryId},now())")
	void insertCity(City city);

	/**
	 *通过id修改城市名字 
	 */
	@Update("update m_city set city_name=#{cityName}, local_city_name=#{localCityName}, date_modified= now() where city_id =#{cityId}")
	void updateCity(City city);
	
	/**
	 * 通过id删除城市记录
	 */
	@Delete("delete from m_city where city_id =#{cityId}")
	void deleteCityByCityId(int cityId);
	
}
