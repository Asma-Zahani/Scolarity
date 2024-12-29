package mywebsite.scolarite.service.IMPL;

import mywebsite.scolarite.entity.Teacher;
import mywebsite.scolarite.entity.User;
import mywebsite.scolarite.repository.UserRepository;
import mywebsite.scolarite.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService{
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

    public User updateUser(Long id , User user) {
        User userToUpdate = userRepository.findById(id).get();
        userToUpdate.setId(id);
        userToUpdate.setFirstName(user.getFirstName());
        userToUpdate.setLastName(user.getLastName());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setPassword(user.getPassword());
        userToUpdate.setActive(user.isActive());
        return userRepository.save(userToUpdate);
    }
}
