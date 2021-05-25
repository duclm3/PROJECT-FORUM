package com.casestudy.service.user;

import com.casestudy.model.User;
import com.casestudy.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IUserService extends IGeneralService<User> {

    Optional<User> findByUsername(String username);

//    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);

    Iterable<User> findUsersByNameContaining(String user_name);
    void save(User user);

    Page<User> findAllByUsernameContaining(String name, Pageable pageable);
    Page<User> findAll(Pageable pageable);
}
