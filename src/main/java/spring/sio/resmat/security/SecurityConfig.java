package spring.sio.resmat.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import spring.sio.resmat.repositories.UtilisateurRepository;

import java.util.List;

/**
 * Connexion par formulaire et deux rôles :
 * USER (emprunteur) et GESTIONNAIRE.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UtilisateurRepository utilisateurRepository) {
        return login -> utilisateurRepository.findByLogin(login)
                .map(u -> new User(u.getLogin(), u.getMotDePasse(),
                        List.of(new SimpleGrantedAuthority(u.isGestionnaire() ? "ROLE_GESTIONNAIRE" : "ROLE_USER"))))
                .orElseThrow(() -> new UsernameNotFoundException("Compte inconnu"));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/login", "/error").permitAll()
                        .requestMatchers("/gestion/**", "/types-materiel/**").hasRole("GESTIONNAIRE")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/catalogue", true)
                        .permitAll())
                .logout(logout -> logout.logoutSuccessUrl("/login?logout"))
                .build();
    }
}
