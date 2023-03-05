package com.server;

import com.server.Entity.SysUser;
import com.server.Mapper.SysUserMapper;
import com.server.Service.SysUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ServerApplicationTests {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Test
    void contextLoads() {
        List<SysUser> users =  sysUserMapper.selectList(null);
        System.out.println(users);

    }

}
