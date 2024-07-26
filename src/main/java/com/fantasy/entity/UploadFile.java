package com.fantasy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author Fantasy0521
 * @since 2024-06-17
 */
@TableName("upload_file")
@ApiModel(value = "UploadFile对象", description = "")
@Data
public class UploadFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String fileName;

    private String originFileName;

    private String downloadUrl;

    private String suffix;

    private String type;

    private Date createTime;

    private Long createUser;

    private String dir;



}
