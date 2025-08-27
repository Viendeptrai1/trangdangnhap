package org.example.trangdangnhap.service;

import org.example.trangdangnhap.model.User;

public interface UserService {
    boolean register(User user);

    User login(String username, String password);
}


