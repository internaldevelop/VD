package org.wnt.core.exception;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.lang.InternalError;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.wnt.web.log.entry.Tdlogs;

public class ExceptionHandler implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		
		Tdlogs tdlogs = new Tdlogs();
		String strex = "";
		if (ex instanceof MaxUploadSizeExceededException) {
			strex = "文件上传大小超出限制";			
		}else if (ex instanceof BadSqlGrammarException) {
			strex = "数据库语句错误";
		}else if (ex instanceof FileNotFoundException) {
			strex = "文件不存在";
		}else if(ex instanceof NullPointerException){
			strex = "调用了未经初始化的对象或者是不存在的对象";
		}else if(ex instanceof DataAccessException){
			strex = "数据库操作失败";
		}else if(ex instanceof IOException){
			strex = "IO 异常";
		}else if(ex instanceof ClassNotFoundException){
			strex = "指定的类不存在";
		}else if(ex instanceof ArithmeticException){
			strex = "数学运算异常";
		}else if(ex instanceof ArrayIndexOutOfBoundsException){
			strex = "数组下标越界";
		}else if(ex instanceof IllegalArgumentException){
			strex = "方法的参数错误";
		}else if(ex instanceof ClassCastException){
			strex = "类型强制转换错误";
		}else if(ex instanceof SecurityException){
			strex = "违背安全原则异常";
		}else if(ex instanceof SQLException){
			strex = "操作数据库异常";
		}else if(ex instanceof Exception){
			strex = "程序内部错误，操作失败";
		}else{
			strex = "异常错误";
		}
		return new ModelAndView("upLoad");
	}
}