package com.scaler7.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("pages")
@Controller
public class PageController {

	@RequestMapping("common_header")
	public String commonHeader() {
		return "pages/common_header";
	}
	
	@RequestMapping("common_footer")
	public String commonFooter() {
		return "pages/common_footer";
	}
	
	@RequestMapping("common_side")
	public String commonSide() {
		return "pages/common_side";
	}
	
}
