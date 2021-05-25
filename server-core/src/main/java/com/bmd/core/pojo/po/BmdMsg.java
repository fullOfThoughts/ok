package com.bmd.core.pojo.po;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "msg对象", description = "")
public class BmdMsg {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String msg;

    @TableField("msg_to")
    private String msgTo;

    @TableField("msg_from")
    private String msgFrom;

    private LocalDateTime createTime;

    private Boolean isRead;

    @TableField("is_deleted")
    @TableLogic
    private Boolean deleted;

    @TableField(exist = false)
    private String token;

    @TableField(exist = false)
    private List<String> ids;


}
