package com.lilma.legou.service.impl;

import com.lilma.legou.mapper.UserMapper;
import com.lilma.legou.pojo.User;
import com.lilma.legou.service.UserService;
import com.lilma.legou.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByName(String name) {
        User u=userMapper.findByName(name);
        return u;
    }

    @Override
    public void register(String username, String pwd, String email, Integer age, String location) {
        userMapper.register(username,pwd,email,age,location);
    }

    @Override
    public void cancel(Integer id) {
        userMapper.cancel(id);
    }

    @Override
    public void loginRecord(Integer id, String clientIp) {
        userMapper.loginRecord(id,clientIp);
    }

    @Override
    public void logout(Integer id) {
        ThreadLocalUtil.remove();
        userMapper.logout(id);
    }


}
