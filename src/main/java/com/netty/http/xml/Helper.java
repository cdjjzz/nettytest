package com.netty.http.xml;

import java.sql.Date;

/**
 * ������
 * @author pet-lsf
 *
 */
public class Helper {
	/**
	 * �������һ���ַ�
	 * @return
	 */
	public static char getRandomChar() {
        return (char) (0x4e00 + (int) (Math.random() * (0x9fa5 - 0x4e00 + 1)));
    }
	/**
	 * �������һ������
	 * @return
	 */
	public static int getRandom() {
        return (int) (Math.random() *100);
    }
	/**
	 * �������һ��С��
	 * @return
	 */
	public static double getRandomIEEE() {
        return Math.random();
    }
	public static Date currtDate(){
		return new Date(System.currentTimeMillis());
	}
	
	
}
