package com.al.svgconverter.dto;

import java.util.List;


public class PathTreeModel {
	// FancyTree JSON 데이터 매핑 용 모델 클래스
	// title = 이름
	// folder = 폴더 여부
	// key = FancyTree에서 구분을 위해 사용하는 키값 - context path 부터 전체 경로
	// children = 하위 경로 표현을 같은 모델 리스트
	
	private String title;
	private boolean folder;
	private String key;
	private List<PathTreeModel> children;

	public PathTreeModel() {
		// TODO Auto-generated constructor stub
		this.title = "";
		this.folder = false;
		this.key = "";
		this.children = null;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isFolder() {
		return folder;
	}

	public void setFolder(boolean folder) {
		this.folder = folder;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<PathTreeModel> getChildren() {
		return children;
	}

	public void setChildren(List<PathTreeModel> children) {
		this.children = children;
	}

}
