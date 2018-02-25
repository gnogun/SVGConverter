package com.al.svgconverter.dto;

public class CsvModel {
	// CSV 번역 내용을 SVG에 적용하기 위해 사용하는 모델 클래스
	// id = CSV 생성 시 지정 된 텍스트 인덱스
	// source = 원문
	// target = 번역문

	private String id;
	private String source;
	private String target;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

}
