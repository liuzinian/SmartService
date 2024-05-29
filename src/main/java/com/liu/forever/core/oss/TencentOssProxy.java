package com.liu.forever.core.oss;

import com.liu.forever.core.properties.TencentOssProperties;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.StorageClass;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.utils.IOUtils;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import xyz.erupt.annotation.fun.AttachmentProxy;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * 腾讯oss代理
 *
 * @author liuzinian
 * @date 2023/08/02
 */
@Service
public class TencentOssProxy implements AttachmentProxy {

    @Resource
    private TencentOssProperties tencentOssProperties;


    /**
     * 获取连接
     *
     * @return {@link COSClient}
     */
    private COSClient getClient() {

        // 1 初始化用户身份信息（secretId, secretKey）。
        // SECRETID 和 SECRETKEY 请登录访问管理控制台 https://console.cloud.tencent.com/cam/capi 进行查看和管理
        String secretId = tencentOssProperties.getSecretId();//用户的 SecretId，建议使用子账号密钥，授权遵循最小权限指引，降低使用风险。子账号密钥获取可参见 https://cloud.tencent.com/document/product/598/37140
        String secretKey = tencentOssProperties.getSecretKey();//用户的 SecretKey，建议使用子账号密钥，授权遵循最小权限指引，降低使用风险。子账号密钥获取可参见 https://cloud.tencent.com/document/product/598/37140
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的地域, COS 地域的简称请参见 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region(tencentOssProperties.getRegion());
        ClientConfig clientConfig = new ClientConfig(region);
        // 这里建议设置使用 https 协议
        // 从 5.6.54 版本开始，默认使用了 https
        clientConfig.setHttpProtocol(HttpProtocol.https);
        // 3 生成 cos 客户端。
        return new COSClient(cred, clientConfig);

    }

    public String getFileType(InputStream inputStream) {

        try {
            Tika tika = new Tika();
            return tika.detect(inputStream);
        } catch (Exception e) {
            return null;
        }

    }

    public String extractFileName(String filePath) {
        // 判断filePath是否为空，或者文件路径不包含/，则直接返回空字符串
        if (filePath == null || !filePath.contains("/")) {
            return filePath;
        }
        // 以/为分隔符将filePath拆分成各个部分
        String[] parts = filePath.split("/");
        // 返回拆分后的最后一部分，即文件名
        return parts[parts.length - 1];
    }

    @Override
    public String upLoad(InputStream inputStream, String path) {

        COSClient cosClient = getClient();

        String extractFileName = extractFileName(path);
        String key = "admin/" + extractFileName;

        try {

            // bucket名需包含appid
            String bucketName = tencentOssProperties.getBucketName();

            ObjectMetadata objectMetadata = new ObjectMetadata();

            byte[] fileContent = IOUtils.toByteArray(inputStream);
            int size = fileContent.length;

            // 从输入流上传必须制定content length, 否则http客户端可能会缓存所有数据，存在内存OOM的情况
            objectMetadata.setContentLength(size);
            // 默认下载时根据cos路径key的后缀返回响应的contenttype, 上传时设置contenttype会覆盖默认值
            objectMetadata.setContentType(getFileType(new ByteArrayInputStream(fileContent)));

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, new ByteArrayInputStream(fileContent), objectMetadata);
            // 设置存储类型, 默认是标准(Standard), 低频(standard_ia)
            putObjectRequest.setStorageClass(StorageClass.Standard);

            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            // putobjectResult会返回文件的etag
            String etag = putObjectResult.getETag();
            System.out.println("etag = " + etag);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 关闭客户端
        cosClient.shutdown();

        return tencentOssProperties.getFileDomain() + "/" + key;
    }

    @Override
    public String fileDomain() {
        return tencentOssProperties.getFileDomain();
    }

    @Override
    public boolean isLocalSave() {
        return false;
    }
}
