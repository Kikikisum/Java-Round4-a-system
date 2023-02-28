package com.server.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.Entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
}
