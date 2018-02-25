package com.al.test;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.al.svgconverter.service.TreeService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml" })
@WebAppConfiguration
public class TreeTest {

	@Autowired
	private TreeService service; 
	
	@Test
	public void getDirectory() {
		/*ArrayList<String> list = service.getDirectory();
		
		for(String fileName : list) {
			System.out.println(fileName);
		}*/
		
	}
}
