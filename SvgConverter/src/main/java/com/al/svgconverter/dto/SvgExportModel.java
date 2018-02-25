package com.al.svgconverter.dto;

import java.util.List;

public class SvgExportModel {
	// 웹에서 보낸 데이터와 매핑되는 모델 클래스
	// SVG 변환을 위해 사용되는 모델 클래스
	// svgFiles = 선택 된 SVG 파일들
	// gifFlag = SVG -> GIF 변환 여부
	// epsFlag = SVG -> EPS 변환 여부
	// sepGifFlag = seperate GIG Flag GIF 변환 시 이미지 분리 여부
	// sepEpsFlag = seperate EPS Flag EPS 변환 시 이미지 분리 여부
	
	private List<String> svgFiles;
	private boolean gifFlag;
	private boolean epsFlag;
	private boolean sepGifFlag;
	private boolean sepEpsFlag;
	
	public SvgExportModel() {
		// TODO Auto-generated constructor stub
	}

	public List<String> getSvgFiles() {
		return svgFiles;
	}

	public void setSvgFiles(List<String> svgFiles) {
		this.svgFiles = svgFiles;
	}

	public boolean isGifFlag() {
		return gifFlag;
	}

	public void setGifFlag(boolean gifFlag) {
		this.gifFlag = gifFlag;
	}

	public boolean isEpsFlag() {
		return epsFlag;
	}

	public void setEpsFlag(boolean epsFlag) {
		this.epsFlag = epsFlag;
	}

	public boolean isSepGifFlag() {
		return sepGifFlag;
	}

	public void setSepGifFlag(boolean sepGifFlag) {
		this.sepGifFlag = sepGifFlag;
	}

	public boolean isSepEpsFlag() {
		return sepEpsFlag;
	}

	public void setSepEpsFlag(boolean sepEpsFlag) {
		this.sepEpsFlag = sepEpsFlag;
	}
	
	


}
