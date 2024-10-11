package com.fantasy.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fantasy.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fantasy.model.vo.PageVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Fantasy0521
 * @since 2023-03-03
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    IPage<User> selectUserListByPage(Page<User> page);
}
