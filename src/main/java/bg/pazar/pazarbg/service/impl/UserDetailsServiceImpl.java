package bg.pazar.pazarbg.service.impl;

import bg.pazar.pazarbg.model.entity.UserEntity;
import bg.pazar.pazarbg.repo.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Get user details for Spring Security
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity != null) {
            return User.withUsername(userEntity.getUsername()).password(userEntity.getPassword()).roles(userEntity.getRole().name()).build();
        } else throw new UsernameNotFoundException("User with username " + username + " not found!");
    }
}
