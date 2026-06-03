package com.ecom.controller;

import com.ecom.dto.OrderDto;
import com.ecom.entity.*;
import com.ecom.service.CartService;
import com.ecom.service.OrderService;
import com.ecom.service.ProductService;
import com.ecom.service.UserService;
import com.ecom.util.OrderStatus;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;
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

    private User getLoggedInUserDetails(Principal p) {
        String email = p.getName();
        User user = userService.getUserByEmail(email);
        return user;
    }

    @GetMapping("/")
    public String orderPage(Principal p, Model m) {
        User user = getLoggedInUserDetails(p);
        List<Cart> carts = cartService.getCartsByUser(user.getId());
        m.addAttribute("carts", carts);
        if (carts.size() > 0) {
            Double orderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
            Double totalOrderPrice = carts.get(carts.size() - 1).getTotalOrderPrice() + 250 + 100;
            m.addAttribute("orderPrice", orderPrice);
            m.addAttribute("totalOrderPrice", totalOrderPrice);
        }
        return "/user/order";
    }

    @PostMapping("/save")
    public String saveOrder(@ModelAttribute OrderDto request, Principal p) throws Exception {
        // System.out.println(request);
        User user = getLoggedInUserDetails(p);
        orderService.saveOrder(user.getId(), request);

        return "redirect:/order/success";
    }
    @GetMapping("/buy-now")
    public String byNowProduct(@RequestParam Integer pid,Integer type,Model model) {
       Product product= productService.getProductById(pid);
        Double orderPrice=product.getPrice();
        Double totalOrderPrice=orderPrice+ 250 + 100;
        model.addAttribute("product",product);
        model.addAttribute("orderPrice", orderPrice);
        model.addAttribute("totalOrderPrice", totalOrderPrice);
        return "/user/single_order";
    }




    @PostMapping("/save-single-order")
    public String saveSingleOrder(@ModelAttribute OrderDto request, @RequestParam Integer pid, Principal p) throws Exception {
        // System.out.println(request);
        User user = getLoggedInUserDetails(p);
        orderService.saveSingleOrder(user.getId(), request, pid);

        return "redirect:/order/success";
    }

    @GetMapping("/success")
    public String loadSuccess() {
        return "/user/success";
    }

    @GetMapping("/view")
    public String myOrder(Model model, Principal p) {
        User loginUser = getLoggedInUserDetails(p);
        List<ProductOrder> orders = orderService.getOrdersByUser(loginUser.getId());
        model.addAttribute("orders", orders);
        return "/user/my_orders";
    }

    @GetMapping("/update-status")
    public String updateOrderStatus(@RequestParam Integer id, @RequestParam Integer st, HttpSession session) {

        OrderStatus[] values = OrderStatus.values();
        String status = null;

        for (OrderStatus orderSt : values) {
            if (orderSt.getId().equals(st)) {
                status = orderSt.getName();
            }
        }

        ProductOrder updateOrder = orderService.updateOrderStatus(id, status);

        if (!ObjectUtils.isEmpty(updateOrder)) {
            session.setAttribute("succMsg", "Status Updated");
        } else {
            session.setAttribute("errorMsg", "status not updated");
        }
        return "redirect:/order/view";
    }
    @GetMapping("/get-all")
    public String getAllOrders(Model model)
    {
        List<ProductOrder> orders= orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "/admin/all_orders";
    }
}
