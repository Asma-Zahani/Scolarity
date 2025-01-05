package security.securityscolarity.service;

import security.securityscolarity.entity.User;

import java.util.List;

public interface IUserService {
    List<User> findAll();
    User findByUserID(Long id);
    boolean existsUserWithRole(String role);
}
