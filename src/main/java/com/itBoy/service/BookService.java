package com.itBoy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itBoy.entity.Book;

public interface BookService extends IService<Book> {
    IPage<Book> getPage(int currentPage, int pageSize, Book book);
}
