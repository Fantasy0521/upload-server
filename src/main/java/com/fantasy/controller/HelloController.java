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
@RequestMapping({ "/common/hello" })
@ApiSupport(author = "fantasy0521")
@Api(tags = "hello")
public class HelloController {

    @GetMapping("/hello")
    @ApiOperation(value = "根据id查询用户",notes = "1")
    public Result hello(){
        return Result.ok("hello");
    }


}
