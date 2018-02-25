package com.al.svgconverter.dto;

import org.springframework.web.multipart.MultipartFile;

public class SvgZipModel {
	// SVG 압축 파일 업로드시 사용되는 모델 클래스
	// currentPath = 업로드 된 SVG 파일이 들어갈 부모 경로
	// svgZipInput = SVG zip MultipartFile
	// rootNameFlag = 압축파일 이름으로 폴더 생성 여부 확인
	
	private String currentPath;
	private MultipartFile svgZipInput;
	private boolean rootNameFlag;

	public SvgZipModel() {
		this.rootNameFlag = false;
	}

	public String getCurrentPath() {
		return currentPath;
	}

	public void setCurrentPath(String currentPath) {
		this.currentPath = currentPath;
	}

	public SvgZipModel(MultipartFile svgZipInput) {
		this.svgZipInput = svgZipInput;
	}

	public MultipartFile getSvgZipInput() {
		return svgZipInput;
	}

	public void setSvgZipInput(MultipartFile svgZipInput) {
		this.svgZipInput = svgZipInput;
	}

	public boolean isRootNameFlag() {
		return rootNameFlag;
	}

	public void setRootNameFlag(boolean rootNameFlag) {
		this.rootNameFlag = rootNameFlag;
	}

}
