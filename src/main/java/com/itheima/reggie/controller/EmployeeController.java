package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @PostMapping("/login")
    public R<Employee> logIn(HttpServletRequest request, @RequestBody Employee employee){
        //加密密码
       String password = employee.getPassword();
       password= DigestUtils.md5DigestAsHex(password.getBytes());
       //数据库查询用户
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp=employeeService.getOne(queryWrapper);
        //查询是否存在
       if(emp==null){
           return R.error("用户名不存在");
       }
        if(!emp.getPassword().equals(password)){
            return R.error("密码错误");
        }
        if(emp.getStatus()==0){
            return R.error("用户已禁用");
        }
        //密码正确
        String a=request.getRequestURI();
        String b= request.getAuthType();
        int c=request.getServerPort();
        String d=request.getRemoteAddr();

        log.info("获取用户数据");
        log.info(a);
        log.info(b);
        log.info(""+c);
        log.info(d);
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.removeAttribute( "employee");
        return R.success("Logged out successfully");
    }
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工，员工信息：{}",employee.toString());
       String password= DigestUtils.md5DigestAsHex("123456".getBytes());
        employee.setPassword(password);
       // employee.setStatus(1);
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        Long id=(Long)request.getSession().getAttribute("employee");
       // employee.setId(id);
        employee.setCreateUser(id);
        employee.setUpdateUser(id);
        employeeService.save(employee);
        //log.info("新增员工成功");
        return R.success("员工新增成功");
    }
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("开始分页查询");
        log.info("page={},pageSize={},name={}", page, pageSize, name);
        //构造分页构造器
        Page pageInfo=new Page(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper();
        //添加过滤条件
        queryWrapper.like(!StringUtils.isEmpty(name),Employee::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

};
