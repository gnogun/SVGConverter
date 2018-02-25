package com.al.svgconverter.dto;

import java.util.List;

public class Svg2CsvModel {
	// 웹에서 보낸 데이터와 매핑되는 모델 클래스
	// SVG 파일에서 번역 용 CSV 파일 생성시 사용
	
	private List<String> svgFiles;

	public List<String> getSvgFiles() {
		return svgFiles;
	}

	public void setSvgFiles(List<String> svgFiles) {
		this.svgFiles = svgFiles;
	}

}
