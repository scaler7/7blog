package com.scaler7.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Menu {
	
	private Integer menuId;
	private Integer parentId;
	private String title;
	private String href;
	private String fontFmaily;
	private String icon;
	private boolean spread;
	private boolean isCheck;
	private List<Menu> children;
}
