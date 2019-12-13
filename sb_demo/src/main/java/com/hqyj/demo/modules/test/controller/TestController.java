package com.hqyj.demo.modules.test.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.pagehelper.PageInfo;
import com.hqyj.demo.modules.test.entity.City;
import com.hqyj.demo.modules.test.entity.Country;
import com.hqyj.demo.modules.test.service.TestService;
import com.hqyj.demo.modules.test.vo.ApplicationTestBean;

@Controller
@RequestMapping("/test")
public class TestController {

	@Autowired
	private TestService testService;

	private final static Logger LOGGER = LoggerFactory.getLogger(TestController.class);
	@Autowired
	private ApplicationTestBean applicationTestBean;
	@Value(value = "${com.hqyj.name}")
	private String name;

	/**
	 * test request
	 */
	@ResponseBody
	@RequestMapping("/hello")
	public String test(HttpServletRequest req,
			@RequestParam(name = "key", defaultValue = "111", required = false) String value) {
		return "hello sb " + name + " " + applicationTestBean.getAge();
	}

	/**
	 * 参数过滤测试
	 */
	@ResponseBody
	@RequestMapping(value = "/param")
	public String testParam(HttpServletRequest req,
			@RequestParam(value = "key", required = false) String value
			) {
		String value2 = req.getParameter("key");
		return "--" + value + "--" + value2 + "--";
	}

	@ResponseBody
	@PostMapping("/hello2")
	public String post() {
		return "hello sb 2 post req";
	}

	/**
	 * log相关接口
	 */
	@RequestMapping("/log")
	@ResponseBody
	public String loggerTest() {
		LOGGER.trace("This is trace log");
		LOGGER.debug("This is debug log");
		LOGGER.info("This is info log");
		LOGGER.warn("This is warn log");
		LOGGER.error("This is error log");

		return "This is logger test.";
	}

	/**
	 * @PathVariable--从路径上获取值{XX}一般是int的id
	 * 
	 *                                     通过国家id查询其拥有的所有city
	 */
	@ResponseBody
	@RequestMapping("/city/{countryId}")
	public List<City> getCitiesByCountryId(@PathVariable int countryId) {
		return testService.getCitiesByCountryId(countryId);

	}

	/**
	 * 通过国家id查询国家
	 */
	@ResponseBody
	@RequestMapping("/country/{countryId}")
	public Country selectCountryByCoubtryId(@PathVariable int countryId) {
		return testService.selectCountryByCountryId(countryId);
	}

	/**
	 * 通过国家名字查国家
	 * 
	 * @RequestParam--Url传值使用的注解;?key=value
	 */
	@ResponseBody
	@RequestMapping("/country")
	public Country selectCountryByCountryName(@RequestParam String countryName) {
		return testService.selectCountryByCountryName(countryName);
	}

	/**
	 * currentPage：当前页 pageSize：每页显示记录数
	 */
	@ResponseBody
	@RequestMapping("/city/{currentPage}/{pageSize}")
	public PageInfo<City> selectCityByPage(@PathVariable int currentPage, @PathVariable int pageSize) {
		return testService.selectCityByPage(currentPage, pageSize);
	}

	/**
	 * consumes = "application/x-www-form-urlencoded"---表示接受from表单数据
	 * 
	 * @ModelAttribute 接收表单数据使用的注解
	 */
	@ResponseBody
	@PostMapping(value = "/city/add", consumes = "application/x-www-form-urlencoded")
	public City insertCity(@ModelAttribute City city) {
		return testService.insertCity(city);
	}

	/**
	 * consumes = "application/json"---接收Json数据
	 * 
	 * @RequestBody接收JSON数据需要使用的注解
	 */
	@ResponseBody
	@PutMapping(value = "/city/update", consumes = "application/json")
	public City updateCity(@RequestBody() City city) {
		return testService.updateCity(city);
	}

	@ResponseBody
	@RequestMapping("/city/delete")
	public void deleteCity(int cityId) {
		testService.deleteCity(cityId);
	}

	@RequestMapping("/index")
	public String testPage(ModelMap modelMap) {
		int countryId = 522;
		List<City> cities = testService.getCitiesByCountryId(countryId);
		cities = cities.stream().limit(10).collect(Collectors.toList());

		modelMap.addAttribute("cities", cities);
		modelMap.addAttribute("thymeleafTitle", "测试");
		modelMap.addAttribute("checked", true);
		modelMap.addAttribute("currentNumber", 80);
		modelMap.addAttribute("changeType", "checkbox");
		modelMap.addAttribute("baiduUrl", "https:www.baidu.com");
		modelMap.addAttribute("shopLogo",
				"https://ss1.bdstaetic.com/5eN1bjq8AAUYm2zgoY3K/r/www/cache/static/protocol/https/home/img/qrcode/zbios_09b6296.png");
		modelMap.addAttribute("country", testService.selectCountryByCountryId(countryId));
		modelMap.addAttribute("city", cities.get(0));
		modelMap.addAttribute("cupdateCityUriity", "/test/city/update");
//		modelMap.addAttribute("template", "test/index");
		return "index";
	}

	/**
	 * method = RequestMethod.POST 表示post请求，
	 * multipart/form-data 表单提交文件专属格式
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = "multipart/form-data")
	public String upload(@RequestParam MultipartFile file, RedirectAttributes redirectAttributes) {

		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "Please choose file!");
			return "redirect:/test/index";
		}

		try {
			String fileName = file.getOriginalFilename();
			String destFileName = "D:/load" + "/" + fileName;
//			String destFileName = "D:/load" +  File.separator + fileName;
			File destFile = new File(destFileName);
			file.transferTo(destFile);
			redirectAttributes.addFlashAttribute("message", "Upload file success!");
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("message", "Upload file fail!");
		}

		return "redirect:/test/index";
	}

	
	/**
	 * 上传多个文件
	 */
	@RequestMapping(value = "/uploadBatchFile", method = RequestMethod.POST, consumes = "multipart/form-data")
	public String uploadBatchFile(@RequestParam MultipartFile[] files, RedirectAttributes redirectAttributes) {

		boolean isEnpty =true;	
		
		try {
			for (MultipartFile file : files) {
				if (file.isEmpty()) {
					continue;
				}

				String fileName = file.getOriginalFilename();
				String destFileName = "D:/load" + "/" + fileName;
				File destFile = new File(destFileName);
				file.transferTo(destFile);
				redirectAttributes.addFlashAttribute("message", "Upload file success!");
				isEnpty =false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("message", "Upload file fail!");
			return "redirect:/test/index";
		}
		
		if (isEnpty) {
			redirectAttributes.addFlashAttribute("message", "Please choose file!");
		}

		return "redirect:/test/index";
	}
	
	/**
	 * 下载文件
	 * 响应头信息
	 * 'Content-Type': 'application/octet-stream',
	 * 'Content-Disposition': 'attachment;filename=req_get_download.js'
	 * @return ResponseEntity ---- spring专门包装响应信息的类
	 */
	@RequestMapping(value = "/download")
	@ResponseBody
	public ResponseEntity<Resource> download(@RequestParam String fileName) {
		try {
			//使用resource下载文件
			Resource resource = new UrlResource(Paths.get("D:/load" + "/" + fileName).toUri());
			if (resource.exists() && resource.isReadable()) {
				return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + resource.getFilename() +"")
						.body(resource);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 下载文件
	 * 将目标文件读取到二进制数组中
	 * 在用response.getOutputStream()获得输出流对象
	 * 将二进制数组中的写入输出流对象
	 */
	@RequestMapping(value = "/download2")
	public void download2(@RequestParam String fileName, HttpServletResponse response) {
		//定义下载文件路径
		File downloadFile = new File("D:/load" + "/" + fileName);
		try {
			//创建输入流对象（读文件）
			FileInputStream fis = new FileInputStream(downloadFile);
			//创建输出流对象（写文件）
			OutputStream os = response.getOutputStream();
			// 设置相关格式
			response.setContentType("application/force-download");
			// 设置下载后的文件名以及header
			response.addHeader("Content-disposition", "attachment;fileName="+ fileName);
			byte[] buffer = new byte[1024];
			int i = fis.read(buffer);
			//循环读取1024字节（1kb）
			while (i!=-1) {
				os.write(buffer, 0, i);
				i = fis.read(buffer);
			}
			fis.close();
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
