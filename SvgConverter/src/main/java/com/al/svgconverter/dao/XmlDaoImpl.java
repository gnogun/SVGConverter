package com.al.svgconverter.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.al.svgconverter.dto.LinkInfo;
import com.al.svgconverter.dto.LinkList;
import com.al.svgconverter.dto.LinkPool;
import com.al.svgconverter.dto.SvgLinkBuildModel;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

@Repository
public class XmlDaoImpl implements XmlDao {

	@Autowired
	private ServletContext context;

	@Override
	public String svgLinkBuild(SvgLinkBuildModel model) {
		String rootPath = context.getRealPath("/root/");

		File outputPath = new File(context.getRealPath("/output/"));

		File sdPath = new File(rootPath + model.getSdPath());
		File ccPath = new File(rootPath + model.getCcPath());
		File clPath = new File(rootPath + model.getClPath());
		File hlPath = new File(rootPath + model.getHlPath());

		File[] files = sdPath.listFiles();

		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

		LinkList linkList = new LinkList();
		Date dt = new Date();
		// "yyyy-MM-dd a hh:mm"
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		linkList.setLdup(sdf.format(dt).toString());
		linkList.setPrjname("");

		LinkPool linkPool = new LinkPool();

		String linkInfoName = outputPath + "/linkinfo.xml";

		try {
			builderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			DocumentBuilder builder = builderFactory.newDocumentBuilder();

			for (File file : files) {
				if (FilenameUtils.getExtension(file.getName()).equals("svg")) {

					Document document = builder.parse(new FileInputStream(file));

					XPathFactory xPathfactory = XPathFactory.newInstance();
					XPath xpath = xPathfactory.newXPath();
					XPathExpression expr = xpath.compile("//tspan[@font-family=\"'Helvetica-Bold'\"]");
					NodeList list = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
					for (int i = 0; i < list.getLength(); i++) {
						Node node = list.item(i);
						LinkInfo linkInfo = new LinkInfo();
						UUID uuid = UUID.randomUUID();
						String uuidString = uuid.toString();
						uuidString = uuidString.replaceAll("-", "");
						
						String linkName = node.getTextContent();
						linkName = linkName.trim();

						linkInfo.setC_NAME(linkName);
						
						linkInfo.setGuid(uuidString);

						linkPool.getLinkInfo().add(linkInfo);
					}

				}
			}
			
			
			linkPool.setLinkInfo(checkDuplicate(linkPool.getLinkInfo()));
			
			checkLink("cc", ccPath, linkPool);
			checkLink("cl", clPath, linkPool);
			checkLink("hl", hlPath, linkPool);
			
			
			linkPool.setLinkInfo(checkBlank(linkPool.getLinkInfo()));

			linkList.setLinkPool(linkPool);

			JacksonXmlModule module = new JacksonXmlModule();
			XmlMapper xmlMapper = new XmlMapper(module);
			xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);

			xmlMapper.writeValue(new File(linkInfoName), linkList);

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return "/linkinfo.xml";
	}

	public void checkLink(String targetPath, File path, LinkPool linkPool) {

		File[] files = path.listFiles();

		List<LinkInfo> linkInfoList = linkPool.getLinkInfo();

		try {
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			DocumentBuilder builder = builderFactory.newDocumentBuilder();


			for (int j = 0; j < files.length; j++) {


				File file = files[j];
				if (FilenameUtils.getExtension(file.getName()).equals("svg")) {
					System.out.println("File ---- " + targetPath + "/" + file.getName());
					
					String fileName = FilenameUtils.removeExtension(file.getName());

					Document document = builder.parse(new FileInputStream(file));
					XPathFactory xPathfactory = XPathFactory.newInstance();
					XPath xpath = xPathfactory.newXPath();
					XPathExpression expr = xpath.compile("//tspan[@font-family=\"'Helvetica-Bold'\"]");
					NodeList list = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

					for (LinkInfo linkInfo : linkInfoList) {

						

						for (int i = 0; i < list.getLength(); i++) {
							Node node = list.item(i);

							String linkName = node.getTextContent();
							linkName = linkName.trim();
							
							if (linkName.equals(linkInfo.getC_NAME())) {
								if (targetPath.equals("cc")) {
									if(!linkInfo.getCC().equals("")) {
										linkInfo.setCC(linkInfo.getCC() + "^" + fileName);
									}else {
										linkInfo.setCC(fileName);
									}
								} else if (targetPath.equals("cl")) {
									if(!linkInfo.getCL().equals("")) {
										linkInfo.setCL(linkInfo.getCL() + "^" + fileName);
									}else {
										linkInfo.setCL(fileName);
									}
								} else {
									if(!linkInfo.getHL().equals("")) {
										linkInfo.setHL(linkInfo.getHL() + "^" + fileName);
									}else {
										linkInfo.setHL(fileName);
									}
								}
							}

						}


					}
				}

			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

	}
	
	public List<LinkInfo> checkDuplicate(List<LinkInfo> linkInfoList) {
		HashMap<String, LinkInfo> linkInfoMap = new HashMap<String, LinkInfo>();
		
		for(LinkInfo linkInfo : linkInfoList) {
			linkInfoMap.put(linkInfo.getC_NAME(), linkInfo);
			
			
			
		}
		
		
		List<LinkInfo> returnList = new ArrayList<LinkInfo>();
		
		Iterator<String> iter = linkInfoMap.keySet().iterator();
		
		while(iter.hasNext()) {
			String key = iter.next();
			
			
			returnList.add(linkInfoMap.get(key));
			
		}
		
		return returnList;
	}
	
	public List<LinkInfo> checkBlank(List<LinkInfo> linkInfoList) {
		
		List<LinkInfo> returnList = new ArrayList<LinkInfo>();
		
		for(LinkInfo linkInfo : linkInfoList) {
			String cc = linkInfo.getCC();
			String cl = linkInfo.getCL();
			String hl = linkInfo.getHL();
			
			if(!cc.equals("") || !cl.equals("") || !hl.equals("")) {
				returnList.add(linkInfo);
			}
		}
		
		return returnList;
		
	}
	
	@Override
	public String getSelector() {

		File selectorFile = new File(context.getRealPath("selector.txt"));

		String returnVal = "";

		BufferedReader br;
		
		try {
			br = new BufferedReader(new FileReader(selectorFile));
			StringBuilder sb = new StringBuilder();
			
			String line = "";
			
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			returnVal = sb.toString();
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return returnVal;
	}

	@Override
	public String addSelector(String selector) {
		
		File selectorFile = new File(context.getRealPath("selector.txt"));

		String returnVal = "";

		BufferedReader br;
		FileWriter fw;
		
		try {
			br = new BufferedReader(new FileReader(selectorFile));
			StringBuilder sb = new StringBuilder();
			
			String line = br.readLine();
			
			while (line != null) {
				sb.append(line);
			}
			returnVal = sb.toString();
			
			String[] selectorSplit = returnVal.split(" ");
			
			for(String selectorString : selectorSplit){
				if(!selectorString.equals(selector)){
					returnVal = returnVal + " " + selector;
				}
			}
			
			fw = new FileWriter(selectorFile);
			fw.write(returnVal);
			fw.flush();
			fw.close();
			
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		
		
		return returnVal;
	}

	@Override
	public String deleteSelector(String selector) {
		File selectorFile = new File(context.getRealPath("selector.txt"));

		String returnVal = "";
		String modifyString = "";

		BufferedReader br;
		FileWriter fw;
		
		try {
			br = new BufferedReader(new FileReader(selectorFile));
			StringBuilder sb = new StringBuilder();
			
			String line = br.readLine();
			
			while (line != null) {
				sb.append(line);
			}
			returnVal = sb.toString();
			
			String[] selectorSplit = returnVal.split(" ");
			
			
			
			for(String selectorString : selectorSplit){
				if(!selectorString.equals(selector)){
					modifyString += selectorString;
				}
			}
			
			fw = new FileWriter(selectorFile);
			fw.write(modifyString);
			fw.flush();
			fw.close();
			
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		
		
		return modifyString;
	}

}


