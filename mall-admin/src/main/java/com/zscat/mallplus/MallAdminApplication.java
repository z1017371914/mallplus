package com.zscat.mallplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 应用启动入口
 * https://gitee.com/zscat-platform/mall on 2018/4/26.
 */
@SpringBootApplication
@MapperScan({"com.zscat.mallplus.mapper", "com.zscat.mallplus.ums.mapper","com.zscat.mallplus.sms.mapper","com.zscat.mallplus.cms.mapper","com.zscat.mallplus.sys.mapper","com.zscat.mallplus.oms.mapper","com.zscat.mallplus.pms.mapper"})
@EnableTransactionManagement
public class MallAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallAdminApplication.class, args);
    }
}
