package com.itBoy.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

//登录用户，包括管理员和用户
@TableName("tb_user")
@Data
public class Employee {
    private Long id;
    private String name;
    private String password;
    private Integer status;
    private String image;
    private Integer money;
    private Integer lendmoney;
    @TableField(fill = FieldFill.INSERT)//插入时填充字段
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)//插入和更新时，填充字段
    private LocalDateTime updateTime;
}
