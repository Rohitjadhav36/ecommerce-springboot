package com.ecom.service;

import com.ecom.dto.OrderDto;
import com.ecom.entity.ProductOrder;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {

    public void saveOrder(Integer userid, OrderDto request) throws Exception;
    public void saveSingleOrder(Integer userid, OrderDto request,Integer pid) throws Exception;

    public List<ProductOrder> getOrdersByUser(Integer userId);

    public ProductOrder updateOrderStatus(Integer id, String status);

    public List<ProductOrder> getAllOrders();

    public  List<ProductOrder> getOrdersByDate(LocalDate orderDate);

    public ProductOrder getOrdersByOrderId(String orderId);
}
