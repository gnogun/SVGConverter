package com.al.svgconverter.dao;

import com.al.svgconverter.dto.SvgLinkBuildModel;

public interface XmlDao {
	public String svgLinkBuild(SvgLinkBuildModel model);
	
	public String getSelector();
	public String addSelector(String selector);
	public String deleteSelector(String selector);
}
