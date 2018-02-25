package com.al.svgconverter.service;

import com.al.svgconverter.dto.SvgLinkBuildModel;

public interface XmlService {
	public String svgLinkBuild(SvgLinkBuildModel model);
	
	public String getSelector();
	public String addSelector(String selector);
	public String deleteSelector(String selector);
	
}
