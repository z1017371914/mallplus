package com.zscat.mallplus.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.sys.entity.SysPermission;
import com.zscat.mallplus.sys.service.ISysPermissionService;
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
 * 后台权限表 前端控制器
 * </p>
 *
 * @author zscat
 * @since 2019-04-14
 */
@Slf4j
@Api(value="权限管理",description = "",tags={"权限管理"})
@RestController
@RequestMapping("/sys/sysPermission")
public class SysPermissionController  extends ApiController {

    @Resource
    private ISysPermissionService sysPermissionService;


    @SysLog(MODULE = "sys", REMARK = "根据条件查询所有权限列表")
    @ApiOperation("根据条件查询所有权限列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('sys:permission:read')")
    public Object getPermissionByPage(SysPermission entity,
                                @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize
    ) {
        try {
            return new CommonResult().success(sysPermissionService.page(new Page<SysPermission>(pageNum, pageSize), new QueryWrapper<>(entity)));
        }catch (Exception e){
            log.error("根据条件查询所有权限列表：%s",e.getMessage(), e);
        }
        return new CommonResult().failed();
    }
    @SysLog(MODULE = "sys", REMARK = "保存权限")
    @ApiOperation("保存权限")
    @PostMapping(value = "/save")
    @PreAuthorize("hasAuthority('sys:permission:create')")
    public Object savePermission(@RequestBody SysPermission entity) {
        try {
            if (sysPermissionService.save(entity)){
                return new CommonResult().success();
            }
        }catch (Exception e){
            log.error("保存权限：%s",e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }
    @SysLog(MODULE = "sys", REMARK = "更新权限")
    @ApiOperation("更新权限")
    @PutMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('sys:permission:update')")
    public Object updatePermission(@RequestBody SysPermission entity) {
        try {
            if (sysPermissionService.updateById(entity)){
                return new CommonResult().success();
            }
        }catch (Exception e){
            log.error("更新权限：%s",e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }
    @SysLog(MODULE = "sys", REMARK = "删除权限")
    @ApiOperation("删除权限")
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('sys:permission:delete')")
    public Object deletePermission(@ApiParam("权限id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)){
                return new CommonResult().paramFailed("权限id");
            }
            if (sysPermissionService.removeById(id)){
                return new CommonResult().success();
            }
        }catch (Exception e){
            log.error("删除权限：%s",e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }
    @SysLog(MODULE = "sys", REMARK = "给权限分配权限")
    @ApiOperation("查询权限明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('sys:permission:read')")
    public Object getPermissionById(@ApiParam("权限id")@PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)){
                return new CommonResult().paramFailed("权限id");
            }
            SysPermission coupon = sysPermissionService.getById(id);
            return new CommonResult().success(coupon);
        }catch (Exception e){
            log.error("查询权限明细：%s",e.getMessage(), e);
            return new CommonResult().failed();
        }

    }
    @ApiOperation(value = "批量删除权限")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量删除品牌")
    @PreAuthorize("hasAuthority('sys:permission:delete')")
    public Object deleteBatch(@RequestParam("ids") List<Long> ids) {
        boolean count = sysPermissionService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

}

