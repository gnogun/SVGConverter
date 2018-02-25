package com.al.svgconverter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.al.svgconverter.dao.XmlDao;
import com.al.svgconverter.dto.SvgLinkBuildModel;

@Service
public class XmlServiceImpl implements XmlService {

	@Autowired
	private XmlDao xmlDao;
	
	@Override
	public String svgLinkBuild(SvgLinkBuildModel model) {
		// TODO Auto-generated method stub
		return xmlDao.svgLinkBuild(model);
	}

	@Override
	public String getSelector() {
		// TODO Auto-generated method stub
		return xmlDao.getSelector();
	}

	@Override
	public String addSelector(String selector) {
		// TODO Auto-generated method stub
		return xmlDao.addSelector(selector);
	}

	@Override
	public String deleteSelector(String selector) {
		// TODO Auto-generated method stub
		return xmlDao.deleteSelector(selector);
	}
	
	
}
