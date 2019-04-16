package com.zscat.mallplus.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.sys.entity.SysPermission;
import com.zscat.mallplus.sys.entity.SysRole;
import com.zscat.mallplus.sys.entity.SysUser;
import com.zscat.mallplus.sys.service.ISysPermissionService;
import com.zscat.mallplus.sys.service.ISysRoleService;
import com.zscat.mallplus.sys.service.ISysUserService;
import com.zscat.mallplus.util.UserUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台用户表 前端控制器
 * </p>
 *
 * @author zscat
 * @since 2019-04-14
 */
@Slf4j
@Api(value="用户管理",description = "",tags={"用户管理"})
@RestController
@RequestMapping("/sys/sysUser")
public class SysUserController  extends ApiController {

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ISysRoleService roleService;
    @Resource
    private ISysPermissionService permissionService;

    @SysLog(MODULE = "sys", REMARK = "根据条件查询所有用户列表")
    @ApiOperation("根据条件查询所有用户列表")
    @GetMapping(value = "/list")
    public Object getUserByPage(SysUser entity,
                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize
    ) {
        try {
            return new CommonResult().success(sysUserService.page(new Page<SysUser>(pageNum, pageSize), new QueryWrapper<>(entity)));
        }catch (Exception e){
            log.error("根据条件查询所有用户列表：%s",e.getMessage(), e);
        }
        return new CommonResult().failed();
    }
    @SysLog(MODULE = "sys", REMARK = "保存用户")
    @ApiOperation("保存用户")
    @PostMapping(value = "/save")
    public Object saveUser(@RequestBody SysUser entity) {
        try {
            if (sysUserService.save(entity)){
                return new CommonResult().success();
            }
        }catch (Exception e){
            log.error("保存用户：%s",e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }
    @SysLog(MODULE = "sys", REMARK = "更新用户")
    @ApiOperation("更新用户")
    @PutMapping(value = "/update/{id}")
    public Object updateUser(@RequestBody SysUser entity) {
        try {
            if (sysUserService.updateById(entity)){
                return new CommonResult().success();
            }
        }catch (Exception e){
            log.error("更新用户：%s",e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }
    @SysLog(MODULE = "sys", REMARK = "删除用户")
    @ApiOperation("删除用户")
    @DeleteMapping(value = "/delete/{id}")
    public Object deleteUser(@ApiParam("用户id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)){
                return new CommonResult().paramFailed("用户id");
            }
            if (sysUserService.removeById(id)){
                return new CommonResult().success();
            }
        }catch (Exception e){
            log.error("删除用户：%s",e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }
    @SysLog(MODULE = "sys", REMARK = "给用户分配角色")
    @ApiOperation("查询用户明细")
    @GetMapping(value = "/{id}")
    public Object getUserById(@ApiParam("用户id")@PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)){
                return new CommonResult().paramFailed("用户id");
            }
            SysUser coupon = sysUserService.getById(id);
            return new CommonResult().success(coupon);
        }catch (Exception e){
            log.error("查询用户明细：%s",e.getMessage(), e);
            return new CommonResult().failed();
        }

    }
    @SysLog(MODULE = "sys", REMARK = "刷新token")
    @ApiOperation(value = "刷新token")
    @RequestMapping(value = "/token/refresh", method = RequestMethod.GET)
    @ResponseBody
    public Object refreshToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String refreshToken = sysUserService.refreshToken(token);
        if (refreshToken == null) {
            return new CommonResult().failed();
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return new CommonResult().success(tokenMap);
    }

    @SysLog(MODULE = "sys", REMARK = "登录以后返回token")
    @ApiOperation(value = "登录以后返回token")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Object login(@RequestBody SysUser umsAdminLoginParam, BindingResult result) {
        String token = sysUserService.login(umsAdminLoginParam.getUsername(), umsAdminLoginParam.getPassword());
        if (token == null) {
            return new CommonResult().paramFailed("用户名或密码错误");
        }
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        tokenMap.put("menus",permissionService.getPermissionsByUserId(UserUtils.getCurrentMember().getId()));
        return new CommonResult().success(tokenMap);
    }

    @SysLog(MODULE = "sys", REMARK = "给用户分配角色")
    @ApiOperation("给用户分配角色")
    @RequestMapping(value = "/role/update", method = RequestMethod.POST)
    @ResponseBody
    public Object updateRole(@RequestParam("adminId") Long adminId,
                             @RequestParam("roleIds") List<Long> roleIds) {
        int count = sysUserService.updateUserRole(adminId, roleIds);
        if (count >= 0) {
            return new CommonResult().success(count);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "获取指定用户的角色")
    @ApiOperation("获取指定用户的角色")
    @RequestMapping(value = "/role/{adminId}", method = RequestMethod.GET)
    @ResponseBody
    public Object getRoleList(@PathVariable Long adminId) {
        List<SysRole> roleList = sysUserService.getRoleListByUserId(adminId);
        return new CommonResult().success(roleList);
    }

    @SysLog(MODULE = "sys", REMARK = "获取指定用户的角色")
    @ApiOperation("获取指定用户的角色")
    @RequestMapping(value = "/userRoleCheck", method = RequestMethod.GET)
    @ResponseBody
    public Object userRoleCheck(@RequestParam("adminId") Long adminId) {
        List<SysRole> roleList = sysUserService.getRoleListByUserId(adminId);
        List<SysRole> allroleList = roleService.list(new QueryWrapper<>());
        for (SysRole a : allroleList){
            for (SysRole u : roleList){
                if(a.getId().equals(u.getId())){
                    a.setChecked(true);
                }
            }
        }
        return new CommonResult().success(allroleList);
    }
    @SysLog(MODULE = "sys", REMARK = "给用户分配+-权限")
    @ApiOperation("给用户分配+-权限")
    @RequestMapping(value = "/permission/update", method = RequestMethod.POST)
    @ResponseBody
    public Object updatePermission(@RequestParam Long adminId,
                                   @RequestParam("permissionIds") List<Long> permissionIds) {
        int count = sysUserService.updatePermissionByUserId(adminId, permissionIds);
        if (count > 0) {
            return new CommonResult().success(count);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "获取用户所有权限（包括+-权限）")
    @ApiOperation("获取用户所有权限（包括+-权限）")
    @RequestMapping(value = "/permission/{adminId}", method = RequestMethod.GET)
    @ResponseBody
    public Object getPermissionList(@PathVariable Long adminId) {
        List<SysPermission> permissionList = sysUserService.getPermissionListByUserId(adminId);
        return new CommonResult().success(permissionList);
    }
}

