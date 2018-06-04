package com.netty.http.xml;

import java.sql.Date;

/**
 * 帮助类
 * @author pet-lsf
 *
 */
public class Helper {
	/**
	 * 随机生成一个字符
	 * @return
	 */
	public static char getRandomChar() {
        return (char) (0x4e00 + (int) (Math.random() * (0x9fa5 - 0x4e00 + 1)));
    }
	/**
	 * 随机生成一个数字
	 * @return
	 */
	public static int getRandom() {
        return (int) (Math.random() *100);
    }
	/**
	 * 随机生成一个小数
	 * @return
	 */
	public static double getRandomIEEE() {
        return Math.random();
    }
	public static Date currtDate(){
		return new Date(System.currentTimeMillis());
	}
	
	
}
