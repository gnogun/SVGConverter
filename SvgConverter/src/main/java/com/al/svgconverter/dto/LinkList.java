package com.al.svgconverter.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "link_list")
public class LinkList {
	// linkinfo.xml 파일 생성을 위해 사용되는 xml 매핑 모델 클래스
	private String prjname;
	private String ldup;
	
	@JacksonXmlProperty(localName = "link_pool")
	private LinkPool linkPool;
		
	public LinkList() {
		// TODO Auto-generated constructor stub
		
	}

	@JacksonXmlProperty(localName = "prjname", isAttribute = true)	
	public String getPrjname() {
		return prjname;
	}

	public void setPrjname(String prjname) {
		this.prjname = prjname;
	}

	@JacksonXmlProperty(localName = "ldup", isAttribute = true)	
	public String getLdup() {
		return ldup;
	}

	public void setLdup(String ldup) {
		this.ldup = ldup;
	}

	public LinkPool getLinkPool() {
		return linkPool;
	}

	public void setLinkPool(LinkPool linkPool) {
		this.linkPool = linkPool;
	}

	
	
	
	
}
