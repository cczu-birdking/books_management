package com.itBoy.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 订单信息表格
 */
@TableName("tb_detail")
@Data
public class Detail {
    private Long id;
    private Long userId;
    private Long bookId;
    private String action;
    @TableField(fill = FieldFill.INSERT)//插入时填充字段
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)//插入和更新时，填充字段
    private LocalDateTime updateTime;
}
