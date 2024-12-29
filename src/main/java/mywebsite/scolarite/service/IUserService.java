package mywebsite.scolarite.service;

import mywebsite.scolarite.entity.User;

import java.util.List;

public interface IUserService {

    public List<User> findAll();
    public User findByUserID(Long id);
    public User addUser(User user);
    public void deleteUser(Long id);
    public User updateUser(Long id , User user);

}
