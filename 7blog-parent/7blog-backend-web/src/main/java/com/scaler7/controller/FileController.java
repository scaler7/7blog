package com.scaler7.controller;

import java.io.IOException;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.scaler7.common.CodeMsg;
import com.scaler7.common.Result;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("file")
public class FileController {

	@Autowired
	private FastFileStorageClient fastFileStorageClient;
	
	private String storagePath = "http://scaler7.online:8888/";
	
	@ApiOperation("文件上传")
	@PostMapping("upload")
	public Object uploadFile(@RequestParam(name = "file")MultipartFile file) {
		String name = file.getOriginalFilename();
		try {
			StorePath uploadFile = fastFileStorageClient.uploadFile(
					file.getInputStream(), 
					file.getSize(), 
					name.substring(name.lastIndexOf(".")), 
					new HashSet<MetaData>(0));
			return new Result(storagePath+uploadFile.getFullPath());
		} catch (IOException e) {
			e.printStackTrace();
			return new Result(CodeMsg.ERROR);
		}
	}
	
}
