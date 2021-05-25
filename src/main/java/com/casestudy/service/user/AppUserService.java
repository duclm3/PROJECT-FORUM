package com.casestudy.service.user;

import com.casestudy.model.User;
import com.casestudy.model.UserPrinciple;
import com.casestudy.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserService implements UserDetailsService, IUserService {
    @Autowired
    IUserRepository iUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = iUserRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        return UserPrinciple.build(userOptional.get());
    }

    @Override
    public Iterable<User> findAll() {
        return iUserRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return iUserRepository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return iUserRepository.findByUsername(username);
    }

//    @Override
//    public Boolean existsByEmail(String email) {
//        return iUserRepository.existsByEmail(email);
//    }

    @Override
    public Boolean existsByUsername(String username) {
        return iUserRepository.existsByUsername(username);
    }

    @Override
    public Iterable<User> findUsersByNameContaining(String user_name) {
        return iUserRepository.findUsersByFullNameContaining(user_name);
    }

    @Override
    public void save(User user) {
        iUserRepository.save(user);
    }

    @Override
    public void remove(Long id) {
        iUserRepository.deleteById(id);
    }

    @Override
    public Page<User> findAllByUsernameContaining(String name, Pageable pageable) {
        return iUserRepository.findAllByUsernameContaining(name, pageable);
    }
    @Override
    public Page<User> findAll(Pageable pageable) {
        return iUserRepository.findAll(pageable);
    }
}