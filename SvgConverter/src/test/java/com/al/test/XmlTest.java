package com.al.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.al.svgconverter.dto.SvgLinkBuildModel;
import com.al.svgconverter.service.XmlService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml" })
@WebAppConfiguration
public class XmlTest {
//	private String sdSvgPath = "E:/gnogun/ETM 링크 참조/OS_18_일반/sd svg/svg";
//	private String ccSvgPath = "E:/gnogun/ETM 링크 참조/OS_18_일반/new cv/원본 svg";
//	private String clSvgPath = "E:/gnogun/ETM 링크 참조/OS_18_일반/new cl/원본 svg";
//	private String hlSvgPath = "E:/gnogun/ETM 링크 참조/OS_18_일반/new hl/원본 svg";
	
	private String sdSvgPath = "E:/data/gnogun/ETM 링크 참조/OS_18_일반/sd svg/svg";
	private String ccSvgPath = "E:/data/gnogun/ETM 링크 참조/OS_18_일반/new cv/원본 svg";
	private String clSvgPath = "E:/data/gnogun/ETM 링크 참조/OS_18_일반/new cl/원본 svg";
	private String hlSvgPath = "E:/data/gnogun/ETM 링크 참조/OS_18_일반/new hl/원본 svg";
	
	@Autowired
	private XmlService service;
	
	@Test
	public void buldLink(){
		
		/*SvgLinkBuildModel model = new SvgLinkBuildModel();
		
		model.setSdPath(sdSvgPath);
		model.setCcPath(ccSvgPath);
		model.setClPath(clSvgPath);
		model.setHlPath(hlSvgPath);
		
		service.svgLinkBuild(model);*/
	}
	
}