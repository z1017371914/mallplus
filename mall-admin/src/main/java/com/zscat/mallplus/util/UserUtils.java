package com.zscat.mallplus.util;

import com.zscat.mallplus.bo.AdminUserDetails;
import com.zscat.mallplus.sys.entity.SysUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @Auther: shenzhuan
 * @Date: 2019/3/30 16:26
 * @Description:
 */
public class UserUtils {
    public static SysUser getCurrentMember() {
        try {
            SecurityContext ctx = SecurityContextHolder.getContext();
            Authentication auth = ctx.getAuthentication();
            AdminUserDetails memberDetails = (AdminUserDetails) auth.getPrincipal();
            return memberDetails.getUmsAdmin();
        }catch (Exception e){
            return new SysUser();
        }
    }
}
