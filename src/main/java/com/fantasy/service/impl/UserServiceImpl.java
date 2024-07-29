package com.fantasy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fantasy.entity.User;
import com.fantasy.mapper.UserMapper;
import com.fantasy.model.vo.PageVo;
import com.fantasy.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fantasy.util.CheckEmptyUtil;
import com.fantasy.util.StorageUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;

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

    @Override
    public User getCurrentUser() {
        User currentUser = StorageUserUtil.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("请先登录");
        }
        return currentUser;
    }

    @Override
    public User login(User user) {
        if (!CheckEmptyUtil.checkIfAnyNullOrEmpty(user.getPhone(),user.getPassword())) {
            throw new RuntimeException("请填写完整信息");
        }
        User one = getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, user.getPhone()).eq(User::getPassword,user.getPassword()));
        if (one == null) {
            throw new RuntimeException("手机号或密码错误");
        }
        one.setPassword("bf4a54197b23998d595c4ac6fb6ebaec");
        StorageUserUtil.addUserAndSetCurrentUser(one);
        return one;
    }

    @Override
    public User register(User user) {
        if (!CheckEmptyUtil.checkIfAnyNullOrEmpty(user.getName(),user.getPhone(),user.getPassword())) {
            throw new RuntimeException("请填写完整信息");
        }
        User one = getOne(new LambdaQueryWrapper<User>().eq(User::getName, user.getName()).or().eq(User::getPhone, user.getPhone()));
        if (one != null) {
            if (one.getName().equals(user.getName())){
                throw new RuntimeException("用户名已被使用");
            } else {
                throw new RuntimeException("手机号已被使用");
            }
        }
        user.setCreateBy("1");
        user.setCreateTime(new Date());
        save(user);
        return login(user);
    }
}
