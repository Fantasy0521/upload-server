package com.fantasy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fantasy.entity.User;
import com.fantasy.model.Result.Result;
import com.fantasy.model.vo.PageVo;
import com.fantasy.service.IUserService;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({ "/common/user" })
@ApiSupport(author = "fantasy0521")
@Api(tags = "用户接口")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("/getUserById")
    @ApiOperation(value = "根据id查询用户",notes = "1")
    public Result getUserById(Long id){
        User byId = userService.getById(id);
        return Result.ok("查询成功",byId);
    }

    @GetMapping("/getUserPage")
    @ApiOperation(value = "分页查询用户列表",notes = "2")
    public Result getUserPage(PageVo page){
        IPage<User> byPage = userService.getUserListByPage(page);
        return Result.ok("查询成功",byPage);
    }

}
