package bg.pazar.pazarbg.service.impl;

import bg.pazar.pazarbg.model.entity.UserEntity;
import bg.pazar.pazarbg.model.enums.UserRole;
import bg.pazar.pazarbg.repo.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTests {
    @Mock
    private UserRepository mockUserRepository;

    private PasswordEncoder passwordEncoder;

    private UserDetailsServiceImpl userDetailsService;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        this.passwordEncoder = Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();
        this.userDetailsService = new UserDetailsServiceImpl(mockUserRepository);

        this.testUser = new UserEntity();
        this.testUser.setUsername("Alex");
        this.testUser.setEmail("alex@gmail.com");
        this.testUser.setPassword(passwordEncoder.encode("12345"));
        this.testUser.setId(1L);
        this.testUser.setRole(UserRole.ADMIN);
        this.testUser.setMarkedForDeletion(false);
    }

    @Test
    void loadUserByUsernameTest() {
        when(mockUserRepository.findByUsername(testUser.getUsername())).thenReturn(testUser);

        UserDetails actual = userDetailsService.loadUserByUsername(testUser.getUsername());

        Assertions.assertEquals(testUser.getUsername(), actual.getUsername(), "Usernames don't match");
        Assertions.assertEquals(testUser.getPassword(), actual.getPassword(), "Passwords don't match");
    }
}
