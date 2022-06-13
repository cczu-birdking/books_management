package com.itBoy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itBoy.entity.Employee;
import com.itBoy.mapper.EmployeeMapper;
import com.itBoy.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
