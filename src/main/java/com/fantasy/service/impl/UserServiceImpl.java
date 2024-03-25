package com.fantasy.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fantasy.entity.User;
import com.fantasy.mapper.UserMapper;
import com.fantasy.model.vo.PageVo;
import com.fantasy.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Fantasy0521
 * @since 2023-03-03
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public IPage<User> getUserListByPage(PageVo page) {
        return userMapper.selectUserListByPage(page.convertPage());
    }
}
