package com.woromedia.api.task.security;

import com.woromedia.api.task.entity.User;
import com.woromedia.api.task.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with username or email:" + usernameOrEmail));
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                new HashSet<>());
        // mapRolesToAuthorities((Set<Role>) user.getRoles()));
    }

    // private Collection< ? extends GrantedAuthority>
    // mapRolesToAuthorities(Set<Role> roles){
    // return roles.stream().map(role -> new
    // SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    // }
}
