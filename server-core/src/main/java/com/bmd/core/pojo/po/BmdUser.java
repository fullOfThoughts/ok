package com.bmd.core.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author ZS
 * @since 2021-04-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "BmdUser对象", description = "")
public class BmdUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户唯一标识")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "用户名，要求唯一")
    private String userName;

    @ApiModelProperty(value = "性别，1：男，0：女")
    private Integer gender;

    @ApiModelProperty(value = "是否结婚，1：已结婚，0：未结婚")
    @TableField("is_marry")
    private Integer marry;

    @ApiModelProperty(value = "角色，0：普通用户，1：管理员")
    @TableField()
    private Integer role;

    @ApiModelProperty(value = "注册时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否删除，1：已删除，0：未删除")
    @TableField("is_deleted")
    @TableLogic
    private Boolean deleted;

    @TableField(exist = false)
    private String verifyCode;
    @TableField(exist = false)
    private String uuid;


}
