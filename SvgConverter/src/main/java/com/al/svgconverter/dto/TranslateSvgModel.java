package com.al.svgconverter.dto;

import org.springframework.web.multipart.MultipartFile;

public class TranslateSvgModel {
	// SVG 번역을 위해 사용되는 클래스
	// translateFile = 번역 CSV MultipartFile
	
	private MultipartFile translateFile;
	
	public TranslateSvgModel() {
		// TODO Auto-generated constructor stub
	}

	public MultipartFile getTranslateFile() {
		return translateFile;
	}

	public void setTranslateFile(MultipartFile translateFile) {
		this.translateFile = translateFile;
	}

}
