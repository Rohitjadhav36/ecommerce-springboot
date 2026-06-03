package com.ecom.service.impl;

import com.ecom.dto.OrderDto;
import com.ecom.entity.*;
import com.ecom.repository.CartRepository;
import com.ecom.repository.OrderRepository;
import com.ecom.service.CartService;
import com.ecom.service.OrderService;
import com.ecom.service.ProductService;
import com.ecom.service.UserService;
import com.ecom.util.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Override
    public void saveOrder(Integer userid, OrderDto orderRequest) throws Exception {
        List<Cart> carts = cartRepository.findByUserId(userid);

        for (Cart cart : carts) {

            ProductOrder order = new ProductOrder();

            order.setOrderId(UUID.randomUUID().toString());
            order.setOrderDate(LocalDate.now());

            order.setProduct(cart.getProduct());
            order.setPrice(cart.getProduct().getDiscountPrice());

            order.setQuantity(cart.getQuantity());
            order.setUser(cart.getUser());

            order.setStatus(OrderStatus.IN_PROGRESS.getName());
            order.setPaymentType(orderRequest.getPaymentType());

            Address address = new Address();
            address.setFirstName(orderRequest.getFirstName());
            address.setLastName(orderRequest.getLastName());
            address.setEmail(orderRequest.getEmail());
            address.setMobileNo(orderRequest.getMobileNo());
            address.setAddress(orderRequest.getAddress());
            address.setCity(orderRequest.getCity());
            address.setState(orderRequest.getState());
            address.setPincode(orderRequest.getPincode());

            order.setDeliveryAddress(address);

            ProductOrder saveOrder = orderRepository.save(order);
            resetCart(cart.getUser());
        }
    }
    @Override
    public void saveSingleOrder(Integer userid, OrderDto orderRequest, Integer pid) throws Exception {

        Product product=productService.getProductById(pid);
        User user=userService.getUserById(userid);

        // save - order
            ProductOrder order = new ProductOrder();

            order.setOrderId(UUID.randomUUID().toString());
            order.setOrderDate(LocalDate.now());

            order.setProduct(product);
            order.setPrice(product.getDiscountPrice());

            order.setQuantity(1);
            order.setUser(user);

            order.setStatus(OrderStatus.IN_PROGRESS.getName());
            order.setPaymentType(orderRequest.getPaymentType());

            Address address = new Address();
            address.setFirstName(orderRequest.getFirstName());
            address.setLastName(orderRequest.getLastName());
            address.setEmail(orderRequest.getEmail());
            address.setMobileNo(orderRequest.getMobileNo());
            address.setAddress(orderRequest.getAddress());
            address.setCity(orderRequest.getCity());
            address.setState(orderRequest.getState());
            address.setPincode(orderRequest.getPincode());

            order.setDeliveryAddress(address);
            ProductOrder saveOrder = orderRepository.save(order);
    }
        private void resetCart(User user) {
            cartRepository.deleteByUser(user);
        }
        @Override
        public List<ProductOrder> getOrdersByUser(Integer userId) {
            List<ProductOrder> orders = orderRepository.findByUserId(userId);
            return orders;
        }

        @Override
        public ProductOrder updateOrderStatus(Integer id, String status) {
            Optional<ProductOrder> findById = orderRepository.findById(id);
            if (findById.isPresent()) {
                ProductOrder productOrder = findById.get();
                productOrder.setStatus(status);
                ProductOrder updateOrder = orderRepository.save(productOrder);
                return updateOrder;
            }
            return null;
        }

        @Override
        public List<ProductOrder> getAllOrders() {
            return orderRepository.findAll();
        }

        /*@Override
        public Page<ProductOrder> getAllOrdersPagination(Integer pageNo, Integer pageSize) {
            Pageable pageable = PageRequest.of(pageNo, pageSize);
            return orderRepository.findAll(pageable);

        }*/

        @Override
        public ProductOrder getOrdersByOrderId(String orderId) {
            return orderRepository.findByOrderId(orderId);
        }

        @Override
        public  List<ProductOrder> getOrdersByDate(LocalDate orderDate)
        {
         return    orderRepository.findByOrderDate(orderDate);
        }
    }
