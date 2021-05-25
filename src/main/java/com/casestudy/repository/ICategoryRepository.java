package com.casestudy.repository;

import com.casestudy.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends JpaRepository<Category,Long> {
    Page<Category> findAllByCateNameContaining(String name, Pageable pageable);
    Page<Category> findAll(Pageable pageable);
    Boolean existsByCateName(String name);
}
