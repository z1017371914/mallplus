package com.zscat.mallplus.bo;


import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.sys.entity.SysAdminLog;
import com.zscat.mallplus.sys.entity.SysUser;
import com.zscat.mallplus.sys.service.ISysAdminLogService;
import com.zscat.mallplus.sys.service.ISysUserService;
import com.zscat.mallplus.util.IpAddressUtil;
import com.zscat.mallplus.util.UserUtils;
import com.zscat.mallplus.utils.ValidatorUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


/**
 * 系统日志，切面处理类
 */
@Aspect
@Component
public class SysLogAspect {
    private Logger logger = LoggerFactory.getLogger(SysLogAspect.class);
    @Resource
    public ISysAdminLogService fopSystemOperationLogService;
    @Resource
    public ISysUserService adminService;
    @Pointcut("@annotation(com.zscat.mallplus.annotation.SysLog)")
    public void logPointCut() {

    }

    @Before("logPointCut()")
    public void saveSysLog(JoinPoint joinPoint) {
        try {

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();

            SysAdminLog sysLog = new SysAdminLog();
            SysLog syslog1 = method.getAnnotation(SysLog.class);
            if (syslog1 != null) {
                //注解上的描述
                sysLog.setServiceName(syslog1.MODULE());
                sysLog.setOperationDesc(syslog1.REMARK());
            }

            //请求的方法名
            String clazzName = joinPoint.getTarget().getClass().getName();
            Class<?> clazz = Class.forName(clazzName);
            String clazzSimpleName = clazz.getSimpleName();
            String methodName = signature.getName();
            sysLog.setMethod(clazzSimpleName + "." + methodName);

            //请求的参数
            String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
            StringBuilder sb = null;
            if (Objects.nonNull(parameterNames)) {
                sb = new StringBuilder();
                for (int i = 0; i < parameterNames.length; i++) {
                    Object param = joinPoint.getArgs()[i] != null ? joinPoint.getArgs()[i] : "";
                    if(StringUtils.isNotEmpty(param.toString()) && !"request".equals(parameterNames[i])&& !"response".equals(parameterNames[i])
                            && !"modelMap".equals(parameterNames[i])){
                        if (param instanceof Integer) {
                            sb.append(parameterNames[i] + ":" + param + "; ");
                        } else if (param instanceof String) {
                            sb.append(parameterNames[i] + ":" + param + "; ");
                        } else if (param instanceof Double) {
                            sb.append(parameterNames[i] + ":" + param + "; ");
                        } else if (param instanceof Float) {
                            sb.append(parameterNames[i] + ":" + param + "; ");
                        } else if (param instanceof Long) {
                            sb.append(parameterNames[i] + ":" + param + "; ");
                        } else if (param instanceof Boolean) {
                            sb.append(parameterNames[i] + ":" + param + "; ");
                        } else if (param instanceof Date) {
                            sb.append(parameterNames[i] + ":" + param + "; ");
                        }else if (param instanceof Timestamp) {
                            sb.append(parameterNames[i] + ":" + param + "; ");
                        }else{
                            sb.append(parameterNames[i] + ":" + getString(param) + "; ");
                        }
                    }
                }
            }
            sb = sb == null ? new StringBuilder() : sb;
            sysLog.setParams(sb.toString());
            //设置IP地址
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

            sysLog.setIp(IpAddressUtil.getIpAddr(request));
            //用户名
            SysUser sysUserEntity = UserUtils.getCurrentMember();
            if (null != sysUserEntity) {
                sysLog.setUserId(sysUserEntity.getId());
                sysLog.setUserName(sysUserEntity.getUsername());
            }
            sysLog.setCreateTime(new Date());
            logger.info(getString(sysLog));

            //保存系统日志
            fopSystemOperationLogService.save(sysLog);
        } catch (Exception ex) {
            logger.error("保存系统日志失败");
        }

    }

    public static String getString(Object o) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuffer sb = new StringBuffer();
        sb.append("entity[");
        Field[] farr = o.getClass().getDeclaredFields();
        for (Field field : farr) {
            try {
                field.setAccessible(true);
                if(!ValidatorUtils.empty(field.get(o))){
                    sb.append(field.getName());
                    sb.append("=");
                    if (field.get(o) instanceof Date) {
                        // 日期的处理
                        sb.append(sdf.format(field.get(o)));
                    } else {
                        sb.append(field.get(o));
                    }
                    sb.append("|");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sb.append("]");
        return sb.toString();
    }

}
