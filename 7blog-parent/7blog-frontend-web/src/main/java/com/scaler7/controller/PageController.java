package com.scaler7.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("page")
public class PageController {

	@GetMapping("archive")
	public Object archive() {
		return "/pages/archive";
	}
	
	@GetMapping("about")
	public Object about() {
		return "/pages/About";
	}
	
	@GetMapping("wait")
	public Object wait1() {
		return "/pages/wait";
	}
	
}
