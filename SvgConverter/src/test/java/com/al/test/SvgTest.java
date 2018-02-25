package com.al.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.al.svgconverter.dto.SvgExportModel;
import com.al.svgconverter.dto.SvgZipModel;
import com.al.svgconverter.dto.TranslateSvgModel;
import com.al.svgconverter.service.SvgService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml" })
@WebAppConfiguration
public class SvgTest {

	@Test
	public void svgExport() {
		
	}
	
}