package com.al.svgconverter.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public class XmlUtil {
	public static boolean xmlWrite(Document newDoc, File makeXmlFile) {
		boolean bSuccessFalse = false;

		TransformerFactory tr_factory = TransformerFactory.newInstance();
		try {
			Properties output = new Properties();
			output.setProperty(OutputKeys.INDENT, "yes");
			output.setProperty(OutputKeys.ENCODING, "UTF-8");
			Transformer transformer = tr_factory.newTransformer();
			transformer.setOutputProperties(output);
			transformer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.transform(new DOMSource(newDoc), new StreamResult(
					new FileOutputStream(makeXmlFile)));
			bSuccessFalse = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bSuccessFalse;
	}
}
