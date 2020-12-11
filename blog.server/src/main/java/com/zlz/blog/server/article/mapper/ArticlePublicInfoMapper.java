package com.zlz.blog.server.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlz.blog.common.entity.article.BlogPublicInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020-05-26 11:57
 * @description 文章信息mapper
 */
@Mapper
public interface ArticlePublicInfoMapper extends BaseMapper<BlogPublicInfo> {
}
