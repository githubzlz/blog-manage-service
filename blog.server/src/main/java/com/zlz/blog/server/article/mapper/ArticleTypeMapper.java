package com.zlz.blog.server.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlz.blog.common.entity.article.BlogType;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020-06-24 17:10
 * @description
 */
@Mapper
public interface ArticleTypeMapper extends BaseMapper<BlogType> {
}
