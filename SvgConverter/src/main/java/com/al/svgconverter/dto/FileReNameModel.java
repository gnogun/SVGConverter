package com.al.svgconverter.dto;

public class FileReNameModel {
	// 웹에서 보낸 데이터와 매핑되는 모델 클래스
	// 폴더, 파일 이름을 변경을 위해 사용 됨
	// oldPath = 변경 하려는 폴더, 파일의 경로
	// newName = 변경 하려고 하는 이름

	private String oldPath;
	private String newName;

	public FileReNameModel() {
		// TODO Auto-generated constructor stub
	}

	public String getOldPath() {
		return oldPath;
	}

	public void setOldPath(String oldPath) {
		this.oldPath = oldPath;
	}

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

}
