package com.ecom.repository;

import java.util.List;

import com.ecom.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.entity.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {


    List<Product> findByCategory(Category category);

    /*List<Product> findByIsActiveTrue();

    Page<Product> findByIsActiveTrue(Pageable pageable);

    List<Product> findByCategory(Category category);

    List<Product> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch, String ch2);

    Page<Product> findByCategory(Pageable pageable, String category);

    Page<Product> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch, String ch2,
                                                                                Pageable pageable);

    Page<Product> findByisActiveTrueAndTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch, String ch2,
                                                                                               Pageable pageable);
*/
}
