package com.ecom.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    public Boolean existsByName(String name);

    public List<Category> findByIsActiveTrue();

    public Optional<Category> findByName(String name);

}
