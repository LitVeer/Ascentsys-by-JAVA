package com.ascent.repository;

import java.util.HashMap;

import com.ascent.bean.User;

public interface UserDataAccessor {

    // 添加用户
    public boolean addUser(User user);

    // 删除用户
    public boolean deleteUser(String username);

    // 更新用户密码权限
    public boolean updateUser(User user);

    // 获取所有用户信息
    public HashMap<String, User> getUsers();
    
}
