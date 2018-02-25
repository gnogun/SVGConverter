package com.al.svgconverter.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.al.svgconverter.dto.PathTreeModel;
import com.al.svgconverter.dto.Svg2CsvModel;
import com.al.svgconverter.dto.SvgExportModel;
import com.al.svgconverter.dto.SvgModel;
import com.al.svgconverter.dto.SvgZipModel;
import com.al.svgconverter.dto.TranslateSvgModel;

public interface SvgDao {
	public String uploadSvgZip(SvgZipModel zipModel);
	public String svg2Csv(Svg2CsvModel svgModel);
	public String csv2Svg(TranslateSvgModel svgModel);
	public String svgUpload(SvgModel svgModel);
	public String svgExport(SvgExportModel model);
	public String svgSeperate(SvgExportModel model);
}
