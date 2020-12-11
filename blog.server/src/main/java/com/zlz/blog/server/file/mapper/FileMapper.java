package com.zlz.blog.server.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlz.blog.common.entity.file.BlogFile;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020/1/16 10:41
 */
@Mapper
public interface FileMapper extends BaseMapper<BlogFile> {
}
