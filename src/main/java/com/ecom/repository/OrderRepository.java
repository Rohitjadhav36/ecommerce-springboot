package com.ecom.repository;

import com.ecom.entity.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<ProductOrder,Integer> {
    List<ProductOrder> findByUserId(Integer userId);

    List<ProductOrder> findByOrderDate(LocalDate orderDate);

    ProductOrder findByOrderId(String orderId);
}
