package com.xj.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xj.entity.Customer;
import com.xj.entity.User;
import com.xj.mapper.CustomerMapper;
import com.xj.mapper.UserMapper;
import com.xj.service.CustomerService;
import com.xj.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
