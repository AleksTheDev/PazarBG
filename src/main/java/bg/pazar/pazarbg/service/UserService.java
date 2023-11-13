package bg.pazar.pazarbg.service;

import bg.pazar.pazarbg.model.dto.user.UserRegisterBindingModel;
import bg.pazar.pazarbg.service.enums.RegistrationResult;

public interface UserService {
    RegistrationResult register(UserRegisterBindingModel userRegisterBindingModel);
}
