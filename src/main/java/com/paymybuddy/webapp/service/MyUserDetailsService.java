package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.model.UserPrincipal;
import com.paymybuddy.webapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByEmailIgnoreCase(email);

        if (!user.isPresent())
        {
            throw new UsernameNotFoundException("User 404");
        }

        return new UserPrincipal(user.get());
    }
}
