package com.gensi.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//模拟服务接收端
@Controller
@RequestMapping("/test")
public class TestController {

	@ResponseBody
	@RequestMapping("/testInfo")
	public void getResp(String testResp) {
		System.out.println("received response => "+testResp);
	}
}
