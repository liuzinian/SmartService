package com.liu.forever;


import com.liu.forever.core.oss.TencentOssProxy;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import xyz.erupt.core.annotation.EruptAttachmentUpload;
import xyz.erupt.core.annotation.EruptScan;

/**
 * 永远应用程序
 *
 * @author 刘子年
 * @date 2023/07/31
 */
@SpringBootApplication(scanBasePackages = "com.liu.forever")
@EntityScan
@EruptScan
@MapperScan("com.liu.forever.mapper")
@EruptAttachmentUpload(TencentOssProxy.class)
public class ForeverApplication extends SpringBootServletInitializer {


    public static void main(String[] args) {
        SpringApplication.run(ForeverApplication.class, args);
        try {
            System.setProperty("java.awt.headless", "true");

        } catch (Exception ignore) {
        }

    }

    //打WAR包的配置
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ForeverApplication.class);
    }

}
