package com.ecom.controller;

import com.ecom.dto.UpdatePasswordDto;
import com.ecom.dto.UserDto;
import com.ecom.entity.ProductOrder;
import com.ecom.entity.User;
import com.ecom.service.OrderService;
import com.ecom.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.format.annotation.DateTimeFormat;
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
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    @ModelAttribute
    public void getUserDetails(Principal p, Model m) {
        if (p != null) {
            String email = p.getName();
            User user = userService.getUserByEmail(email);
            m.addAttribute("user", user);
        }
    }
    @RequestMapping("/")
    public String adminLogin(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                 LocalDate orderDate, Model model)
    {
        List<ProductOrder> orders=null;
        if(orderDate == null) {
            orders = orderService.getAllOrders();
        }
        else
        {
            orders = orderService.getOrdersByDate(orderDate);
        }
        model.addAttribute("orders", orders);
        return "admin/index";
    }

    @GetMapping("/add-admin")
    public String loadAdminAdd() {
        return "/admin/add_admin";
    }

    /*@PostMapping("/save-admin")
    public String saveAdmin(@ModelAttribute User user1, @RequestParam(value = "img",required = false) MultipartFile file, HttpSession session)
            throws IOException {

        String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
        user1.setProfileImage(imageName);
        User saveUser = userService.saveAdmin(user1);

        if (!ObjectUtils.isEmpty(saveUser)) {
            if (!file.isEmpty()) {
                File saveFile = new ClassPathResource("static/img").getFile();

                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" + File.separator
                        + file.getOriginalFilename());

//				System.out.println(path);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            session.setAttribute("succMsg", "Register successfully");
        } else {
            session.setAttribute("errorMsg", "something wrong on server");
        }

        return "redirect:/admin/add-admin";
    }*/

    @PostMapping("/save-admin")
    public String saveAdmin(@ModelAttribute UserDto userDto,
                            @RequestParam(value = "img", required = false) MultipartFile file,
                            HttpSession session) throws IOException {

        if (!userDto.getPassword().equals(userDto.getCpassword())) {
            session.setAttribute("errorMsg", "Passwords do not match");
            return "redirect:/admin/add-admin";
        }

        User user = new User();

        user.setName(userDto.getName());
        user.setMobileNumber(userDto.getMobileNumber());
        user.setEmail(userDto.getEmail());
        user.setAddress(userDto.getAddress());
        user.setCity(userDto.getCity());
        user.setState(userDto.getState());
        user.setPincode(userDto.getPincode());
        user.setPassword(userDto.getPassword());

        String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
        user.setProfileImage(imageName);

        User saveUser = userService.saveAdmin(user);

        // image save logic...
        if (!ObjectUtils.isEmpty(saveUser)) {
            if (!file.isEmpty()) {
                File saveFile = new ClassPathResource("static/img").getFile();

                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" + File.separator
                        + file.getOriginalFilename());

//				System.out.println(path);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            session.setAttribute("succMsg", "Register successfully");
        } else {
            session.setAttribute("errorMsg", "something wrong on server");
        }

        return "redirect:/admin/add-admin";
    }
    @GetMapping("/users")
    public String getAllUsers(Model m, @RequestParam Integer type) {
        List<User> users = null;
        if (type == 1) {
            users = userService.getUsers("ROLE_USER");
        } else {
            users = userService.getUsers("ROLE_ADMIN");
        }
        m.addAttribute("userType",type);
        m.addAttribute("users", users);
        return "/admin/users";
    }

    @GetMapping("/updateSts")
    public String updateUserAccountStatus(@RequestParam Boolean status, @RequestParam Integer id,@RequestParam Integer type, HttpSession session) {
        Boolean f = userService.updateAccountStatus(id, status);
        if (f) {
            session.setAttribute("succMsg", "Account Status Updated");
        } else {
            session.setAttribute("errorMsg", "Something wrong on server");
        }
        return "redirect:/admin/users?type="+type;
    }
    @GetMapping("/user/{id}")
    public String userDetails(@PathVariable Integer id, Model model){

        User user = userService.getUserById(id);

        model.addAttribute("userInfo", user);

        return "admin/user_details";
    }
    @RequestMapping("/profile")
   public String getProfile()
    {
        return "admin/profile";
    }
    @RequestMapping("/edit-profile")
    public String updateProfile()
    {
        return "admin/edit_profile";
    }
    @PostMapping("/update-profile")
    public String saveUpdate(@ModelAttribute User user)
    {
        User saveUser = userService.updateAdmin(user);
        return "admin/profile";
    }
    @PostMapping("/update-profile-image")
    public String updateProfileImage(
            @RequestParam Integer id,
            @RequestParam("file") MultipartFile file,
            HttpSession session) throws IOException {

        userService.updateProfileImage(id, file);

        session.setAttribute("succMsg",
                "Profile image updated successfully");

        return "redirect:/admin/profile";
    }

    @GetMapping("/change-password")
    public String changePassword()
    {
        return "admin/change_password";
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
        return "redirect:/admin/change-password";
    }

}
