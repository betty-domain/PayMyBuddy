package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.FunctionalException;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.model.UserPrincipal;
import com.paymybuddy.webapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MyUserDetailsServiceTests {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Test
    public void loadUserByUsernameNotFoundUser()
    {
        when(userRepository.findUserByEmailIgnoreCase(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
                    userDetailsService.loadUserByUsername("myEmail");
                }
        );

        assertThat(exception.getMessage()).contains("User 404");
    }

    @Test
    public void loadUserByUsernameExistingUser()
    {
        User user = new User();
        user.setEmail("email@gmail.fr");
        user.setPassword("myPassword");
        when(userRepository.findUserByEmailIgnoreCase(anyString())).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        assertThat(userDetails.getUsername()).isEqualTo(user.getEmail());
        assertThat(userDetails.getPassword()).isEqualTo(user.getPassword());
        assertThat(userDetails.isEnabled()).isTrue();
        assertThat(userDetails.isAccountNonExpired()).isTrue();
        assertThat(userDetails.isAccountNonLocked()).isTrue();
        assertThat(userDetails.isCredentialsNonExpired()).isTrue();
        assertThat(userDetails.getAuthorities().contains("USER"));
    }
}
