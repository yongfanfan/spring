package com.dark.common.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {
	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

	/**
	 * 追加内容到文件
	 * @param content
	 * @param file
	 */
	public static void appendToFile(String content, String file) {
		File f = new File(file);
		FileWriter writer = null;
		try {
			if (!f.exists())
				f.createNewFile();
			// 以追加形式写文件
			writer = new FileWriter(file, true);
			writer.write(content);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("追加内容到文件{}失败:{}", file,e.getMessage());
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("追加内容到文件{}失败:{}",file, e.getMessage());
			}
		}
	}

}
