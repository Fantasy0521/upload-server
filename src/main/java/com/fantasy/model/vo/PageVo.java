package com.fantasy.model.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分页对象用于 Mybatis mapper 配合 MybatisPlus分页插件 使用
 */
@Data
@ApiModel(description = "分页对象")
public class PageVo {

    @ApiModelProperty(value = "每页显示页数")
    protected Long size;
    @ApiModelProperty(value = "当前页")
    protected Long current;

    public Page convertPage() {
        Page page = new Page();
        if (size != null) {
            page.setSize(size);
        }
        if (current != null) {
            page.setCurrent(current);
        }
        return page;
    }
}
