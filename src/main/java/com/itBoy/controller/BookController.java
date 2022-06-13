package com.itBoy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itBoy.entity.Book;
import com.itBoy.entity.Common.R;
import com.itBoy.entity.Detail;
import com.itBoy.entity.Employee;
import com.itBoy.entity.Ids;
import com.itBoy.service.BookService;
import com.itBoy.service.DetailService;
import com.itBoy.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private DetailService detailService;
    //展示所有图书信息
//    @GetMapping
//    public R getAll() {
//        return new R(true, bookService.list());
//    }

    //新添图书信息

    @PostMapping
    public R save(@RequestBody Book book) {
        if(book.getPrice()<0){
            return new R(false,"图书价格必须大于0");
        }
        boolean flag = bookService.save(book);
        return new R(flag, flag ? "添加成功^_^" : "添加失败-_-!");
    }

    /**
     * 修改图书信息的第一步
     * 首先做的是根据id查询图书信息
     *
     * @param
     * @return
     */
    @GetMapping("/{id}")
    public R getById(@PathVariable Integer id) {
        return new R(true, bookService.getById(id));
    }

    /**
     * 修改图书信息第二步，将修改好的图书信息到数据库
     *
     * @param book
     * @return
     * @throws IOException
     */
    @PutMapping
    public R update(@RequestBody Book book) {
        if(book.getPrice()<0){
            return new R(false,"图书价格必须大于0");
        }
        boolean flag = bookService.updateById(book);
        return new R(flag, flag ? "修改成功^_^" : "修改失败-_-!");
    }

    /**
     * 删除图书信息
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Integer id) {
        boolean flag = bookService.removeById(id);
        return new R(flag, flag ? "修改成功^_^" : "修改失败-_-!");
    }

    /**
     * 分页查询图书信息
     *
     * @param currentPage
     * @param pageSize
     * @param book
     * @return
     */
    @GetMapping("/{currentPage}/{pageSize}")
    public R getPage(@PathVariable int currentPage, @PathVariable int pageSize, Book book) {

        Page<Book> pageInfo = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Book::getId);
        queryWrapper.like(book.getName() != null, Book::getName, book.getName());
        queryWrapper.like(book.getType() != null, Book::getType, book.getType());
        queryWrapper.like(book.getDescription() != null, Book::getDescription, book.getDescription());
//        进行分页查询
        Page<Book> page = bookService.page(pageInfo, queryWrapper);
        return new R(true, page);
    }


    /**
     * 顾客图书分页查询
     * @param currentPage
     * @param pageSize
     * @param book
     * @return
     */
    @GetMapping("/custom/{currentPage}/{pageSize}")
    public R getPage_custom(@PathVariable int currentPage, @PathVariable int pageSize, Book book) {

        Page<Book> pageInfo = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Book::getId);
        queryWrapper.like(book.getName() != null, Book::getName, book.getName());
        queryWrapper.like(book.getType() != null, Book::getType, book.getType());
        queryWrapper.like(book.getDescription() != null, Book::getDescription, book.getDescription());
        //保证顾客看到的书本信息都是在售卖状态
        queryWrapper.eq(Book::getStatus,1);
//        进行分页查询
        List<Book> list = bookService.list(queryWrapper);
        Page<Book> page = bookService.page(pageInfo, queryWrapper);
        return new R(true, page);
    }

    /**
     * 修改图书状态
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public R updateStatus(@PathVariable int id){
        Book book = bookService.getById(id);
        if(book.getStatus()==1){
            book.setStatus(0);
        }else{
            book.setStatus(1);
        }
        bookService.updateById(book);
        return new R (true,"状态修改成功！");
    }


    /**
     * 购买一本书
     * @param ids
     * @return
     */
    @PutMapping("/buy")
    public R buyBook(@RequestBody Ids ids){
        Long bookId = ids.getBookId();
        Long userId = ids.getUserId();
        //获取用户信息
        Employee user = employeeService.getById(userId);
        //获取图书信息
        Book book = bookService.getById(bookId);
        //首先是判断图书剩余库存
        if(book.getCount()>0){
            //这里的图书全部是售卖状态的，那么我们就要判断客户借书的总价值，就是未出账的价格和余额哪个大
            if(user.getMoney()<(user.getLendmoney()+book.getPrice())){
                return new R(false,"余额不足，请及时充值！");
            }else{
                //用户余额减少！
                user.setMoney(user.getMoney()-book.getPrice());
                //图书库存减一本，卖出数量加一本
                book.setCount(book.getCount()-1);
                book.setBuy(book.getBuy()+1);
                employeeService.updateById(user);
                bookService.updateById(book);
                Detail detail = new Detail();
                detail.setUserId(userId);
                detail.setBookId(bookId);
                detail.setAction("购买图书！");
                detail.setUpdateTime(LocalDateTime.now());
                boolean b = detailService.save(detail);
                return new R(true,user);
            }
        }else{
            return new R(false,"当前图书剩余数量不足！");
        }
    }


    /**
     * 用户借书
     * @param ids
     * @return
     */
    @PutMapping("/lend")
    public R lendBeforeBook(@RequestBody Ids ids){
        Long bookId = ids.getBookId();
        Long userId = ids.getUserId();
        //获取用户信息
        Employee user = employeeService.getById(userId);
        //获取图书信息
        Book book = bookService.getById(bookId);
        //首先是判断图书剩余库存
        if(book.getCount()>0){
            //这里的图书全部是售卖状态的，那么我们就要判断客户借书的总价值，就是未出账的价格和余额哪个大
            if(user.getMoney()<(user.getLendmoney()+book.getPrice())){
                return new R(false,"余额不足，请及时充值！");
            }else{
                //用户余额减少！
                user.setLendmoney(user.getLendmoney()+book.getPrice());
                //图书库存减一本，卖出数量加一本
                book.setCount(book.getCount()-1);
                book.setLend(book.getCount()+1);
                employeeService.updateById(user);
                bookService.updateById(book);
                Detail detail = new Detail();
                detail.setUserId(userId);
                detail.setBookId(bookId);
                detail.setAction("借阅图书！");
                detail.setUpdateTime(LocalDateTime.now());
                boolean b = detailService.save(detail);
                return new R(true,user);
            }
        }else{
            return new R(false,"当前图书剩余数量不足！");
        }
    }


    /**
     * 图书归还
     * @param id
     * @return
     */
    @PutMapping("/back/{id}")
    //传过来的是订单号
    @Transactional
    public R backBook(@PathVariable Integer id){
        Detail detail = detailService.getById(id);
        Long userId = detail.getUserId();
        Long bookId = detail.getBookId();
        //首先对书进行操作,借出图书-1，库存+1
        Book book = bookService.getById(bookId);
        book.setLend(book.getLend()-1);
        book.setCount(book.getCount()+1);
        bookService.updateById(book);
        //其次对用户操作，用户待出账单金额减少，
        Employee employee = employeeService.getById(userId);
        employee.setLendmoney(employee.getLendmoney()-book.getPrice());
        //然后就是借书的费用了
        LocalDateTime createTime = detail.getCreateTime();
        LocalDateTime now = LocalDateTime.now();;
        long hours = ChronoUnit.HOURS.between(createTime, now);
        //给顾客俩小时的余地
        long lendTime = (hours-336)-2;
        int spendMoney;
        if(lendTime>0){
            spendMoney = ((int)lendTime/24)*3 +1;
            //扣费太多就按照图书本来价格减少5元扣
            if(spendMoney> book.getPrice()){
                spendMoney=book.getPrice()-5;
            }
        }else{
            spendMoney=0;
        }
        employee.setMoney(employee.getMoney()-spendMoney);
        employeeService.updateById(employee);
        //其次是对订单信息进行修改
        detail.setAction("图书已经归还！");
        detailService.updateById(detail);
        return new R(true,spendMoney);
    }


    /**
     * 管理员给预约图书的用户借书
     * 这里和借书的不同点在于需要删除原本的
     * @param
     * @return
     */
    @Transactional
    @PutMapping("/lendBefore/{id}")
    public R lendBeforeBook(@PathVariable int id){
        Detail detail1 = detailService.getById(id);
        Long bookId = detail1.getBookId();
        Long userId = detail1.getUserId();
        //获取用户信息
        Employee user = employeeService.getById(userId);
        //获取图书信息
        Book book = bookService.getById(bookId);
        //首先是判断图书剩余库存
        if(book.getCount()>0){
            //这里的图书全部是售卖状态的，那么我们就要判断客户借书的总价值，就是未出账的价格和余额哪个大
            if(user.getMoney()<(user.getLendmoney()+book.getPrice())){
                return new R(false,"余额不足，请及时充值！");
            }else{
                //用户余额减少！
                user.setLendmoney(user.getLendmoney()+book.getPrice());
                //图书库存减一本，卖出数量加一本
                book.setCount(book.getCount()-1);
                book.setLend(book.getLend()+1);
                employeeService.updateById(user);
                bookService.updateById(book);
                Detail detail = new Detail();
                detail.setUserId(userId);
                detail.setBookId(bookId);
                detail.setAction("借阅图书！");
                detail.setUpdateTime(LocalDateTime.now());
                boolean b = detailService.save(detail);
                detailService.removeById(id);
                return new R(true,user);
            }
        }else{
            return new R(false,"当前图书剩余数量不足！");
        }
    }

    /**
     * 用户预约图书
     * @param
     * @return
     */
    @Transactional
    @PutMapping("/customLendBefore")
    public R lendBefore(@RequestBody Ids ids){
        Long bookId = ids.getBookId();
        Long userId = ids.getUserId();
        //获取用户信息
        Employee user = employeeService.getById(userId);
        //获取图书信息
        Book book = bookService.getById(bookId);
        //首先是判断图书剩余库存
        if(book.getCount()>0){
            //这里的图书全部是售卖状态的，那么我们就要判断客户借书的总价值，就是未出账的价格和余额哪个大
            if(user.getMoney()<(user.getLendmoney()+book.getPrice())){
                return new R(false,"余额不足，请及时充值！");
            }else{
                //用户余额减少！
                user.setLendmoney(user.getLendmoney()+book.getPrice());
                //图书库存减一本，卖出数量加一本
                book.setCount(book.getCount()-1);
                book.setLend(book.getCount()+1);
                employeeService.updateById(user);
                bookService.updateById(book);
                Detail detail = new Detail();
                detail.setUserId(userId);
                detail.setBookId(bookId);
                detail.setAction("预约图书！");
                detail.setUpdateTime(LocalDateTime.now());
                boolean b = detailService.save(detail);
                return new R(true,user);
            }
        }else{
            return new R(false,"当前图书剩余数量不足！");
        }
    }


}
