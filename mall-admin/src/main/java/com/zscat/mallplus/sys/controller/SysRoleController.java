package com.zscat.mallplus.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.sys.entity.SysRole;
import com.zscat.mallplus.sys.service.ISysPermissionService;
import com.zscat.mallplus.sys.service.ISysRoleService;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 后台角色表 前端控制器
 * </p>
 *
 * @author zscat
 * @since 2019-04-14
 */
@Slf4j
@Api(value="角色管理",description = "",tags={"角色管理"})
@RestController
@RequestMapping("/sys/sysRole")
public class SysRoleController  extends ApiController {
    
    @Resource
    private ISysRoleService sysRoleService;
    @Resource
    private ISysRoleService roleService;
    @Resource
    private ISysPermissionService permissionService;

    @SysLog(MODULE = "sys", REMARK = "根据条件查询所有角色列表")
    @ApiOperation("根据条件查询所有角色列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('sys:role:read')")
    public Object getRoleByPage(SysRole entity,
                                @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize
    ) {
        try {
            return new CommonResult().success(sysRoleService.page(new Page<SysRole>(pageNum, pageSize), new QueryWrapper<>(entity)));
        }catch (Exception e){
            log.error("根据条件查询所有角色列表：%s",e.getMessage(), e);
        }
        return new CommonResult().failed();
    }
    @SysLog(MODULE = "sys", REMARK = "保存角色")
    @ApiOperation("保存角色")
    @PostMapping(value = "/save")
    @PreAuthorize("hasAuthority('sys:role:create')")
    public Object saveRole(@RequestBody SysRole entity) {
        try {
            entity.setId(IdWorker.getId());
            if (sysRoleService.save(entity)){
                return new CommonResult().success();
            }
        }catch (Exception e){
            log.error("保存角色：%s",e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }
    @SysLog(MODULE = "sys", REMARK = "更新角色")
    @ApiOperation("更新角色")
    @PutMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('sys:role:update')")
    public Object updateRole(@RequestBody SysRole entity) {
        try {
            if (sysRoleService.updateById(entity)){
                return new CommonResult().success();
            }
        }catch (Exception e){
            log.error("更新角色：%s",e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }
    @SysLog(MODULE = "sys", REMARK = "删除角色")
    @ApiOperation("删除角色")
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    public Object deleteRole(@ApiParam("角色id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)){
                return new CommonResult().paramFailed("角色id");
            }
            if (sysRoleService.removeById(id)){
                return new CommonResult().success();
            }
        }catch (Exception e){
            log.error("删除角色：%s",e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }
    @SysLog(MODULE = "sys", REMARK = "给角色分配角色")
    @ApiOperation("查询角色明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('sys:role:read')")
    public Object getRoleById(@ApiParam("角色id")@PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)){
                return new CommonResult().paramFailed("角色id");
            }
            SysRole coupon = sysRoleService.getById(id);
            return new CommonResult().success(coupon);
        }catch (Exception e){
            log.error("查询角色明细：%s",e.getMessage(), e);
            return new CommonResult().failed();
        }

    }
    @ApiOperation(value = "批量删除角色")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量删除品牌")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    public Object deleteBatch(@RequestParam("ids") List<Long> ids) {
        boolean count = sysRoleService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }
    
}

