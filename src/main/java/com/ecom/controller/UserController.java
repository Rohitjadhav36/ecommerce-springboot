package com.ecom.controller;

import com.ecom.dto.OrderDto;
import com.ecom.dto.UpdatePasswordDto;
import com.ecom.entity.*;
import com.ecom.service.CartService;
import com.ecom.service.ProductService;
import com.ecom.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;

    @ModelAttribute
    public void getUserDetails(Principal p, Model m) {
        if (p != null) {
            String email = p.getName();
            User user = userService.getUserByEmail(email);
            m.addAttribute("user", user);
            Integer countCart = cartService.getCountCart(user.getId());
            m.addAttribute("countCart", countCart);
        }
    }
    @GetMapping("/buy-now")
    public String byNowProduct(@RequestParam Integer pid,Model model) {
        Product product= productService.getProductById(pid);
        Double orderPrice=product.getPrice();
        Double totalOrderPrice=orderPrice+ 250 + 100;
        model.addAttribute("orderPrice", orderPrice);
        model.addAttribute("totalOrderPrice", totalOrderPrice);
        return "/user/order";
    }
    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute User user, @RequestParam("img") MultipartFile file, HttpSession session)
            throws IOException {

        Boolean existsEmail = userService.existsEmail(user.getEmail());

        if (existsEmail) {
            session.setAttribute("errorMsg", "Email already exist");
        } else {
            String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
            user.setProfileImage(imageName);
            User saveUser = userService.saveUser(user);

            if (!ObjectUtils.isEmpty(saveUser)) {
                if (!file.isEmpty()) {
                    File saveFile = new ClassPathResource("static/img").getFile();

                    Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" + File.separator
                            + file.getOriginalFilename());

                    System.out.println(path);
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                }
                session.setAttribute("succMsg", "Register successfully");
            } else {
                session.setAttribute("errorMsg", "something wrong on server");
            }
        }

        return "redirect:/register";
    }
    @GetMapping("/profile")
    public String viewProfile()
    {
        return "/user/profile";
    }

    @RequestMapping("/edit-profile")
    public String updateProfile()
    {
        return "user/edit_profile";
    }
    @PostMapping("/update-profile")
    public String saveUpdate(@ModelAttribute User user)
    {
        User saveUser = userService.updateUser(user);
        return "user/profile";
    }
    @PostMapping("/update-profile-image")
    public String updateProfileImage(
            @RequestParam Integer id,
            @RequestParam("file") MultipartFile file,
            HttpSession session) throws IOException {

        userService.updateProfileImage(id, file);

        session.setAttribute("succMsg",
                "Profile image updated successfully");

        return "redirect:/users/profile";
    }

    @GetMapping("/change-password")
    public String changePassword()
    {
        return "user/change_password";
    }
    @PostMapping("/save-updated-password")
    public String savePassword(@ModelAttribute UpdatePasswordDto updatePasswordDto, Principal p, HttpSession session) {

        if(updatePasswordDto.getNewPassword().equals(updatePasswordDto.getConfirmPassword())) {

            if(userService.changePassword(updatePasswordDto.getCurrentPassword(),
                    updatePasswordDto.getNewPassword(),p))
            {
                session.setAttribute("succMsg", "Password Updated sucessfully");
            }
            else
            {
                session.setAttribute("errorMsg", "Current Password incorrect");
            }
        }
        else {
            session.setAttribute("errorMsg", "Passwords do not match. Please try again.");
        }
        return "redirect:/users/change-password";
    }

}
