package security.securityscolarity.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import security.securityscolarity.entity.Teacher;
import security.securityscolarity.entity.User;
import security.securityscolarity.service.IMPL.TeacherService;
import security.securityscolarity.service.IMPL.UserService;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/users")
public class RestUserController {

    @Autowired
    UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable("userId") Long id) {
        return userService.findByUserID(id);
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable("userId") Long id) {
        userService.deleteUser(id);
        return "User with ID " + id + " deleted successfully";
    }

    @PutMapping("/{userId}")
    public User updateUser(@PathVariable("userId") Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }
}
