package com.scaler7.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scaler7.entity.BlogRecord;
import com.scaler7.service.BlogRecordService;

@Controller
@RequestMapping("record")
public class RecordController {

	@Autowired
	BlogRecordService blogRecordService;
	
	@GetMapping("init")
	public Object init(Model model) {
		List<BlogRecord> records = blogRecordService.list();
		model.addAttribute("records", records);
		return "pages/record";
	}
	
}
