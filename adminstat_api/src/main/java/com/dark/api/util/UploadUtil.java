package com.dark.api.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FilenameUtils;
import org.springframework.util.FileCopyUtils;

import com.dark.api.exception.UploadException;
import com.dark.common.domain.Constants;
import com.dark.common.util.Identities;

public class UploadUtil {

	public static String uploadImg(String path, String format,
			InputStream content) {
		if (Constants.OSS_USE) {
			return OSSObject.uploadImg(path, format, content);
		} else {
			format = format.toLowerCase();
			String key = path + Identities.uuid2()
					+ FilenameUtils.EXTENSION_SEPARATOR + format;
			String absolutePath = Constants.RESOURCE_ROOT_PATH + key;
			File folder = new File(Constants.RESOURCE_ROOT_PATH + path);
			if( !folder.exists() )
				folder.mkdirs();
			try {
				OutputStream out = new FileOutputStream(new File(absolutePath));
				FileCopyUtils.copy(content, out);
			} catch (IOException e) {
				throw new UploadException(e.getMessage());
			}
			return key;
		}
	}

}
