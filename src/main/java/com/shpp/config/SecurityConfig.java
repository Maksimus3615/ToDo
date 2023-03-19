package com.shpp.config;

import com.shpp.models.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    private static final String API_ANT_PATTERN = "/tasks/**";
    private static final String DB_ANT_PATTERN = "/h2-console/**";
    private static final String USER_PASSWORD = "0000";
    private static final String ADMIN_PASSWORD = "1111";

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().antMatchers(DB_ANT_PATTERN);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, API_ANT_PATTERN).hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                .antMatchers(HttpMethod.POST, API_ANT_PATTERN).hasRole(Role.ADMIN.name())
                .antMatchers(HttpMethod.PUT, API_ANT_PATTERN).hasRole(Role.ADMIN.name())
                .antMatchers(HttpMethod.DELETE, API_ANT_PATTERN).hasRole(Role.ADMIN.name())
                .and()
                .httpBasic();
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user1 = User.builder()
                .username("user")
                .password(passwordEncoder().encode(USER_PASSWORD))
                .roles(Role.USER.name())
                .build();
        UserDetails user2 = User.builder()
                .username("admin")
                .password(passwordEncoder().encode(ADMIN_PASSWORD))
                .roles(Role.ADMIN.name())
                .build();
        return new InMemoryUserDetailsManager(user1, user2);
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}


//***************deprecated***************deprecated***************deprecated***************deprecated***************
/*
        import com.shpp.models.Role;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.http.HttpMethod;
        import org.springframework.security.config.annotation.web.builders.HttpSecurity;
        import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
        import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
        import org.springframework.security.config.http.SessionCreationPolicy;
        import org.springframework.security.core.userdetails.User;
        import org.springframework.security.core.userdetails.UserDetailsService;
        import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
        import org.springframework.security.crypto.password.PasswordEncoder;
        import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/h2-console/**").permitAll()
                .and()
                .csrf().ignoringAntMatchers("/h2-console/**")
                .and()
                .headers()
                .frameOptions()
                .sameOrigin();

        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                // .antMatchers("/").permitAll()
                .antMatchers(HttpMethod.GET, "/tasks/**").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                .antMatchers(HttpMethod.POST, "/tasks/**").hasRole(Role.ADMIN.name())
                .antMatchers(HttpMethod.PUT, "/tasks/**").hasRole(Role.ADMIN.name())
                .antMatchers(HttpMethod.DELETE, "/tasks/**").hasRole(Role.ADMIN.name())
                .and()
                .httpBasic();
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                User
                        .builder()
                        .username("admin")
                        .password(passwordEncoder().encode("999"))
                        .roles(Role.ADMIN.name())
                        .build(),
                User
                        .builder()
                        .username("user")
                        .password(passwordEncoder().encode("111"))
                        .roles(Role.USER.name())
                        .build()
        );
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
 */
//***************deprecated***************deprecated***************deprecated***************deprecated***************