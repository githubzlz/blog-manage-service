package com.zlz.blog.server.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlz.blog.common.entity.article.BlogRecommend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020-08-11 14:45
 * @description
 */
@Mapper
public interface BlogRecommendMapper extends BaseMapper<BlogRecommend> {

    /**
     * 批量插入
     * @param list
     * @return
     */
    Integer insertList(@Param("list")List<BlogRecommend> list);
}
