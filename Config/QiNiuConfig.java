package com.server.Config;


import com.google.gson.Gson;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class QiNiuConfig {
    /**
     * 七牛域名domain
     */
    @Value("${qiniu.domain}")
    private String domain ;
    /**
     * 七牛ACCESS_KEY
     */
    @Value("${qiniu.accessKey}")
    private String accessKey;
    /**
     * 七牛SECRET_KEY
     */
    @Value("${qiniu.secretKey}")
    private String secretKey;
    /**
     * 七牛空间名
     */
    @Value("${qiniu.bucket}")
    private String bucket;

    @Bean
    public com.qiniu.storage.Configuration qiniuConfig() {
        return new com.qiniu.storage.Configuration(Zone.zone2());
    }

    /**
     * 构建一个七牛上传工具实例
     */
    @Bean
    public UploadManager uploadManager() {
        return new UploadManager(qiniuConfig());
    }

    /**
     * 认证信息实例
     *
     * @return
     */
    @Bean
    public Auth auth() {
        return Auth.create(accessKey, secretKey);
    }

    /**
     * 构建七牛空间管理实例
     */
    @Bean
    public BucketManager bucketManager() {
        return new BucketManager(auth(), qiniuConfig());
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }
}
