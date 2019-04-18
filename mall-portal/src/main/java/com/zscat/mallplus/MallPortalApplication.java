package com.zscat.mallplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan({"com.zscat.mallplus.mapper", "com.zscat.mallplus.portal.dao", "com.zscat.mallplus.ums.mapper","com.zscat.mallplus.sms.mapper","com.zscat.mallplus.cms.mapper","com.zscat.mallplus.sys.mapper","com.zscat.mallplus.oms.mapper","com.zscat.mallplus.pms.mapper"})
@EnableScheduling
public class MallPortalApplication {

    public static void main(String[] args) {

        SpringApplication.run(MallPortalApplication.class, args);
    }

}
