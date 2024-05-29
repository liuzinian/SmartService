package com.liu.forever.core.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class TencentOssProperties {
    @Value("${tencentOss.bucketName}")
    private String bucketName;
    @Value("${tencentOss.secretId}")
    private String secretId;
    @Value("${tencentOss.secretKey}")
    private String secretKey;
    @Value("${tencentOss.region}")
    private String region;
    @Value("${tencentOss.fileDomain}")
    private String fileDomain;;
}
