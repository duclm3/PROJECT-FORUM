package com.casestudy.service.category;

import com.casestudy.model.Category;
import com.casestudy.model.User;
import com.casestudy.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICategoryService extends IGeneralService<Category> {

    Page<Category> findAllByCateNameContaining(String name, Pageable pageable);

    Page<Category> findAll(Pageable pageable);
    Boolean existsByCateName(String username);
}
