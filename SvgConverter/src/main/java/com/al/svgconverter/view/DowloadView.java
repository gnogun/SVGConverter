package com.al.svgconverter.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

public class DowloadView extends AbstractView {
	
	@Autowired
	private ServletContext context;

	public void Download() {
		setContentType("application/download; utf-8");
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String filePath = (String) context.getRealPath("/output/") + model.get("downloadFile");

		File file = new File(filePath);

		String fileName = file.getName();

		System.out.println("DownloadView --> file.getPath() : "
				+ file.getPath());
		System.out.println("DownloadView --> file.getName() : "
				+ file.getName());

		response.setContentType(getContentType());
		response.setContentLength((int) file.length());

		String header = request.getHeader("User-Agent");

		String encodedFilename = "";

		if (header.indexOf("MSIE") > -1 || header.indexOf("Trident") > -1) {
			encodedFilename = URLEncoder.encode(fileName, "UTF-8").replaceAll(
					"\\+", "%20");
		} else if (header.indexOf("Chrome") > -1) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < fileName.length(); i++) {
				char c = fileName.charAt(i);
				if (c > '~') {
					sb.append(URLEncoder.encode("" + c, "UTF-8"));
				} else {
					sb.append(c);
				}
			}
			encodedFilename = sb.toString();
		} else if (header.indexOf("Opera") > -1) {
			encodedFilename =

			"\"" + new String(fileName.getBytes("UTF-8"), "8859_1") + "\"";
		} else {
			// firefox
			encodedFilename = "\""
					+ new String(fileName.getBytes("UTF-8"), "8859_1") + "\"";
		}

		// boolean ie = userAgent.indexOf("MSIE") > -1;
		//
		// String fileName = null;
		//
		// if (ie) {
		// fileName = URLEncoder.encode(documentName, "utf-8");
		// } else {
		// fileName = new String(documentName.getBytes("utf-8"), "UTF-8");
		// }
		//
		// String encodingFileName = URLEncoder.encode(fileName,
		// "utf-8").replace(
		// "+", "%20"); // /

		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ encodedFilename + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");

		OutputStream out = response.getOutputStream();

		FileInputStream fis = null;

		try {

			fis = new FileInputStream(file);

			FileCopyUtils.copy(fis, out);

		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			if (fis != null) {

				try {
					fis.close();
				} catch (Exception e) {
				}
			}

		} // try end;
		out.flush();
	}

}
