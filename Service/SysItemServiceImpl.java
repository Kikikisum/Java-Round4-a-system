package com.server.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.Entity.SysItem;
import com.server.Entity.SysUser;
import com.server.Mapper.SysItemMapper;
import com.server.Service.SysItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysItemServiceImpl extends ServiceImpl<SysItemMapper, SysItem> implements SysItemService {

    @Autowired
    SysItemMapper sysItemMapper;

    @Override
    public int addOneItem(SysItem item) {
        return sysItemMapper.insert(item);
    }
}
