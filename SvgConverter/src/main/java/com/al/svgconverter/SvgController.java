package com.al.svgconverter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.al.svgconverter.dao.TreeDao;
import com.al.svgconverter.dto.FileReNameModel;
import com.al.svgconverter.dto.PathTreeModel;
import com.al.svgconverter.dto.Svg2CsvModel;
import com.al.svgconverter.dto.SvgExportModel;
import com.al.svgconverter.dto.SvgLinkBuildModel;
import com.al.svgconverter.dto.SvgModel;
import com.al.svgconverter.dto.SvgZipModel;
import com.al.svgconverter.dto.TranslateSvgModel;
import com.al.svgconverter.service.SvgService;
import com.al.svgconverter.service.TreeService;
import com.al.svgconverter.service.XmlService;

@Controller
public class SvgController {

	@Autowired
	private SvgService svgService;

	@Autowired
	private TreeService treeService;

	@Autowired
	private XmlService xmlService;

	// 메인화면 뷰 리턴 컨트롤러
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView homeFull(ModelAndView mav) {
		mav.setViewName("homeFull");
		return mav;
	}

	// SVG 파일, 디렉토리 구조 리턴 컨트롤러
	// 메인화면 좌측 fancytree load src
	@RequestMapping(value = "/tree", method = RequestMethod.GET)
	public @ResponseBody List<PathTreeModel> getPathTree() {
		ArrayList<PathTreeModel> list = new ArrayList<PathTreeModel>();

		PathTreeModel model = new PathTreeModel();
		model.setTitle("root");
		model.setFolder(true);

		ArrayList<PathTreeModel> children = treeService.getTree();

		if (children != null && children.size() == 0) {
			children = null;
		}

		model.setChildren(children);

		list.add(model);

		return list;
	}

	// SVG 디렉토리 구조 리턴 컨트롤러
	// 사용하지 않는 컨트롤러
	@RequestMapping(value = "/tree/dir", method = RequestMethod.GET)
	public @ResponseBody List<PathTreeModel> getDirectoryPathTree() {
		ArrayList<PathTreeModel> list = new ArrayList<PathTreeModel>();

		PathTreeModel model = new PathTreeModel();
		model.setTitle("root");
		model.setFolder(true);

		ArrayList<PathTreeModel> children = treeService.getDirectoryTree();

		if (children != null && children.size() == 0) {
			children = null;
		}

		model.setChildren(children);

		list.add(model);

		return list;
	}

	// SVG 경로 폴더 구조 리턴 컨트롤러
	// LinkInfo 생성시 폴더 구조 표현에 사용
	@RequestMapping(value = "/dir", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<String> getDirectory() {

		return treeService.getDirectory();
	}

	// SVG 압축 파일 업로드 컨트롤러
	@RequestMapping(value = "/upload/svgZip", method = RequestMethod.POST)
	public @ResponseBody String uploadSvgZip(SvgZipModel model) {
		svgService.uploadSvgZip(model);
		return "";
	}

	// SVG 폴더 구조 새 폴더 생성 컨트롤러
	@RequestMapping(value = "/create/folder", method = RequestMethod.POST)
	public @ResponseBody String createNewFolder(@RequestBody String folderName) {

		return treeService.createNewFolder(folderName);
	}

	// SVG 폴더 구조 이름 변경 컨트롤러
	@RequestMapping(value = "/rename", method = RequestMethod.POST)
	public @ResponseBody String reNameFile(@RequestBody FileReNameModel reNameModel) {

		return treeService.reName(reNameModel);
	}

	// SVG 폴더 구조 삭제 컨트롤러
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody String deleteFile(@RequestBody String folderName) {

		return treeService.delete(folderName);
	}

	// 텍스트 추출 CSV 생성 컨트롤러
	@RequestMapping(value = "/create/csv", method = RequestMethod.POST)
	public @ResponseBody String createCsvMulti(@RequestBody Svg2CsvModel svgModel) {

		return svgService.svg2Csv(svgModel);
	}

	// SVG 변환 컨트롤러
	@RequestMapping(value = "/export", method = RequestMethod.POST)
	public @ResponseBody String exportImage(@RequestBody SvgExportModel svgExportModel) {

		return svgService.svgExport(svgExportModel);
	}

	// SVG 분리 컨트롤러
	// 사용하지 않는 컨트롤러
	@RequestMapping(value = "/seperate", method = RequestMethod.POST)
	public @ResponseBody String seperateImage(@RequestBody SvgExportModel svgExportModel) {

		return svgService.svgSeperate(svgExportModel);
	}

	// 번역 SVG 생성 컨트롤러
	@RequestMapping(value = "/translate/svg", method = RequestMethod.POST)
	public @ResponseBody String createTranslatedSvg(TranslateSvgModel svgModel) {

		return svgService.csv2Svg(svgModel);
	}

	// LinkInfo.xml 생성 컨트롤러
	@RequestMapping(value = "/create/link", method = RequestMethod.POST)
	public @ResponseBody String createLink(@RequestBody SvgLinkBuildModel linkModel) {

		return xmlService.svgLinkBuild(linkModel);
	}

	// SVG 개별 업로드 컨트롤러
	@RequestMapping(value = "/upload/svg", method = RequestMethod.POST)
	public @ResponseBody String uploadSvg(SvgModel model) {
		return svgService.svgUpload(model);
	}
	
	// 다운로드 처리 컨트롤러
	@RequestMapping(value = "/download/{documentId}/", method = RequestMethod.GET)
	public ModelAndView download(@PathVariable String documentId) {

		ModelAndView mav = new ModelAndView();
		mav.setViewName("download");
		mav.addObject("downloadFile", documentId);

		return mav;
	}

	// LinkInfo 구분 selector 로드 컨트롤러
	@RequestMapping(value = "/selector", method = RequestMethod.GET)
	public @ResponseBody String getSelector() {
		return xmlService.getSelector();
	}

	// LinkInfo 구분 selector 추가 컨트롤러
	@RequestMapping(value = "/selector/add", method = RequestMethod.POST)
	public @ResponseBody String addSelector(@RequestBody String selector) {
		return xmlService.addSelector(selector);
	}

	// LinkInfo 구분 selector 삭제 컨트롤러
	@RequestMapping(value = "/selector/delete", method = RequestMethod.POST)
	public @ResponseBody String deleteSelector(@RequestBody String selector) {
		return xmlService.deleteSelector(selector);
	}
}
