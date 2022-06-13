package com.itBoy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itBoy.entity.*;
import com.itBoy.entity.Common.R;
import com.itBoy.entity.dto.DetailDto;
import com.itBoy.entity.dto.Dto;
import com.itBoy.service.BookService;
import com.itBoy.service.DetailService;
import com.itBoy.service.EmployeeService;
import com.sun.webkit.dom.CSSPageRuleImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javafx.scene.input.KeyCode.T;

@RestController
@RequestMapping("/detail")
public class DetailController {
    @Autowired
    private BookService bookService;
    @Autowired
    private DetailService detailService;
    @Autowired
    private EmployeeService employeeService;


    /**
     * 根据订单号查询图书id和用户id，返回给前端
     * @return
     */
    @GetMapping("/{id}")
    public R getIds(@PathVariable int id){
        Detail detail = detailService.getById(id);
        Long bookId = detail.getBookId();
        Long userId = detail.getUserId();
        Ids ids = new Ids();
        ids.setBookId(bookId);
        ids.setUserId(userId);
        return new R(true,ids);
    }


    /**
     * 用户查询订单信息
     * @param currentPage
     * @param pageSize
     * @param id
     * @param dto
     * @return
     */
    @GetMapping("/{currentPage}/{pageSize}/{id}")
    public R getPage(@PathVariable int currentPage, @PathVariable int pageSize, @PathVariable int id, Dto dto) {

        int userId = id;
        LambdaQueryWrapper<Detail> queryWrapper = new LambdaQueryWrapper();
        //传过来的userId等于订单信息记录表的userid
        queryWrapper.eq(Detail::getUserId,userId);
        queryWrapper.orderByDesc(Detail::getUpdateTime);

        List<Detail> list = detailService.list(queryWrapper);
        List<DetailDto> detailDtoList=list.stream().map((item) ->{
            DetailDto detailDto= new DetailDto();
            detailDto.setAction(item.getAction());
            detailDto.setId(item.getId());
            detailDto.setUpdateTime(item.getUpdateTime());
            Long bookId = item.getBookId();
            Book book = bookService.getById(bookId);
            detailDto.setName(book.getName());
            detailDto.setType(book.getType());
            detailDto.setImage(book.getImage());
            detailDto.setPrice(book.getPrice());
            return detailDto;
        }).collect(Collectors.toList());

        //执行查询
        return new R(true,detailDtoList);
    }

    @Transactional
    @PutMapping("/check")
    public R rule(){
        int count=0;
        LambdaQueryWrapper<Detail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Detail::getAction,"预约图书！");
        List<Detail> list = detailService.list(lambdaQueryWrapper);
        System.out.println("here is me ");
        System.out.println(list);
        for (Detail item : list) {
            LocalDateTime createTime = item.getCreateTime();
            System.out.println(createTime);
            LocalDateTime now = LocalDateTime.now();;
            long hours = ChronoUnit.HOURS.between(createTime, now);
            System.out.println("here is me ");
            System.out.println(hours);
            if(hours>168){
                item.setAction("预约超时！");
                Long userId = item.getUserId();
                Employee employee = employeeService.getById(userId);
                employee.setMoney(employee.getMoney()-5);
                employeeService.updateById(employee);
                detailService.updateById(item);
                count++;
            }
        }
        if(count>0){
            return new R(true,"注意！有用户未按预定时间借书");
        }
        return new R(true,"");
    }

    /**
     * 查user的订单信息
     * @param currentPage
     * @param pageSize
     * @param id
     * @param dto
     * @return
     */
    @GetMapping("/page/{currentPage}/{pageSize}/{id}")
    public R getAllPage(@PathVariable int currentPage, @PathVariable int pageSize,@PathVariable Integer id ,Dto dto) {
//这里的id是user的id

        LambdaQueryWrapper<Detail> queryWrapper = new LambdaQueryWrapper();
        //传过来的userId等于订单信息记录表的userid
        queryWrapper.orderByDesc(Detail::getUpdateTime);
        //查询，和传过来的id相同
        queryWrapper.eq(id!=0, Detail::getUserId,id);
        queryWrapper.eq(dto.getId()!=null,Detail::getId,dto.getId());
        List<Detail> list = detailService.list(queryWrapper);
        List<DetailDto> list2=list.stream().map((item) ->{
            DetailDto detailDto= new DetailDto();
            detailDto.setAction(item.getAction());
            detailDto.setId(item.getId());
            detailDto.setUpdateTime(item.getUpdateTime());
            LambdaQueryWrapper<Book> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(item.getBookId()!=null, Book::getId,item.getBookId());
            lambdaQueryWrapper.like(dto.getName()!=null,Book::getName,dto.getName());
            Book book = bookService.getOne(lambdaQueryWrapper);
            if(book!=null){
                detailDto.setName(book.getName());
                detailDto.setType(book.getType());
                detailDto.setImage(book.getImage());
                detailDto.setPrice(book.getPrice());
            }else{
                return null;
            }
            Employee user = employeeService.getById(item.getUserId());
            detailDto.setUsername(user.getName());
            return detailDto;
        }).collect(Collectors.toList());

        List<DetailDto> detailDtoList = list2.stream().filter(x -> x!=null).collect(Collectors.toList());
        // 分页代码片段
        // T表示对象实体 list是所要处理的列表数据
        Page<DetailDto> page = new Page<>(currentPage,pageSize);
        page.setTotal(detailDtoList.size());
        // 当前页第一条数据在List中的位置
        int start = (int)((page.getCurrent() - 1) * page.getSize());
        // 当前页最后一条数据在List中的位置
        int end = (int)((start + page.getSize()) >detailDtoList.size() ? detailDtoList.size() : (page.getSize() * page.getCurrent()));
        page.setRecords(new ArrayList<>());
        if (page.getSize()*(page.getCurrent()-1) <= page.getTotal()) {
            // 分隔列表 当前页存在数据时 设置
            page.setRecords(detailDtoList.subList(start, end));
        }

        //执行查询
        return new R(true,page);
    }
}
