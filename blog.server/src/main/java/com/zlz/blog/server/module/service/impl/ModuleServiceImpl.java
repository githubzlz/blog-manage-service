package com.zlz.blog.server.module.service.impl;

import com.zlz.blog.common.entity.module.Module;
import com.zlz.blog.common.response.ResultSet;
import com.zlz.blog.server.module.mapper.ModuleMapper;
import com.zlz.blog.server.module.service.ModuleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * created by zlz on 2020/12/21 9:59
 **/
@Service
public class ModuleServiceImpl implements ModuleService {

    @Resource
    private ModuleMapper moduleMapper;

    @Override
    public ResultSet<Module> getPageList(Module module) {



//        moduleMapper.selectPage();
        return null;
    }
}
