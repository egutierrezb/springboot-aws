package com.booking.room.security;

import com.booking.room.service.JwtUserDetailsService;
import com.booking.room.util.JwtAuthenticationEntryPoint;
import com.booking.room.util.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SpringSecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private UserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    //For jwt
    /*@Bean
    public AuthenticationManager authenticationManager(HttpSecurity http)
            throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService(bCryptPasswordEncoder()))
                .passwordEncoder(bCryptPasswordEncoder());
        return authenticationManagerBuilder.build();

    }

     //For basic auth: username and password
     */
    /*@Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {

        UserDetails user = User.withUsername("user")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .build();

        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("USER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }*/

    // Secure the endpoints with HTTP Basic authentication
    /*@Bean
    public SecurityFilterChain authBasic(HttpSecurity http) throws Exception {


        http
                //HTTP Basic authentication
                .httpBasic()
                .and()
                .authorizeRequests()
                .requestMatchers(HttpMethod.PUT, "/api/v1/rooms").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/reservations").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/rooms").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/rooms").hasRole("USER")
                .requestMatchers(HttpMethod.GET, "/api/v1/reservations/**").hasRole("USER")
                .and()
                .csrf().disable()
                .formLogin().disable();

    return http.build();

    }*/

    @Bean
    public SecurityFilterChain authJWT(HttpSecurity http) throws Exception {


        // We don't need CSRF for this example
        http.csrf().disable()
                // dont authenticate this particular request
                // with the second requestMatches we are only requesting authentication for PUT /api/v1/rooms
                .authorizeRequests().requestMatchers("/authenticate").permitAll().requestMatchers(HttpMethod.PUT, "/api/v1/rooms").authenticated().and().
                // all other requests need to be authenticated
                        //anyRequest().authenticated().and().
                // make sure we use stateless session; session won't be used to
                // store user's state.
                        exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Add a filter to validate the tokens with every request
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    //For basic auth
    /*@Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return encoder;
    }*/
}