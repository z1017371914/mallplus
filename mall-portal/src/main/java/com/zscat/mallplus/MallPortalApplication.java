package com.zscat.mallplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan({"com.macro.mall.mapper", "com.macro.mall.portal.dao", "com.macro.mall.ums.mapper","com.macro.mall.sms.mapper","com.macro.mall.cms.mapper","com.macro.mall.sys.mapper","com.macro.mall.oms.mapper","com.macro.mall.pms.mapper"})
@EnableScheduling
public class MallPortalApplication {

    public static void main(String[] args) {

        SpringApplication.run(MallPortalApplication.class, args);
    }

}
