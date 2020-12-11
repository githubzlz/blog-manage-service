package com.zlz.blog.common.entity.file;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zlz.blog.common.entity.common.BaseEntity;
import lombok.Data;

/**
 * 文件实体，保存文件url
 *
 * @author zhulinzhong
 */
@Data
@TableName("blog_file")
public class BlogFile extends BaseEntity {

    private static final long serialVersionUID = 4966885479653303286L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    private Long blogId;

    private String fileName;

    private String fileType;

    private String fileSize;

    private String fileUrl;

    private String md5;
}