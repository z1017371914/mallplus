package com.zscat.mallplus.annotation;

import java.lang.annotation.*;

/**
 * 系统日志注解
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017年3月8日 上午10:19:56
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

    String MODULE() default "操作模块";
    String REMARK() default "操作日志";
}
