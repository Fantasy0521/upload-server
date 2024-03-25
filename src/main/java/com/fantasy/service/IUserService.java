package com.fantasy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fantasy.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fantasy.model.vo.PageVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Fantasy0521
 * @since 2023-03-03
 */
public interface IUserService extends IService<User> {

    IPage<User> getUserListByPage(PageVo page);
}
