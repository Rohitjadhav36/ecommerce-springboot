package com.ecom.service.impl;

import com.ecom.entity.User;
import com.ecom.repository.UserRepository;
import com.ecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public User saveUser(User user) {
        user.setRole("ROLE_USER");
        user.setIsEnable(true);
        user.setAccountNonLocked(true);
        user.setFailedAttempt(0);

       String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //user.setPassword(user.getPassword());

        User saveUser = userRepository.save(user);
        return saveUser;
    }
    @Override
    public User updateUser(User user) {

        User dbUser = userRepository.findById(user.getId()).get();

        dbUser.setName(user.getName());
        dbUser.setEmail(user.getEmail());
        dbUser.setMobileNumber(user.getMobileNumber());
        dbUser.setAddress(user.getAddress());
        dbUser.setCity(user.getCity());
        dbUser.setState(user.getState());
        dbUser.setPincode(user.getPincode());

        return userRepository.save(dbUser);
    }
    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getUsers(String role) {
        return userRepository.findByRole(role);
    }

    @Override
    public Boolean existsEmail(String email)
    {
       return userRepository.existsByEmail(email);
    }

    @Override
    public User saveAdmin(User user) {
        user.setRole("ROLE_ADMIN");
        user.setIsEnable(true);
        user.setAccountNonLocked(true);
        user.setFailedAttempt(0);
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        User saveUser = userRepository.save(user);
        return saveUser;
    }
    @Override
    public User updateAdmin(User user) {
        User dbUser = userRepository.findById(user.getId()).get();

        dbUser.setName(user.getName());
        dbUser.setEmail(user.getEmail());
        dbUser.setMobileNumber(user.getMobileNumber());
        dbUser.setAddress(user.getAddress());
        dbUser.setCity(user.getCity());
        dbUser.setState(user.getState());
        dbUser.setPincode(user.getPincode());

        return userRepository.save(dbUser);
    }

    @Override
    public Boolean updateAccountStatus(Integer id, Boolean status) {

        Optional<User> findByuser = userRepository.findById(id);

        if (findByuser.isPresent()) {
            User userDtls = findByuser.get();
            userDtls.setIsEnable(status);
            userRepository.save(userDtls);
            return true;
        }

        return false;
    }
    @Override
    public User getUserById(Integer id)
    {
       return userRepository.findById(id).get();
    }
    @Override
    public void updateProfileImage(Integer id, MultipartFile file)  {
        User user=userRepository.findById(id).get();
        String imageName = file.getOriginalFilename();
      if(user == null) return;
        user.setProfileImage(imageName);


     try {
    File saveFile = new ClassPathResource("static/img").getFile();
    Path path = Paths.get(
            saveFile.getAbsolutePath()
                    + File.separator
                    + "profile_img"
                    + File.separator
                    + imageName);

    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
    }
    catch (Exception e)
   {
    e.printStackTrace();
    }
        userRepository.save(user);
    }
    @Override
    public Boolean changePassword(String currentPassword, String newPassword, Principal p)
    {

        String email=p.getName();
        User user=userRepository.findByEmail(email);
        boolean matches = passwordEncoder.matches(currentPassword, user.getPassword());
        if(matches)
        {
          user.setPassword(passwordEncoder.encode(newPassword));
          userRepository.save(user);
          return true;
        }
       return false;
    }

}
