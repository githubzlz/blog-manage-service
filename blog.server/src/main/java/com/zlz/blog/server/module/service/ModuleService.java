package com.zlz.blog.server.module.service;

import com.zlz.blog.common.entity.module.Module;
import com.zlz.blog.common.response.ResultSet;

/**
 * created by zlz on 2020/12/21 9:58
 **/
public interface ModuleService {

    /**
     * 分页查询，模糊查询
     * @param module
     * @return
     */
    ResultSet<Module> getPageList(Module module);
}
