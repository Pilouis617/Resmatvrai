package fr.bts.resmat.security;

import fr.bts.resmat.repository.UtilisateurRepository;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean UserDetailsService userDetailsService(UtilisateurRepository users) {
        return login-> {
            var u=users.findByLogin(login).orElseThrow(()->new UsernameNotFoundException("Compte inconnu"));
            return new User(u.login,u.motDePasse,java.util.List.of(new SimpleGrantedAuthority(u.gestionnaire?"ROLE_GESTIONNAIRE":"ROLE_USER")));
        }
        ;
    }

    @Bean SecurityFilterChain filterChain(HttpSecurity http)throws Exception {
        return http.authorizeHttpRequests(a->a.requestMatchers("/css/**","/login","/error").permitAll().requestMatchers("/gestion/**").hasRole("GESTIONNAIRE").anyRequest().authenticated()).formLogin(f->f.loginPage("/login").defaultSuccessUrl("/catalogue",true).permitAll()).logout(l->l.logoutSuccessUrl("/login?logout")).build();
    }
}
