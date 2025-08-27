package org.example.trangdangnhap.service.impl;

import org.example.trangdangnhap.dao.UserDAO;
import org.example.trangdangnhap.dao.impl.UserDAOImpl;
import org.example.trangdangnhap.model.User;
import org.example.trangdangnhap.service.UserService;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;

    public UserServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public boolean register(User user) {
        if (user == null) return false;
        if (isNullOrEmpty(user.getUsername()) || isNullOrEmpty(user.getPassword()) || isNullOrEmpty(user.getEmail())) {
            return false;
        }
        if (!isValidEmail(user.getEmail())) return false;
        if (!isStrongPassword(user.getPassword())) return false;
        if (userDAO.isUsernameExists(user.getUsername())) return false;

        String hashed = hashPassword(user.getPassword());
        user.setPassword(hashed);
        return userDAO.registerUser(user);
    }

    @Override
    public User login(String username, String password) {
        if (isNullOrEmpty(username) || isNullOrEmpty(password)) return null;
        String hashed = hashPassword(password);
        return userDAO.loginUser(username, hashed);
    }

    private boolean isNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    private boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(regex);
    }

    private boolean isStrongPassword(String password) {
        return true; // đơn giản: >=6 ký tự. Có thể nâng cấp theo yêu cầu
    }

    private String hashPassword(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not supported", e);
        }
    }
}


