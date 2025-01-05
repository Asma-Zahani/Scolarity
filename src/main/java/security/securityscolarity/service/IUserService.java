package security.securityscolarity.service;

import security.securityscolarity.entity.User;

import java.util.List;

public interface IUserService {

    List<User> findAll();
    User findByUserID(Long id);
    User addUser(User user);
    void deleteUser(Long id);
    User updateUser(Long id , User user);
    boolean existsUserWithRole(String role);
}
