package com.al.svgconverter.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class LinkPool {
	// linkinfo.xml 파일 생성을 위해 사용되는 xml 매핑 모델 클래스

	@JacksonXmlProperty(localName = "link_info")
	private List<LinkInfo> linkInfo;

	private String name;

	public LinkPool() {
		// TODO Auto-generated constructor stub
		this.linkInfo = new ArrayList<LinkInfo>();
		this.name = "link_info";
	}

	public List<LinkInfo> getLinkInfo() {
		return linkInfo;
	}

	public void setLinkInfo(List<LinkInfo> linkInfo) {
		this.linkInfo = linkInfo;

	}

	@JacksonXmlProperty(localName = "name", isAttribute = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
