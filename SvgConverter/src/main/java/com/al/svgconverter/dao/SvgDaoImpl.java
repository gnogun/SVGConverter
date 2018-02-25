package com.al.svgconverter.dao;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.fop.render.ps.EPSTranscoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.al.svgconverter.dto.CsvModel;
import com.al.svgconverter.dto.Svg2CsvModel;
import com.al.svgconverter.dto.SvgExportModel;
import com.al.svgconverter.dto.SvgModel;
import com.al.svgconverter.dto.SvgZipModel;
import com.al.svgconverter.dto.TranslateSvgModel;
import com.al.svgconverter.util.XmlUtil;

@Repository
public class SvgDaoImpl implements SvgDao {
	/**
	 * SVG 관련 기능을 처리하는 DAO 클래스
	 * 
	 */

	private static final Logger logger = LoggerFactory.getLogger(SvgDaoImpl.class);
	
	@Autowired
	private ServletContext context;

		
	@Override
	public String uploadSvgZip(SvgZipModel zipModel) {
		// 업로드한 SVG zip 파일을 처리하는 함수
		
		MultipartFile zipFile = zipModel.getSvgZipInput();

		String encoding = Charset.defaultCharset().toString();

		// Tomcat 설치경로/webapps/svgconverter/root 경로
		String filePath = context.getRealPath("/root/");

		String currentPath = zipModel.getCurrentPath();

		String zipFileName = "";

		if (zipModel.isRootNameFlag()) {
			zipFileName = FilenameUtils.removeExtension(zipFile.getOriginalFilename());
		}

		// 압축을 해제 할 전체 경로
		String fullPath = filePath + currentPath + "/" + zipFileName + "/";

		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];

		ArchiveEntry zipEntry;
		File tempFile;

		try {
			File file = new File(fullPath);

			if (!file.exists()) {
				file.mkdirs();
			}

			ZipArchiveInputStream zis = new ZipArchiveInputStream(zipFile.getInputStream(), encoding);

			while ((zipEntry = zis.getNextEntry()) != null) {

				File entryFile = new File(fullPath + zipEntry.getName());

				if (zipEntry.isDirectory()) {
					entryFile.mkdirs();
				} else {

					// 압축파일 안에 svg entry만 처리한다
					if (FilenameUtils.getExtension(entryFile.getName()).equals("svg")) {

						tempFile = File.createTempFile(zipEntry.getName(), "tmp");
						tempFile.deleteOnExit();

						FileOutputStream fos = new FileOutputStream(entryFile);
						BufferedOutputStream bos = new BufferedOutputStream(fos, bufferSize);
						int count;
						while ((count = zis.read(buffer, 0, bufferSize)) != -1) {
							bos.write(buffer, 0, count);
						}
						bos.flush();
						bos.close();
					}

				}

			}
			zis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 성공, 실패 결과 값을 보낸다 추후 조정
		return null;
	}

	@Override
	public String svg2Csv(Svg2CsvModel svgModel) {
		// SVG 파일들에서 텍스트를 추출한 후 CSV 파일을 만드는 함수

		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		// 선택한 SVG 파일 리스트
		List<String> svgFiles = svgModel.getSvgFiles();

		StringBuffer sb = new StringBuffer();

		File outputPath = new File(context.getRealPath("/output/"));

		if (!outputPath.exists()) {
			outputPath.mkdirs();
		}

		// CSV 헤더 기본 구조 path, id, source, target
		sb.append("path, ").append("id, ").append("source, ").append("target, ").append("\n");

		for (String svgFileName : svgFiles) {

			// 앞에 붙은 / 제거
			svgFileName = svgFileName.substring(1);

			
			File svgFile = new File(context.getRealPath("/root/") + svgFileName);

			if (svgFile.isDirectory()) {
				continue;
			}

			Document document;

			try {

				DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
				builderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
				DocumentBuilder builder = builderFactory.newDocumentBuilder();
				document = builder.parse(new FileInputStream(svgFile));

				Element element = document.getDocumentElement();
				
				// SVG xml 구조 상에서 tspan 엘리먼트만 가져온다
				NodeList list = element.getElementsByTagName("tspan");

				for (int i = 0; i < list.getLength(); i++) {
					Node node = list.item(i);

					String textVal = node.getTextContent();

					if (textVal != null && textVal != "") {

						// path | id | source | target
						// SVG 파일 경로 | tsapn 엘리먼트 인덱스 |  tspan.textcontent | 공백
						sb.append(svgFileName).append(", ").append(i + 1).append(", ").append(textVal).append(", ")
								.append(" ").append("\n");

						// tspan 엘리먼트 텍스트 노드에 인덱스 값을 넣어준다
						node.setTextContent(String.valueOf(i + 1));
					}

				}

				String svgName = FilenameUtils.removeExtension(svgFile.getName());
				String svgSkeletonName = svgFile.getParent() + "/" + svgName + ".skt";
				
				// 텍스트노드 값이 인덱스로 변경 된 Document 는 .skt 확장자 스켈레톤 파일로 저장한다
				XmlUtil.xmlWrite(document, new File(svgSkeletonName));

			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		String csvName = outputPath + "/" + sdf.format(dt).toString() + "_CSV.csv";
		
		// StringBuilder 값을 csv 파일로 저장한다
		csvWrite(sb.toString(), new File(csvName));
		
		// 저장한 csv파일의 경로를 리턴 해준다
		return sdf.format(dt).toString() + "_CSV.csv";

	}

	@Override
	public String csv2Svg(TranslateSvgModel svgModel) {
		// 번역 된 CSV 파일을 이용해서 번역 SVG 생성를 생성하는 함수

		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		try {

			MultipartFile csvFile = svgModel.getTranslateFile();
			
			// 업로드 된 csv 파일을 저장후 파일 이름을 사용
			String csvName = csvUpload(csvFile);

			// 번역 된 SVG zip 파일을 생성할 경로			
			String outZipNm = context.getRealPath("/output/") + sdf.format(dt).toString() + "_translated.zip";

			// csv파일의 path(라인당 첫번쩨 값)을 키로 Map 생성 
			HashMap<String, List<CsvModel>> csvMap = csvToMap(csvName);
			
			Iterator<String> iter = csvMap.keySet().iterator();

			ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(outZipNm)));

			while (iter.hasNext()) {
				String key = iter.next();

				String path = key;
				
				// svg 파일 이름의 확장자를 변경하여 스켈레톤 파일 불러오기
				String sktPath = path.replace(".svg", ".skt");
				String svgFile = context.getRealPath("/root/") + sktPath;

				ArrayList<CsvModel> list = (ArrayList<CsvModel>) csvMap.get(key);

				HashMap<String, String> textMap = new HashMap<String, String>();

				for (CsvModel model : list) {
					// csv 파일 내용을 토대로 번역용 map 객체를 만들어 준다
					String id = model.getId();
					String source = model.getSource();
					String target = model.getTarget();

					if (target.trim().equals("")) {
						target = source;
					}

					textMap.put(id, target);
				}

				// 번역 된 SVG파일을 저장하면서 zip 엔트리로 바로 추가한다
				zip(sktSvgWrite(textMap, svgFile), zos, path);

			}
			zos.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 저장한 번역 SVG zip 파일 경로를 리턴한다
		return sdf.format(dt).toString() + "_translated.zip";

	}

	private String csvUpload(MultipartFile multipartFile) {
		// 업로드 된 csv 파일을 저장하는 함수 
		
		// Tomcat/webapps/svgconverter/upload 경로
		String filePath = context.getRealPath("/upload/");

		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		String fileName = multipartFile.getOriginalFilename();
		
		String fullFilePath = filePath + fileName; //

		try {
			File uploadPath = new File(filePath);
			if (!uploadPath.exists()) {
				uploadPath.mkdirs();
			}

			bos = new BufferedOutputStream(new FileOutputStream(fullFilePath));
			bis = new BufferedInputStream(multipartFile.getInputStream());

			int read = 0;

			while ((read = bis.read()) != -1) {
				bos.write(read);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {

				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {

				}
			}
		}
		return fullFilePath;
	}

	@Override
	public String svgExport(SvgExportModel model) {
				
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dt = new Date();
		File[] oldFiles = new File(context.getRealPath("/output/")).listFiles();

		for (File oldFile : oldFiles) {
			try {
				if (oldFile.isDirectory()) {
					FileUtils.deleteDirectory(oldFile);
				} else {
					oldFile.delete();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		List<String> files = model.getSvgFiles();

		String outZipNm = context.getRealPath("/output/") + sdf.format(dt).toString() + "_export.zip";

		ZipOutputStream zos;
		try {
			zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(outZipNm)));
			for (String fileName : files) {

				boolean originFlag = true;
				boolean originSepFlag = true;

				String svgFileName = context.getRealPath("/root/") + fileName;
				if (new File(svgFileName).isDirectory()) {
					continue;
				}

				if (model.isEpsFlag()) {
					zip(exportEps(svgFileName), zos, fileName.replace(".svg", ".eps"));

					if (originFlag) {
						zip(new File(svgFileName), zos, fileName);
					}
					originFlag = false;

				}

				if (model.isGifFlag()) {
					zip(exportGif(svgFileName), zos, fileName.replace(".svg", ".gif"));

					if (originFlag) {
						zip(new File(svgFileName), zos, fileName);
					}
					originFlag = false;
				}

				if (model.isSepEpsFlag()) {
					File sepFile = svgSeparator(svgFileName);

					for (File file : sepFile.listFiles()) {
						if (file.getName().endsWith(".svg") && file.getName()
								.startsWith(fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length() - 4))) {
							String entryName = fileName.substring(0, fileName.lastIndexOf("/") + 1)
									+ file.getName().replace(".svg", ".eps");
							zip(exportEps(file.getAbsolutePath()), zos, entryName);
							
							if (originSepFlag) {
								
								zip(file, zos, fileName.substring(0, fileName.lastIndexOf("/") + 1) + file.getName());
							}
						}

					}
					originSepFlag = false;

				}

				if (model.isSepGifFlag()) {
					File sepFile = svgSeparator(svgFileName);

					for (File file : sepFile.listFiles()) {
						if (file.getName().endsWith(".svg") && file.getName()
								.startsWith(fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length() - 4))) {
							String entryName = fileName.substring(0, fileName.lastIndexOf("/") + 1)
									+ file.getName().replace(".svg", ".gif");
							zip(exportGif(file.getAbsolutePath()), zos, entryName);
							if (originSepFlag) {
								
								zip(file, zos, fileName.substring(0, fileName.lastIndexOf("/") + 1) + file.getName());
							}
						}
					}
					originSepFlag = false;

				}

			}
			zos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sdf.format(dt).toString() + "_export.zip";
	}

	private void zip(File file, ZipOutputStream zos, String originName) {
		// zip entry 추가 함수
		try {
			int size = 1024;
			byte[] buf = new byte[size];

			if (file != null) {

				FileInputStream fis = new FileInputStream(file);
				BufferedInputStream bis = new BufferedInputStream(fis, size);

				zos.putNextEntry(new ZipEntry(originName));

				final int COMPRESSION_LEVEL = 8;
				zos.setLevel(COMPRESSION_LEVEL);

				int len;
				while ((len = bis.read(buf, 0, size)) != -1) {
					zos.write(buf, 0, len);
				}

				zos.closeEntry();
				bis.close();
				fis.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public File exportEps(String svgFileName) {
		String outputPath = "";
		try {
			String svg_URI_input = Paths.get(svgFileName).toUri().toURL().toString();
			TranscoderInput input_svg_image = new TranscoderInput(svg_URI_input);

			String svgName = FilenameUtils.removeExtension(new File(svgFileName).getName());

			outputPath = context.getRealPath("/output/") + svgName + ".eps";
			OutputStream ostream = new FileOutputStream(outputPath);
			TranscoderOutput output = new TranscoderOutput(ostream);
			EPSTranscoder epsTranscoder = new EPSTranscoder();

			epsTranscoder.transcode(input_svg_image, output);

			ostream.flush();
			ostream.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (TranscoderException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new File(outputPath);
	}

	public File exportGif(String svgFileName) {
		String outputPath = "";
		try {
			String svg_URI_input = Paths.get(svgFileName).toUri().toURL().toString();
			TranscoderInput input_svg_image = new TranscoderInput(svg_URI_input);

			String svgName = FilenameUtils.removeExtension(new File(svgFileName).getName());

			outputPath = context.getRealPath("/output/") + svgName + ".gif";
			OutputStream ostream = new FileOutputStream(outputPath);
			TranscoderOutput output = new TranscoderOutput(ostream);
			PNGTranscoder transcoder = new PNGTranscoder();

			transcoder.transcode(input_svg_image, output);

			ostream.flush();
			ostream.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (TranscoderException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new File(outputPath);
	}

	public File svgSeparator(String svgFileName) {
		int suffixNum = 1;

		File outputPath = new File(context.getRealPath("/output/"));

		File svgFile = new File(svgFileName);

		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();


		try {
			builderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document document = builder.parse(new FileInputStream(svgFile));

			Element element = document.getDocumentElement();
			NodeList list = element.getChildNodes();

			Document tempDoc = builder.newDocument();

			Element tempElem = (Element) element.cloneNode(true);
			tempDoc.adoptNode(tempElem);
			tempDoc.appendChild(tempElem);

			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				if (node.getNodeType() == 1) {

					removeChilds(tempElem);

					Node clonedNode = node.cloneNode(true);
					tempDoc.adoptNode(clonedNode);
					tempElem.appendChild(clonedNode);

					String outputFileName = FilenameUtils.removeExtension(svgFile.getName());

					outputFileName = outputPath + "/" + outputFileName + "_" + suffixNum + ".svg";
					suffixNum++;

					XmlUtil.xmlWrite(tempDoc, new File(outputFileName));
					// System.out.println(elem.getAttribute("id") +
					// " done");


				}

			}

			System.out.println("job done");
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return outputPath;
	}

	public boolean csvWrite(String csvContent, File outputFile) {
		boolean flag = false;

		try {
			PrintWriter pw = new PrintWriter(outputFile);
			pw.write(csvContent);
			pw.close();
			flag = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return flag;
	}

	public HashMap<String, List<CsvModel>> csvToMap(String csvFileName) {
		HashMap<String, List<CsvModel>> returnMap = new HashMap<String, List<CsvModel>>();
		try {

			FileInputStream fis = new FileInputStream(new File(csvFileName));

			// BufferedReader br = new BufferedReader(new FileReader(new
			// File(csvFileName)));
			BufferedReader br = new BufferedReader(new InputStreamReader(fis, "euc-kr"));

			String line = "";
			while ((line = br.readLine()) != null) {
				String[] csvContent = line.split(",");
				String svgName = csvContent[0];
				if (svgName.equals("path")) {
					continue;
				}

				ArrayList<CsvModel> list = null;

				if (returnMap.containsKey(svgName)) {
					list = (ArrayList<CsvModel>) returnMap.get(svgName);
				} else {
					list = new ArrayList<CsvModel>();
				}

				CsvModel model = new CsvModel();
				model.setId(csvContent[1]);
				model.setSource(csvContent[2]);
				model.setTarget(csvContent[3]);

				list.add(model);

				returnMap.put(svgName, list);

			}

			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return returnMap;
	}

	public File sktSvgWrite(HashMap<String, String> csvMap, String svgFileName) {
		// 스켈레톤 파일로 번역 된 SVG 파일을 생성하는 함수
		
		Document document;

		try {
			File svgFile = new File(svgFileName);

			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			document = builder.parse(new FileInputStream(svgFile));

			Element element = document.getDocumentElement();
			NodeList list = element.getElementsByTagName("tspan");

			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);

				String textVal = node.getTextContent();

				if (textVal != null && textVal != "") {

					String val = csvMap.get(textVal);
					if (val != null && val != " ") {
						node.setTextContent(val);
					}

				}
			}

			XmlUtil.xmlWrite(document, new File(svgFileName));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new File(svgFileName);
	}

	public void removeChilds(Node node) {
		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node childNode = list.item(i);
			if (childNode.getNodeType() == 1)
				node.removeChild(childNode);
		}
	}

	@Override
	public String svgUpload(SvgModel svgModel) {

		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;

		String filePath = context.getRealPath("/root/") + svgModel.getCurrentPath();
		List<MultipartFile> svgFiles = svgModel.getSvgFiles();

		try {
			for (MultipartFile svgFile : svgFiles) {
				String fullPath = filePath + "/" + svgFile.getOriginalFilename();
				bos = new BufferedOutputStream(new FileOutputStream(fullPath));
				bis = new BufferedInputStream(svgFile.getInputStream());
				int read = 0;

				while ((read = bis.read()) != -1) {
					bos.write(read);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {

				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {

				}
			}
		}

		return null;
	}

	@Override
	public String svgSeperate(SvgExportModel model) {

		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		File[] oldFiles = new File(context.getRealPath("/output/")).listFiles();

		for (File oldFile : oldFiles) {
			try {
				if (oldFile.isDirectory()) {
					FileUtils.deleteDirectory(oldFile);
				} else {
					oldFile.delete();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		List<String> files = model.getSvgFiles();

		String outZipNm = context.getRealPath("/output/") + sdf.format(dt).toString() + "_export.zip";

		ZipOutputStream zos;
		try {
			zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(outZipNm)));
			for (String fileName : files) {

				String svgFileName = context.getRealPath("/root/") + fileName;
				if (new File(svgFileName).isDirectory()) {
					continue;
				}

				File sepFile = svgSeparator(svgFileName);

				for (File file : sepFile.listFiles()) {
					if (file.getName().endsWith(".svg") && file.getName()
							.startsWith(fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length() - 4))) {
						String entryName = fileName.substring(0, fileName.lastIndexOf("/") + 1) + file.getName();

						zip(file, zos, entryName);
					}
				}

			}
			zos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sdf.format(dt).toString() + "_export.zip";
	}

}
