package com.zlz.blog.server.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.zlz.fastdfs.config.FastdfsConfig;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

/**
 * mybatis-plus配置类
 *
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020/1/16 10:27
 */
@EnableTransactionManagement
@Configuration
public class CommonConfig {

    private static final String DATE_TIME_FORMATTER = "yyyy-MM-dd HH:mm:ss";

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // paginationInterceptor.setLimit(你的最大单页限制数量，默认 500 条，小于 0 如 -1 不受限制);
        return paginationInterceptor;
    }

    /**
     * 解决Jackson导致Long型数据精度丢失问题
     *
     * @return Jackson2ObjectMapperBuilderCustomizer
     */
    @Bean("jackson2ObjectMapperBuilderCustomizer")
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder
                .serializerByType(Long.class, ToStringSerializer.instance)
                .serializerByType(Long.TYPE, ToStringSerializer.instance);
//                .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(
//                        DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER)))
//                .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(
//                        DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER)));
    }

    /**
     * Fastdfs配置文件地址
     *
     * @return
     */
    @Bean
    @Primary
    public FastdfsConfig getFastdfsConfig(){
        FastdfsConfig fastdfsConfig = new FastdfsConfig();
        fastdfsConfig.setConfigFile("/dev/blog-server/fdfs_client.conf");
        return fastdfsConfig;
    }

}
