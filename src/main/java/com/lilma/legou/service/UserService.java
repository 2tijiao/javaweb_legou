package com.lilma.legou.service;


import com.lilma.legou.pojo.User;

public interface UserService {
    User findByName(String name);


    void register(String username, String pwd, String email, Integer age, String location);

    void cancel(Integer id);

    void loginRecord(Integer id, String clientIp);

    void logout(Integer id);
}
