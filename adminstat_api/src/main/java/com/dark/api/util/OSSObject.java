package com.dark.api.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import com.aliyun.openservices.ClientConfiguration;
import com.aliyun.openservices.ClientException;
import com.aliyun.openservices.ServiceException;
import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.OSSErrorCode;
import com.aliyun.openservices.oss.OSSException;
import com.aliyun.openservices.oss.model.CannedAccessControlList;
import com.aliyun.openservices.oss.model.GetObjectRequest;
import com.aliyun.openservices.oss.model.OSSObjectSummary;
import com.aliyun.openservices.oss.model.ObjectListing;
import com.aliyun.openservices.oss.model.ObjectMetadata;
import com.dark.common.domain.Constants;
import com.dark.common.util.Identities;

public class OSSObject {
	
	private static Logger logger = Logger.getLogger(OSSObject.class);

    private static final String ACCESS_ID = "H6S2e2pEd1R6X8UL";
    private static final String ACCESS_KEY = "AHWz6DCcNP5eJJQkmaqgQR9D6whHLe";
    private static final String BUCKET_NAME = "yunqi";
    private static final String IMG_TYPE_JPG = "image/jpeg";
    private static final String IMG_TYPE_PNG = "image/png";
    private static OSSClient client = null;
    
    static {
    	ClientConfiguration config = new ClientConfiguration();
        client = new OSSClient(Constants.OSS_ENDPOINT, ACCESS_ID, ACCESS_KEY, config);
    }

    // 创建Bucket.
    public static void ensureBucket(OSSClient client, String bucketName)
            throws OSSException, ClientException{

        try {
            // 创建bucket
            client.createBucket(bucketName);
        } catch (ServiceException e) {
            if (!OSSErrorCode.BUCKES_ALREADY_EXISTS.equals(e.getErrorCode())) {
                // 如果Bucket已经存在，则忽略
                throw e;
            }
        }
    }

    // 删除一个Bucket和其中的Objects 
    public static void deleteBucket(OSSClient client, String bucketName)
            throws OSSException, ClientException {

        ObjectListing ObjectListing = client.listObjects(bucketName);
        List<OSSObjectSummary> listDeletes = ObjectListing
                .getObjectSummaries();
        for (int i = 0; i < listDeletes.size(); i++) {
            String objectName = listDeletes.get(i).getKey();
            // 如果不为空，先删除bucket下的文件
            client.deleteObject(bucketName, objectName);
        }
        client.deleteBucket(bucketName);
    }

    // 把Bucket设置为所有人可读
    public static void setBucketPublicReadable(OSSClient client, String bucketName)
            throws OSSException, ClientException {
        //创建bucket
        client.createBucket(bucketName);

        //设置bucket的访问权限，public-read-write权限
        client.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
    }

    // 上传文件
    public static void uploadFile(OSSClient client, String bucketName, String key, String filename)
            throws OSSException, ClientException, FileNotFoundException {
        File file = new File(filename);

        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentLength(file.length());
        // 可以在metadata中标记文件类型
        objectMeta.setContentType("image/jpeg");

        InputStream input = new FileInputStream(file);
        client.putObject(bucketName, key, input, objectMeta);
    }
    
    public static void uploadFile(String key ,InputStream content,String contentType){
    	ObjectMetadata objectMeta = new ObjectMetadata();
        try {
			objectMeta.setContentLength(content.available());
			objectMeta.setContentType(contentType);
	        client.putObject(BUCKET_NAME, key, content, objectMeta);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error(String.format("upload file %s failed",key), e);
			}
		}
    }
    
    /**
     * 上传图片
     * @param path
     * 			图片存放的相对路径如：1/news/
     * @param content
     * 
     * @param format
     * 			文件后缀格式
     * @return
     * 		
     */
    public static String uploadImg(String path ,String format, InputStream content) {
    	format = format.toLowerCase();
    	String key = path + Identities.uuid2() + FilenameUtils.EXTENSION_SEPARATOR + format;
    	String contentType = IMG_TYPE_JPG;
    	if ("png".equals(format)) {
    		contentType = IMG_TYPE_PNG;
    	}
    	uploadFile(key ,content, contentType);
    	return key;
    }

    // 下载文件
    public static void downloadFile(OSSClient client, String bucketName, String key, String filename)
            throws OSSException, ClientException {
        client.getObject(new GetObjectRequest(bucketName, key),
                new File(filename));
    }
}
