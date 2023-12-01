package bg.pazar.pazarbg.service.impl;

import bg.pazar.pazarbg.model.dto.user.UserRegisterBindingModel;
import bg.pazar.pazarbg.model.entity.UserEntity;
import bg.pazar.pazarbg.model.enums.UserRole;
import bg.pazar.pazarbg.model.view.UserViewModel;
import bg.pazar.pazarbg.repo.MessageRepository;
import bg.pazar.pazarbg.repo.UserRepository;
import bg.pazar.pazarbg.service.UserService;
import bg.pazar.pazarbg.service.enums.RegistrationResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private MessageRepository mockMessageRepository;
    @Mock
    private AuthenticationService mockAuthenticationService;
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        this.passwordEncoder = Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();
        this.userService = new UserServiceImpl(mockUserRepository, mockMessageRepository, mockAuthenticationService, passwordEncoder);

        this.testUser = new UserEntity();
        this.testUser.setUsername("Alex");
        this.testUser.setEmail("alex@gmail.com");
        this.testUser.setId(1L);
        this.testUser.setRole(UserRole.ADMIN);
        this.testUser.setMarkedForDeletion(false);
    }

    @Test
    void testRegisterUserUsernameTaken() {
        when(mockUserRepository.findByUsername("Alex")).thenReturn(this.testUser);

        UserRegisterBindingModel userRegisterBindingModel = new UserRegisterBindingModel();
        userRegisterBindingModel.setUsername("Alex");
        userRegisterBindingModel.setEmail("alex2@gmail.com");
        userRegisterBindingModel.setPassword("12345");
        userRegisterBindingModel.setConfirmPassword("12345");

        RegistrationResult result = userService.register(userRegisterBindingModel);

        Assertions.assertEquals(RegistrationResult.USERNAME_TAKEN, result, "RegistrationResult must be USERNAME_TAKEN");
    }

    @Test
    void testRegisterUserEmailTaken() {
        when(mockUserRepository.findByEmail("alex@gmail.com")).thenReturn(this.testUser);

        UserRegisterBindingModel userRegisterBindingModel = new UserRegisterBindingModel();
        userRegisterBindingModel.setUsername("Alex2");
        userRegisterBindingModel.setEmail("alex@gmail.com");
        userRegisterBindingModel.setPassword("12345");
        userRegisterBindingModel.setConfirmPassword("12345");

        RegistrationResult result = userService.register(userRegisterBindingModel);

        Assertions.assertEquals(RegistrationResult.EMAIL_TAKEN, result, "RegistrationResult must be EMAIL_TAKEN");
    }

    @Test
    void testRegisterUserPasswordsDoNotMatch() {
        UserRegisterBindingModel userRegisterBindingModel = new UserRegisterBindingModel();
        userRegisterBindingModel.setUsername("Alex2");
        userRegisterBindingModel.setEmail("alex2@gmail.com");
        userRegisterBindingModel.setPassword("12345");
        userRegisterBindingModel.setConfirmPassword("123456");

        RegistrationResult result = userService.register(userRegisterBindingModel);

        Assertions.assertEquals(RegistrationResult.PASSWORDS_DO_NOT_MATCH, result, "RegistrationResult must be PASSWORDS_DO_NOT_MATCH");
    }

    @Test
    void testRegisterUserSuccessful() {
        UserRegisterBindingModel userRegisterBindingModel = new UserRegisterBindingModel();
        userRegisterBindingModel.setUsername("Alex2");
        userRegisterBindingModel.setEmail("alex2@gmail.com");
        userRegisterBindingModel.setPassword("12345");
        userRegisterBindingModel.setConfirmPassword("12345");

        RegistrationResult result = userService.register(userRegisterBindingModel);

        Assertions.assertEquals(RegistrationResult.OK, result, "RegistrationResult must be OK");
    }

    @Test
    void testToggleDeletion() {
        when(mockUserRepository.findById(1L)).thenReturn(Optional.ofNullable(this.testUser));

        userService.toggleDeletion(1L);

        Assertions.assertTrue(mockUserRepository.findById(1L).get().isMarkedForDeletion(), "User must be marked for deletion");
    }

    @Test
    void testToggleRole() {
        when(mockUserRepository.findById(1L)).thenReturn(Optional.ofNullable(this.testUser));

        userService.toggleRole(1L);

        Assertions.assertEquals(UserRole.USER, mockUserRepository.findById(1L).get().getRole(), "User role must be USER");
    }

    @Test
    void testGetUserViewModel() {
        when(mockUserRepository.findByUsername("Alex")).thenReturn(this.testUser);

        UserViewModel actual = userService.getUserViewModel(mockUserRepository.findByUsername("Alex"));

        Assertions.assertEquals(testUser.getId(), actual.getId(), "UserViewModel Id doesn't match");
        Assertions.assertEquals(testUser.getUsername(), actual.getUsername(), "UserViewModel Username doesn't match");
        Assertions.assertEquals(testUser.getEmail(), actual.getEmail(), "UserViewModel Email doesn't match");
        Assertions.assertTrue(actual.isAdmin(), "UserViewModel isAdmin doesn't match");
        Assertions.assertFalse(actual.isMarkedForDeletion(), "UserViewModel isMarkedForDeletion doesn't match");
    }

    @Test
    void testGetAllUsersViewModels() {
        when(mockAuthenticationService.getCurrentUserId()).thenReturn(2L);
        when(mockUserRepository.findAllByIdNot(mockAuthenticationService.getCurrentUserId())).thenReturn(List.of(this.testUser));

        List<UserViewModel> models = userService.getAllUsersViewModels();
        UserViewModel actual = models.get(0);

        Assertions.assertEquals(1, models.size(), "There should be only one model returned");

        Assertions.assertEquals(testUser.getId(), actual.getId(), "UserViewModel Id doesn't match");
        Assertions.assertEquals(testUser.getUsername(), actual.getUsername(), "UserViewModel Username doesn't match");
        Assertions.assertEquals(testUser.getEmail(), actual.getEmail(), "UserViewModel Email doesn't match");
        Assertions.assertTrue(actual.isAdmin(), "UserViewModel isAdmin doesn't match");
        Assertions.assertFalse(actual.isMarkedForDeletion(), "UserViewModel isMarkedForDeletion doesn't match");
    }
}
