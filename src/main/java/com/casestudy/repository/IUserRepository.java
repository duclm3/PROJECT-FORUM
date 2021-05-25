package com.casestudy.repository;

import com.casestudy.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends PagingAndSortingRepository<User, Long> {
    Optional<User> findByUsername(String username);

//    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);

    Iterable<User> findUsersByFullNameContaining(String user_name);
    Page<User> findAllByUsernameContaining(String ame, Pageable pageable);
}