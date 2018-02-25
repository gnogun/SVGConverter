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

		String rootPath = context.getRealPath("/root/");

		return buildTree(rootPath);
	}

	@Override
	public ArrayList<PathTreeModel> getDirectoryTree() {
		String rootPath = context.getRealPath("/root/");

		return buildDirectoryTree(rootPath);
	}

	public ArrayList<PathTreeModel> buildTree(String path) {

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
		String rootPath = context.getRealPath("/root/");
		String oldPath = model.getOldPath();

		String newName = model.getNewName();

		File oldFile = new File(rootPath + oldPath);

		if (!oldFile.isDirectory()) {
			newName = newName + "." + FilenameUtils.getExtension(oldPath);
		}
		File newFile = new File(rootPath + newName);

		// Rename file (or directory)
		if (oldFile.renameTo(newFile)) {
			return "success";
		} else {
			return null;
		}

	}

	@Override
	public String delete(String filePath) {
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
		ArrayList<String> returnVal = new ArrayList<String>();
		String rootPath = context.getRealPath("/root/");
		
		buildDirectory(rootPath, rootPath, returnVal);

		return returnVal;
	}

	private void buildDirectory(String rootPath, String currentPath, ArrayList<String> list) {

		File root = new File(currentPath);
		File[] files = root.listFiles();

		if(files != null) {
			for (File file : files) {
				if(file.isDirectory()) {
					String fileName = file.getAbsolutePath().replace(rootPath, "");
					list.add(fileName);
					buildDirectory(rootPath, file.getAbsolutePath(), list);
				}else {
					continue;
				}
				
			}
		}
		
	}

}
