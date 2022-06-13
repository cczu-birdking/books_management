package com.itBoy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itBoy.entity.Book;
import com.itBoy.entity.Common.R;
import com.itBoy.entity.Employee;
import com.itBoy.entity.User;
import com.itBoy.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @Value("${host6.name}")
    private String username;
    @Value("${host6.password}")
    private String userpassword;
    //管理员登录专用接口
    @PostMapping("/login000")
    public R log(HttpServletRequest request,@RequestBody User user){
        //接收管理员的账号密码
        String name = user.getName();
        String password = user.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        if(name.equals(username) && password.equals(userpassword)){
            request.getSession().setAttribute("employee",1L);
            User user1 = new User();
            user1.setName(name);
            user1.setPassword("6666666");
            user1.setId(1L);
            return new R(true,user1);
        }else {
            return new R(false,"不是管理员，请退出该界面");
        }

    }



    /**
     * 用户注册
     * @return
     */
    @PostMapping
    public R add(@RequestBody Employee employee){
        String password = employee.getPassword();
        String name = employee.getName();
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getName,name);
        Employee user = employeeService.getOne(queryWrapper);
        if(user!=null){
            return new R(false,"账号已经存在");
        }
        employee.setPassword(DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)));
        boolean flag = employeeService.save(employee);
        return new R(flag, flag ? "添加成功^_^" : "添加失败-_-!");
    }


    /**
     * 修改用户密码
     * @param employee
     * @return
     */
    @PutMapping("/psd")
    public R updatepsd(@RequestBody Employee employee){
        String password = employee.getPassword();
        if(password.length()<6){
            return new R(false,"密码必须为6位数以上");
        }
        employee.setPassword(DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)));
        boolean flag = employeeService.updateById(employee);
        return new R(flag, flag ? "添加成功^_^" : "添加失败-_-!");
    }

    /**
     * 用户充值
     * @param employee
     * @return
     */
    @PutMapping("/money")
    public R recharge(@RequestBody Employee employee){
        Long userId = employee.getId();
        Integer addMoney = employee.getMoney();
        if(addMoney<0 ||addMoney>1000){
            return new R(false,"充值金额必须在0-1000内");
        }
        Employee user = employeeService.getById(userId);
        user.setMoney(user.getMoney()+addMoney);
        employeeService.updateById(user);
        return new R(true,user);
    }



    @PutMapping()
    public R update(@RequestBody Employee employee){
        employeeService.updateById(employee);
        return new R(true,employee);
    }


    /**
     * 用户登录专用接口
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R login(HttpServletRequest request, @RequestBody Employee employee){
        //        密码先进行md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
//        根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getName, employee.getName());
        Employee emp = employeeService.getOne(queryWrapper);
//        如果没有查到则返回查询失败
        if (emp == null) {
            return new R(false,"账号输入错误！");
        }

        //密码对比，如果不一致返回失败
        if (!emp.getPassword().equals(password)) {
            return new R(false,"密码输入错误！");
        }

        //查看用户状态
        if (emp.getStatus() == 0) {
            return new R(false,"账号已经被禁用");
        }
        //登录成功,将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee", emp.getId());
        return new R(true,emp);
    }


    @PostMapping("/logout")
    public R logout(HttpServletRequest request) {
        //清理session中保存的当前员工id
        request.removeAttribute("employee");
        return new R(true,"退出成功！");
    }


    /**
     * 分页查询用户信息
     * @param currentPage
     * @param pageSize
     * @param employee
     * @return
     */
    @GetMapping("/{currentPage}/{pageSize}")
    public R getPage(@PathVariable int currentPage, @PathVariable int pageSize, Employee employee) {

        Page<Employee> pageInfo = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Employee::getId);
        queryWrapper.like(employee.getName() != null, Employee::getName, employee.getName());
        queryWrapper.like(employee.getId() != null, Employee::getId,employee.getId());

        List<Employee> list = employeeService.list(queryWrapper);
        Page<Employee> page = employeeService.page(pageInfo, queryWrapper);
        return new R(true, page);
    }

    /**
     * 修改用户信息
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public R updateStatus(@PathVariable int id){
        Employee user = employeeService.getById(id);
        if(user.getStatus()==1){
            user.setStatus(0);
        }else {
            user.setStatus(1);
        }
        employeeService.updateById(user);
        return new R(true,user);
    }

    @GetMapping("/{id}")
    public R getUsers(@PathVariable int id){
        Employee user = employeeService.getById(id);
        return new R(true,user);
    }

}
