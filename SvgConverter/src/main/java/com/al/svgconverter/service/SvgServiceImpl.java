package com.al.svgconverter.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.al.svgconverter.dao.SvgDao;
import com.al.svgconverter.dto.Svg2CsvModel;
import com.al.svgconverter.dto.SvgExportModel;
import com.al.svgconverter.dto.SvgModel;
import com.al.svgconverter.dto.SvgZipModel;
import com.al.svgconverter.dto.TranslateSvgModel;

@Service
public class SvgServiceImpl implements SvgService {
	
	@Autowired
	private SvgDao svgDao;
	
	@Override
	public String uploadSvgZip(SvgZipModel zipModel) {
		// TODO Auto-generated method stub
		return svgDao.uploadSvgZip(zipModel);
	}
	
	@Override
	public String svg2Csv(Svg2CsvModel svgModel) {
		// TODO Auto-generated method stub
		return svgDao.svg2Csv(svgModel);

	}

	@Override
	public String csv2Svg(TranslateSvgModel svgModel) {
		// TODO Auto-generated method stub
		return svgDao.csv2Svg(svgModel);

	}
	
	@Override
	public String svgExport(SvgExportModel model) {
		// TODO Auto-generated method stub
		return svgDao.svgExport(model);
	}
	
	@Override
	public String svgUpload(SvgModel svgModel) {
		// TODO Auto-generated method stub
		return svgDao.svgUpload(svgModel);
	}
	
	@Override
	public String svgSeperate(SvgExportModel model) {
		// TODO Auto-generated method stub
		return svgDao.svgSeperate(model);
	}

}
