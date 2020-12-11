package com.zlz.blog.server.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlz.blog.common.entity.article.BlogTagClasses;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020-07-07 11:25
 * @description 标签类型mapper
 */
@Mapper
public interface ArticleTagClassesMapper extends BaseMapper<BlogTagClasses> {
}
