package com.bmd.core.pojo.po;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;

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
@ApiModel(value = "BmdPicture对象", description = "")
public class BmdPicture implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "头像或者商品图像，1：头像，0：商品图像")
    @TableField("is_u")
    private Boolean u;

    @ApiModelProperty(value = "映射的用户名称")
    private String mapperId;

    @ApiModelProperty(value = "图像地址")
    private String url;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "商品名称")
    private String pid;


    @ApiModelProperty(value = "是否删除，1：已删除，0：未删除")
    @TableField("is_deleted")
    @TableLogic
    private Boolean deleted;


}
