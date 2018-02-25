package com.al.svgconverter.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class LinkInfo {
	// linkinfo.xml 파일 생성을 위해 사용되는 xml 매핑 모델 클래스	
	
	private String COMP;

	private String C_NAME;

	private String CC;

	private String CL;

	private String HL;

	private String ETC;

	private String SVGFILES;

	private String guid;

	public LinkInfo() {
		this.COMP = "";
		this.C_NAME = "";
		this.CC = "";
		this.CL = "";
		this.HL = "";
		this.ETC = "";
		this.SVGFILES = "";
		this.guid = "";
	}

	@JacksonXmlProperty(localName = "COMP", isAttribute = true)	
	public String getCOMP() {
		return COMP;
	}

	public void setCOMP(String cOMP) {
		COMP = cOMP;
	}

	@JacksonXmlProperty(localName = "C_NAME", isAttribute = true)
	public String getC_NAME() {
		return C_NAME;
	}

	public void setC_NAME(String c_NAME) {
		C_NAME = c_NAME;
	}

	@JacksonXmlProperty(localName = "CC", isAttribute = true)
	public String getCC() {
		return CC;
	}

	public void setCC(String cC) {
		CC = cC;
	}

	@JacksonXmlProperty(localName = "CL", isAttribute = true)
	public String getCL() {
		return CL;
	}

	public void setCL(String cL) {
		CL = cL;
	}

	@JacksonXmlProperty(localName = "HL", isAttribute = true)
	public String getHL() {
		return HL;
	}

	public void setHL(String hL) {
		HL = hL;
	}

	@JacksonXmlProperty(localName = "ETC", isAttribute = true)
	public String getETC() {
		return ETC;
	}

	public void setETC(String eTC) {
		ETC = eTC;
	}

	@JacksonXmlProperty(localName = "SVGFILES", isAttribute = true)
	public String getSVGFILES() {
		return SVGFILES;
	}

	public void setSVGFILES(String sVGFILES) {
		SVGFILES = sVGFILES;
	}

	@JacksonXmlProperty(localName = "guid", isAttribute = true)
	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

}
