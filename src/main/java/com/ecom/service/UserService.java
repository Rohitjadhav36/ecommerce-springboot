package com.ecom.service;

import com.ecom.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

public interface UserService {

    public User saveUser(User user);

    public User updateUser(User user);

    public User getUserByEmail(String email);

    public List<User> getUsers(String role);

    public Boolean existsEmail(String email);

    public User saveAdmin(User user);

    public User updateAdmin(User user);

    public Boolean updateAccountStatus(Integer id, Boolean status);

    public User getUserById(Integer id);

    public void updateProfileImage(Integer id, MultipartFile file);

    public Boolean changePassword(String currentPassword, String newPassword, Principal p);
}
