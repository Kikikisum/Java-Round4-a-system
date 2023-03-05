package com.server.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.Entity.SysItem;

public interface SysItemService extends IService<SysItem> {

    public int  addOneItem(SysItem item);
}
