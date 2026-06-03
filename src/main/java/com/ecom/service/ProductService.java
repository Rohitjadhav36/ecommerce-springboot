package com.ecom.service;

import java.util.List;
import java.util.Optional;

import com.ecom.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.entity.Product;

public interface ProductService {

    public Product saveProduct(Product product);

    public List<Product> getAllProducts();

    public void deleteProduct(Integer id);

    public Product getProductById(Integer id);

    public Product updateProduct(Product product, MultipartFile file);
    List<Product> getProductsByCategory(Category category);

   /*
    public Product getProductById(Integer id);

    public List<Product> getAllActiveProducts(String category);*/

   /* public List<Product> searchProduct(String ch);

    public Page<Product> getAllActiveProductPagination(Integer pageNo, Integer pageSize, String category);

    public Page<Product> searchProductPagination(Integer pageNo, Integer pageSize, String ch);

    public Page<Product> getAllProductsPagination(Integer pageNo, Integer pageSize);

    public Page<Product> searchActiveProductPagination(Integer pageNo, Integer pageSize, String category, String ch);
*/
}
