package bg.pazar.pazarbg.service.impl;

import bg.pazar.pazarbg.exception.UserNotFoundException;
import bg.pazar.pazarbg.model.dto.user.UserRegisterBindingModel;
import bg.pazar.pazarbg.model.entity.UserEntity;
import bg.pazar.pazarbg.model.enums.UserRole;
import bg.pazar.pazarbg.model.view.UserViewModel;
import bg.pazar.pazarbg.repo.MessageRepository;
import bg.pazar.pazarbg.repo.UserRepository;
import bg.pazar.pazarbg.service.UserService;
import bg.pazar.pazarbg.service.enums.RegistrationResult;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, MessageRepository messageRepository, AuthenticationService authenticationService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.authenticationService = authenticationService;
        this.passwordEncoder = passwordEncoder;
    }

    //Register user
    @Override
    public RegistrationResult register(UserRegisterBindingModel userRegisterBindingModel) {
        //Check if username is taken
        if (userRepository.findByUsername(userRegisterBindingModel.getUsername()) != null) {
            return RegistrationResult.USERNAME_TAKEN;
        }

        //Check if email is taken
        if (userRepository.findByEmail(userRegisterBindingModel.getEmail()) != null) {
            return RegistrationResult.EMAIL_TAKEN;
        }

        //Check if password and confirm password match
        if (!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {
            return RegistrationResult.PASSWORDS_DO_NOT_MATCH;
        }

        //Create user entity
        UserEntity user = new UserEntity();
        user.setUsername(userRegisterBindingModel.getUsername());
        user.setEmail(userRegisterBindingModel.getEmail());
        user.setPassword(passwordEncoder.encode(userRegisterBindingModel.getPassword()));
        user.setRole(UserRole.USER);
        user.setMarkedForDeletion(false);

        //Save user entity to database
        userRepository.save(user);

        //Return user entity for testing
        return RegistrationResult.OK;
    }

    //Toggle use role
    @Override
    public void toggleRole(Long id) {
        //Check if user exists
        UserEntity user = userRepository.findById(id).orElse(null);
        if(user == null) throw new UserNotFoundException();

        //Set the user's role to the opposite of the current role
        if(user.getRole().name().equals(UserRole.USER.name())) user.setRole(UserRole.ADMIN);
        else user.setRole(UserRole.USER);

        //Save user to database
        userRepository.save(user);
    }

    @Override
    public void toggleDeletion(Long id) {
        //Check if user exists
        UserEntity user = userRepository.findById(id).orElse(null);
        if(user == null) throw new UserNotFoundException();

        //Toggle the marker's state
        user.setMarkedForDeletion(!user.isMarkedForDeletion());

        //Save user to database
        userRepository.save(user);
    }

    @Override
    public List<UserViewModel> getAllUsersViewModels() {
        List<UserEntity> users = userRepository.findAllByIdNot(authenticationService.getCurrentUserId());
        List<UserViewModel> models = new ArrayList<>();

        users.forEach(user -> models.add(getUserViewModel(user)));

        return models;
    }

    @Override
    public UserViewModel getUserViewModel(UserEntity user) {
        UserViewModel model = new UserViewModel();

        model.setId(user.getId());
        model.setUsername(user.getUsername());
        model.setEmail(user.getEmail());
        model.setOffersAdded(user.getOffers().size());
        model.setOffersBought(user.getBoughtOffers().size());
        model.setMessagesSent(messageRepository.findAllByFromId(user.getId()).size());
        model.setAdmin(user.getRole().name().equals(UserRole.ADMIN.name()));
        model.setMarkedForDeletion(user.isMarkedForDeletion());

        return model;
    }
}
