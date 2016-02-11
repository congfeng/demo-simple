package com.cf.code.common;
/**
 * @Filename: CodeCreator.java
 * @version : 1.0
 * @author  : guiren 
 * @email   : juguiren@91nongye.com
 * @date    : 2015年8月28日
 */
public final class CodeCreator {

	private final static char[] letter = { 'A', 'B', 'C', 'D', 'E', 'F', 'G',
			'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
			'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
			'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
			'x', 'y', 'z' };
	private final static char[] lowerLetter = {'0', '1', '2', '3', '4', '5', '6',
		'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
		'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
		'x', 'y', 'z' };
	
	private final static char[] numberLetter = {'0', '1', '2', '3', '4', '5', '6',
		'7', '8', '9'};
	private CodeCreator() {
	}
	

	/**
	 * 创建数字随机数串
	 * @param n
	 * @return
	 */
	public static String generateNumberArr(int n) {
		boolean[] flag = new boolean[numberLetter.length];
		char[] result = new char[n];
		for (int i = 0; i < n; i++) {
			int index;
			do {
				index = (int) (Math.random() * (numberLetter.length));
				result[i] = (char) (Math.random() * (numberLetter.length));
			} while (flag[index]);
			result[i] = numberLetter[index];
			flag[index] = true;
		}
		return String.valueOf(result);
	}
	/**
	 * 创建数字+字符的随机字符串
	 * @param n
	 * @return
	 */
	public static String generateCharArr(int n) {
		boolean[] flag = new boolean[letter.length];
		char[] result = new char[n];
		for (int i = 0; i < n; i++) {
			int index;
			do {
				index = (int) (Math.random() * (letter.length));
				result[i] = (char) (Math.random() * (letter.length));
			} while (flag[index]);
			result[i] = letter[index];
			flag[index] = true;
		}
		return String.valueOf(result);
	}
	/*
	 * 创建数字+大写字母的随机字符串
	 */
	
	public static String getUpperCaseCode(int n) {
		return generateCharArr(n).toUpperCase();
	}
	/*
	 * 创建数字+小写字母的随机字符串
	 */
	public static String generateLowerCharArr(int n) {
		boolean[] flag = new boolean[lowerLetter.length];
		char[] result = new char[n];
		for (int i = 0; i < n; i++) {
			int index;
			do {
				index = (int) (Math.random() * (lowerLetter.length));
				result[i] = (char) (Math.random() * (lowerLetter.length));
			} while (flag[index]);
			result[i] = lowerLetter[index];
			flag[index] = true;
		}
		return String.valueOf(result);
	}
	/**
	 * 创建扰乱编码
	 * */
	public static String generateEntityId(String id ){
		return generateLowerCharArr(4)+id+generateLowerCharArr(4);
	}
	/**
	 * 恢复编码、
	 * */
	public static String recoverEntityId(String id){
		if(StringUtil.isNullOrEmpty(id)){
			return null;
		}
		return id.substring(4, id.length()-4);
	} 
	
	
}

