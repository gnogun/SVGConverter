package com.al.svgconverter.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class SvgModel {
	// SVG 단일 파일 업로드시 사용되는 모델 클래스
	// currentPath = 업로드 된 SVG 파일이 들어갈 부모 경로
	// svgFiles = SVG MultipartFile
	
	private String currentPath;
	private List<MultipartFile> svgFiles;
	
	
	public SvgModel() {
	}
	
	public String getCurrentPath() {
		return currentPath;
	}
	public void setCurrentPath(String currentPath) {
		this.currentPath = currentPath;
	}
	public List<MultipartFile> getSvgFiles() {
		return svgFiles;
	}
	public void setSvgFiles(List<MultipartFile> svgFiles) {
		this.svgFiles = svgFiles;
	}
	
	
	
	
}
