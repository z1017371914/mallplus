package com.zscat.mallplus.sys.mapper;

import com.zscat.mallplus.sys.entity.SysPermission;
import com.zscat.mallplus.sys.entity.SysRole;
import com.zscat.mallplus.sys.entity.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 后台用户和角色关系表 Mapper 接口
 * </p>
 *
 * @author zscat
 * @since 2019-04-14
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    List<SysPermission> getPermissionList(Long adminId);

    List<SysRole> getRoleList(Long adminId);

    List<SysPermission> getRolePermissionListByUserId(Long adminId);
}
