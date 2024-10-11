package com.fantasy.controller;

import com.fantasy.entity.User;
import com.fantasy.model.Result.Result;
import com.fantasy.service.IUserService;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping({ "/common/user" })
@ApiSupport(author = "fantasy0521")
@Api(tags = "user")
public class UserController {


    @Autowired
    private IUserService userService;

    @Autowired
    private HttpSession session;

    @GetMapping("/getCurrentUser")
    @ApiOperation(value = "获取当前登录用户",notes = "1")
    public Result getCurrentUser(){
        User user = userService.getCurrentUser();
        user.setPassword("bf4a54197b23998d595c4ac6fb6ebaec");
        return Result.ok("获取成功",user);
    }


    @PostMapping("login")
    @ApiOperation(value = "login",notes = "1")
    public Result login(@RequestBody User user) {
        User currentUser = userService.login(user);
        return Result.ok("登陆成功",currentUser);
    }
    @PostMapping("register")
    @ApiOperation(value = "register",notes = "1")
    public Result register(@RequestBody User user) {
        User currentUser = userService.register(user);
        return Result.ok("注册成功",currentUser);
    }


}
