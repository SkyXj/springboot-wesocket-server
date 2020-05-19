package com.xj.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xj.entity.User;
import com.xj.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value="用户",tags={"用户"})
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/getUser")
    private User getUser(@RequestParam(name="id") Integer id){
        User user = userService.selectById(id);
        return user;
    }

    @GetMapping("/login")
    private User login(@RequestParam(name="username") String username,@RequestParam(name="password") String password){
        Wrapper<User> wrapper=new EntityWrapper<>();
        wrapper.eq("name",username);
        wrapper.eq("password",password);
        User user = userService.selectOne(wrapper);
        return user;
    }
}
