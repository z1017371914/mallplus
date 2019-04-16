package com.zscat.mallplus.utils;

/**
 * 通用返回对象
 * Created by macro on 2018/4/26.
 */
public class CommonResult {
    //操作成功
    public static final int SUCCESS = 200;
    //操作失败
    public static final int FAILED = 500;
    private int code;
    private String msg;
    private Object data;

    /**
     * 普通成功返回
     *
     * @param data 获取的数据
     */
    public CommonResult success(Object data) {
        this.code = SUCCESS;
        this.msg = "操作成功";
        this.data = data;
        return this;
    }
    /**
     * 普通成功返回
     *
     */
    public CommonResult success() {
        this.code = SUCCESS;
        this.msg = "操作成功";
        this.data =  "操作成功";
        return this;
    }
    /**
     * 普通成功返回
     */
    public CommonResult success(String msg, Object data) {
        this.code = SUCCESS;
        this.msg = msg;
        this.data = data;
        return this;
    }


    /**
     * 普通失败提示信息
     */
    public CommonResult failed() {
        this.code = FAILED;
        this.msg = "操作失败";
        return this;
    }

    public CommonResult failed(String msg) {
        this.code = FAILED;
        this.msg = msg;
        return this;
    }
    /**
     * 普通失败提示信息
     */
    public CommonResult paramFailed() {
        this.code = FAILED;
        this.msg = "参数失败";
        return this;
    }

    public CommonResult paramFailed(String msg) {
        this.code = FAILED;
        this.msg = msg;
        return this;
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getmsg() {
        return msg;
    }

    public void setmsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
