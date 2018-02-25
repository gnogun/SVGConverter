package com.al.svgconverter.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.al.svgconverter.dao.TreeDao;
import com.al.svgconverter.dto.FileReNameModel;
import com.al.svgconverter.dto.PathTreeModel;

@Service
public class TreeServiceImpl implements TreeService {
	
	@Autowired
	private TreeDao treeDao;

	@Override
	public ArrayList<PathTreeModel> getTree() {
		// TODO Auto-generated method stub
		return treeDao.getTree();
	}
	
	@Override
	public ArrayList<PathTreeModel> getDirectoryTree() {
		// TODO Auto-generated method stub
		return treeDao.getDirectoryTree();
	}
	
	@Override
	public String createNewFolder(String folderName) {
		// TODO Auto-generated method stub
		return treeDao.createNewFolder(folderName);
	}

	
	@Override
	public String reName(FileReNameModel model) {
		// TODO Auto-generated method stub
		return treeDao.reName(model);
	}
	
	@Override
	public String delete(String filePath) {
		// TODO Auto-generated method stub
		return treeDao.delete(filePath);
	}

	@Override
	public ArrayList<String> getDirectory() {
		// TODO Auto-generated method stub
		return treeDao.getDirectory();
	}
}
