package com.dark.api.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Random;

public class DataUtil {
	
	private static final String DATA_PATH = "/com/dark/api/data/";

	public static String getContent(String fileName) {
		StringBuffer sb = new StringBuffer();
		try {
			Reader r = new InputStreamReader(
					DataUtil.class.getResourceAsStream(DATA_PATH + fileName), "utf-8");
			int length = 0;
			for (char[] c = new char[1024]; (length = r.read(c)) != -1;) {
				sb.append(c, 0, length);
			}
			r.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		String fileName = "ads.json";
		System.out.println(getContent(fileName));
		System.out.println(getSomeJoke());
	}
	
	public static String getSomeJoke() {
		// 指定读取的行号
		int lineNumber = new Random().nextInt(10) + 1;
		// 读取指定的行
		return readAppointedLineNumber("joke.txt", lineNumber);
	}
	
	public static String getPointTip() {
		int lineNumber = new Random().nextInt(10) + 1;
		return readAppointedLineNumber("win.txt", lineNumber);
	}

	// 读取文件指定行。
	public static String readAppointedLineNumber(String fileName, int lineNumber) {
		
		String s = "";
		try {
			Reader in = new InputStreamReader(
					DataUtil.class.getResourceAsStream(DATA_PATH + fileName), "utf-8");
			LineNumberReader reader = new LineNumberReader(in);
			int totalNum = getTotalLines(fileName);
			if (lineNumber < 0 || lineNumber > totalNum) {
				lineNumber = totalNum;
			}
			int lines = 0;
			while (s != null) {
				lines++;
				s = reader.readLine();
				if ((lines - lineNumber) == 0) {
					break;
				}
			}
			reader.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	// 文件内容的总行数。
	static int getTotalLines(String fileName) {
		int lines = 0;
		try {
			Reader in = new InputStreamReader(
					DataUtil.class.getResourceAsStream(DATA_PATH + fileName), "utf-8");
			LineNumberReader reader = new LineNumberReader(in);
			String s = reader.readLine();
			while (s != null) {
				lines++;
				s = reader.readLine();
			}
			reader.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lines;
	}

}
