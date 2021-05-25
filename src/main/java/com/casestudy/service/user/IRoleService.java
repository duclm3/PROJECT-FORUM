package com.casestudy.service.user;

import com.casestudy.model.Role;
import com.casestudy.model.RoleName;

import java.util.Optional;

public interface IRoleService {
    Optional<Role> findByName(RoleName name);
}
