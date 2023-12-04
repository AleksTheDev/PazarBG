package bg.pazar.pazarbg.controller;

import bg.pazar.pazarbg.model.dto.user.UserRegisterBindingModel;
import bg.pazar.pazarbg.service.UserService;
import bg.pazar.pazarbg.service.enums.RegistrationResult;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {
    private static final String BINDING_RESULT_PATH = "org.springframework.validation.BindingResult";
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Get mapping for login
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    //Post mapping for login-error
    @PostMapping("/login-error")
    public String onFailure(@ModelAttribute("username") String username, Model model) {

        model.addAttribute("username", username);
        model.addAttribute("bad_credentials", "true");

        return "login";
    }

    //Get mapping for register page
    @GetMapping("/register")
    public String register(Model model) {
        //Add binding model
        if (!model.containsAttribute("userRegisterBindingModel")) {
            model.addAttribute("userRegisterBindingModel", new UserRegisterBindingModel());
        }

        return "register";
    }

    @PostMapping("/register")
    public ModelAndView register(@Valid @ModelAttribute("userRegisterBindingModel") UserRegisterBindingModel userRegisterBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        //Validation
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);
            redirectAttributes.addFlashAttribute(BINDING_RESULT_PATH, bindingResult);
            return new ModelAndView("register");
        }

        //Try to register user
        RegistrationResult result = userService.register(userRegisterBindingModel);

        //Handle registration result
        if (result != RegistrationResult.OK) {
            ModelAndView modelAndView = new ModelAndView("register");
            if (result == RegistrationResult.USERNAME_TAKEN) modelAndView.addObject("usernameTaken", true);
            if (result == RegistrationResult.EMAIL_TAKEN) modelAndView.addObject("emailTaken", true);
            if (result == RegistrationResult.PASSWORDS_DO_NOT_MATCH)
                modelAndView.addObject("passwordsDoNotMatch", true);

            modelAndView.addObject("bindingModel", userRegisterBindingModel);

            return modelAndView;
        }

        return new ModelAndView("redirect:/login");
    }
}
