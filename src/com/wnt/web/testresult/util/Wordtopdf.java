package com.wnt.web.testresult.util;

import java.io.File;

import com.artofsolving.jodconverter.DefaultDocumentFormatRegistry;
import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.DocumentFamily;
import com.artofsolving.jodconverter.DocumentFormat;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

public class Wordtopdf {
	
	public static boolean wordtopdf(String wordpath, String pdfpath)
	{
		File inputFile = new  File( wordpath );   
		File outputFile = new  File( pdfpath );
		
		DocumentFormat xml = new  DocumentFormat( "OpenOffice.org 3 Template" , 
				DocumentFamily.TEXT,  "text/xml" ,  "xml" );
		
        DefaultDocumentFormatRegistry formatReg = new  DefaultDocumentFormatRegistry();
        DocumentFormat pdf = formatReg.getFormatByFileExtension("pdf" );
		
		OpenOfficeConnection connection = new  SocketOpenOfficeConnection( 8100 );   
        try  {   
            connection.connect();   
            DocumentConverter converter = new  OpenOfficeDocumentConverter(connection);   
            converter.convert(inputFile, xml, outputFile, pdf);
        } catch (Exception e) {   
            e.printStackTrace();   
        } finally  {   
            try {  
            	if (connection !=  null )
            	{
            		connection.disconnect(); 
            		connection =  null ;
            	}
            }
            catch (Exception e)
            {
            	
            }   
        }
		return true;
	}
	
	public static void main(String[] args) {
		boolean flag = Wordtopdf.wordtopdf("E:/workspace/VD/WebRoot/jsp/testresult/ftl/测试报告模板.xml", 
				"E:/workspace/VD/WebRoot/jsp/testresult/ftl/in4.pdf");
		System.out.println(flag);
	}
	
}
