package com.al.svgconverter.dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.al.svgconverter.dto.FileReNameModel;
import com.al.svgconverter.dto.PathTreeModel;

@Repository
public class TreeDaoImpl implements TreeDao {

	@Autowired
	private ServletContext context;

	@Override
	public ArrayList<PathTreeModel> getTree() {
		// SVG 파일, 디렉토리 구조 생성 함수

		// 업로드 된 SVG 파일들이 저장되는 경로
		String rootPath = context.getRealPath("/root/");

		return buildTree(rootPath);
	}

	@Override
	public ArrayList<PathTreeModel> getDirectoryTree() {
		// SVG 디렉토리 구조 생성 함수
		// 디렉토리 구조만 담는다
		// 현재 사용하지 않는 함수

		// 업로드 된 SVG 파일들이 저장되는 경로
		String rootPath = context.getRealPath("/root/");

		return buildDirectoryTree(rootPath);
	}

	public ArrayList<PathTreeModel> buildTree(String path) {
		// SVG 경로의 파일, 디렉토리 구조를 FancyTree(javascript library)에서 사용하는 JSON 데이터
		// 구조로 생성

		File currentFile = new File(path);
		File[] files = currentFile.listFiles();

		if (files == null)
			return null;

		ArrayList<PathTreeModel> treePath = new ArrayList<PathTreeModel>();

		for (File file : files) {
			/*
			 * if(file.getName().endsWith("_skt.svg")){ continue; }
			 */

			PathTreeModel model = new PathTreeModel();
			model.setTitle(file.getName());
			model.setKey(file.getName());
			if (file.isDirectory()) {
				model.setFolder(file.isDirectory());
				model.setChildren(buildTree(file.getAbsolutePath()));
				treePath.add(model);
			} else {

				if (file.getName().endsWith(".svg")) {
					treePath.add(model);
				}
			}

		}

		return treePath;
	}

	public ArrayList<PathTreeModel> buildDirectoryTree(String path) {
		// SVG 경로의 디렉토리 구조를 FancyTree(javascript library)에서 사용하는 JSON 데이터 구조 생성
		// 디렉토리 구조만 생성
		// 현재 사용하지 않는 함수

		File currentFile = new File(path);
		File[] files = currentFile.listFiles();

		if (files == null)
			return null;

		ArrayList<PathTreeModel> treePath = new ArrayList<PathTreeModel>();

		for (File file : files) {
			if (file.isDirectory()) {
				PathTreeModel model = new PathTreeModel();
				model.setTitle(file.getName());
				model.setKey(file.getName());
				model.setFolder(file.isDirectory());
				model.setChildren(buildDirectoryTree(file.getAbsolutePath()));
				treePath.add(model);
			}
		}

		return treePath;
	}

	@Override
	public String createNewFolder(String folderName) {
		// 새폴더 생성
		// folderName은 폴더 구조를 가지고 있는 full path String

		String rootPath = context.getRealPath("/root/");

		File targetFile = new File(rootPath + folderName);

		if (!targetFile.exists()) {
			targetFile.mkdirs();
			return "success";
		} else {
			return ".";
		}

	}

	@Override
	public String reName(FileReNameModel model) {
		// 파일, 디렉토리 이름 변경 함수

		String rootPath = context.getRealPath("/root/");

		String oldPath = model.getOldPath();
		String newName = model.getNewName();

		File oldFile = new File(rootPath + oldPath);

		if (!oldFile.isDirectory()) {
			newName = newName + "." + FilenameUtils.getExtension(oldPath);
		}
		File newFile = new File(rootPath + newName);

		if (oldFile.renameTo(newFile)) {
			return "success";
		} else {
			return null;
		}

	}

	@Override
	public String delete(String filePath) {
		// 파일, 디렉토리 삭제
		
		String rootPath = context.getRealPath("/root/");

		File file = new File(rootPath + filePath);

		if (file.exists()) {
			if (file.isDirectory()) {
				try {
					FileUtils.deleteDirectory(file);
					return "success";
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				file.delete();
				return "success";
			}
		}

		return null;
	}

	@Override
	public ArrayList<String> getDirectory() {
		// SVG root 경로의 폴더구조를 리턴하는 함수
		// LinkInfo 생성시 사용되는 select option 값에 사용
		
		ArrayList<String> returnVal = new ArrayList<String>();
		String rootPath = context.getRealPath("/root/");

		buildDirectory(rootPath, rootPath, returnVal);

		return returnVal;
	}

	private void buildDirectory(String rootPath, String currentPath, ArrayList<String> list) {
		// 하위 경로까지의 탐색을 튀해 재귀용으로 분리한 함수

		File root = new File(currentPath);
		File[] files = root.listFiles();

		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					String fileName = file.getAbsolutePath().replace(rootPath, "");
					list.add(fileName);
					buildDirectory(rootPath, file.getAbsolutePath(), list);
				} else {
					continue;
				}

			}
		}

	}

}
