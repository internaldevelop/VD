package com.wnt.web.testresult.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.wnt.core.uitl.SystemPath;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import jodd.util.StringUtil;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

public class WriterPdfs {
	/* 使用中文字体 */
	private static BaseFont bfChinese;
	// 标题部分文字
	private static Font firstTitle = new Font(bfChinese, 16, Font.BOLD);
	// 小标题字体
	private static Font secondTitle = new Font(bfChinese, 14, Font.BOLD);
	// 再小一号字体
	private static Font thirdFont = new Font(bfChinese, 12, Font.NORMAL);
	// 币种和租金金额的小一号字体
	private static Font smallFont = new Font(bfChinese, 8, Font.NORMAL);
	// 表格的头
	private static Font theadFont = new Font(bfChinese, 12, Font.BOLD);

	/**
	 * 
	 * @Title: initChinese
	 * @Description: 设置中文字体样式,中文采用宋体
	 * @param fontSize
	 * @param style
	 * @return
	 * @return: Font
	 */
	private static Font initChineseFont(float fontSize, int style) {
		try {
			String fontPath = SystemPath.getSysPath() + "font/simsun.ttc,1";
			bfChinese = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Font(bfChinese, fontSize, style);
	}

	/**
	 * 
	 * @Title: initChineseFont
	 * @Description: 设置中文字体样式,中文采用宋体
	 * @param font
	 * @return
	 * @return: Font
	 */
	private static Font initChineseFont(Font font) {
		try {
			String sysPath = SystemPath.getSysPath();
			if (StringUtil.isNotBlank(sysPath) && sysPath.contains("//")) {
				sysPath = "/" + sysPath.replace("//", "/");
			}
			String fontPath = sysPath + "font/simsun.ttc,1";
			bfChinese = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Font(bfChinese, font.getSize(), font.getStyle());
	}

	/**
	 * 
	 * @Title: buildDocument
	 * @Description:
	 * @param pdfpath
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 * @return: Document
	 */
	public static Document buildDocument(String pdfpath) throws DocumentException, IOException {
		// A4纸大小 左、右、上、下
		Document document = new Document(PageSize.A4, 80, 79, 20, 45);
		// 页面存放位置
		if (StringUtils.isNotBlank(pdfpath)) {
			// 先创建目录
			File file = new File(pdfpath);
			if (!file.exists()) {
				file.createNewFile();
			}
			PdfWriter.getInstance(document, new FileOutputStream(pdfpath));
		}
		return document;
	}

	/**
	 * 
	 * @Title: buildDocument
	 * @Description:
	 * @param pdfpath
	 * @param fileName
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 * @return: Document
	 */
	public static Document buildDocument(String pdfpath, String fileName) throws DocumentException, IOException {
		// A4纸大小 左、右、上、下
		Document document = new Document(PageSize.A4, 80, 79, 20, 45);
		// 页面存放位置
		if (StringUtils.isNotBlank(pdfpath)) {
			// 先创建目录
			File filePath = new File(pdfpath);
			if (!filePath.exists()) {
				filePath.mkdirs();
			}
			filePath.setWritable(true, false);
			String fileFullName = pdfpath + "/" + fileName;
			File file = new File(fileFullName);
			if (!file.exists()) {
				file.createNewFile();
			}
			PdfWriter.getInstance(document, new FileOutputStream(fileFullName));
		}
		return document;
	}

	public static Document buildDocument(OutputStream outputStream) throws DocumentException, IOException {
		// A4纸大小 左、右、上、下
		Document document = new Document(PageSize.A4, 80, 79, 20, 45);
		// 页面存放位置
		PdfWriter.getInstance(document, outputStream);
		return document;
	}

	public static void buildTile(Document document, String title) throws DocumentException {
		Paragraph p = new Paragraph(title, initChineseFont(firstTitle));// 抬头
		p.setAlignment(Element.ALIGN_LEFT); // 居中设置
		p.setLeading(42f);// 设置行间距//设置上面空白宽度
		document.add(p);
	}

	public void buildTile2(Document document, String title) throws DocumentException {
		Paragraph title3 = new Paragraph(title, initChineseFont(secondTitle));// 抬头
		title3.setAlignment(Element.ALIGN_LEFT); // 居中设置
		title3.setLeading(22f);// 设置行间距//设置上面空白宽度
		document.add(title3);
	}

	public void buildTile3(Document document, String title) throws DocumentException {
		Paragraph title3 = new Paragraph(title, initChineseFont(thirdFont));// 抬头
		title3.setAlignment(Element.ALIGN_LEFT); // 居中设置
		title3.setLeading(22f);// 设置行间距//设置上面空白宽度
		document.add(title3);
	}

	public static void buildString(Document document, String msg) throws DocumentException {
		Paragraph title1 = new Paragraph(msg, initChineseFont(smallFont));// 抬头
		title1.setAlignment(Element.ALIGN_LEFT); // 居中设置
		title1.setLeading(22f);// 设置行间距//设置上面空白宽
		document.add(title1);
	}

	public static void buildContent(Document document, String msg) throws DocumentException {
		Paragraph title1 = new Paragraph(msg, initChineseFont(thirdFont));// 抬头
		title1.setAlignment(Element.ALIGN_LEFT); // 居中设置
		title1.setLeading(22f);// 设置行间距//设置上面空白宽
		document.add(title1);
	}

	/**
	 * 创建表格
	 * 
	 * @param document
	 * @param widths
	 *            列宽
	 * @param head
	 *            表头
	 * @param content
	 *            内容
	 * @throws DocumentException
	 */
	public static void buildTable(Document document, float[] widths, String[] head, String[][] content)
			throws DocumentException {
		// 开始设置表格
		PdfPTable table = new PdfPTable(widths);// 建立一个pdf表格
		table.setSpacingBefore(20f);// 设置表格上面空白宽度
		table.setTotalWidth(500);// 设置表格的宽度
		table.setWidthPercentage(100);// 设置表格宽度为%100
		// 表格设置
		table.setComplete(false);

		PdfPCell cell = new PdfPCell();
		if (head != null) {
			cell.setFixedHeight(20);
			cell.setBackgroundColor(new BaseColor(0, 117, 192));
			Font initChineseFont = initChineseFont(theadFont);
			// ---表头
			for (int i = 0; i < head.length; i++) {
				Paragraph paragraph = new Paragraph(head[i], initChineseFont);// 描述
				cell.setPhrase(paragraph);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);// 设置内容水平居中显示
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // 设置垂直居中
				table.addCell(cell);
			}
		}
		// 内容
		if (null != content && content.length > 0) {
			cell = new PdfPCell();
			cell.setBackgroundColor(new BaseColor(255, 255, 255));
			Font initChineseFont = initChineseFont(thirdFont);
			for (int i = 0; i < content.length; i++) {
				for (int j = 0; j < content[i].length; j++) {
					Paragraph p = new Paragraph(content[i][j], initChineseFont);
					// 设置行距(没有作用)
					p.setLeading(100f);
					cell.setPhrase(p);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);// 设置内容水平居中显示
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // 设置垂直居中
					table.addCell(cell);
				}
			}
		}

		table.setComplete(true);
		document.add(table);
	}

	/**
	 * 创建测试详情表格
	 * 
	 * @param document
	 * @param widths
	 *            列宽
	 * @param head
	 *            表头
	 * @param content
	 *            内容
	 * @throws DocumentException
	 */
	public static void buildDetailTable(Document document, float[] widths, String[] head, String[][] content)
			throws DocumentException {
		// 开始设置表格
		PdfPTable table = new PdfPTable(widths);// 建立一个pdf表格
		table.setSpacingBefore(20f);// 设置表格上面空白宽度
		table.setTotalWidth(500);// 设置表格的宽度
		table.setWidthPercentage(100);// 设置表格宽度为%100
		// 表格设置
		table.setComplete(false);

		Font init_theadFont = initChineseFont(theadFont);
		Font init_thirdFont = initChineseFont(thirdFont);
		PdfPCell cell = new PdfPCell();
		if (head != null) {
			// ---表头
			for (int i = 0; i < head.length; i++) {
				Paragraph paragraph = new Paragraph(head[i], init_theadFont);// 描述
				cell.setFixedHeight(20);
				cell.setBackgroundColor(new BaseColor(0, 117, 192));
				cell.setPhrase(paragraph);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);// 设置内容水平居中显示
				cell.setVerticalAlignment(Element.ALIGN_CENTER); // 设置垂直居中
				table.addCell(cell);
			}
		}
		// 内容
		for (int i = 0; i < content.length; i++) {
			for (int j = 0; j < content[i].length; j++) {
				Paragraph p = new Paragraph(content[i][j], init_thirdFont);
				// 设置行距(没有作用)
				p.setLeading(100f);
				cell.setPhrase(p);// 内容
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);// 设置内容水平居中显示
				cell.setVerticalAlignment(Element.ALIGN_CENTER); // 设置垂直居中
				table.addCell(cell);
			}
		}

		table.setComplete(true);
		document.add(table);
	}

	/**
	 * 
	 * @Title: clearMultiSpace
	 * @Description: 多空格替换为单空格
	 * @param srcString
	 * @return
	 * @return: String
	 */
	private static String clearMultiSpace(String srcString) {
		if (StringUtils.isNotBlank(srcString)) {
			Pattern p = Pattern.compile("\\s+|\t|\r|\n");
			Matcher m = p.matcher(srcString);
			return m.replaceAll(" ");
		}
		return srcString;
	}

	/**
	 * 
	 * @Title: xmlToPdf
	 * @Description: 读取服务器文件，在本地生成PDF文件
	 * @param outputFilePath
	 * @throws Exception
	 * @return: void
	 */
	public static void xmlToPdf(String filePath, String fileName) throws Exception {
		// 读取测试结果的XML文件，并解析成JSON
		String content = "";
		// 此处的路径：需求当时定的固定路径
		File testResultFile = new File("/ast_report/test.xml");
		if (testResultFile.exists()) {
			byte[] readFileToByteArray = FileUtils.readFileToByteArray(testResultFile);
			content = new String(readFileToByteArray);
		}
		content = content.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
		content = "<root>" + content + "</root>";
		XMLSerializer xmlSerializer = new XMLSerializer();
		JSON json = xmlSerializer.read(content);
		// PDF展示,拼装显示内容
		Document buildDocument = WriterPdfs.buildDocument(filePath, fileName);
		buildDocument.open();
		float[] widths2 = { 30f, 65f };
		String[] head2 = { "Service(Port)", "Threat Level" };
		String[][] c2 = null;
		float[] widths4 = { 20f, 20f, 25f, 30f };
		String[] head4 = { "Service(Port)", "解决方法", "漏洞检查方法", "总结" };
		String[][] c4 = null;
		JSONObject rootObj = null;
		// 1 测试综述
		if (null != json && json.isArray()) {
			JSONArray rootArray = JSONArray.fromObject(json);
			if (null != rootArray && rootArray.size() > 0) {
				rootObj = (JSONObject) ((JSONObject) rootArray.get(0)).get("report");
			}
			JSONObject portsObj = (JSONObject) rootObj.get("ports");
			if (null != portsObj) {
				JSONArray jsonArray = JSONArray.fromObject(portsObj.get("port"));
				if (null != jsonArray && jsonArray.size() > 0) {
					c2 = new String[jsonArray.size()][head2.length];
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject fromObject = JSONObject.fromObject(jsonArray.get(i));
						String port = (String) fromObject.get("#text");
						String level = (String) fromObject.get("threat");
						c2[i][0] = port;
						c2[i][1] = level;
					}
				}
			}

			// 2 测试详情
			// 解决方法
			JSONObject resultsObj = (JSONObject) rootObj.get("results");
			if (null != resultsObj) {
				Object object = resultsObj.get("result");
				if (null != object && object instanceof JSONArray) {
					JSONArray jsonArray = (JSONArray) resultsObj.get("result");
					if (null != jsonArray && jsonArray.size() > 0) {
						c4 = new String[jsonArray.size()][head4.length];
						for (int i = 0; i < jsonArray.size(); i++) {
							JSONObject resultObj = JSONObject.fromObject(jsonArray.get(i));
							JSONObject nvtObj = JSONObject.fromObject(resultObj.get("nvt"));
							String detail = resultObj.getString("name");
							String port = resultObj.getString("port");
							String oid = nvtObj.getString("@oid");
							// 漏洞检查方法
							String summary = "";
							String solution = "";
							String tags = (String) nvtObj.get("tags");
							if (org.apache.commons.lang3.StringUtils.isNotBlank(tags) && tags.contains("|")) {
								String[] tempSolution = tags.split("\\|");
								for (String string : tempSolution) {
									if (string.contains("summary")) {
										int indexOf = string.lastIndexOf("=");
										summary = string.substring(indexOf + 1, string.length());
									}
									if (string.contains("solution")) {
										int indexOf = string.lastIndexOf("=");
										solution = string.substring(indexOf + 1, string.length());
									}
								}
							}
							c4[i][0] = port;
							c4[i][1] = clearMultiSpace(solution);
							c4[i][2] = "详细:" + clearMultiSpace(detail) + "        OID:" + clearMultiSpace(oid);
							c4[i][3] = clearMultiSpace(summary);
						}
					}
				} else if (null != object && object instanceof JSONObject) {
					c4 = new String[1][head4.length];
					JSONObject resultObj = JSONObject.fromObject(object);
					JSONObject nvtObj = JSONObject.fromObject(resultObj.get("nvt"));
					String detail = resultObj.getString("name");
					String port = resultObj.getString("port");
					String oid = nvtObj.getString("@oid");
					// 漏洞检查方法
					String summary = "";
					String solution = "";
					String tags = (String) nvtObj.get("tags");
					if (org.apache.commons.lang3.StringUtils.isNotBlank(tags) && tags.contains("|")) {
						String[] tempSolution = tags.split("\\|");
						for (String string : tempSolution) {
							if (string.contains("summary")) {
								int indexOf = string.lastIndexOf("=");
								summary = string.substring(indexOf + 1, string.length());
							}
							if (string.contains("solution")) {
								int indexOf = string.lastIndexOf("=");
								solution = string.substring(indexOf + 1, string.length());
							}
						}
					}
					c4[0][0] = port;
					c4[0][1] = clearMultiSpace(solution);
					c4[0][2] = "详细:" + clearMultiSpace(detail) + "        OID:" + clearMultiSpace(oid);
					c4[0][3] = clearMultiSpace(summary);
				}
			}

			WriterPdfs.buildTile(buildDocument, "1   测试综述");
			WriterPdfs.buildTable(buildDocument, widths2, head2, c2);
			WriterPdfs.buildTile(buildDocument, "2   测试详情");
			WriterPdfs.buildDetailTable(buildDocument, widths4, head4, c4);
			buildDocument.close();
		}
	}

}
