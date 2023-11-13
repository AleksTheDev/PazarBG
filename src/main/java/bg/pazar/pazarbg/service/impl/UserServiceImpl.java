package bg.pazar.pazarbg.service.impl;

import bg.pazar.pazarbg.model.dto.user.UserRegisterBindingModel;
import bg.pazar.pazarbg.model.entity.UserEntity;
import bg.pazar.pazarbg.model.enums.UserRole;
import bg.pazar.pazarbg.repo.UserRepository;
import bg.pazar.pazarbg.service.UserService;
import bg.pazar.pazarbg.service.enums.RegistrationResult;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public RegistrationResult register(UserRegisterBindingModel userRegisterBindingModel) {
        if(userRepository.findByUsername(userRegisterBindingModel.getUsername()) != null) {
            return RegistrationResult.USERNAME_TAKEN;
        }

        if(userRepository.findByEmail(userRegisterBindingModel.getEmail()) != null) {
            return RegistrationResult.EMAIL_TAKEN;
        }

        if(!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {
            return RegistrationResult.PASSWORDS_DO_NOT_MATCH;
        }

        UserEntity user = new UserEntity();
        user.setUsername(userRegisterBindingModel.getUsername());
        user.setEmail(userRegisterBindingModel.getEmail());
        user.setPassword(passwordEncoder.encode(userRegisterBindingModel.getPassword()));
        user.setRole(UserRole.USER);

        userRepository.save(user);

        return RegistrationResult.OK;
    }
}
