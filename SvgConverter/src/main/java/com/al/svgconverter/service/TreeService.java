package com.al.svgconverter.service;

import java.util.ArrayList;

import com.al.svgconverter.dto.FileReNameModel;
import com.al.svgconverter.dto.PathTreeModel;

public interface TreeService {
	public ArrayList<PathTreeModel> getTree();
	public ArrayList<PathTreeModel> getDirectoryTree();
	
	public ArrayList<String> getDirectory();

	public String createNewFolder(String folderName);

	public String reName(FileReNameModel model);
	
	public String delete(String filePath);
}
