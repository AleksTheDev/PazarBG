package bg.pazar.pazarbg.service;

import bg.pazar.pazarbg.model.dto.user.UserRegisterBindingModel;
import bg.pazar.pazarbg.model.entity.UserEntity;
import bg.pazar.pazarbg.model.view.UserViewModel;
import bg.pazar.pazarbg.service.enums.RegistrationResult;

import java.util.List;

public interface UserService {
    RegistrationResult register(UserRegisterBindingModel userRegisterBindingModel);

    void toggleRole(Long id);

    List<UserViewModel> getAllUsersViewModels();

    UserViewModel getUserViewModel(UserEntity user);
}
