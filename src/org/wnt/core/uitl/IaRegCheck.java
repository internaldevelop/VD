
/**   
 * Copyright © 2016 Beijing WINICSSEC Technologies Co.,Ltd. All rights reserved.
 * 
 * @Title: IaRegCheck.java 
 * @Prject: USM
 * @Package: com.usm.commons.regularcheck.ia 
 * @Description: 【审计】各规则校验
 * @author: jfQiao   
 * @date: 2016年12月1日 下午5:03:21 
 * @version: V1.0   
 */
package org.wnt.core.uitl;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName: IaRegCheck
 * @Description: 【审计】各规则校验工具类
 * @author: jfQiao
 * @date: 2016年12月1日 下午5:03:21
 */
public class IaRegCheck {

	/** 字符串 */
	private static final String PATTERN_STRING = "^[0-9a-zA-Z\\w\\d\u4e00-\u9fff-_]{0,32}$";

	private static final String PATTERN_NUM = "^[0-9]+$";

	/** IPV4地址 */
	private static final String PATTERN_IPV4 = "^(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})$";

	/** IPV6地址 */
	private static final String PATTERN_IPV6 = "^([\\dA-F]{1,4}:((?=.*(::))(?!.*\\3.+\\3?)([\\dA-F]{1,4}(\\3|:\\b)|\\2){5}(([\\dA-F]{1,4}(\\3|:\\b|$)|\\2){2}|(((2[0-4]|1\\d|[1-9])?\\d|25[0-5])\\.?\\b){4})\\z";

	/** 端口 */
	private static final String PATTERN_PORT = "^([0-9]|[1-9]\\d|[1-9]\\d{2}|[1-9]\\d{3}|[1-5]\\d{4}|6[0-4]\\d{3}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5])$";

	/** MAC地址 */
	private static final String PATTERN_MACADDRESS = "^([A-Fa-f0-9]{2}[:]{1}){5}[A-Fa-f0-9]+$";

	/** 日期 */
	private static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");

	/** 日期时间 */
	private static final SimpleDateFormat DATETIMEFORMAT = new SimpleDateFormat("yyyy-MM-dd");

	/** URL地址校验 */
	private static final String PATTERN_URL = "^((https|http|ftp|rtsp|mms)?://)[^\\s]+";

	/** 信箱校验 */
	private static final String PATTERN_MAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

	/** SMTP信箱校验 */
	private static final String PATTERN_MAIL_SMTP = "^[0-9a-zA-Z\\w\\d\u4e00-\u9fff-\\@\\.]{0,1024}$";

	/** 上下行流量的校验 */
	private static final String PATTERN_FLOW = "^([0]|([1-9]{1}[0-9]{0,9}))$";

	/** 验证只能包含汉字、数字、单词、字母和连接符 */
	private static final String PATTERN_STRING2 = "^[0-9a-zA-Z\\w\\d\u4e00-\u9fff-_]+$";

	/** 密码的校验 */
	
	private static final String PATTERN_PASSWORD = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[#@!~%^&*])[0-9a-zA-Z\\d#@!~%^&*]{8,16}$";
//	private static final String PATTERN_PASSWORD = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[#@!~%^&*])[a-zA-Z\\d#@!~%^&*]{8,16}$";
	/** uuid */
	private static final String UUID = "^[A-Fa-f0-9]{32}$";

	/** ftp地址 */
	private static final String PATTERN_FTP = "^ftp://((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?).*$";

	/**
	 * 模糊查询url
	 */
	private static final String LIKE_URL = "^[0-9a-zA-Z\\w\\d\\u4e00-\\u9fff-\\:\\.\\/]{0,1024}$";

	/**
	 * 判断是否是int 正整数类型 如果字符串是数值字符串也返回true
	 * 
	 * @param param
	 * @return 如果为 null 返回false
	 * @author gyk
	 * @data 2016年12月22日
	 */
	public static boolean checkInt(Object param) {
		if (param == null) {
			return false;
		}
		Integer t = -1;
		try {
			if (param instanceof String) {
				if (Pattern.compile(PATTERN_NUM).matcher(param.toString()).matches()) {
					t = Integer.parseInt(param.toString());
				} else {
					return false;
				}
			} else if (param instanceof Integer) {
				t = (Integer) param;
			} else if (param instanceof Long) {
				t = ((Long) param).intValue();
			}
		} catch (Exception e) {
			return false;
		}
		if (t < 0 || t > Integer.MAX_VALUE) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 校验是否是long 类型的正整数
	 * 
	 * @param param
	 * @return 是否是long 类型
	 * @author gyk
	 * @data 2017年1月10日
	 */
	public static boolean checkLong(Object param) {
		return checkLong(param, true);
	}

	/**
	 * 校验是否是long 类型的正整数
	 * 
	 * @param param
	 *            参数
	 * @param isStrict
	 *            是否是严格的校验 (true，参数只可以是long 类型，false 参数可以是字符串和int 类型)
	 * @return 是否是 long
	 * @author gyk
	 * @data 2017年1月10日
	 */
	public static boolean checkLong(Object param, boolean isStrict) {
		if (param == null) {
			return false;
		}
		long t = -1;
		try {
			if (!isStrict) {
				if (param instanceof String) {
					if (Pattern.compile(PATTERN_NUM).matcher(param.toString()).matches()) {
						t = Long.parseLong(param.toString());
					} else {
						return false;
					}
				} else if (param instanceof Integer) {
					t = (Long) param;
				}
			}
			if (param instanceof Long) {
				t = (Long) param;
			}
		} catch (Exception e) {
			return false;
		}
		if (t < 0 || t > Long.MAX_VALUE) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 
	 * @Title: checkString
	 * @Description: 【审计】字符串检查
	 * @param string
	 * @return
	 * @return: boolean
	 */
	public static boolean checkString(String string) {
		if (StringUtils.isNotBlank(string)) {
			return Pattern.compile(PATTERN_STRING).matcher(string.trim()).matches();
		}
		return false;
	}

	/**
	 * 校验输入的字符串 只允许输入汉字、数字、单词、字母和连接符
	 * 
	 * @param str
	 *            输入的值
	 * @param length
	 *            输入的最大长度 （大于0 的正整数）
	 * @return
	 * @author gyk
	 * @data 2017年1月5日
	 */
	public static boolean checkString(String str, int length) {
		if (length < 1) {
			return false;
		}
		if (StringUtils.isNotBlank(str)) {
			if (str.length() > length) {
				return false;
			}
			return Pattern.compile(PATTERN_STRING2).matcher(str.trim()).matches();
		}
		return false;
	}

	/**
	 * 
	 * @Title: checkIpv4
	 * @Description: 【审计】ipv4检查
	 * @param ipv4
	 * @return
	 * @return: boolean
	 */
	public static boolean checkIpv4(String ipv4) {
		if (StringUtils.isNotBlank(ipv4)) {
			return Pattern.compile(PATTERN_IPV4).matcher(ipv4.trim()).matches();
		}
		return false;
	}

	/**
	 * 
	 * @Title: checkIpv6
	 * @Description: 【审计】ipv6检查
	 * @param ipv6
	 * @return
	 * @return: boolean
	 */
	public static boolean checkIpv6(String ipv6) {
		if (StringUtils.isNotBlank(ipv6)) {
			return Pattern.compile(PATTERN_IPV6).matcher(ipv6.trim()).matches();
		}
		return false;
	}

	/**
	 * 
	 * @Title: checkPort
	 * @Description: 【审计】端口检查
	 * @param port
	 * @return
	 * @return: boolean
	 */
	public static boolean checkPort(Object port) {
		if (null == port)
			return false;
		if (port instanceof String && StringUtils.isNotBlank((String) port)) {
			return Pattern.compile(PATTERN_PORT).matcher((String) port).matches();
		} else if (port instanceof Integer) {
			Integer port_int = (Integer) port;
			if (null != port_int && port_int >= 0 && port_int <= 65535) {
				return Pattern.compile(PATTERN_PORT).matcher(String.valueOf(port_int)).matches();
			}
		}

		return false;
	}

	/**
	 * 
	 * @Title: checkMacaddress
	 * @Description: 【审计】MAC地址检查
	 * @param mac
	 * @return
	 * @return: boolean
	 */
	public static boolean checkMacaddress(String mac) {
		if (StringUtils.isNotBlank(mac)) {
			return Pattern.compile(PATTERN_MACADDRESS).matcher(mac.trim()).matches();
		}
		return false;
	}

	/**
	 * 
	 * @Title: checkProto
	 * @Description: 【审计】协议检查
	 * @param proto
	 * @return
	 * @return: boolean
	 */
	public static boolean checkProto(String proto) {
		if (StringUtils.isNotBlank(proto)) {
			proto = proto.trim();
			if (proto.equalsIgnoreCase("TCP") || proto.equalsIgnoreCase("UDP")) {
				return true;
			}
		}
		return false;
	}

	public static boolean checkAlproto(String alproto) {
		if (StringUtils.isNotBlank(alproto)) {
			alproto = alproto.trim();
			if (alproto.equalsIgnoreCase("OPC") || alproto.equalsIgnoreCase("S7") || alproto.equalsIgnoreCase("MODBUS")
					|| alproto.equalsIgnoreCase("DNP3") || alproto.equalsIgnoreCase("IEC104")
					|| alproto.equalsIgnoreCase("MMS")||alproto.equalsIgnoreCase("CIP")||alproto.equalsIgnoreCase("853")
					|| alproto.equalsIgnoreCase("DOS") || alproto.equals("1") || alproto.equals("2")
					|| alproto.equals("3") || alproto.equals("4") || alproto.equals("5") || alproto.equals("6")||alproto.equals("7")||alproto.equals("8")|| alproto.equals("14")) {
				/*
				 * alproto.equalsIgnoreCase("ICS_RST")||alproto.equalsIgnoreCase
				 * ("PROTOCOL_SANITY_CHECK")||
				 * alproto.equalsIgnoreCase("KEY_EVENT_CHECK")||alproto.
				 * equalsIgnoreCase("USER_DEFINED_ALERT_SUPPORT")||
				 * alproto.equalsIgnoreCase("USER_DEFINED_ALERT_UNSUPPORT")||
				 * alproto.equalsIgnoreCase("NO_TRAFFIC")||
				 */
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @Title: checkStatisticsNum
	 * @Description: 【审计】检查统计列数量
	 * @param num
	 * @return
	 * @return: boolean
	 */
	public static boolean checkStatisticsNum(Integer num) {
		if (null != num && num >= 0 && num <= 50) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @Title: checkDate
	 * @Description: 校验日期是否合法
	 * @param date
	 * @return
	 * @return: boolean
	 */
	private static boolean checkDate(String date) {
		try {
			DATEFORMAT.setLenient(false);
			if (StringUtils.isNotBlank(date)) {
				DATEFORMAT.parse(date);
				return true;
			}
		} catch (Exception ex) {
			return false;
		}
		return false;
	}

	/**
	 * 
	 * @Title: checkDateTime
	 * @Description: 校验日期是否合法
	 * @param dateTime
	 * @return
	 * @return: boolean
	 */
	private static boolean checkDateTime(String dateTime) {
		try {
			DATETIMEFORMAT.setLenient(false);
			if (StringUtils.isNotBlank(dateTime)) {
				DATETIMEFORMAT.parse(dateTime);
				return true;
			}
		} catch (Exception ex) {
			return false;
		}
		return false;
	}

	/**
	 * 
	 * @Title: checkBeginDateTime
	 * @Description: 检查开始时间
	 * @param beginDateTime
	 * @return
	 * @return: boolean
	 */
	public static boolean checkBeginDateTime(String beginDateTime) {
		return checkDateTime(beginDateTime);
	}

	/**
	 * 
	 * @Title: checkEndDateTime
	 * @Description: beginDateTime
	 * @param endDateTime
	 * @param beginDateTime
	 * @return
	 * @return: boolean
	 */
	public static boolean checkEndDateTime(String endDateTime, String beginDateTime) {
		if (!checkDateTime(beginDateTime) || !checkDateTime(endDateTime)) {
			return false;
		}
		return endDateTime.compareTo(beginDateTime) >= 0 ? true : false;
	}

	/**
	 * 
	 * @Title: checkBeginDate
	 * @Description: 【审计】检查开始时间
	 * @param beginDate
	 * @return
	 * @return: boolean
	 */
	public static boolean checkBeginDate(String beginDate) {
		return checkDate(beginDate);
	}

	/**
	 * 
	 * @Title: checkEndDate
	 * @Description: 【审计】检查结束时间，检查结束时间格式并且是否大于等于开始时间
	 * @param endDate
	 * @param beginDate
	 * @return
	 * @return: boolean
	 */
	public static boolean checkEndDate(String endDate, String beginDate) {
		if (!checkDate(beginDate) || !checkDate(endDate)) {
			return false;
		}
		return endDate.compareTo(beginDate) >= 0 ? true : false;
	}

	/**
	 * 
	 * @Title: checkUrl
	 * @Description: 【审计】检查URL
	 * @param url
	 * @return
	 * @return: boolean
	 */
	public static boolean checkUrl(String url) {
		if (StringUtils.isNotBlank(url)) {
			return Pattern.compile(PATTERN_URL).matcher(url.trim()).matches();
		}
		return false;
	}

	/**
	 * 
	 * @Title: checkPop3Mail
	 * @Description: 【审计】检查信箱
	 * @param pop3Mail
	 * @return
	 * @return: boolean
	 */
	public static boolean checkMail(String mail) {
		if (StringUtils.isNotBlank(mail)) {
			return Pattern.compile(PATTERN_MAIL).matcher(mail.trim()).matches();
		}
		return false;
	}

	/**
	 * 
	 * @Title: checkFlow
	 * @Description: 【审计】检查上下行流量
	 * @param flow
	 * @return
	 * @return: boolean
	 */
	public static boolean checkFlow(String flow) {
		if (StringUtils.isNotBlank(flow)) {
			return Pattern.compile(PATTERN_FLOW).matcher(Long.parseLong(flow.trim()) + "").matches();
		}
		return false;
	}

	/**
	 * 
	 * @Title: checkSmtpMail
	 * @Description: 【审计】SMTP信箱校验
	 * @param mail
	 * @return
	 * @return: boolean
	 */
	public static boolean checkSmtpMail(String mail) {
		if (StringUtils.isNotBlank(mail)) {
			return Pattern.compile(PATTERN_MAIL_SMTP).matcher(mail.trim()).matches();
		}
		return false;
	}

	/**
	 * 模糊查询url
	 * 
	 * @param url
	 * @return
	 */
	public static boolean checkLikeUrl(String url) {
		if (StringUtils.isNotBlank(url)) {
			return Pattern.compile(LIKE_URL).matcher(url.trim()).matches();
		}
		return false;
	}

	/**
	 * 检测值的范围在 0 - 255 之间
	 * 
	 * @param param
	 * @return
	 * @author gyk
	 * @data 2016年12月23日
	 */
	public static boolean check0_255(Object param) {
		if (checkInt(param)) {
			int temp = Integer.valueOf(param.toString());
			if (temp <= 255) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检测值的范围在 0 - 16777215 之间
	 * 
	 * @param param
	 * @return
	 * @author gyk
	 * @data 2016年12月23日
	 */
	public static boolean check0_16777215(Object param) {
		if (checkInt(param)) {
			int temp = Integer.valueOf(param.toString());
			if (temp <= 16777215) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @Title: checkPassword
	 * @Description: 检查密码
	 * @param password
	 * @return
	 * @return: boolean
	 */
	public static boolean checkPassword(String password) {
		if (StringUtils.isNotBlank(password)) {
			return Pattern.compile(PATTERN_PASSWORD).matcher(password.trim()).matches();
		}
		return false;
	}

	/**
	 * uuid 合法型校验 字符串长度32 位
	 * 
	 * @param uuid
	 * @return
	 * @author gyk
	 * @data 2016年11月1日
	 */
	public static boolean checkUuid(String uuid) {
		if (StringUtils.isNotBlank(uuid)) {
			return Pattern.compile(UUID).matcher(uuid).matches();
		}
		return false;
	}

	/**
	 * 判断掩码是否正确
	 * 
	 * @param param
	 * @return 如果为 null 返回false
	 * @author zmy
	 * @data 2017年01月5日
	 */
	public static boolean checkMask(Object param) {
		if (param == null) {
			return false;
		}
		if (param instanceof Integer) {
			if ((Integer) param > -1 && (Integer) param <= 32) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * 判断告警级别是否正确
	 * 
	 * @param param
	 * @return 如果为 null 返回false
	 * @author zmy
	 * @data 2017年01月5日
	 */
	public static boolean checkLevel(Object param) {
		if (param == null) {
			return false;
		}
		if (param instanceof Integer) {
			if ((Integer) param > 0 && (Integer) param <= 8) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * 
	 * @Title: checkRemark
	 * @Description: 检查备注
	 * @param remark
	 * @return
	 * @return: boolean
	 */
	public static boolean checkRemark(String remark) {
		if (StringUtils.isNotBlank(remark)) {
			return remark.length() <= 256 ? true : false;
		}
		return false;
	}

	/**
	 * 
	 * @Title: checkFtp
	 * @Description: 检查ftp地址
	 * @param ftp
	 * @return
	 * @return: boolean
	 */
	public static boolean checkFtp(String ftp) {
		if (StringUtils.isNotBlank(ftp) && ftp.length() <= 256) {
			return Pattern.compile(PATTERN_FTP).matcher(ftp.trim()).matches();
		}
		return false;
	}
}
