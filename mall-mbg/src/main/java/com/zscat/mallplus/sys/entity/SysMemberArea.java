package com.zscat.mallplus.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zscat
 * @since 2019-04-14
 */
@TableName("sys_member_area")
public class SysMemberArea implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("member_id")
    private Long memberId;

    @TableField("obj_id")
    private Long objId;

    /**
     * 1 会员区域 2 会员学校
     */
    private String type;


    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getObjId() {
        return objId;
    }

    public void setObjId(Long objId) {
        this.objId = objId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SysMemberArea{" +
        ", memberId=" + memberId +
        ", objId=" + objId +
        ", type=" + type +
        "}";
    }
}
