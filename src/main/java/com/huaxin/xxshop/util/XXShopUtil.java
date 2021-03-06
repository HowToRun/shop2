package com.huaxin.xxshop.util;

import com.huaxin.xxshop.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 工具类，提供常用的工具方法
 * 
 * @author 没有蜡笔的小新 2015-12-21
 */
public class XXShopUtil {

	/**
	 * 使用UUID类提供的静态方法randomUUID() 得到一个id
	 * UUID: A class that represents an immutable universally unique identifier (UUID).
	 * A UUID represents a 128-bit value.
	 * length: 32
	 * @return 随机id，32位字符串
	 */
	public static String getId() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replace("-", "");
	}
	/*public static User tourist(){
		User user = new User();
		user.setId(getId());
		user.setName(getId());
		user.setEmail(getNow());
		user.setStatus();
		return user;
	}*/
	/**
	 * 得到当前的时间
	 * @return 当前时间，格式为"yyyy-MM-dd  HH:mm:ss"
	 */
	public static String getNow() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
		return sdf.format(new Date());
	}

	/**
	 * 得到商品的货号，用时间作为产生的方法
	 * @return 18位商品货号，格式为"yyyyMMddHHmmssms"
	 */
	public static String getGoodsNo() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssms");
		return sdf.format(new Date());
	}
	/**
	 *获取Ip地址的函数  方便上面的登录模块调用
	 */
	public static  String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
