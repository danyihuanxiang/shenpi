package com.ssm.demo.util;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * ID生成器
 * 
 * @author jimmy
 *
 */
public class IdGen {

	/** random */
	private static SecureRandom random = new SecureRandom();

	/** 使用原生的UUID生成ID字符串 */
	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/** SecureRandom获取 Long */
	public static long randomLong() {
		return Math.abs(random.nextLong());
	}
}
