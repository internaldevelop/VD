package org.wnt.core.log;

import java.lang.reflect.Method;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.wnt.core.uitl.DataUtils;
import org.wnt.core.uitl.IpUtil;

import com.wnt.web.log.dao.TdlogsDao;
import com.wnt.web.log.entry.Tdlogs;


@Aspect
public class LogService {

	@Resource
	private TdlogsDao tdlogsdao;
	public LogService() {
//		System.out.println("3432");
	}

	// @Before("execution(* com.bpm.project.web.*.*(..))")
	public void logAll(JoinPoint point) throws Throwable {
		System.out.println("打印========================");
	}

	// @After("execution(* com.bpm.project.web.*.*(..))")
	public void after() {
		System.out.println("after");
	}

	// 方法执行的前后调用
	@Around("execution(* *..control*..*(..))")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		
		Tdlogs tdlogs = new Tdlogs();
		MethodLogEntry ml = getMthod(point);
		String currentURL = request.getRequestURI();
		currentURL = currentURL.substring(currentURL.indexOf("/", 1),
				currentURL.length());
		String packages = point.getThis().getClass().getName();
		if (packages.indexOf("$$EnhancerByCGLIB$$") > -1) { // 如果是CGLIB动态生成的类
			try {
				packages = packages.substring(0, packages.indexOf("$$"));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		tdlogs.setCreatetime(DataUtils.gettimestamp());
		tdlogs.setContent(ml.getRemark());
		tdlogs.setIp(IpUtil.getIpAddr(request));
		System.out.println(point.getThis().getClass().getName());
		tdlogs.setModule(point.getThis().getClass().getName());
		tdlogs.setOperation(ml.getOperType());
		tdlogs.setType("操作日志");
		tdlogs.setUrl(currentURL);
		tdlogs.setUsername("admin");
		tdlogsdao.insertLog(tdlogs);
		
		Object object;
		try {
			object = point.proceed();
		} catch (Exception e) {
	
			throw e;
		}
		return object;
	}

	// 方法运行出现异常时调用
	@AfterThrowing(pointcut = "execution(* *..control*..*(..))", throwing = "ex")
	public void afterThrowing(Exception ex) {
		System.out.println("afterThrowing");
		System.out.println(ex);
	}

	// 获取方法的中文备注____用于记录用户的操作日志描述
	public static MethodLogEntry getMthod(ProceedingJoinPoint joinPoint)
			throws Exception {
		MethodLogEntry ml = new MethodLogEntry();
		String targetName = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		Object[] arguments = joinPoint.getArgs();

		Class targetClass = Class.forName(targetName);
		Method[] method = targetClass.getMethods();
	
		for (Method m : method) {
			if (m.getName().equals(methodName)) {
				Class[] tmpCs = m.getParameterTypes();
				if (tmpCs.length == arguments.length) {
					MethodLog methodCache = m.getAnnotation(MethodLog.class);
					if (methodCache != null) {
						ml.setRemark(methodCache.remark());
						ml.setOperType(methodCache.operType());
					}
					break;
				}
			}
		}
		return ml;
	}

	public String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("http_client_ip");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip != null && ip.indexOf(",") != -1) {
			ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
		}
		if ("0:0:0:0:0:0:0:1".equals(ip)) {
			ip = "127.0.0.1";
		}
		return ip;
	}
}