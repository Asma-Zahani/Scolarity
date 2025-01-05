package security.securityscolarity.init;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import security.securityscolarity.entity.Admin;
import security.securityscolarity.entity.Day;
import security.securityscolarity.entity.Role;
import security.securityscolarity.repository.AdminRepository;
import security.securityscolarity.repository.DayRepository;
import security.securityscolarity.service.IMPL.UserService;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataInitializer {

    @Autowired
    private DayRepository dayRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private AdminRepository adminRepository;

    @PostConstruct
    public void initDay() {
        Map<String, Integer> days = new LinkedHashMap<>();
        days.put("Lundi", 1);
        days.put("Mardi", 2);
        days.put("Mercredi", 3);
        days.put("Jeudi", 4);
        days.put("Vendredi", 5);
        days.put("Samedi", 6);

        for (Map.Entry<String, Integer> entry : days.entrySet()) {
            String dayName = entry.getKey();
            Integer dayNumber = entry.getValue();

            if (dayRepository.findByDayName(dayName) == null) {
                dayRepository.save(new Day((long) dayNumber, dayName, dayNumber));
            }
        }
    }

    @PostConstruct
    public void initUser() {
        System.out.println("Checking if admin exist...");
        if (!userService.existsUserWithRole(Role.GLOBAL_ADMIN.toString())) {
            System.out.println("Is admin exist? " + userService.existsUserWithRole(Role.GLOBAL_ADMIN.toString()));
        }
        if (adminRepository.findByEmail("admin@gmail.com") == null) {
            System.out.println("Creating admin...");
            Admin admin = new Admin();
            admin.setEmail("admin@gmail.com");
            admin.setPassword(new BCryptPasswordEncoder().encode("123456"));
            admin.setRoles(Collections.singletonList(Role.GLOBAL_ADMIN));
            admin.setActive(true);
            adminRepository.save(admin);
            System.out.println("Admin created.");
        }
    }
}
