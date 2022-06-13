package com.itBoy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itBoy.entity.Book;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookMapper extends BaseMapper<Book> {
}
