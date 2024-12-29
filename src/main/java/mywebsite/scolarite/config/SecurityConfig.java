//package mywebsite.scolarite.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    //    @Bean
////    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
////        http
////                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
////                .csrf(csrf -> csrf.disable());
////
////        return http.build();
////    }
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/login", "/register", "/static/dist/**", "/subject/**").permitAll() // Allow access to login and register endpoints
//                        .anyRequest().authenticated()                      // Protect other endpoints
//                )
//                .formLogin(form -> form
//                        .loginPage("/login")       // Custom login page
//                        .defaultSuccessUrl("/home", true) // Redirect after successful login
//                        .permitAll()
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/login")
//                        .permitAll()
//                )
//                .csrf(csrf -> csrf.disable()); // Disable CSRF for simplicity during development
//
//        return http.build();
//    }
//}