package security.securityscolarity.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import security.securityscolarity.entity.CustomUserDetail;
import security.securityscolarity.service.CustomUserDetailService;

import java.util.Collection;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    CustomUserDetailService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/dist/**" , "/forgetPassword", "/resetPassword", "/api/**" , "/api/universities/**").permitAll()
                        .requestMatchers(
                                "/universities/**",
                                "/universityAdmins/**",
                                "/index"
                        ).hasAuthority("GLOBAL_ADMIN")
                        .requestMatchers("/universityAdmin/**",
                                "/building/**",
                                "/chrono/**",
                                "/chronoDay/**",
                                "/day/**",
                                "/group/**",
                                "/room/**",
                                "/schedule/**",
                                "/students/**",
                                "/subGroup/**",
                                "/subject/**",
                                "/teachers/**"
                        ).hasAuthority("UNIVERSITY_ADMIN")
                            .requestMatchers("/student/**"
                        ).hasAuthority("STUDENT")
                            .requestMatchers("/teacher/**"
                        ).hasAuthority("TEACHER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .failureUrl("/login?error=true")
                        .successHandler(authenticationSuccessHandler())
                        .usernameParameter("email")
                        .passwordParameter("password")
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .exceptionHandling(withDefaults())
                .csrf(AbstractHttpConfigurer::disable);

        http.authenticationProvider(authenticationProvider());

        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    private String getRedirectUrlBasedOnRole(Authentication authentication) {
        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("GLOBAL_ADMIN")) {
                return "/index";
            } else if (authority.getAuthority().equals("UNIVERSITY_ADMIN")) {
                return "universityAdmin/index";
            } else if (authority.getAuthority().equals("TEACHER")) {
                return "/teacher/index";
            } else if (authority.getAuthority().equals("STUDENT")) {
                return "/student/index";
            }
        }
        return "/home";
    }


    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
            String redirectUrl = getRedirectUrlBasedOnRole(authentication);
            response.sendRedirect(redirectUrl);
        };
    }
}
