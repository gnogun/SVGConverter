package com.al.svgconverter.dto;

public class SvgLinkBuildModel {
	// 웹에서 보낸 데이터와 매핑되는 모델 클래스
	// linkinfo.xml 파일 생성시 사용
	// sdPath = linkinfo C_NAME 추출을 위해 사용되는 기준 SVG 경로
	// ccPath = linkinfo CC 추출을 위해 사용되는 SVG 경로 
	// clPath = linkinfo CL 추출을 위해 사용되는 SVG 경로
	// hlPath = linkinfo HL 추출을 위해 사용되는 SVG 경로
	// selector = linkinfo 추출 기준이 되는 조건 값 - 현쟈 Font-Family="Helvetica-Bold"
	
	private String sdPath;
	private String ccPath;
	private String clPath;
	private String hlPath;
	private String selector;

	public SvgLinkBuildModel() {
		// TODO Auto-generated constructor stub
	}

	public SvgLinkBuildModel(String sdPath, String ccPath, String clPath,
			String hlPath, String selector) {
		this.sdPath = sdPath;
		this.ccPath = ccPath;
		this.clPath = clPath;
		this.hlPath = hlPath;
		this.selector = selector;
	}

	public String getSdPath() {
		return sdPath;
	}

	public void setSdPath(String sdPath) {
		this.sdPath = sdPath;
	}

	public String getCcPath() {
		return ccPath;
	}

	public void setCcPath(String ccPath) {
		this.ccPath = ccPath;
	}

	public String getClPath() {
		return clPath;
	}

	public void setClPath(String clPath) {
		this.clPath = clPath;
	}

	public String getHlPath() {
		return hlPath;
	}

	public void setHlPath(String hlPath) {
		this.hlPath = hlPath;
	}

	public String getSelector() {
		return selector;
	}

	public void setSelector(String selector) {
		this.selector = selector;
	}

}
