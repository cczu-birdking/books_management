package com.itBoy.entity.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用来传输detail信息的，这个比较重要，仔细看
 */
@Data
public class DetailDto {
    private Long id;
    private String image;
    private String type;
    private String name;
    private Integer price;
    private String action;
    private LocalDateTime updateTime;
    private String username;
}
