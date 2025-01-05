package security.securityscolarity.service.IMPL;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import security.securityscolarity.entity.User;
import security.securityscolarity.repository.UserRepository;
import security.securityscolarity.service.IUserService;

import java.util.List;

@Service
public class UserService implements IUserService{

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByUserID(Long id) {
        return userRepository.findById(id).get();
    }

    public User addUser(User User) {
        return userRepository.save(User);
    }
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public boolean existsUserWithRole(String role) {
        String sqlQuery = "SELECT COUNT(*) FROM `user_roles` WHERE roles = ?";
        Query query = entityManager.createNativeQuery(sqlQuery);
        query.setParameter(1, role);

        Long count = (Long) query.getSingleResult();
        return count > 0;
    }
}
