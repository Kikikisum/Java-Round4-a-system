package com.server.Service.Impl;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.server.Service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service
public class OssServiceImpl implements OssService {

    @Autowired
    private UploadManager uploadManager;

    @Autowired
    private BucketManager bucketManager;

    @Autowired
    private Auth auth;

    @Value("${qiniu.bucket}")
    private String bucket;
    @Value("${qiniu.domain}")
    private String prefix;

    /**
     * 定义七牛云上传的相关策略
     */
    private StringMap putPolicy;

    /**
     * 获取上传凭证
     *
     * @return
     */
    private String getUploadToken() {
        return this.auth.uploadToken(bucket, null, 3600, putPolicy);
    }

    @Override
    public String uploadFile(File file, String fileName) throws QiniuException {
        Response response = this.uploadManager.put(file, fileName, getUploadToken());
        int retry = 0;
        while (response.needRetry() && retry < 3) {
            response = this.uploadManager.put(file, fileName, getUploadToken());
            retry++;
        }
        if (response.statusCode == 200) {
            return new StringBuffer().append(prefix).append(fileName).toString();
        }
        return "上传失败!";
    }

    @Override
    public String uploadFile(InputStream inputStream, String fileName) throws QiniuException {
        Response response = this.uploadManager.put(inputStream, fileName, getUploadToken(), null, null);
        int retry = 0;
        while (response.needRetry() && retry < 3) {
            response = this.uploadManager.put(inputStream, fileName, getUploadToken(), null, null);
            retry++;
        }

        System.out.println("addr==" + response.address);
        if (response.statusCode == 200) {
            return new StringBuffer().append(prefix).append(fileName).toString();
        }
        return "上传失败!";
    }

    @Override
    public String delete(String key) throws QiniuException {
        Response response = bucketManager.delete(this.bucket, key);
        int retry = 0;
        while (response.needRetry() && retry++ < 3) {
            response = bucketManager.delete(bucket, key);
        }
        return response.statusCode == 200 ? "删除成功!" : "删除失败!";
    }

    @Override
    public String downloadFile(String fileName) throws UnsupportedEncodingException {

        String encodedFileName = URLEncoder.encode(fileName, "utf-8").replace("+", "%20");
        if (encodedFileName.isEmpty())
            return "null";
        else
        {
            String downloadUrl = auth.privateDownloadUrl(fileName);
            String publicUrl = String.format("%s%s", prefix, encodedFileName);
//            long expireInSeconds = 3600;//1小时，可以自定义链接过期时间
//            String finalUrl = auth.privateDownloadUrl(publicUrl, expireInSeconds);
            return publicUrl;
        }
    }
    @Override
    public boolean deletePhoto(String fileName) {
        Configuration configuration = new Configuration(Region.region2());
        BucketManager bucketManager = new BucketManager(auth, configuration);
        try {
            if (fileName != null) {
                fileName.replace(prefix,"");
                bucketManager.delete(bucket, fileName);
                return true;
            }
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
        }
        return false;
    }
}
