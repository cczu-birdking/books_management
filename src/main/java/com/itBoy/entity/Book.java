package com.itBoy.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

//图书类，
@TableName("tb_book")
@Data
public class Book {
    private Long id;
    private String type;
    private String name;
    private String description;
    private Integer count;
    private Integer lend;
    private Integer buy;
    private Integer price;
    private String image;
    private Integer status;

    @TableField(fill = FieldFill.INSERT)//插入时填充字段
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)//插入和更新时，填充字段
    private LocalDateTime updateTime;
}
