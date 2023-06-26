package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.mapper.EmployeeMapper;
import com.itheima.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>implements EmployeeService {
    @Override
    @Transactional
    public boolean save(Employee employee) {
        return SqlHelper.retBool(this.getBaseMapper().insert(employee));
    }
}
