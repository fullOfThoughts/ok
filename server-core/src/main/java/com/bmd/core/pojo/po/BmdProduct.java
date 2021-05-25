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
@ApiModel(value = "BmdProduct对象", description = "")
public class BmdProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "对应着用户名")
    @TableField(fill = FieldFill.UPDATE)
    private String uid;

    @ApiModelProperty(value = "商品名称")
    @TableField(fill = FieldFill.INSERT)
    private String pname;

    @ApiModelProperty(value = "商品分类，1：日用百货，2：电子数码，3：肉禽蛋奶，4：美妆护肤")
    private Integer pcategory;

    @ApiModelProperty(value = "商品价格")
    private Integer pmoney;

    @ApiModelProperty(value = "商品联系人")
    private String pcontact;

    @ApiModelProperty(value = "是否包邮，0：包邮，1：不包邮")
    private Boolean pbaoyou;

    @ApiModelProperty(value = "商品描述")
    private String pdesc;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否删除，0：未删除，1：已删除")
    @TableField("is_deleted")
    @TableLogic
    private Boolean deleted;


}
