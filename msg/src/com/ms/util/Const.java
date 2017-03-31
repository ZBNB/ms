package com.ms.util;

import java.util.Calendar;

import org.springframework.context.ApplicationContext;

/**
 * 项目名称：
 * 
 * @author:
 * 
 */
public class Const {
	public static final String SESSION_SECURITY_CODE = "sessionSecCode";
	public static final String SESSION_USER = "sessionUser";
	public static final String SESSION_USERNAME = "USERNAME"; // 用户名

	public static final int LOGINVALIDIDY = 3; // Token有效期

	public static final String LOGIN = "/login_toLogin.do"; // 登录地址
	public static final String SYSNAME = "admin/config/SYSNAME.txt"; // 系统名称路径
	public static final String PAGE = "admin/config/PAGE.txt"; // 分页条数配置路径

	public static final String SMS1 = "admin/config/SMS1.txt"; // 短信账户配置路径1
	public static final String SMS2 = "admin/config/SMS2.txt"; // 短信账户配置路径2
	public static final String FILEPATHIMG = "uploadFiles/uploadImgs/"; // 图片上传路径
	public static final String FILEPATHFILE = "uploadFiles/file/"; // 文件上传路径
	public static final String FILEPATHTWODIMENSIONCODE = "uploadFiles/twoDimensionCode/"; // 二维码存放路径
	public static final String NO_INTERCEPTOR_PATH = ".*/((login)|(logout)|(uploadFiles)|(static)|(main)).*"; // 不对匹配该值的访问路径拦截（正则）

	//课时有效期
	static Calendar cal = Calendar.getInstance();
	public static final String bigmonth01 = String.valueOf(cal.get(Calendar.YEAR))+"-"+"08-01";//大月份
	public static final String bigmonth02 = String.valueOf(cal.get(Calendar.YEAR))+"-"+"08-02";//大月份
	public static final String smallmonth01 = String.valueOf(cal.get(Calendar.YEAR))+"-"+"02-01";//小月份
	public static final String smallmonth02 = String.valueOf(cal.get(Calendar.YEAR))+"-"+"02-02";//小月份
	public static final String nsmallmonth = String.valueOf(cal.get(Calendar.YEAR)+1)+"-"+"02-01";//下一年小月份
	public static final String obigmonth = String.valueOf(cal.get(Calendar.YEAR)-1)+"-"+"08-02";//前一年大月份

	/* 后台操作日志 */
	public static final String SESSION_USERID = "USER_ID"; // 用户ID
	public static final String SESSION_IP = "IP";

	public static ApplicationContext WEB_APP_CONTEXT = null; // 该值会在web容器启动时由WebAppContextListener初始化

	// app根据用户名获取会员信息接口_请求协议中的参数
	public static final String[] APP_GETAPPUSER_PARAM_ARRAY = new String[] { "USERNAME" };
	public static final String[] APP_GETAPPUSER_VALUE_ARRAY = new String[] { "用户名" };
	
}
